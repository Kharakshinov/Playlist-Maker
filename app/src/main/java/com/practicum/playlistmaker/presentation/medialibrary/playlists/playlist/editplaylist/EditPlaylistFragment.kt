package com.practicum.playlistmaker.presentation.medialibrary.playlists.playlist.editplaylist

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.medialibrary.models.Playlist
import com.practicum.playlistmaker.presentation.medialibrary.playlists.newplaylist.NewPlaylistFragment
import com.practicum.playlistmaker.presentation.medialibrary.playlists.playlist.PlaylistState
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment: NewPlaylistFragment() {

    private val viewModel: EditPlaylistViewModel by viewModel()
    private lateinit var chosenPlaylist: Playlist

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setPlaylistNameTextWatcher()
        setPlaylistDescriptionTextWatcher()
        setPickMedia()
        clickListeners()

        viewModel.getPlaylistById(requireArguments().getInt(PLAYLIST_ID))

        viewModel.state.observe(viewLifecycleOwner){ state ->
            when(state){
                is PlaylistState.Content -> {
                    chosenPlaylist = state.playlist
                    initView()
                }
            }
        }
    }

    private fun initView(){
        val radius = resources.getDimensionPixelSize(R.dimen.dp_8)
        Glide.with(requireContext())
            .load(chosenPlaylist.playlistImage)
            .placeholder(R.drawable.icon_no_reply)
            .transform(RoundedCorners(radius))
            .into(binding.playlistPhotoTemplate)

        binding.playlistName.setText(chosenPlaylist.playlistName)
        binding.playlistDescription.setText("${chosenPlaylist.playlistDescription}")
        binding.buttonCreate.text = getString(R.string.save)
        binding.fragmentText.text = getString(R.string.edit)
    }

    override fun clickListeners() {
        binding.buttonGoBack.setOnClickListener{
            findNavController().navigateUp()
        }
        buttonCreateClickListener()
        playlistPhotoClickListener()
    }

    override fun buttonCreateClickListener(){
        binding.buttonCreate.setOnClickListener{
            if(imageUri != null)
                saveImageToPrivateStorage(imageUri!!)
             else
                fileString = chosenPlaylist.playlistImage
            viewModel.editPlaylist(
                chosenPlaylist.playlistId,
                binding.playlistName.text.toString(),
                binding.playlistDescription.text.toString(),
                fileString,
            )
            findNavController().navigateUp()
        }
    }

    companion object {
        const val PLAYLIST_ID = "playlist_id"
    }
}