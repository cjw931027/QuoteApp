package com.example.quoteapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Initialize DataManager
        DataManager.init(this)

        // 2. Apply Theme
        if (DataManager.isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        setContentView(R.layout.activity_main)

        // 3. Setup Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        // 4. Setup Views
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        // Removed Drawer Views

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Setup AppBarConfiguration with top-level destinations (no back arrow)
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.homeFragment, R.id.categoryFragment, R.id.favoriteFragment, R.id.settingsFragment, R.id.loginFragment)
            // Removed drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        
        // 5. Visibility Logic
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.registerFragment -> {
                    bottomNav.visibility = View.GONE
                    toolbar.visibility = View.GONE
                }
                R.id.homeFragment -> {
                    bottomNav.visibility = View.VISIBLE
                    toolbar.visibility = View.GONE // Hide default toolbar, use fragment UI
                }
                R.id.categoryFragment, R.id.favoriteFragment, R.id.settingsFragment, R.id.categoryQuotesFragment -> {
                    bottomNav.visibility = View.VISIBLE
                    toolbar.visibility = View.GONE // Hide default toolbar, use fragment UI

                    // Update bottom nav selection
                    if (bottomNav.selectedItemId != destination.id && destination.id != R.id.categoryQuotesFragment) {
                        bottomNav.menu.findItem(destination.id)?.isChecked = true
                    }
                }
                R.id.addQuoteFragment, R.id.addCategoryFragment -> {
                    bottomNav.visibility = View.GONE
                    toolbar.visibility = View.GONE
                }
                else -> {
                    bottomNav.visibility = View.VISIBLE
                    toolbar.visibility = View.VISIBLE
                }
            }
        }

        // 6. Bottom Nav Listener
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.homeFragment -> {
                    if (navController.currentDestination?.id != R.id.homeFragment) {
                        navController.popBackStack(R.id.homeFragment, false)
                        navController.navigate(R.id.homeFragment)
                    }
                    true
                }
                R.id.categoryFragment -> {
                    if (navController.currentDestination?.id != R.id.categoryFragment) {
                        navController.popBackStack(R.id.categoryFragment, false)
                        navController.navigate(R.id.categoryFragment)
                    }
                    true
                }
                R.id.favoriteFragment -> {
                    if (navController.currentDestination?.id != R.id.favoriteFragment) {
                        navController.popBackStack(R.id.favoriteFragment, false)
                        navController.navigate(R.id.favoriteFragment)
                    }
                    true
                }
                R.id.settingsFragment -> {
                    if (navController.currentDestination?.id != R.id.settingsFragment) {
                        navController.popBackStack(R.id.settingsFragment, false)
                        navController.navigate(R.id.settingsFragment)
                    }
                    true
                }
                else -> false
            }
        }
    }

    // Handle Up button (Hamburger or Back)
    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}