package com.sopanha.qrisesigma.ui.qr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sopanha.qrisesigma.databinding.FragmentQrViewBinding
import com.sopanha.qrisesigma.util.QRGenerator

class QRViewFragment : Fragment() {

    private var _binding: FragmentQrViewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQrViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val base64 = arguments?.getString("QR_BASE64") ?: ""
        val label = arguments?.getString("ALARM_LABEL") ?: "Your QR Code"

        binding.tvQrLabel.text = "QR Code for: $label"
        binding.tvQrHint.text = "Print or save this. You MUST scan it to stop the alarm."

        if (base64.isNotEmpty()) {
            val bmp = QRGenerator.base64ToBitmap(base64)
            binding.ivQrCode.setImageBitmap(bmp)
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
