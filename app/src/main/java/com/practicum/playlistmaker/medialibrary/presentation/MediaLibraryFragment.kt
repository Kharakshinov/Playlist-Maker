package com.practicum.playlistmaker.medialibrary.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentMedialibraryBinding

class MediaLibraryFragment: Fragment() {

    companion object {
        private const val NUMBER = "number"

        fun newInstance(number: Int) = MediaLibraryFragment().apply {
            arguments = Bundle().apply {
                putInt(NUMBER, number)
            }
        }
    }

    private lateinit var binding: FragmentMedialibraryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMedialibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (requireArguments().getInt(NUMBER)) {
            1 -> {
                binding.placeholderMessage.text = requireActivity().getString(R.string.empty_medialibrary)
                binding.buttonNewPlaylist.visibility = View.GONE
            }
            2 -> {
                binding.placeholderMessage.text = requireActivity().getString(R.string.no_playlist)
                binding.buttonNewPlaylist.visibility = View.VISIBLE
            }
        }
    }
}