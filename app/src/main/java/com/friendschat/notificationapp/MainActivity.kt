package com.friendschat.notificationapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val provider: NotificationProvider = NotificationProviderImpl(this)
        findViewById<Button>(R.id.notification_btn).setOnClickListener {
            provider.showNotification(Counter.value)
        }
    }
}