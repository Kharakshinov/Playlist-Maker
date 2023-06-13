package com.practicum.playlistmaker.settings.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import com.practicum.playlistmaker.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsActivity : AppCompatActivity() {

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonGoBack = findViewById<ImageView>(R.id.button_go_back)
        val buttonShareApplication = findViewById<Button>(R.id.button_share_application)
        val buttonWriteSupport = findViewById<Button>(R.id.button_write_support)
        val buttonOpenUserAgreement = findViewById<Button>(R.id.button_user_agreement)
        val buttonThemeSwitcher = findViewById<Switch>(R.id.switch1)

        val viewModel: SettingsViewModel by viewModel()
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
               is SettingsState.Start -> {
                   buttonThemeSwitcher.isChecked = state.savedTheme
               }
               is SettingsState.DarkTheme -> {
                   viewModel.setTheme(state.isDark)
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
            if(clickDebounce()){
                viewModel.shareApplicationClicked()
            }
        }

        buttonWriteSupport.setOnClickListener {
            if(clickDebounce()) {
                viewModel.writeToSupportClicked()
            }
        }

        buttonOpenUserAgreement.setOnClickListener {
            if(clickDebounce()) {
                viewModel.openUserAgreementClicked()
            }
        }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed){
            isClickAllowed = false
            handler.postDelayed({isClickAllowed = true}, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}