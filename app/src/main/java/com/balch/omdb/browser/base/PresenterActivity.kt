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
import android.view.View
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

/**
 * This class enhances the Activity functionality by providing Presenter and View
 * integration
 *
 * @param <P> Presenter to inject and initialize
 * @param <V> View to Inject
</V> */
abstract class PresenterActivity<P : BaseContract.Presenter, V : BaseContract.View> : AppCompatActivity() {

    @Inject protected lateinit var presenter: P
    @Inject protected lateinit var view: V

    protected val viewLayout: View
        get() = view as View

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AndroidInjection.inject(this)

        presenter.initialize(savedInstanceState)
        lifecycle.addObserver(presenter.lifecycleObserver)
    }

    @CallSuper
    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(presenter.lifecycleObserver)
        view.cleanup()
    }

    @CallSuper
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.onSaveInstanceState(outState)
    }

}
