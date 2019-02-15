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

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.balch.omdb.browser.R
import com.balch.omdb.browser.ext.inflate
import com.balch.omdb.browser.ui.EndlessScrollListener
import io.reactivex.Observable
import kotlinx.android.synthetic.main.view_movie.view.*

/**
 * Main View containing a list of movies and implementing
 * the EndlessScrolling pattern
 */
class MovieView : FrameLayout, MovieContract.MovieView {

    // public properties
    override val onLoadMore: Observable<Unit>
        get() = recyclerOnScrollListener.onLoadMore

    override var showBusy: Boolean
        get() = progressBar.visibility == View.VISIBLE
        set(value) {
            progressBar.visibility = if (value) View.VISIBLE else View.GONE
        }

    // private properties
    private lateinit var recyclerOnScrollListener: EndlessScrollListener

    // private view layouts
    private val progressBar: ProgressBar by lazy { movie_view_progress_bar }
    private val recyclerView: RecyclerView by lazy { movie_view_recycler }

    constructor(context: Context) : super(context) {
        initializeLayout()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initializeLayout()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        initializeLayout()
    }

    private fun initializeLayout() {
        inflate(R.layout.view_movie, true)

        id = View.generateViewId()

        val layoutManager = LinearLayoutManager(context)
        recyclerOnScrollListener = EndlessScrollListener(layoutManager, 3)

        recyclerView.layoutManager = layoutManager
        recyclerView.addOnScrollListener(recyclerOnScrollListener)
    }

    override fun setMovieAdapter(movieAdapter: MovieAdapter) {
        recyclerView.adapter = movieAdapter
    }

    override fun cleanup() {
        recyclerView.clearOnScrollListeners()
        recyclerView.adapter = null
    }

    override fun clearMovies() {
        recyclerOnScrollListener.reset()
    }

    override fun doneLoading(hasMore: Boolean) {
        recyclerOnScrollListener.doneLoading(hasMore)
    }
}
