package com.devsung.bangull.service

import android.app.*
import android.content.Intent
import android.os.Build
import android.speech.tts.TextToSpeech
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import com.devsung.bangull.CallActivity
import com.devsung.bangull.MainActivity
import com.devsung.bangull.R
import com.devsung.bangull.data.Customer
import com.devsung.bangull.data.SQLite
import com.devsung.bangull.data.SettingRepository
import com.devsung.bangull.data.UserRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
class BangullService : LifecycleService() {

    companion object {
        var isAlive = false
    }

    private var tts: TextToSpeech? = null

    private val channelId = "foreground"
    private val channelIdCall = "call"
    private val idCall = 2

    private val ttsLoop = 100
    private val ttsDelay = 2000L

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        createNotificationChannel(channelId, "알림", false)
        val setting = SettingRepository(applicationContext).getSetting()
        val call = setting[0] || setting[1]
        if (call) callEvent(setting[0], setting[1])
        startForeground(
            1,
            NotificationCompat.Builder(this, channelId)
                .setContentTitle("전화 ${if (call) "ON" else "OFF"} & 출입 ${if (setting[2]) "ON" else "OFF"}")
                .setContentText("알림 서비스가 실행 중입니다.")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(
                    PendingIntent.getActivity(
                        this,
                        0,
                        Intent(this, MainActivity::class.java),
                        0
                    )
                )
                .build())
        return START_NOT_STICKY
    }

    private fun createNotificationChannel(id: String, name: String, import: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(NotificationChannel(
                    id,
                    name,
                    if (import) NotificationManager.IMPORTANCE_MAX
                    else NotificationManager.IMPORTANCE_DEFAULT
                ))
        }
    }

    private fun callEvent(voice: Boolean, popup: Boolean) {
        val manager = (this.getSystemService(TELEPHONY_SERVICE) as TelephonyManager)
        manager.listen(object : PhoneStateListener() {
            override fun onCallStateChanged(state: Int, phoneNumber: String) {
                if (state == TelephonyManager.CALL_STATE_RINGING) {
                    val path = "${UserRepository(applicationContext).getUser().salt}.db"
                    SQLite(applicationContext, path).getCustomer(phoneNumber)?.let {
                        if (voice) speakTTS(it.name)
                        if (popup) showPopup(it)
                    }
                }
            }
        }, PhoneStateListener.LISTEN_CALL_STATE)
    }

    private fun speakTTS(name: String) {
        val text = "${name}님에게 전화가 왔습니다."
        tts = TextToSpeech(applicationContext) {
            if (it == TextToSpeech.SUCCESS) {
                tts?.language = Locale.KOREAN
                for (i in 0..ttsLoop) {
                    tts?.speak(text, TextToSpeech.QUEUE_ADD, null, null)
                    tts?.playSilentUtterance(ttsDelay, TextToSpeech.QUEUE_ADD, null)
                }
            }
        }
        val timer = Timer()
        timer.schedule(object: TimerTask() {
            override fun run() {
                val manager = (getSystemService(TELEPHONY_SERVICE) as TelephonyManager)
                if (manager.callState == TelephonyManager.CALL_STATE_IDLE ||
                    manager.callState ==TelephonyManager.CALL_STATE_OFFHOOK) {
                    tts?.stop()
                    tts?.shutdown()
                    tts = null
                    timer.cancel()
                }
            }
        }, 500, 500)
    }

    private fun showPopup(customer: Customer) {
        val builder =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                NotificationCompat.Builder(this, channelIdCall)
            else
                NotificationCompat.Builder(this)
        createNotificationChannel(channelIdCall, "전화", true)
        (applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
            .notify(idCall, builder
                .setContentTitle("${customer.name}님에게 전화가 왔습니다.")
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentIntent(
                    PendingIntent.getActivity(
                        this,
                        0,
                        Intent(this, CallActivity::class.java).apply {
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            putExtra("customer", customer)
                        },
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
                .setAutoCancel(true)
                .build())
    }
}