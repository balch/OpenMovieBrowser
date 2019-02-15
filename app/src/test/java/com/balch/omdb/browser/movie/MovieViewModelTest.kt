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

import androidx.lifecycle.MutableLiveData
import com.balch.omdb.browser.movie.model.Movie
import com.balch.omdb.browser.movie.model.MovieData
import com.balch.omdb.browser.movie.model.OpenMovieRepository
import com.balch.omdb.browser.test.BaseTest
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations.initMocks
import kotlin.test.assertEquals


class MovieViewModelTest : BaseTest() {
    private lateinit var viewModel: MovieViewModel

    @Mock
    private lateinit var mockAdapter: MovieAdapter
    @Mock
    private lateinit var mockAuctionDataLive: MutableLiveData<MovieData>

    @Mock
    private lateinit var movieRepository: OpenMovieRepository

    @Before
    fun setUp() {

        initMocks(this)

        viewModel = spy(MovieViewModel(mockAdapter, movieRepository, mockAuctionDataLive))
    }

    @Test
    fun testCalculateTotalPages() {
        assertEquals(0, viewModel.calculateTotalPages(0, 0))
        assertEquals(0, viewModel.calculateTotalPages(10, 0))
        assertEquals(1, viewModel.calculateTotalPages(9, 10))
        assertEquals(1, viewModel.calculateTotalPages(10, 10))
        assertEquals(2, viewModel.calculateTotalPages(11, 10))
        assertEquals(2, viewModel.calculateTotalPages(20, 10))
        assertEquals(3, viewModel.calculateTotalPages(21, 10))
        assertEquals(1, viewModel.calculateTotalPages(5, 10))
        assertEquals(1, viewModel.calculateTotalPages(20, 21))
        assertEquals(1, viewModel.calculateTotalPages(21, 21))
        assertEquals(2, viewModel.calculateTotalPages(22, 21))
        assertEquals(2, viewModel.calculateTotalPages(42, 21))
        assertEquals(10, viewModel.calculateTotalPages(209, 21))
        assertEquals(10, viewModel.calculateTotalPages(210, 21))
        assertEquals(11, viewModel.calculateTotalPages(211, 21))
    }

    @Test
    fun testLoadMovies() {
        val searchText = "Search"

        val movieList: MutableList<Movie> = ArrayList()
        movieList.add(mock(Movie::class.java))
        val movieData = MovieData().apply {
            movies = movieList
        }

        doReturn(Single.just(movieData)).`when`(movieRepository).getMovies(searchText, 1)

        //region Execute Test
        viewModel.loadMovies(searchText)
        testScheduler.triggerActions()
        //endregion

        verify(movieRepository).getMovies(searchText, 1)
        verify(mockAuctionDataLive).value = movieData
    }

    @Test
    fun testLoadMoviesNextPage() {
        val searchText = "Search"
        val currentPage = 5

        viewModel.searchText = searchText
        viewModel.currentPage = currentPage

        val movieList: MutableList<Movie> = ArrayList()
        movieList.add(mock(Movie::class.java))
        val movieData = MovieData().apply {
            movies = movieList
        }
        doReturn(Single.just(movieData)).`when`(movieRepository)
                .getMovies(searchText, currentPage + 1L)

        //region Execute Test
        viewModel.loadMoviesNextPage()
        testScheduler.triggerActions()
        //endregion

        verify(movieRepository).getMovies(searchText, currentPage + 1L)
        verify(mockAuctionDataLive).value = movieData
    }

}