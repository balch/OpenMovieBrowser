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

import androidx.lifecycle.LifecycleOwner
import com.balch.omdb.browser.movie.model.MovieData
import com.balch.omdb.browser.movie.model.OpenMovieRepository
import com.balch.omdb.browser.test.BaseTest
import io.reactivex.Observable
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks


class MoviePresenterTest : BaseTest() {

    @Mock
    private lateinit var mockView: MovieContract.MovieView
    @Mock
    private lateinit var lifecycleOwner: LifecycleOwner
    @Mock
    private lateinit var openMovieRepository: OpenMovieRepository

    private lateinit var movieAdapter: MovieAdapter
    private lateinit var movieViewModel: MovieViewModel

    private lateinit var presenter: MoviePresenter

    @Before
    fun setUp() {
        initMocks(this)

        movieAdapter = spy(MovieAdapter())
        movieViewModel = spy(MovieViewModel(movieAdapter, openMovieRepository))
        presenter = spy(MoviePresenter(mockView, movieViewModel, lifecycleOwner))

        doReturn(Observable.just(Unit)).`when`(mockView).onLoadMore
        doNothing().`when`(movieAdapter).notifyDataSetChanged()
    }

    @Test
    fun testOnLoadMore() {
        doNothing().`when`(movieViewModel).loadMoviesNextPage()

        //region Execute Test
        presenter.onLoadMorePages()
        //endregion

        verify(mockView).showBusy = true
        verify(movieViewModel).loadMoviesNextPage()
    }

    @Test
    fun testShowMoviesSuccess() {

        val movieData = MovieData().apply {
            movies = ArrayList()
            response = true
        }

        //region Execute Test
        presenter.showMovies(movieData)
        //endregion

        verify(mockView).showBusy = false
        verify(movieAdapter).addMovies(movieData.movies)
        verify(mockView).doneLoading(false)
    }

    @Test
    fun testShowMoviesFail() {

        val movieData = MovieData().apply {
            movies = ArrayList()
            response = false
        }

        //region Execute Test
        presenter.showMovies(movieData)
        //endregion

        verify(mockView).showBusy = false
        verify(movieAdapter, never()).addMovies(movieData.movies)
        verify(movieAdapter).clearMovies()
        verify(mockView).doneLoading(false)
    }

}
