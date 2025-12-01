package com.example.quoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        // ❌ 不要再用這行了：bottomNav.setupWithNavController(navController)

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

        // 預設選到首頁
        bottomNav.selectedItemId = R.id.homeFragment
    }
}
