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

import com.balch.omdb.browser.movie.model.Movie
import com.balch.omdb.browser.test.BaseTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class MovieAdapterTest : BaseTest() {
    private lateinit var adapter: MovieAdapter

    @Before
    fun setUp() {

        MockitoAnnotations.initMocks(this)

        adapter = spy(MovieAdapter())
        doNothing().`when`(adapter).notifyDataSetChanged()
    }

    @Test
    fun testClearMovies() {
        val movies: List<Movie> = List(5) { mock(Movie::class.java) }
        adapter.addMovies(movies)
        clearInvocations(adapter)

        //region Execute Test
        adapter.clearMovies()
        //endregion

        assertThat(adapter.itemCount).isEqualTo(0)
        verify(adapter).notifyDataSetChanged()
    }

    @Test
    fun testAddMovies() {
        val movies: List<Movie> = List(5) { mock(Movie::class.java) }
        clearInvocations(adapter)

        //region Execute Test
        adapter.addMovies(movies)
        //endregion

        assertThat(adapter.itemCount).isEqualTo(5)
        verify(adapter).notifyDataSetChanged()
    }
}

