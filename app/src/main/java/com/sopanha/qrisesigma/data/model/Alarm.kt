package com.sopanha.qrisesigma.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarms")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val label: String = "",
    val hour: Int,
    val minute: Int,
    val isEnabled: Boolean = true,
    val repeatDays: String = "", // e.g. "1,3,5" for Mon,Wed,Fri
    val qrCodeContent: String,   // The QR code value that must be scanned to dismiss
    val qrCodeBitmap: String = "",// Base64 encoded QR image for display
    val userId: String = "",      // Firebase UID
    val firestoreId: String = ""  // Firestore document ID
)
