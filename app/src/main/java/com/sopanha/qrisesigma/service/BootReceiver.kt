package com.sopanha.qrisesigma.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sopanha.qrisesigma.data.repository.AlarmRepository
import com.sopanha.qrisesigma.util.AlarmScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED ||
            intent.action == "android.intent.action.LOCKED_BOOT_COMPLETED"
        ) {
            val repo = AlarmRepository(context)
            CoroutineScope(Dispatchers.IO).launch {
                val alarms = repo.getEnabledAlarms()
                alarms.forEach { alarm ->
                    AlarmScheduler.schedule(context, alarm)
                }
            }
        }
    }
}
