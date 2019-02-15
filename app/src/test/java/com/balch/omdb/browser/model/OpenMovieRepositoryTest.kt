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

package com.balch.omdb.browser.model

import com.balch.omdb.browser.movie.model.OpenMovieApi
import com.balch.omdb.browser.movie.model.OpenMovieRepository
import com.balch.omdb.browser.test.BaseTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import kotlin.test.assertEquals

/**
 * test OpenMovieRepository by injecting json resources
 */
class OpenMovieRepositoryTest : BaseTest() {

    lateinit var openMovieRepository: OpenMovieRepository
    lateinit var mockServer : MockWebServer
    lateinit var openMovieApi : OpenMovieApi

    companion object {
        const val OMDB_API_KEY = "dummyKey"
    }

    @Before
    @Throws
    fun setUp() {
        mockServer = MockWebServer()
        mockServer.start()

        val baseUrl = mockServer.url("")

        // Get an instance of Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()

        openMovieApi = retrofit.create(OpenMovieApi::class.java)
        openMovieRepository = OpenMovieRepository(OMDB_API_KEY, openMovieApi)
    }

    @After
    @Throws fun tearDown() {
        mockServer.shutdown()
    }

    @Test
    fun testGetMoviesWithEmptyResults() {

        val pageNumber = 1L
        val keyword = "troopers"
        val json = getJson("json/empty.json")

        val response = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .addHeader("Cache-Control", "no-cache")
            .setBody(json)

        mockServer.enqueue(response)

        val testObserver = openMovieRepository.getMovies(keyword, pageNumber).test()
        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        val movieData = testObserver.values()[0]
        assertEquals(0, movieData.movies.size)
        assertEquals(0, movieData.totalResults)
    }

    @Test
    fun testGetMoviesWithResults() {

        val pageNumber = 1L
        val keyword = "troopers"
        val json = getJson("json/troopers.json")

        val response = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .addHeader("Cache-Control", "no-cache")
            .setBody(json)

        // Schedule some responses.
        mockServer.enqueue(response)

        // Call the API
        val testObserver = openMovieRepository.getMovies(keyword, pageNumber).test()
        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)

        testObserver.assertNoErrors()
        testObserver.assertValueCount(1)

        val movieData = testObserver.values()[0]
        assertEquals(10, movieData.movies.size)
        assertEquals(34, movieData.totalResults)
    }

    @Test
    fun testGetMoviesWithError() {

        val pageNumber = 1L
        val keyword = "troopers"
        val json = getJson("json/troopers.json")

        val response = MockResponse()
            .addHeader("Content-Type", "application/json; charset=utf-8")
            .addHeader("Cache-Control", "no-cache")
            .setResponseCode(500)
            .setBody(json)

        mockServer.enqueue(response)

        val testObserver = openMovieRepository.getMovies(keyword, pageNumber).test()
        testObserver.awaitTerminalEvent(2, TimeUnit.SECONDS)

        assertEquals(1, testObserver.errorCount())
    }
}
