package com.sopanha.qrisesigma.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sopanha.qrisesigma.data.model.Alarm
import com.sopanha.qrisesigma.data.repository.AlarmRepository
import com.sopanha.qrisesigma.util.AlarmScheduler
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: AlarmRepository) : ViewModel() {
    val alarms = repo.allAlarms

    fun toggleAlarm(alarm: Alarm, enabled: Boolean, context: Context) {
        viewModelScope.launch {
            val updated = alarm.copy(isEnabled = enabled)
            repo.update(updated)
            if (enabled) {
                AlarmScheduler.schedule(context, updated)
            } else {
                AlarmScheduler.cancel(context, alarm.id)
            }
        }
    }

    fun deleteAlarm(alarm: Alarm, context: Context) {
        viewModelScope.launch {
            AlarmScheduler.cancel(context, alarm.id)
            repo.delete(alarm)
        }
    }
}

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return HomeViewModel(AlarmRepository(context)) as T
    }
}
