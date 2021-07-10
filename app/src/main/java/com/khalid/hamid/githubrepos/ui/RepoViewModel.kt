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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khalid.hamid.githubrepos.databinding.ItemCardBinding
import com.khalid.hamid.githubrepos.network.Resource
import com.khalid.hamid.githubrepos.testing.OpenForTesting
import com.khalid.hamid.githubrepos.vo.GitRepos
import java.security.SecureRandom
import java.util.Random
import javax.inject.Inject
import kotlinx.android.synthetic.main.item_card.view.*

@OpenForTesting
class RepoViewModel@Inject constructor() : ViewModel() {

    val default = GitRepos("", emptyList(), 0)
    val items: LiveData<Resource<GitRepos>>
    get() = _items
    private val _items = MutableLiveData<Resource<GitRepos>>().apply {
        value = Resource.loading(default) }
    var steps: Int = 0
    var previousNumber: ItemCardBinding? = null
    var current: ItemCardBinding? = null
    val matchedNumbers = mutableListOf<ItemCardBinding>()

    val CARD_PAIRS_VALUE = mutableListOf<Int>()
    lateinit var itemCards: MutableList<ItemCardBinding>

    fun randInt(): Int {
        val rand: Random = SecureRandom()
        return rand.nextInt(100 - 1 + 1) + 1
    }
    val uniqueNumbers = 6

    init {
        buildNumbers()
    }

    fun buildNumbers() {
        CARD_PAIRS_VALUE.clear()
        for (uniqueNumbers in 1..6) {
            CARD_PAIRS_VALUE.add(randInt())
        }
        CARD_PAIRS_VALUE.addAll(CARD_PAIRS_VALUE)
        CARD_PAIRS_VALUE.shuffle()
    }
}

data class Card(var isFlipped: Boolean = false, val id: Int, var isAnimationInProgress: Boolean = false)
