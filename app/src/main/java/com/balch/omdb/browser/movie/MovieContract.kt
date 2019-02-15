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

import androidx.appcompat.widget.SearchView
import com.balch.omdb.browser.base.BaseContract
import io.reactivex.Observable

/**
 * Contract to represent Movie Presenter and View functionality
 */
class MovieContract : BaseContract {
    interface MoviePresenter : BaseContract.Presenter {
        var searchView: SearchView?
        fun doSearch(keyword: String)
    }

    interface MovieView : BaseContract.View {
        val onLoadMore: Observable<Unit>
        var showBusy: Boolean
        fun setMovieAdapter(movieAdapter: MovieAdapter)
        fun clearMovies()
        fun doneLoading(hasMore: Boolean)
    }

}