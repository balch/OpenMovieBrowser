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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.balch.omdb.browser.movie.model.OpenMovieRepository
import javax.inject.Inject

/**
 * Factory for injecting a MovieViewModel into the activity
 */
@Suppress("UNCHECKED_CAST")
class MovieViewModelFactory
@Inject constructor(private val adapter: MovieAdapter,
                    private val openMovieRepository: OpenMovieRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            return MovieViewModel(adapter, openMovieRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}