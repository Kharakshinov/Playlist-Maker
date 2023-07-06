package com.practicum.playlistmaker.search.data

import com.practicum.playlistmaker.search.domain.model.Track

class TracksSearchResponse(
    val results: List<Track>
) : Response()