/*
 * Copyright 2020 Mohammed Khalid Hamid.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.khalid.hamid.githubrepos.ui

import com.khalid.hamid.githubrepos.BaseUnitTest
import com.khalid.hamid.githubrepos.network.BaseRepository
import com.khalid.hamid.githubrepos.vo.GitRepos
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class RepoViewModelTest : BaseUnitTest() {

    private lateinit var mockWebServer: MockWebServer

    lateinit var subject: RepoViewModel

    @Mock
    lateinit var baseRepository: BaseRepository

    val default = GitRepos("", emptyList(), 0)

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        subject = RepoViewModel()
    }

    @Test
    fun verifyPairs() {
        assertTrue(subject.CARD_PAIRS_VALUE.size == 12)
    }

    @Test
    fun `verify buildNumbers always generates new values`() {
        subject.buildNumbers()
        val setValues1 = mutableSetOf<Int>()
        val setValues2 = mutableSetOf<Int>()
        setValues1.addAll(subject.CARD_PAIRS_VALUE)
        subject.buildNumbers()
        setValues2.addAll(subject.CARD_PAIRS_VALUE)
        assertTrue(setValues1 != setValues2 && setValues1.size == setValues2.size)
    }
}
