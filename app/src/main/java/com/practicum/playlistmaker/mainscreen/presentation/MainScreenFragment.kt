package com.practicum.playlistmaker.mainscreen.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMainBinding
import com.practicum.playlistmaker.medialibrary.presentation.MediaLibraryFragment
import com.practicum.playlistmaker.search.presentation.SearchFragment
import com.practicum.playlistmaker.settings.presentation.SettingsFragment
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
                    parentFragmentManager.commit {
                        replace(
                            R.id.rootFragmentContainerView,
                            SearchFragment(),
                            SearchFragment.TAG
                        )

                        addToBackStack(SearchFragment.TAG)
                    }
                    viewModel.setStartState()
                }
                MainScreenState.OpenMediaLibrary -> {
                    parentFragmentManager.commit {
                        replace(
                            R.id.rootFragmentContainerView,
                            MediaLibraryFragment(),
                            MediaLibraryFragment.TAG
                        )

                        addToBackStack(MediaLibraryFragment.TAG)
                    }
                    viewModel.setStartState()
                }
                MainScreenState.OpenSettings -> {
                    parentFragmentManager.commit {
                        replace(
                            R.id.rootFragmentContainerView,
                            SettingsFragment(),
                            SettingsFragment.TAG
                        )

                        addToBackStack(SettingsFragment.TAG)
                    }
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