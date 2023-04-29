package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.domain.ISearchRepository
import com.practicum.playlistmaker.search.iTunesApi
import com.practicum.playlistmaker.search.domain.model.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchRepository(
    private val api: iTunesApi
) : ISearchRepository {

    override fun loadTracks(query: String, onSuccess: (List<Track>) -> Unit, onError: () -> Unit){
        api.searchTrack(query).enqueue(object :
        Callback<TracksResponse> {
        override fun onResponse(call: Call<TracksResponse>,
                                response: Response<TracksResponse>
        ) {
            if (response.code() == 200) {
                onSuccess.invoke(response.body()?.results!!)
            } else {
                onError.invoke()
            }
        }
        override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
            onError.invoke()
        }
    })
    }
}