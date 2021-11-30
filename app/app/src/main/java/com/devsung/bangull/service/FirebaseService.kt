package com.devsung.bangull.service

import android.content.ClipData
import android.content.ClipboardManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import android.telephony.TelephonyManager
import android.widget.Toast
import com.devsung.bangull.R
import com.devsung.bangull.data.SettingRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class FirebaseService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Handler(Looper.getMainLooper()).post {
            (applicationContext.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager)
                .setPrimaryClip(ClipData.newPlainText("key", p0))
            Toast.makeText(applicationContext, "토큰이 복사되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        val setting = SettingRepository(applicationContext).getSetting()[2]
        val manager = (getSystemService(TELEPHONY_SERVICE) as TelephonyManager)
        if (setting &&
            BangullService.isAlive &&
            manager.callState == TelephonyManager.CALL_STATE_IDLE) {
            MediaPlayer.create(applicationContext, R.raw.door).apply {
                setOnCompletionListener { this.release() }
                start()
            }
        }
    }
}