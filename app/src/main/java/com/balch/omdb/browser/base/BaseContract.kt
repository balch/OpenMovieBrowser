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

package com.balch.omdb.browser.base

import android.os.Bundle
import androidx.lifecycle.LifecycleObserver

/**
 * Contract class to provide all Presenters and Views with a
 * a base interface
 */
interface BaseContract {
    interface Presenter {
        fun initialize(savedInstanceState: Bundle?)
        val view: BaseContract.View
        val lifecycleObserver: LifecycleObserver

        fun onSaveInstanceState(outState: Bundle)
        fun onDestroy()
    }

    interface View {
        fun cleanup()
    }
}
