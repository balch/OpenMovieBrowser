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
import androidx.lifecycle.ViewModelProviders
import com.balch.omdb.browser.MainActivity
import com.balch.omdb.browser.dagger.ActivityScope
import dagger.Binds
import dagger.Module
import dagger.Provides

/**
 * Dagger Module for inject Movie MVP
 */
@Module
abstract class MovieModule {

    @Binds
    @ActivityScope
    abstract fun bindsLifecycleOwner(activity: MainActivity): LifecycleOwner

    @Binds
    @ActivityScope
    abstract fun bindsMoviePresenter(presenter: MoviePresenter): MovieContract.MoviePresenter

    @Module
    companion object {
        @JvmStatic
        @Provides
        @ActivityScope
        fun providesMovieView(activity: MainActivity): MovieContract.MovieView {
            return MovieView(activity)
        }

        @JvmStatic
        @Provides
        @ActivityScope
        fun providesMovieViewModel(activity: MainActivity, factory: MovieViewModelFactory): MovieViewModel {
            return ViewModelProviders.of(activity, factory).get(MovieViewModel::class.java)
        }
    }

}
