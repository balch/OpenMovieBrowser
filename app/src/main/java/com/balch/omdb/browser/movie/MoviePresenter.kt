/*
 * Author: Balch
 *
 * This file is part of OpenMovieBrowser.
 *
 * OpenMovieBrowser is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenMovieBrowser is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with OpenMovieBrowser.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2019
 *
 */

package com.balch.omdb.browser.movie

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.balch.omdb.browser.base.BasePresenter
import com.balch.omdb.browser.dagger.ActivityScope
import com.balch.omdb.browser.movie.model.MovieData
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import timber.log.Timber
import javax.inject.Inject

/**
 * Movie Presenter to handle interactions between the MovieRepository
 * and MovieView
 */
@ActivityScope
class MoviePresenter
@Inject constructor(override val view: MovieContract.MovieView,
                    private val movieViewModel: MovieViewModel,
                    private val lifecycleOwner: LifecycleOwner) :
    BasePresenter(), MovieContract.MoviePresenter {

    override var searchView: SearchView? = null
        set(value) {
            field = value
            searchView!!.setQuery(movieViewModel.searchText, false)
        }

    private val movieAdapter: MovieAdapter
        get() = movieViewModel.movieAdapter

    private val disposables = CompositeDisposable()
    private var disposableSaveNote: Disposable? = null
    private var disposableClearNote: Disposable? = null

    @SuppressLint("VisibleForTests")
    override fun initialize(savedInstanceState: Bundle?) {
        movieViewModel.movieData.observe(lifecycleOwner, Observer<MovieData>(this::showMovies))

        view.setMovieAdapter(movieAdapter)

        disposables.addAll(
                view.onLoadMore
                        .subscribe({ onLoadMorePages() },
                                { throwable -> Timber.e(throwable, "onLoadMorePages error") })
        )
    }

    override fun doSearch(keyword: String) {
        searchView?.clearFocus()

        view.showBusy = true
        view.clearMovies()
        movieViewModel.loadMovies(keyword)
    }

    @VisibleForTesting
    fun onLoadMorePages() {
        view.showBusy = true
        movieViewModel.loadMoviesNextPage()
    }

    @VisibleForTesting
    fun showMovies(movieData: MovieData?) {
        view.showBusy = false

        if (movieData?.response == true) {
            movieAdapter.addMovies(movieData.movies)
        } else {
            if (searchView?.query?.isNotEmpty() != false) {
                movieAdapter.clearMovies()
                Timber.e("Error getting movies")
            }
        }

        view.doneLoading(movieViewModel.hasMorePages)

    }

    override fun cleanup() {

        disposableSaveNote?.dispose()
        disposableClearNote?.dispose()
        disposables.dispose()

        movieViewModel.movieData.removeObservers(lifecycleOwner)
    }
}