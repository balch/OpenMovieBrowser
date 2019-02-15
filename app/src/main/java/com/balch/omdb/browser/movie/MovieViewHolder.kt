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

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.balch.omdb.browser.R
import com.balch.omdb.browser.ext.inflate
import com.balch.omdb.browser.ext.loadUrl
import com.balch.omdb.browser.movie.model.Movie
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_movie_vh.view.*

/**
 * ViewHolder to represent the view for a single movie
 */
class MovieViewHolder(parent: ViewGroup)
    : RecyclerView.ViewHolder(parent.inflate(R.layout.item_movie_vh)) {

    // view layouts
    private val itemImageView: ImageView by lazy { itemView.item_movie_img }
    private val titleTextView: TextView by lazy { itemView.list_item_movie_title }
    private val yearTextView: TextView by lazy { itemView.list_item_year }

    fun bind(movie: Movie) {

        with(movie) {
            if (posterUrl != IMAGE_NA) {
                itemImageView.loadUrl(posterUrl) {
                    it.apply(RequestOptions.centerCropTransform())
                        .apply(RequestOptions().placeholder(R.drawable.ic_camera))
                }
            } else  {
                itemImageView.setImageResource(R.drawable.ic_camera)
            }
            titleTextView.text = title
            yearTextView.text = year
        }
    }

    companion object {
        const val IMAGE_NA = "N/A"
    }
}

