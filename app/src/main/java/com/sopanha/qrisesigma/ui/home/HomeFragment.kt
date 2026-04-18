package com.sopanha.qrisesigma.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sopanha.qrisesigma.R
import com.sopanha.qrisesigma.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels { HomeViewModelFactory(requireContext()) }
    private lateinit var adapter: AlarmAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AlarmAdapter(
            onToggle = { alarm, enabled ->
                viewModel.toggleAlarm(alarm, enabled, requireContext())
            },
            onDelete = { alarm ->
                viewModel.deleteAlarm(alarm, requireContext())
            },
            onViewQr = { alarm ->
                val bundle = Bundle().apply {
                    putString("QR_CONTENT", alarm.qrCodeContent)
                    putString("QR_BASE64", alarm.qrCodeBitmap)
                    putString("ALARM_LABEL", alarm.label)
                }
                findNavController().navigate(R.id.action_home_to_qrView, bundle)
            }
        )

        binding.recyclerAlarms.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAlarms.adapter = adapter

        viewModel.alarms.observe(viewLifecycleOwner) { alarms ->
            adapter.submitList(alarms)
            binding.tvEmpty.visibility = if (alarms.isEmpty()) View.VISIBLE else View.GONE
        }

        binding.fabAddAlarm.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_addAlarm)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
