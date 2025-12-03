package com.example.quoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. 讀取設定
        DataManager.loadSettings(this)

        // 2. 應用深色模式主題 (必須在 setContentView 之前設定)
        if (DataManager.isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        // 監聽頁面切換，如果在「新增」頁面，就隱藏底部導覽列
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.addQuoteFragment, R.id.addCategoryFragment -> {
                    bottomNav.visibility = View.GONE
                }
                else -> {
                    bottomNav.visibility = View.VISIBLE
                }
            }
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    navController.popBackStack(R.id.homeFragment, false)
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.categoryFragment -> {
                    navController.popBackStack(R.id.categoryFragment, false)
                    navController.navigate(R.id.categoryFragment)
                    true
                }
                R.id.favoriteFragment -> {
                    navController.popBackStack(R.id.favoriteFragment, false)
                    navController.navigate(R.id.favoriteFragment)
                    true
                }
                R.id.settingsFragment -> {
                    navController.popBackStack(R.id.settingsFragment, false)
                    navController.navigate(R.id.settingsFragment)
                    true
                }
                else -> false
            }
        }

        // 避免旋轉螢幕或深色模式切換重啟 Activity 時，重設選取項目
        if (savedInstanceState == null) {
            bottomNav.selectedItemId = R.id.homeFragment
        }
    }
}