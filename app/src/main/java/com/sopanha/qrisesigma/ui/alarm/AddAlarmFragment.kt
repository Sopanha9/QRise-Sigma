package com.sopanha.qrisesigma.ui.alarm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sopanha.qrisesigma.R
import com.sopanha.qrisesigma.data.model.Alarm
import com.sopanha.qrisesigma.data.repository.AlarmRepository
import com.sopanha.qrisesigma.databinding.FragmentAddAlarmBinding
import com.sopanha.qrisesigma.util.AlarmScheduler
import com.sopanha.qrisesigma.util.QRGenerator
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class AddAlarmFragment : Fragment() {

    private var _binding: FragmentAddAlarmBinding? = null
    private val binding get() = _binding!!

    private val viewModel: AddAlarmViewModel by viewModels {
        AddAlarmViewModelFactory(requireContext())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddAlarmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Generate QR preview immediately
        viewModel.generateQr()
        viewModel.qrBitmap.observe(viewLifecycleOwner) { bmp ->
            binding.ivQrPreview.setImageBitmap(bmp)
        }

        binding.btnRegenerateQr.setOnClickListener {
            viewModel.generateQr()
        }

        binding.btnSave.setOnClickListener {
            val hour = binding.timePicker.hour
            val minute = binding.timePicker.minute
            val label = binding.etLabel.text.toString().trim()

            if (viewModel.qrContent == null) {
                Toast.makeText(requireContext(), "QR not ready", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.saveAlarm(
                hour = hour,
                minute = minute,
                label = label,
                context = requireContext()
            ) {
                Toast.makeText(requireContext(), "Alarm set!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class AddAlarmViewModel(private val repo: AlarmRepository) : ViewModel() {
    private val _qrBitmap = androidx.lifecycle.MutableLiveData<android.graphics.Bitmap>()
    val qrBitmap: androidx.lifecycle.LiveData<android.graphics.Bitmap> = _qrBitmap
    var qrContent: String? = null
    private var qrBase64: String? = null

    fun generateQr() {
        qrContent = QRGenerator.generateUniqueContent()
        val bmp = QRGenerator.generateBitmap(qrContent!!)
        qrBase64 = QRGenerator.bitmapToBase64(bmp)
        _qrBitmap.value = bmp
    }

    fun saveAlarm(hour: Int, minute: Int, label: String, context: android.content.Context, onDone: () -> Unit) {
        viewModelScope.launch {
            val alarm = Alarm(
                hour = hour,
                minute = minute,
                label = label,
                qrCodeContent = qrContent!!,
                qrCodeBitmap = qrBase64 ?: ""
            )
            val id = repo.insert(alarm)
            AlarmScheduler.schedule(context, alarm.copy(id = id.toInt()))
            onDone()
        }
    }
}

class AddAlarmViewModelFactory(private val context: android.content.Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return AddAlarmViewModel(AlarmRepository(context)) as T
    }
}
