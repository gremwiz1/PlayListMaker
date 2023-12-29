package com.example.playlistmaker.data.dto

class TrackResponseBody(
    val resultCount: Int,
    var results: ArrayList<TrackDto>,
) : Response() {
    override fun toString(): String {
        return "TrackResponseBody(results=$results )"
    }
}