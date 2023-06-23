package com.practicum.playlistmaker.mainscreen.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentMainBinding
import com.practicum.playlistmaker.medialibrary.presentation.MediaLibrary
import com.practicum.playlistmaker.search.presentation.SearchActivity
import com.practicum.playlistmaker.settings.presentation.SettingsActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainScreenFragment: Fragment() {

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())
    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel: MainScreenViewModel by viewModel()
        viewModel.state.observe(viewLifecycleOwner){ state ->
            when(state){
                MainScreenState.OpenSearch -> {
                    val displaySearch = Intent(requireContext(), SearchActivity::class.java)
                    startActivity(displaySearch)
                    viewModel.setStartState()
                }
                MainScreenState.OpenMediaLibrary -> {
                    val displayMediaLibrary = Intent(requireContext(), MediaLibrary::class.java)
                    startActivity(displayMediaLibrary)
                    viewModel.setStartState()
                }
                MainScreenState.OpenSettings -> {
                    val displaySettings = Intent(requireContext(), SettingsActivity::class.java)
                    startActivity(displaySettings)
                    viewModel.setStartState()
                }
                MainScreenState.Start -> {}
            }

        }

        binding.buttonSearch.setOnClickListener {
            if(clickDebounce()){
                viewModel.searchScreenOpened()
            }
        }

        binding.buttonMediaLibrary.setOnClickListener {
            if(clickDebounce()) {
                viewModel.mediaLibraryOpened()
            }
        }

        binding.buttonSettings.setOnClickListener {
            if(clickDebounce()) {
                viewModel.settingsOpened()
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