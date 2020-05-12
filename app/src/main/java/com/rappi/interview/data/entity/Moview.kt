package com.rappi.interview.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.rappi.interview.BuildConfig
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "favorites_movie", ignoredColumns = ["genreIds", "genres"])
data class Movie(
    var popularity: Double = 0.0,
    @SerializedName("vote_count")
    var voteCount: Int = 0,
    var video: Boolean = false,
    @SerializedName("poster_path")
    var posterPath: String = "",
    @PrimaryKey
    var id: Int = 0,
    var adult: Boolean = false,
    @SerializedName("backdrop_path")
    var backdropPath: String = "",
    @SerializedName("original_language")
    var originalLanguage: String = "",
    @SerializedName("original_title")
    var originalTitle: String = "",
    @SerializedName("genre_ids")
    var genreIds: List<Int> = listOf(),
    var title: String = "",
    @SerializedName("vote_average")
    var voteAverage: Double = 0.0,
    var overview: String = "",
    @SerializedName("release_date")
    var releaseDate: String = "",
    var genres: List<Genre> = listOf()
) : Parcelable {
    fun bannerUrl() = "${BuildConfig.IMAGE_URL}$backdropPath"
    fun posterUrl() = "${BuildConfig.IMAGE_URL}$posterPath"
}

@Parcelize
data class Genre(
    val id: Int = 0,
    val name: String = ""
) : Parcelable

data class MovieResponse(
    val page: Int = 0,
    @SerializedName("total_results")
    val totalResults: Int = 0,
    @SerializedName("total_pages")
    val totalPages: Int = 0,
    val results: List<Movie> = listOf()
)