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

package com.balch.omdb.browser.dagger

import android.content.Context
import com.balch.omdb.browser.OpenMovieApplication
import com.balch.omdb.browser.R
import dagger.Module
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

/**
 * Dagger Module for Global App configuration
 */
@Module
class ApplicationModule {

    companion object {
        const val OMDB_APP_ID = "OpenMovieAppId"
        const val APP_CONTEXT = "appContext"
    }

    @Provides
    @Singleton
    @Named(APP_CONTEXT)
    internal fun providesApplicationContext(app: OpenMovieApplication): Context {
        return app
    }

    @Provides
    @Singleton
    @Named(OMDB_APP_ID)
    internal fun providesOpenMovieAppId(@Named(APP_CONTEXT) context: Context): String {
        return context.getString(R.string.omdb_api_key)
    }

}
