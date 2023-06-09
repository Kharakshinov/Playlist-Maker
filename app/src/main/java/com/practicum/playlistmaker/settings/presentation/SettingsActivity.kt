package com.practicum.playlistmaker.settings.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.util.Creator

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonGoBack = findViewById<ImageView>(R.id.button_go_back)
        val buttonShareApplication = findViewById<Button>(R.id.button_share_application)
        val buttonWriteSupport = findViewById<Button>(R.id.button_write_support)
        val buttonOpenUserAgreement = findViewById<Button>(R.id.button_user_agreement)
        val buttonThemeSwitcher = findViewById<Switch>(R.id.switch1)

        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        val viewModel = ViewModelProvider(this, Creator.provideSettingsViewModelFactory(this, sharedPreferences))[SettingsViewModel::class.java]
        viewModel.setStartState()

        viewModel.state.observe(this){state ->
           when(state){
               SettingsState.ShareApplication -> {
                   viewModel.shareApplication()
                   viewModel.setStartState()
               }
               SettingsState.WriteToSupport -> {
                   viewModel.writeToSupport()
                   viewModel.setStartState()
               }
               SettingsState.OpenUserAgreement -> {
                   viewModel.openUserAgreement()
                   viewModel.setStartState()
               }
               SettingsState.Start -> {
                   buttonThemeSwitcher.isChecked = viewModel.getTheme()
               }
               is SettingsState.DarkTheme -> {
                   viewModel.setTheme(applicationContext, state.isDark)
               }
           }

       }

        buttonThemeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchThemeClicked(checked)
            viewModel.saveTheme(checked)
        }

        buttonGoBack.setOnClickListener {
            finish()
        }

        buttonShareApplication.setOnClickListener {
            viewModel.shareApplicationClicked()
        }

        buttonWriteSupport.setOnClickListener {
            viewModel.writeToSupportClicked()
        }

        buttonOpenUserAgreement.setOnClickListener {
            viewModel.openUserAgreementClicked()
        }
    }

    companion object {
        const val SHARED_PREFERENCES = "shared_preferences_playlistmaker"
    }
}