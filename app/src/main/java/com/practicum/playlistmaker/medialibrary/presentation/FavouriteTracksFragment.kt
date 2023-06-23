package com.practicum.playlistmaker.medialibrary.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentFavouriteTracksBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTracksFragment: Fragment() {

    private val viewModel: FavouriteTracksViewModel by viewModel()

    private lateinit var binding: FragmentFavouriteTracksBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFavouriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
                binding.placeholderMessage.text = requireActivity().getString(R.string.empty_medialibrary)
    }
}