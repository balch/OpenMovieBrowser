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

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.balch.omdb.browser.movie.model.MovieData
import com.balch.omdb.browser.movie.model.OpenMovieRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

/**
 * This ViewModel exposes a LiveData object which emits MovieData objects from the
 * OMDB API. A MovieData object contains the current page of found Movies.
 *
 * The ViewModel also stores state data that should survive a ConfigChange operation. This
 * includes the MovieAdapter that contains the entire Movie list and the info necessary
 * to retrieve the next page of Movies.
 *
 * The class implements simple dependency injection using the `inject()` method to setter-inject
 * the Adapter and ModelApis.
 */
class MovieViewModel(val movieAdapter: MovieAdapter,
                     private val openMovieRepository: OpenMovieRepository,
                     private val movieDataLive: MutableLiveData<MovieData> = MutableLiveData()) : ViewModel() {

    // public properties
    val hasMorePages
        get() = hasMorePages(currentPage)

    val movieData: LiveData<MovieData>
        get() = movieDataLive

    @set:VisibleForTesting
    var searchText: String? = null

    // paging vars
    @VisibleForTesting
    var currentPage: Int = 0
    @VisibleForTesting
    var totalPages: Long = 0

    // disposables
    private var disposableGetMovies: Disposable? = null

    fun loadMovies(searchText: String? = null) {
        totalPages = -1
        currentPage = 1
        this.searchText = searchText ?: this.searchText ?: ""
        movieAdapter.clearMovies()
        movieDataLive.value = null

        getMoviesAsync()
    }

    fun loadMoviesNextPage() {
        this.currentPage++
        getMoviesAsync()
    }

    private fun hasMorePages(page: Int): Boolean {
        return totalPages == -1L || page < totalPages
    }

    private fun getMoviesAsync() {

        disposeGetMoviesDisposable()
        disposableGetMovies = openMovieRepository
                .getMovies(searchText!!, currentPage.toLong())
                .subscribeOn(Schedulers.io())
                .doOnSuccess { movieData ->
                        if (totalPages == -1L && movieData.movies.isNotEmpty()) {
                            totalPages = calculateTotalPages(movieData.totalResults, movieData.movies.size)
                        }
                    }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(movieDataLive::setValue
                ) { throwable ->
                    Timber.e(throwable, "Error in getMovies()")
                    movieDataLive.value = null
                }
    }

    /**
     * Calculates the number of pages based on the total results count and
     * the page size
     */
    @VisibleForTesting
    fun calculateTotalPages(totalResults: Int, pageSize: Int): Long {
        var pages = 0L
        if (pageSize > 0) {
            pages = (totalResults / pageSize).toLong()
            pages += if (totalResults % pageSize != 0) 1L else 0L
        }

        return pages
    }

    private fun disposeGetMoviesDisposable() {
        disposableGetMovies?.dispose()
        disposableGetMovies = null
    }

    override fun onCleared() {
        disposeGetMoviesDisposable()
    }

}


