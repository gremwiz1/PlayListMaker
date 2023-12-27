package com.example.playlistmaker.domain.models

data class TrackSearchResult<T>(
    val data: T?,
    val isSuccess: Boolean,
)
