package com.sopanha.qrisesigma.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sopanha.qrisesigma.data.model.Alarm
import com.sopanha.qrisesigma.databinding.ItemAlarmBinding
import com.sopanha.qrisesigma.util.AlarmScheduler

class AlarmAdapter(
    private val onToggle: (Alarm, Boolean) -> Unit,
    private val onDelete: (Alarm) -> Unit,
    private val onViewQr: (Alarm) -> Unit
) : ListAdapter<Alarm, AlarmAdapter.AlarmViewHolder>(DiffCallback()) {

    inner class AlarmViewHolder(private val binding: ItemAlarmBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(alarm: Alarm) {
            binding.tvTime.text = AlarmScheduler.formatTime(alarm.hour, alarm.minute)
            binding.tvLabel.text = alarm.label.ifEmpty { "Alarm" }
            binding.switchEnabled.isChecked = alarm.isEnabled

            binding.switchEnabled.setOnCheckedChangeListener { _, checked ->
                onToggle(alarm, checked)
            }

            binding.btnDelete.setOnClickListener { onDelete(alarm) }
            binding.btnViewQr.setOnClickListener { onViewQr(alarm) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val binding = ItemAlarmBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AlarmViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<Alarm>() {
        override fun areItemsTheSame(oldItem: Alarm, newItem: Alarm) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Alarm, newItem: Alarm) = oldItem == newItem
    }
}
