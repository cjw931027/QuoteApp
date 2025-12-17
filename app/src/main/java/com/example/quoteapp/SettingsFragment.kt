package com.example.quoteapp

import android.Manifest
import android.app.TimePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider
import java.util.Calendar
import java.util.Locale

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    // 處理 Android 13 通知的權限請求
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            DataManager.isNotificationEnabled = true
            DataManager.saveSettings(requireContext())
            NotificationScheduler.scheduleDailyNotification(requireContext())
        } else {
            Toast.makeText(requireContext(), "需要通知權限才能發送每日名言", Toast.LENGTH_LONG).show()
            view?.findViewById<SwitchCompat>(R.id.switch_notification)?.isChecked = false
            DataManager.isNotificationEnabled = false
            DataManager.saveSettings(requireContext())
        }
    }

    // [New] Gallery Launcher for Avatar Update
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            try {
                // Copy to internal storage
                val context = requireContext()
                val inputStream = context.contentResolver.openInputStream(uri)
                val avatarDir = java.io.File(context.filesDir, "avatars")
                if (!avatarDir.exists()) avatarDir.mkdirs()

                val fileName = "avatar_${System.currentTimeMillis()}.jpg"
                val file = java.io.File(avatarDir, fileName)
                val outputStream = java.io.FileOutputStream(file)

                inputStream?.copyTo(outputStream)
                inputStream?.close()
                outputStream.close()

                val newUri = file.absolutePath

                // Update Database
                DataManager.updateUserAvatar(newUri)

                // Update UI immediately
                view?.findViewById<android.widget.ImageView>(R.id.iv_settings_avatar)?.setImageURI(android.net.Uri.fromFile(file))
                
                Toast.makeText(context, "頭貼已更新", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "更新失敗", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutTheme = view.findViewById<LinearLayout>(R.id.layout_theme)
        val textThemeValue = view.findViewById<TextView>(R.id.text_theme_value)
        val sliderFontSize = view.findViewById<Slider>(R.id.slider_font_size)
        val switchNotification = view.findViewById<SwitchCompat>(R.id.switch_notification)
        val layoutTime = view.findViewById<LinearLayout>(R.id.layout_notification_time)
        val textTimeValue = view.findViewById<TextView>(R.id.text_time_value)

        // Profile UI
        val ivAvatar = view.findViewById<android.widget.ImageView>(R.id.iv_settings_avatar)
        val tvUsername = view.findViewById<TextView>(R.id.tv_settings_username)
        val tvEmail = view.findViewById<TextView>(R.id.tv_settings_email)
        val btnLogout = view.findViewById<android.widget.Button>(R.id.btn_settings_logout)

        // Click to change avatar
        ivAvatar.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        // Populate Profile
        val user = DataManager.getCurrentUser()
        if (user != null) {
            tvUsername.text = user.username
            tvEmail.text = user.email
            if (user.avatarUri != null) {
                ivAvatar.setImageURI(android.net.Uri.fromFile(java.io.File(user.avatarUri)))
            } else if (user.avatarResId != 0) {
                ivAvatar.setImageResource(user.avatarResId)
            } else {
                ivAvatar.setImageResource(R.drawable.ic_user_placeholder)
            }
        } else {
             // Guest or default
             tvUsername.text = "Guest"
             tvEmail.text = ""
             ivAvatar.setImageResource(R.drawable.ic_user_placeholder)
        }

        // Logout Logic
        btnLogout.setOnClickListener {
            DataManager.logout()
            // Navigate to Login and clear stack
            val navOptions = androidx.navigation.NavOptions.Builder()
                .setPopUpTo(R.id.nav_graph, true)
                .build()
            androidx.navigation.Navigation.findNavController(view).navigate(R.id.loginFragment, null, navOptions)
        }

        // 初始化狀態
        sliderFontSize.value = DataManager.fontSize
        updateThemeText(textThemeValue)

        // 設定通知 UI 初始狀態
        switchNotification.isChecked = DataManager.isNotificationEnabled
        updateTimeText(textTimeValue, DataManager.notificationHour, DataManager.notificationMinute)
        layoutTime.alpha = if (DataManager.isNotificationEnabled) 1.0f else 0.5f
        layoutTime.isEnabled = DataManager.isNotificationEnabled

        // 1. 顏色外觀
        layoutTheme.setOnClickListener {
            DataManager.isDarkMode = !DataManager.isDarkMode
            DataManager.saveSettings(requireContext())
            updateThemeText(textThemeValue)
            applyTheme()
        }

        // 2. 字體大小
        sliderFontSize.addOnChangeListener { _, value, _ ->
            DataManager.fontSize = value
            DataManager.saveSettings(requireContext())
        }

        // 3. 通知開關
        switchNotification.setOnCheckedChangeListener { buttonView, isChecked ->
            layoutTime.alpha = if (isChecked) 1.0f else 0.5f
            layoutTime.isEnabled = isChecked

            if (isChecked) {
                // 檢查並請求權限 (Android 13+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.POST_NOTIFICATIONS) !=
                        PackageManager.PERMISSION_GRANTED) {
                        requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        return@setOnCheckedChangeListener // 先不儲存，等權限結果
                    }
                }
            }

            // 儲存設定並排程
            DataManager.isNotificationEnabled = isChecked
            DataManager.saveSettings(requireContext())

            if (isChecked) {
                NotificationScheduler.scheduleDailyNotification(requireContext())
            } else {
                NotificationScheduler.cancelNotification(requireContext())
            }
        }

        // 4. 通知時間選擇器
        layoutTime.setOnClickListener {
            val hour = DataManager.notificationHour
            val minute = DataManager.notificationMinute

            TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                // 更新 UI
                updateTimeText(textTimeValue, selectedHour, selectedMinute)

                // 儲存設定
                DataManager.notificationHour = selectedHour
                DataManager.notificationMinute = selectedMinute
                DataManager.saveSettings(requireContext())

                // 重新排程
                if (DataManager.isNotificationEnabled) {
                    NotificationScheduler.scheduleDailyNotification(requireContext())
                }

            }, hour, minute, false).show()
        }
    }

    private fun updateThemeText(textView: TextView) {
        textView.text = if (DataManager.isDarkMode) "深色模式" else "系統預設"
    }

    private fun updateTimeText(textView: TextView, hour: Int, minute: Int) {
        val amPm = if (hour >= 12) "下午" else "上午"
        val displayHour = if (hour > 12) hour - 12 else if (hour == 0) 12 else hour
        val timeFormat = String.format(Locale.getDefault(), "%s %d:%02d", amPm, displayHour, minute)
        textView.text = timeFormat
    }

    private fun applyTheme() {
        if (DataManager.isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}