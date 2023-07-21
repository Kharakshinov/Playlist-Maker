package com.practicum.playlistmaker.medialibrary.presentation.favouriteTracks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.FavouriteTracksInteractor
import com.practicum.playlistmaker.medialibrary.domain.MediaLibraryInteractor
import com.practicum.playlistmaker.search.domain.model.TrackDomainSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteTracksViewModel(
    private val favouriteTracksInteractor: FavouriteTracksInteractor,
    private val mediaLibraryInteractor: MediaLibraryInteractor,
): ViewModel() {

    private val _state = MutableLiveData<FavouriteTracksState>()
    val state: LiveData<FavouriteTracksState> = _state

    fun downloadFavouriteTracks(){
        viewModelScope.launch{
            withContext(Dispatchers.IO){
                favouriteTracksInteractor
                    .getFavouriteTracks()
                    .collect() {
                        if(it.isEmpty())
                            _state.postValue(FavouriteTracksState.Empty)
                        else
                            _state.postValue(FavouriteTracksState.Content(it))
                    }
            }
        }
    }

    fun readFromSharedPreferences(): ArrayList<TrackDomainSearch>{
        return mediaLibraryInteractor.readFromSharedPreferences()
    }

    fun writeToSharedPreferences(trackList: ArrayList<TrackDomainSearch>){
        mediaLibraryInteractor.writeToSharedPreferences(trackList)
    }

}