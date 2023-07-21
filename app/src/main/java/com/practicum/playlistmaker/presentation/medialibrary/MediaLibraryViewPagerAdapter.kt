package com.practicum.playlistmaker.presentation.medialibrary

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.presentation.medialibrary.favouriteTracks.FavouriteTracksFragment
import com.practicum.playlistmaker.presentation.medialibrary.playlists.PlaylistsFragment

class MediaLibraryViewPagerAdapter (fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavouriteTracksFragment()
            else -> PlaylistsFragment()
        }
    }
}