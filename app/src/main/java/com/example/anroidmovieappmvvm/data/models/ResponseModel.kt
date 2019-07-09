package com.example.anroidmovieappmvvm.data.models

import com.squareup.moshi.Json

data class ResponseModel(
    @Json(name = "page")
    val page: Int,
    @Json(name = "results")
    val movies: List<MovieModel>,
    @Json(name = "total_pages")
    val totalPages: Int,
    @Json(name = "total_results")
    val totalResults: Int
)