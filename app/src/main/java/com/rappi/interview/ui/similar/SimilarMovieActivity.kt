package com.rappi.interview.ui.similar

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rappi.interview.R
import com.rappi.interview.abstraction.state.LoaderState
import com.rappi.interview.abstraction.util.showToast
import com.rappi.interview.abstraction.util.viewModelProvider
import com.rappi.interview.di.DaggerMainComponent
import com.rappi.interview.di.module.MovieModule
import com.rappi.interview.di.module.RoomModule
import com.rappi.interview.viewmodel.SimilarMovieViewModel
import javax.inject.Inject

class SimilarMovieActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var similarViewModel: SimilarMovieViewModel

    companion object {
        private const val ID_MOVIE = "id_movie"
        @JvmStatic
        fun generateIntent(context: Context, id: Int): Intent {
            return Intent(context, SimilarMovieActivity::class.java).apply {
                putExtra(ID_MOVIE, id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_similar)
        val extras = intent.extras

        initInjector()
        similarViewModel = viewModelProvider(viewModelFactory)

        if (extras != null) {
            val idMovie = extras.getInt(ID_MOVIE)
            similarViewModel.getSimilarMoview(idMovie)
        }

        initObservable()
    }

    private fun initObservable() {
        similarViewModel.error.observe(this, Observer {
            showToast(it)
        })
        similarViewModel.state.observe(this, Observer {
            when(it) {
                is LoaderState.ShowLoading -> {
                    showToast("Loading...")
                }
                is LoaderState.HideLoading -> {
                    showToast("Complete!")
                }
            }
        })
        similarViewModel.similar.observe(this, Observer {

        })
    }

    private fun initInjector() {
        DaggerMainComponent
            .builder()
            .roomModule(RoomModule(application))
            .movieModule(MovieModule())
            .build()
            .injectSimilar(this)
    }
}