package com.practicum.playlistmaker.presentation.audioplayer

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityAudioplayerBinding
import com.practicum.playlistmaker.domain.audioplayer.model.TrackDomainAudioplayer
import com.practicum.playlistmaker.presentation.medialibrary.playlists.PlaylistsState
import com.practicum.playlistmaker.presentation.medialibrary.playlists.newplaylist.NewPlaylistFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity : AppCompatActivity() {

    private var _binding: ActivityAudioplayerBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var chosenTrack : TrackDomainAudioplayer
    private lateinit var url: String
    private lateinit var extras: Bundle
    private val viewModel: AudioPlayerViewModel by viewModel()
    private val playlistsAdapterAudioplayer: PlaylistsAdapterAudioplayer by lazy { PlaylistsAdapterAudioplayer() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAudioplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAdapter()
        setChosenTrack()
        setTrackData()
        setUpBottomSheetBehavior()
        viewModel.startPreparingPlayer(url)
        viewModel.checkTrackInFavourites(chosenTrack)
        viewModel.downloadPlaylists()

        viewModel.state.observe(this){ state ->
            when (state){
                AudioPlayerState.NotReady -> playButtonAvailability(false)
                AudioPlayerState.Ready -> playButtonAvailability(true)
                AudioPlayerState.OnStart -> {
                    updateTrackTimePassed(ZERO)
                    binding.buttonPlayTrack.updateButtonState()
                }
                AudioPlayerState.Pause -> {}
                is AudioPlayerState.Play -> {
                    updateTrackTimePassed(state.currentPosition)
                }
            }
        }

        viewModel.favourites.observe(this){ state ->
            when(state){
                TrackState.Liked -> {
                    showActiveAddToFavouritesButton()
                }
                TrackState.NotLiked -> {
                    hideActiveAddToFavouritesButton()
                }
            }
        }

        viewModel.statePlaylists.observe(this){ state ->
            when(state){
                is PlaylistsState.Content -> {
                    playlistsAdapterAudioplayer.playlists = state.playlists
                    playlistsAdapterAudioplayer.notifyDataSetChanged()
                    binding.recyclerViewPlaylists.visibility = View.VISIBLE
                }
                PlaylistsState.Empty -> {
                    binding.recyclerViewPlaylists.visibility = View.GONE
                }
            }
        }

        viewModel.trackToPlaylist.observe(this){ state ->
            when(state){
                is TrackToPlaylistState.InPlaylist -> {
                    val message = "Трек уже добавлен в плейлист ${state.playlistName}"
                    showToast(message)
                }
                is TrackToPlaylistState.NotInPlaylist -> viewModel.addTrackToPlaylist(state.track, state.playlist)
                is TrackToPlaylistState.SuccessfulAdd -> {
                    val message = "Добавлено в плейлист ${state.playlistName}"
                    showToast(message)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                    viewModel.downloadPlaylists()
                }
            }
        }

        binding.buttonGoBack.setOnClickListener {
            finish()
        }

        binding.buttonPlayTrack.onTouchListener = {
            viewModel.onPlayButtonClicked()
        }

        binding.buttonAddToPlaylist.setOnClickListener{
            binding.overlay.visibility = View.VISIBLE
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        binding.buttonNewPlaylist.setOnClickListener{
            val fragmentManager = supportFragmentManager
            val fragment = NewPlaylistFragment()
            fragmentManager.beginTransaction().add(R.id.rootFragmentContainerView, fragment).commit()
            binding.rootFragmentContainerView.visibility = View.VISIBLE
        }

        binding.buttonAddToFavourites.setOnClickListener{
            viewModel.addTrackToFavourites(chosenTrack)
        }

        binding.buttonAddToFavouritesActivated.setOnClickListener{
            viewModel.deleteTrackFromFavourites(chosenTrack)
        }

        playlistsAdapterAudioplayer.setOnTrackClickListener(object: PlaylistsAdapterAudioplayer.OnTrackClickListener {
            override fun onTrackClick(position: Int) {
                val chosenPlaylist = playlistsAdapterAudioplayer.playlists[position]
                viewModel.checkTrackInPlaylist(chosenTrack, chosenPlaylist)
            }
        })

    }

    override fun onPause() {
        super.onPause()
        viewModel.pausePlayer()
    }

    override fun onResume() {
        super.onResume()
        viewModel.downloadPlaylists()
    }

    private fun setChosenTrack(){
        extras = intent.extras!!
        val chosenTrackJSON = extras.getString(CHOSEN_TRACK)
        chosenTrack = Gson().fromJson(chosenTrackJSON, TrackDomainAudioplayer::class.java)
    }

    private fun setTrackData(){
        val radius = resources.getDimensionPixelSize(R.dimen.dp_8)
        Glide.with(applicationContext)
            .load(chosenTrack.artworkUrl100.replaceAfterLast('/',"512x512bb.jpg"))
            .centerInside()
            .placeholder(R.drawable.icon_no_reply)
            .transform(RoundedCorners(radius))
            .into(binding.trackImage)

        binding.trackName.text = chosenTrack.trackName
        binding.trackAuthor.text = chosenTrack.artistName
        binding.trackTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(chosenTrack.trackTimeMillis)
        binding.trackAlbum.text = chosenTrack.collectionName
        binding.trackYear.text = chosenTrack.releaseDate.take(4)
        binding.trackGenre.text = chosenTrack.primaryGenreName
        binding.trackCountry.text = chosenTrack.country
        url = chosenTrack.previewUrl
    }

    private fun playButtonAvailability(isAvailable: Boolean) {
        binding.buttonPlayTrack.isEnabled = isAvailable
    }

    private fun updateTrackTimePassed(position: String) {
        binding.trackTimePassed.text = position
    }

    private fun showActiveAddToFavouritesButton() {
        binding.buttonAddToFavouritesActivated.visibility = View.VISIBLE
    }

    private fun hideActiveAddToFavouritesButton() {
        binding.buttonAddToFavouritesActivated.visibility = View.GONE
    }

    private fun initAdapter() {
        binding.recyclerViewPlaylists.adapter = playlistsAdapterAudioplayer
        binding.recyclerViewPlaylists.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun setUpBottomSheetBehavior(){
        bottomSheetBehavior = BottomSheetBehavior.from(binding.standardBottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {

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

    private fun showToast(message: String){
        Toast.makeText(applicationContext, message, Toast.LENGTH_LONG)
            .show()
    }

    companion object {
        private const val CHOSEN_TRACK = "chosen_track"
        private const val ZERO = "00:00"
    }
}