package com.practicum.playlistmaker.medialibrary.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment() {

    private val viewModel: PlaylistsViewModel by viewModel()

    companion object {
        private const val TEXT = "text"

        fun newInstance(text: String) = PlaylistsFragment().apply {
            arguments = Bundle().apply {
                putString(TEXT, text)
            }
        }
    }

    private lateinit var binding: FragmentPlaylistsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
                binding.placeholderMessage.text = requireArguments().getString(TEXT)
                binding.buttonNewPlaylist.visibility = View.VISIBLE
    }
}