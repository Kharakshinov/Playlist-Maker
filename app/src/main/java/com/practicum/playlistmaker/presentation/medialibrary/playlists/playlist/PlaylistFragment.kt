package com.practicum.playlistmaker.presentation.medialibrary.playlists.playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.domain.medialibrary.models.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment() {

    private val viewModel: PlaylistViewModel by viewModel()

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private var bottomNavigationView: BottomNavigationView? = null
    private var bottomNavigationViewLine: View? = null

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var chosenPlaylist: Playlist

    private val trackAdapter: PlaylistTrackAdapter by lazy { PlaylistTrackAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigationView()
        initAdapter()

        val playlistId = requireArguments().getInt(PLAYLIST_ID)
        viewModel.getPlaylistById(playlistId)
        viewModel.state.observe(viewLifecycleOwner){ state ->
            when(state){
                is PlaylistState.Content -> {
                    chosenPlaylist = state.playlist
                    viewModel.getTracksInPlaylist(chosenPlaylist.addedTracksId)
                }
            }
        }

        viewModel.tracks.observe(viewLifecycleOwner){ state ->
            when(state){
                is TracksInPlaylistState.Content -> {
                    trackAdapter.tracks = state.tracks
                    trackAdapter.notifyDataSetChanged()
                    initView()
                }
                TracksInPlaylistState.Empty -> {
                    initView()
                }
            }
        }

        binding.buttonGoBack.setOnClickListener{
            findNavController().navigateUp()
        }

        bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        val bottomSheetPeekHeight = (resources.displayMetrics.heightPixels*0.3).toInt()
        bottomSheetBehavior.peekHeight = bottomSheetPeekHeight
    }

    override fun onResume() {
        super.onResume()
        hideBottomNavigationView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        showBottomNavigationView()
        _binding = null
    }

    private fun hideBottomNavigationView(){
            bottomNavigationView = requireActivity().findViewById(R.id.bottomNavigationView)
            bottomNavigationViewLine = requireActivity().findViewById(R.id.bottomNavigationViewLine)
            bottomNavigationView!!.visibility = View.GONE
            bottomNavigationViewLine!!.visibility = View.GONE
    }

    private fun showBottomNavigationView(){
            bottomNavigationView!!.visibility = View.VISIBLE
            bottomNavigationViewLine!!.visibility = View.VISIBLE
    }

    private fun initView(){
        Glide.with(requireContext())
            .load(chosenPlaylist.playlistImage)
            .placeholder(R.drawable.icon_no_reply)
            .into(binding.playlistPhoto)

        binding.playlistName.text = chosenPlaylist.playlistName
        binding.playlistDescription.text = chosenPlaylist.playlistDescription

        var trackTimeSumSeconds: Long = 0
        trackAdapter.tracks.forEach { trackTimeSumSeconds += it.trackTimeMillis/1000 }
        val trackTimeSumMin = (trackTimeSumSeconds/60).toInt()
        val trackTimeSumEnding = resources.getQuantityString(R.plurals.plurals_2,
            trackTimeSumMin, trackTimeSumMin)
        binding.playlistTime.text = trackTimeSumEnding

        val trackWordEnding = resources.getQuantityString(R.plurals.plurals_1, chosenPlaylist.addedTracksNumber, chosenPlaylist.addedTracksNumber)
        binding.playlistTracksNumber.text = trackWordEnding
    }

    private fun initAdapter() {
        binding.recyclerViewPlaylists.adapter = trackAdapter
        binding.recyclerViewPlaylists.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    companion object {
        const val PLAYLIST_ID = "playlist_id"
    }
}