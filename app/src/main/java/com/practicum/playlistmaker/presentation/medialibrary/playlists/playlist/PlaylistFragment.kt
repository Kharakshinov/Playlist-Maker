package com.practicum.playlistmaker.presentation.medialibrary.playlists.playlist

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentPlaylistBinding
import com.practicum.playlistmaker.domain.medialibrary.models.Playlist
import com.practicum.playlistmaker.domain.medialibrary.models.TrackDomainMediaLibrary
import com.practicum.playlistmaker.domain.search.model.TrackDomainSearch
import com.practicum.playlistmaker.presentation.audioplayer.AudioPlayerActivity
import com.practicum.playlistmaker.presentation.search.SearchFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistFragment: Fragment() {

    private val viewModel: PlaylistViewModel by viewModel()

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private var isClickAllowed = true
    private val trackAdapter: PlaylistTrackAdapter by lazy { PlaylistTrackAdapter() }

    private var bottomNavigationView: BottomNavigationView? = null
    private var bottomNavigationViewLine: View? = null

    private lateinit var bottomSheetFixed: BottomSheetBehavior<LinearLayout>
    private lateinit var bottomSheetMenu: BottomSheetBehavior<LinearLayout>
    private lateinit var chosenPlaylist: Playlist
    private lateinit var confirmDialogDeleteTrack: MaterialAlertDialogBuilder
    private lateinit var confirmDialogDeletePlaylist: MaterialAlertDialogBuilder
    private var trackWordEnding = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideBottomNavigationView()
        initAdapter()
        bottomSheetBehavior()

        viewModel.getPlaylistById(requireArguments().getInt(PLAYLIST_ID))

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
                    binding.noTracksTextview.visibility = View.GONE
                }
                TracksInPlaylistState.Empty -> {
                    trackAdapter.tracks = arrayListOf()
                    trackAdapter.notifyDataSetChanged()
                    initView()
                    binding.noTracksTextview.visibility = View.VISIBLE
                }
            }
        }

        clickListeners()
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

    private fun initAdapter() {
        binding.recyclerViewPlaylists.adapter = trackAdapter
        binding.recyclerViewPlaylists.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }

    private fun bottomSheetBehavior(){
        bottomSheetFixed = BottomSheetBehavior.from(binding.standardBottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        val bottomSheetPeekHeight = (resources.displayMetrics.heightPixels*0.3).toInt()
        bottomSheetFixed.peekHeight = bottomSheetPeekHeight

        bottomSheetMenu = BottomSheetBehavior.from(binding.menuBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetMenu.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {

                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        binding.overlay.visibility = View.GONE
                    }
                    else -> {
                        binding.overlay.visibility = View.VISIBLE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
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

        trackWordEnding = resources.getQuantityString(R.plurals.plurals_1, chosenPlaylist.addedTracksNumber, chosenPlaylist.addedTracksNumber)
        binding.playlistTracksNumber.text = trackWordEnding
    }

    private fun showToast(){
        val message = requireActivity().getString(R.string.no_tracks_in_playlist)
        Toast.makeText(requireActivity().applicationContext, message, Toast.LENGTH_LONG)
            .show()
    }

    private fun clickListeners(){
        binding.buttonGoBack.setOnClickListener{
            findNavController().navigateUp()
        }

        binding.iconShare.setOnClickListener{
            shareTracks()
        }

        binding.iconMore.setOnClickListener{
            bottomSheetMenu.state = BottomSheetBehavior.STATE_COLLAPSED
            initBottomSheetMenu()
        }

        binding.bottomSheetButtonShare.setOnClickListener{
            shareTracks()
        }

        binding.bottomSheetButtonDeletePlaylist.setOnClickListener{
            setConfirmDialogDeletePlaylist(chosenPlaylist)
            confirmDialogDeletePlaylist.show()
        }

        setOnTrackClickListener()
        setOnLongTrackClickListener()
    }

    private fun setOnTrackClickListener(){
        trackAdapter.setOnTrackClickListener(object: PlaylistTrackAdapter.OnTrackClickListener {
            override fun onTrackClick(position: Int) {
                if(clickDebounce()){
                    val chosenTrack = mapTrackDomainFromMediaLibraryToSearch(trackAdapter.tracks[position])
                    addTrackToHistory(chosenTrack)

                    val displayAudioPlayer = Intent(requireContext(), AudioPlayerActivity::class.java)
                    displayAudioPlayer.putExtra("chosen_track", Gson().toJson(chosenTrack))
                    startActivity(displayAudioPlayer)
                }
            }
        })
    }

    private fun setOnLongTrackClickListener(){
        trackAdapter.setOnLongClickListener(object: PlaylistTrackAdapter.OnLongTrackClickListener {
            override fun onLongTrackClick(position: Int) {
                setConfirmDialogDeleteTrack(trackAdapter.tracks[position])
                confirmDialogDeleteTrack.show()
            }
        })
    }

    private fun setConfirmDialogDeleteTrack(chosenTrack: TrackDomainMediaLibrary){
        confirmDialogDeleteTrack = MaterialAlertDialogBuilder(requireContext())
            .setTitle(requireActivity().getString(R.string.delete_track))
            .setMessage(requireActivity().getString(R.string.shure_to_delete_playlist))
            .setNegativeButton(requireActivity().getString(R.string.cancel)) { _, _ ->

            }.setPositiveButton(requireActivity().getString(R.string.delete)) { _, _ ->
                viewModel.deleteTrackFromPlaylist(chosenTrack, chosenPlaylist)
                viewModel.getTracksInPlaylist(chosenPlaylist.addedTracksId)
            }
    }

    private fun setConfirmDialogDeletePlaylist(chosenPlaylist: Playlist){
        confirmDialogDeletePlaylist = MaterialAlertDialogBuilder(requireContext())
            .setTitle("Хотите удалить плейлист \"${chosenPlaylist.playlistName}\"?")
            .setNegativeButton(requireActivity().getString(R.string.no)) { _, _ ->

            }.setPositiveButton(requireActivity().getString(R.string.yes)) { _, _ ->
                viewModel.deletePlaylist(chosenPlaylist.playlistId)
                findNavController().navigateUp()
            }
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed){
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(SearchFragment.CLICK_DEBOUNCE_DELAY)
                isClickAllowed = true
            }
        }
        return current
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

    private fun addTrackToHistory(chosenTrack: TrackDomainSearch){
        val historyList = viewModel.readFromSharedPreferences()
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

    private fun shareTracks() {
        if(trackAdapter.tracks.isEmpty()){
            showToast()
        } else {
            val trackWordEnding = resources.getQuantityString(R.plurals.plurals_1, chosenPlaylist.addedTracksNumber, chosenPlaylist.addedTracksNumber)

            var number = 1
            var tracksTable = ""
            while(number <= chosenPlaylist.addedTracksNumber){
                tracksTable += "$number. ${trackAdapter.tracks[number - 1].artistName} - ${trackAdapter.tracks[number - 1].trackName} - ${SimpleDateFormat("mm:ss", Locale.getDefault()).format(trackAdapter.tracks[number - 1].trackTimeMillis)}\n"
                number++
            }

            val tracksInfo = "${chosenPlaylist.playlistName}\n${chosenPlaylist.playlistDescription}\n$trackWordEnding\n$tracksTable"
            val sendIntent : Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, tracksInfo)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, null)
            requireActivity().startActivity(shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    private fun initBottomSheetMenu(){
        val radius = resources.getDimensionPixelSize(R.dimen.dp_2)
        Glide.with(requireContext())
            .load(chosenPlaylist.playlistImage)
            .centerInside()
            .placeholder(R.drawable.icon_no_reply)
            .transform(RoundedCorners(radius))
            .into(binding.bottomSheetPlaylistPhoto)

        binding.bottomSheetPlaylistName.text = chosenPlaylist.playlistName
        binding.bottomSheetTracksNumber.text = trackWordEnding
    }

    companion object {
        const val PLAYLIST_ID = "playlist_id"
    }
}