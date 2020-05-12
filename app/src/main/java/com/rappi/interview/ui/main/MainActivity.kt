package com.rappi.interview.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rappi.interview.R
import com.rappi.interview.abstraction.state.LoaderState
import com.rappi.interview.abstraction.util.showToast
import com.rappi.interview.abstraction.util.viewModelProvider
import com.rappi.interview.data.entity.Movie
import com.rappi.interview.di.DaggerMainComponent
import com.rappi.interview.di.module.MovieModule
import com.rappi.interview.di.module.RoomModule
import com.rappi.interview.ui.favorite.FavoriteMovieActivity
import com.rappi.interview.viewmodel.MovieViewModel
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), CategoryListDialogFragment.CategoryListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: MovieViewModel
    private val movies = mutableListOf<Movie>()
    private lateinit var _adapter: MovieAdapter
    private lateinit var categoryDialog: CategoryListDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initInjector()

        viewModel = viewModelProvider(viewModelFactory)
        viewModel.getPopularMovie()

        initView()
        initObservable()
    }

    private fun initView() {
        setSupportActionBar(toolbar_main)
        toolbar_main.title = getString(R.string.app_name)
        toolbar_main.setTitleTextColor(ContextCompat.getColor(this, R.color.colorTextTertiary))
        _adapter = MovieAdapter(movies)
        lstMovies.apply {
            layoutManager = LinearLayoutManager(
                this@MainActivity
            )
            adapter = _adapter
        }
        categoryDialog = CategoryListDialogFragment.newInstance(this)

        btnChoseCategory.setOnClickListener {
            categoryDialog.show(supportFragmentManager, "category")
        }
    }

    private fun initObservable() {
        viewModel.error.observe(this, Observer {
            showToast(it)
        })

        viewModel.state.observe(this, Observer {
            when (it) {
                is LoaderState.ShowLoading -> {
                    showToast("Loading...")
                }
                is LoaderState.HideLoading -> {
                    showToast("Complete!")
                }
            }
        })

        viewModel.movies.observe(this, Observer {
            movies.clear()
            movies.addAll(it.results)
            _adapter.notifyDataSetChanged()
        })
    }

    private fun initInjector() {
        DaggerMainComponent
            .builder()
            .roomModule(RoomModule(application))
            .movieModule(MovieModule())
            .build()
            .injectMain(this)
    }

    override fun onClickNowPlaying() {
        viewModel.getNowPlayingMovie()
        categoryDialog.dismiss()
    }

    override fun onTopRated() {
        viewModel.getTopRatedMovie()
        categoryDialog.dismiss()
    }

    override fun onClickUpcoming() {
        viewModel.getUpcomingMovie()
        categoryDialog.dismiss()
    }

    override fun onClickPopular() {
        viewModel.getPopularMovie()
        categoryDialog.dismiss()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_favorite) {
            startActivity(FavoriteMovieActivity.generateIntent(this))
        }
        return super.onOptionsItemSelected(item)
    }
}
