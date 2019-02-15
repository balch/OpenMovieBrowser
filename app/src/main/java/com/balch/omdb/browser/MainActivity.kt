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

package com.balch.omdb.browser

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.SearchView
import com.balch.omdb.browser.base.PresenterActivity
import com.balch.omdb.browser.movie.MovieContract

/**
 * Main UI Screen providing a SearchView in the toolbar and a
 * RecyclerView of found Movie items
 */
class MainActivity : PresenterActivity<MovieContract.MoviePresenter,
        MovieContract.MovieView>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewLayout)
        handleIntent()
    }

    override fun onNewIntent(intent: Intent) {
        handleIntent(intent)
    }

    @VisibleForTesting
    fun handleIntent(): Boolean {
        return handleIntent(intent)
    }

    private fun handleIntent(intent: Intent): Boolean {
        var handled = false
        if (Intent.ACTION_SEARCH == intent.action) {
            val query = intent.getStringExtra(SearchManager.QUERY)
            presenter.doSearch(query)

            handled = true
        }

        return handled
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val menuItem = menu.findItem(R.id.menu_search)
        val searchView = menuItem.actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setIconifiedByDefault(false)
        menuItem.expandActionView()
        presenter.searchView = searchView

        return true
    }

}
