package com.friendschat.notificationapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class CounterNotificationBroadcasr: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val manager: NotificationProvider = NotificationProviderImpl(context)
        manager.showNotification(++Counter.value)
    }
}