package com.github.yeetologist.githubusers.ui.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.github.yeetologist.githubusers.R
import com.github.yeetologist.githubusers.ui.SettingPreferences
import com.github.yeetologist.githubusers.ui.dataStore
import com.github.yeetologist.githubusers.ui.viewmodel.ViewModelFactory
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        val switchTheme = findViewById<SwitchMaterial>(R.id.switch_theme)

        val preferences = SettingPreferences.getInstance(application.dataStore)

        val settingViewModel = ViewModelProvider(this, ViewModelFactory(preferences))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) {
            if (it) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }

        }
        switchTheme.setOnCheckedChangeListener { _, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }
}