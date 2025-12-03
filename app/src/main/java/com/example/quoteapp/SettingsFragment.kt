package com.example.quoteapp

import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider
import java.util.Calendar
import java.util.Locale

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 綁定畫面上的元件
        val layoutTheme = view.findViewById<LinearLayout>(R.id.layout_theme)
        val textThemeValue = view.findViewById<TextView>(R.id.text_theme_value)
        val sliderFontSize = view.findViewById<Slider>(R.id.slider_font_size)
        val switchNotification = view.findViewById<SwitchCompat>(R.id.switch_notification)
        val layoutTime = view.findViewById<LinearLayout>(R.id.layout_notification_time)
        val textTimeValue = view.findViewById<TextView>(R.id.text_time_value)

        // 初始化狀態 (顯示目前的設定值)
        sliderFontSize.value = DataManager.fontSize
        updateThemeText(textThemeValue)

        // 1. 顏色外觀 (深色模式切換)
        layoutTheme.setOnClickListener {
            DataManager.isDarkMode = !DataManager.isDarkMode // 切換狀態

            // 儲存設定
            DataManager.saveSettings(requireContext())

            updateThemeText(textThemeValue)
            applyTheme()
        }

        // 2. 字體大小 Slider
        sliderFontSize.addOnChangeListener { _, value, _ ->
            DataManager.fontSize = value
            // 儲存設定
            DataManager.saveSettings(requireContext())
        }

        // 3. 通知開關
        switchNotification.setOnCheckedChangeListener { _, isChecked ->
            // 如果關閉通知，就把時間設定變成半透明，且不可點擊
            layoutTime.alpha = if (isChecked) 1.0f else 0.5f
            layoutTime.isEnabled = isChecked
        }

        // 4. 通知時間選擇器
        layoutTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            // 跳出時間選擇視窗
            TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
                val timeFormat = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
                textTimeValue.text = "上午 $timeFormat" // 這裡簡單顯示，你可以改進上午/下午判斷
            }, hour, minute, false).show()
        }
    }

    // 更新文字顯示 (深色/淺色)
    private fun updateThemeText(textView: TextView) {
        textView.text = if (DataManager.isDarkMode) "深色模式" else "系統預設"
    }

    // 套用主題
    private fun applyTheme() {
        if (DataManager.isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}