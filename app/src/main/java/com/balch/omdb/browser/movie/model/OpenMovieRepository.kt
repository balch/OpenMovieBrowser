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

package com.balch.omdb.browser.movie.model

import com.balch.omdb.browser.dagger.ApplicationModule
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

/**
 * OpenMovieRepository for getting data from the omdb.com api.
 * This class wraps the Retrofit API and provide validation
 * and error handling
 */
@Singleton
class OpenMovieRepository
@Inject constructor(@Named(ApplicationModule.OMDB_APP_ID) private val apiKey: String,
                    private val openMovieApi: OpenMovieApi) {

    fun getMovies(keyword: String, pageNumber: Long): Single<MovieData> {
        return if (keyword.isNotEmpty()) openMovieApi.search(keyword, pageNumber,  apiKey)
        else Single.just(MovieData())
    }
}
