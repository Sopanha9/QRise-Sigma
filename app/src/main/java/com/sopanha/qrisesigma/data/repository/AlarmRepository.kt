package com.sopanha.qrisesigma.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sopanha.qrisesigma.data.model.Alarm
import kotlinx.coroutines.tasks.await

class AlarmRepository(context: Context) {

    private val dao = AlarmDatabase.getInstance(context).alarmDao()
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    val allAlarms: LiveData<List<Alarm>> = dao.getAllAlarms()

    suspend fun insert(alarm: Alarm): Long {
        val id = dao.insert(alarm)
        syncToFirestore(alarm.copy(id = id.toInt()))
        return id
    }

    suspend fun update(alarm: Alarm) {
        dao.update(alarm)
        syncToFirestore(alarm)
    }

    suspend fun delete(alarm: Alarm) {
        dao.delete(alarm)
        deleteFromFirestore(alarm)
    }

    suspend fun getById(id: Int): Alarm? = dao.getAlarmById(id)

    suspend fun getEnabledAlarms(): List<Alarm> = dao.getEnabledAlarms()

    private fun syncToFirestore(alarm: Alarm) {
        val uid = auth.currentUser?.uid ?: return
        val data = mapOf(
            "label" to alarm.label,
            "hour" to alarm.hour,
            "minute" to alarm.minute,
            "isEnabled" to alarm.isEnabled,
            "repeatDays" to alarm.repeatDays,
            "qrCodeContent" to alarm.qrCodeContent,
            "localId" to alarm.id
        )
        firestore.collection("users").document(uid)
            .collection("alarms").document(alarm.id.toString())
            .set(data)
    }

    private fun deleteFromFirestore(alarm: Alarm) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid)
            .collection("alarms").document(alarm.id.toString())
            .delete()
    }
}
