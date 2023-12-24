package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.models.Track

class TrackResponseBody(
    val resultCount: Int,
    var results: ArrayList<Track>,
) {
    override fun toString(): String {
        return "TrackResponseBody(results=$results )"
    }
}