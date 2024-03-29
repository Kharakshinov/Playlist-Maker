package com.practicum.playlistmaker.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true
    private val viewModel: SettingsViewModel by viewModel()

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setStartState()

        viewModel.state.observe(viewLifecycleOwner){state ->
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
                    binding.buttonThemeSwitcher.isChecked = state.savedTheme
                }
                is SettingsState.DarkTheme -> {
                    viewModel.setTheme(state.isDark)
                }
            }
        }

        binding.buttonThemeSwitcher.setOnCheckedChangeListener { _, checked ->
            viewModel.switchThemeClicked(checked)
            viewModel.saveTheme(checked)
        }

        binding.buttonShareApplication.setOnClickListener {
            if(clickDebounce()){
                viewModel.shareApplicationClicked()
            }
        }

        binding.buttonWriteSupport.setOnClickListener {
            if(clickDebounce()) {
                viewModel.writeToSupportClicked()
            }
        }

        binding.buttonUserAgreement.setOnClickListener {
            if(clickDebounce()) {
                viewModel.openUserAgreementClicked()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed){
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
    }
}