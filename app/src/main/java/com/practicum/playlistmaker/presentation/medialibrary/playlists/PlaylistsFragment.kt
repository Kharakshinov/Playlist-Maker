package com.practicum.playlistmaker.presentation.medialibrary.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistsFragment: Fragment() {

    private val viewModel: PlaylistsViewModel by viewModel()

    private val playListsAdapterMedialibrary: PlaylistsAdapterMedialibrary by lazy { PlaylistsAdapterMedialibrary() }

    private var _binding: FragmentPlaylistsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        viewModel.downloadPlaylists()

        viewModel.state.observe(viewLifecycleOwner){ state ->
            when(state){
                is PlaylistsState.Content -> {
                    playListsAdapterMedialibrary.playlists = state.playlists
                    playListsAdapterMedialibrary.notifyDataSetChanged()
                    binding.recyclerViewPlaylists.visibility = View.VISIBLE
                    binding.placeholderMessage.visibility = View.GONE
                    binding.iconNothingFound.visibility = View.GONE
                }
                PlaylistsState.Empty -> {
                    binding.recyclerViewPlaylists.visibility = View.GONE
                    binding.placeholderMessage.visibility = View.VISIBLE
                    binding.iconNothingFound.visibility = View.VISIBLE
                }
            }
        }

        binding.buttonNewPlaylist.visibility = View.VISIBLE

        binding.buttonNewPlaylist.setOnClickListener(){
            findNavController().navigate(R.id.action_mediaLibraryFragment_to_newPlaylistFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.downloadPlaylists()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter() {
        binding.recyclerViewPlaylists.adapter = playListsAdapterMedialibrary
        binding.recyclerViewPlaylists.layoutManager = GridLayoutManager(requireContext(), 2)
    }
}