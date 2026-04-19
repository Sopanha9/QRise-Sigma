package com.sopanha.qrisesigma.ui.alarm

import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.sopanha.qrisesigma.databinding.ActivityAlarmRingBinding
import com.sopanha.qrisesigma.service.AlarmService

class AlarmRingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAlarmRingBinding
    private var expectedQrContent: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Show on lock screen
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )

        binding = ActivityAlarmRingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        expectedQrContent = intent.getStringExtra("QR_CONTENT") ?: ""
        val label = intent.getStringExtra("ALARM_LABEL") ?: "Alarm"

        binding.tvAlarmLabel.text = label
        binding.tvInstruction.text = "Scan your QR code to dismiss"

        binding.btnScanQr.setOnClickListener {
            launchScanner()
        }
    }

    private fun launchScanner() {
        IntentIntegrator(this).apply {
            setPrompt("Scan your alarm QR code")
            setBeepEnabled(true)
            setOrientationLocked(false)
            setCaptureActivity(QRScanActivity::class.java)
            initiateScan()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: android.content.Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Scan cancelled", Toast.LENGTH_SHORT).show()
            } else {
                handleScanResult(result.contents)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun handleScanResult(scanned: String) {
        if (scanned == expectedQrContent) {
            // Correct QR — stop alarm
            AlarmService.stop(this)
            finish()
        } else {
            Toast.makeText(this, "Wrong QR code! Try again.", Toast.LENGTH_LONG).show()
        }
    }

    @android.annotation.SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        // Block back — must scan QR to dismiss
    }
}
