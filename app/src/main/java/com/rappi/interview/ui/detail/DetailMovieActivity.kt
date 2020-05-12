package com.rappi.interview.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rappi.interview.R
import com.rappi.interview.abstraction.state.FavoriteState
import com.rappi.interview.abstraction.state.LoaderState
import com.rappi.interview.abstraction.util.DateUtils
import com.rappi.interview.abstraction.util.load
import com.rappi.interview.abstraction.util.showToast
import com.rappi.interview.abstraction.util.viewModelProvider
import com.rappi.interview.data.entity.Genre
import com.rappi.interview.data.entity.Movie
import com.rappi.interview.data.entity.Review
import com.rappi.interview.di.DaggerMainComponent
import com.rappi.interview.di.module.MovieModule
import com.rappi.interview.di.module.RoomModule
import com.rappi.interview.viewmodel.DetailMovieViewModel
import com.rappi.interview.viewmodel.FavoriteMovieViewModel
import kotlinx.android.synthetic.main.activity_detail_movie.*
import kotlinx.android.synthetic.main.content_detail.*
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class DetailMovieActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var detailViewModel: DetailMovieViewModel
    private lateinit var favoriteViewModel: FavoriteMovieViewModel
    private var movie = Movie()
    private var isFavorite: Boolean = false
    private lateinit var _adapter: ReviewAdapter
    private val reviews = mutableListOf<Review>()

    companion object {
        private const val ID_MOVIE = "id_movie"
        @JvmStatic
        fun generateIntent(context: Context?, id: Int): Intent {
            return Intent(context, DetailMovieActivity::class.java).apply {
                putExtra(ID_MOVIE, id)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_movie)
        val extras = intent.extras

        initInjector()
        detailViewModel = viewModelProvider(viewModelFactory)
        favoriteViewModel = viewModelProvider(viewModelFactory)

        if (extras != null) {
            val idMovie = extras.getInt(ID_MOVIE)
            detailViewModel.getDetailMovie(idMovie)
            detailViewModel.getReviewMovie(idMovie)
        }

        initView()
        initObservable()
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        _adapter = ReviewAdapter(reviews)
        list_review.apply {
            layoutManager = LinearLayoutManager(
                this@DetailMovieActivity
            )
            adapter = _adapter
        }
        img_favorite.setOnClickListener {
            if (isFavorite) {
                favoriteViewModel.deleteFavoriteMovie(movie)
            } else {
                favoriteViewModel.insertFavoriteMovie(movie)
            }
            favoriteViewModel.getAllFavoriteMovie()
        }
    }

    private fun initObservable() {
        detailViewModel.error.observe(this, Observer {
            showToast(it)
        })
        detailViewModel.state.observe(this, Observer {
            when (it) {
                is LoaderState.ShowLoading -> {
                    showToast("Loading...")
                }
                is LoaderState.HideLoading -> {
                    showToast("Complete!")
                }
            }
        })
        detailViewModel.detailMovie.observe(this, Observer {
            this.movie = it
            updateUi(it)
            prepareRecGenres(it.genres)
            favoriteViewModel.getAllFavoriteMovie()

        })
        detailViewModel.reviews.observe(this, Observer {
            reviews.clear()
            reviews.addAll(it.results)
            _adapter.notifyDataSetChanged()
        })
        favoriteViewModel.favoriteMovies.observe(this, Observer {
            this.isFavorite = it.any { x ->
                x.id == movie.id
            }
            setImageFavorite(isFavorite)
        })
        favoriteViewModel.favoriteState.observe(this, Observer {
            when (it) {
                is FavoriteState.InsertSuccess -> {
                    showToast(" Insert Data Success")
                }
                is FavoriteState.DeleteSuccess -> {
                    showToast("Delete Data Success")
                }
            }
        })
    }

    private fun setImageFavorite(isFavorite: Boolean) {
        if (isFavorite) {
            img_favorite.setImageResource(R.drawable.ic_favorite_black_24dp)
        } else {
            img_favorite.setImageResource(R.drawable.ic_favorite_border_black_24dp)
        }
    }

    private fun updateUi(movie: Movie) {
        collapsing_toolbar.title = movie.originalTitle
        img_back_drop.load(movie.bannerUrl())
        img_poster.load(movie.posterUrl())
        val date = SimpleDateFormat(
            DateUtils.NORMAL_DATE_PATTERN,
            Locale.getDefault()
        ).parse(movie.releaseDate)
        txt_name.text = movie.originalTitle
        txt_date_rilis.text = DateUtils.format(date, DateUtils.FULL_DATE_PATTERN)
        rat_rating.rating = (movie.voteAverage / 2).toFloat()
        rat_rating.isEnabled = false
        txt_overview.text = movie.overview
    }

    private fun prepareRecGenres(genres: List<Genre>?) {
        val adapter = GenreAdapter()
        adapter.setGenre(genres)
        rv_genre.adapter = adapter
        rv_genre.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
    }

    private fun initInjector() {
        DaggerMainComponent
            .builder()
            .roomModule(RoomModule(application))
            .movieModule(MovieModule())
            .build()
            .injectDetail(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


}