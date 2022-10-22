package com.friendschat.notificationapp

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private var provider: NotificationProvider? = null
    private val requestPermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val deniedPermissions = arrayListOf<String>()
            permissions.entries.forEach {
                if (!it.value) {
                    deniedPermissions.add(it.key)
                }
                deniedPermissions.size.takeIf { size -> size > 0 }?.let {
                    showDialog()
                }
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        provider = NotificationProviderImpl(this)
        setOnClickListeners()
    }

    private fun setOnClickListeners() {
        findViewById<Button>(R.id.notification_btn).setOnClickListener {
            if(isPermissionGranted()) {
                provider?.showNotification(Counter.value)
            }
        }
        findViewById<Button>(R.id.permission_button).setOnClickListener {
            if (!isPermissionGranted()) {
                requestPermission()
            }
        }
    }

    private fun requestPermission() =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissions.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
        } else Unit

    private fun isPermissionGranted() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    } else true

    private fun showDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission required")
            .setMessage("Please grant all permissions to use this app")
            .setNegativeButton("Cancel") { dialog: DialogInterface, _ ->
                dialog.dismiss()
                finish()
            }
            .setPositiveButton("Ok") { dialog: DialogInterface, _ ->
                dialog.dismiss()
                startCameraPermissionSettingsPage()
            }.show()
    }

    private fun startCameraPermissionSettingsPage() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }
}