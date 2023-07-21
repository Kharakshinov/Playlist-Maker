package com.practicum.playlistmaker.presentation.medialibrary.favouriteTracks

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practicum.playlistmaker.presentation.audioplayer.AudioPlayerActivity
import com.practicum.playlistmaker.databinding.FragmentFavouriteTracksBinding
import com.practicum.playlistmaker.domain.medialibrary.models.TrackDomainMediaLibrary
import com.practicum.playlistmaker.domain.search.model.TrackDomainSearch
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteTracksFragment: Fragment() {

    private val viewModel: FavouriteTracksViewModel by viewModel()

    private val favouriteTracksAdapter = FavouriteTracksAdapter()
    private lateinit var historyList: ArrayList<TrackDomainSearch>
    private var isClickAllowed = true

    private var _binding: FragmentFavouriteTracksBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        viewModel.downloadFavouriteTracks()

        viewModel.state.observe(viewLifecycleOwner){ state ->
            when(state) {
                is FavouriteTracksState.Content -> {
                    favouriteTracksAdapter.tracks = state.tracks
                    favouriteTracksAdapter.notifyDataSetChanged()
                    showRecyclerView()
                    hidePlaceHolder()
                }
                FavouriteTracksState.Empty -> {
                    hideRecyclerView()
                    showPlaceHolder()
                }
            }
        }

        favouriteTracksAdapter.setOnTrackClickListener(object: FavouriteTracksAdapter.onTrackClickListener {
            override fun onTrackClick(position: Int) {
                if(clickDebounce()){
                    val chosenTrack = mapTrackDomainFromMediaLibraryToSearch(favouriteTracksAdapter.tracks[position])
                    addTrackToHistory(chosenTrack)

                    val displayAudioPlayer = Intent(requireContext(), AudioPlayerActivity::class.java)
                    displayAudioPlayer.putExtra("chosen_track", Gson().toJson(chosenTrack))
                    startActivity(displayAudioPlayer)
                }
            }
        })

    }

    override fun onResume() {
        super.onResume()
        viewModel.downloadFavouriteTracks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun mapTrackDomainFromMediaLibraryToSearch(track: TrackDomainMediaLibrary): TrackDomainSearch {
        return TrackDomainSearch(
            trackId = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            previewUrl = track.previewUrl
        )
    }

    private fun initAdapter() {
        binding.recyclerViewFavouriteTracks.adapter = favouriteTracksAdapter
        binding.recyclerViewFavouriteTracks.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun addTrackToHistory(chosenTrack: TrackDomainSearch){
        historyList = viewModel.readFromSharedPreferences()
        if (historyList.size < 10){
            if(historyList.isNotEmpty()){
                if(historyList.contains(chosenTrack)){
                    historyList.remove(chosenTrack)
                }
                historyList.add(0, chosenTrack)
            } else {
                historyList.add(chosenTrack)
            }
        } else {
            if(historyList.contains(chosenTrack)){
                historyList.remove(chosenTrack)
                historyList.add(0, chosenTrack)
            } else {
                for(i in 9 downTo 1){
                    historyList[i] = historyList[i-1]
                }
                historyList[0] = chosenTrack
            }
        }
        viewModel.writeToSharedPreferences(historyList)
    }

    private fun showPlaceHolder(){
        binding.iconNothingFound.visibility = View.VISIBLE
        binding.placeholderMessage.visibility = View.VISIBLE
    }

    private fun hidePlaceHolder(){
        binding.iconNothingFound.visibility = View.GONE
        binding.placeholderMessage.visibility = View.GONE
    }

    private fun showRecyclerView(){
        binding.recyclerViewFavouriteTracks.visibility = View.VISIBLE
    }

    private fun hideRecyclerView(){
        binding.recyclerViewFavouriteTracks.visibility = View.GONE
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

    companion object {
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}
