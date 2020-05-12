package com.rappi.interview.data.entity

import com.google.gson.annotations.SerializedName

data class ReviewResponse(
    val id: Int = 0,
    val page: Int = 0,
    val results: List<Review> = listOf(),
    @SerializedName("total_pages")
    val totalPages: Int = 0,
    @SerializedName("total_results")
    val totalResults: Int = 0
)

data class Review(
    val id: String = "",
    val author: String = "",
    val content: String = "",
    val url: String = ""
)