/*
 * MIT License
 *
 * Copyright 2021 Mohammed Khalid Hamid.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.khalid.hamid.githubrepos.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.khalid.hamid.githubrepos.testing.OpenForTesting
import java.security.SecureRandom
import java.util.Random
import javax.inject.Inject
import timber.log.Timber

@OpenForTesting
class RepoViewModel@Inject constructor() : ViewModel() {

    var steps: Int = 0
    var previousNumber: Card? = null
    var current: Card? = null
    val matchedCards = mutableListOf<Card>()
    var waitForFlipBack = false

    val repoEventLiveData: LiveData<RepoEvent>
        get() = _repoEventLiveData
    private val _repoEventLiveData = MutableLiveData<RepoEvent>()

    val CARD_PAIRS_VALUE = mutableListOf<Int>()
    var itemCards = mutableListOf<Card>()

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
        var shouldContinue = true
        while (shouldContinue) {
            if (CARD_PAIRS_VALUE.size == uniqueNumbers) {
                shouldContinue = false
                continue
            }
            val randomInteger = randInt()
            if (CARD_PAIRS_VALUE.contains(randomInteger)) {
                // skip this number
                continue
            }
            CARD_PAIRS_VALUE.add(randomInteger)
        }
        CARD_PAIRS_VALUE.addAll(CARD_PAIRS_VALUE)
        CARD_PAIRS_VALUE.shuffle()
    }

    fun onCardClicked(cardId: Int) {
        if (waitForFlipBack) {
            Timber.d("Game in Play")
            return
        }
        val clickedCard = itemCards.first { it.id == cardId }
        if (clickedCard.isFlipped) {
            Timber.d("isFlipped")
            return
        }
        if (clickedCard.isAnimationInProgress) {
            Timber.d("isAnimationInProgress")
            return
        }
        steps += 1
        _repoEventLiveData.value = StepUpdateEvent(steps)
        current = clickedCard
        if (!checkForMatch(card = clickedCard)) {
            Timber.d("It's not a match")
            waitForFlipBack = true
        }
        flipInItemEvent(cardId)
        _repoEventLiveData.value = DelayRunnableEvent()
        Timber.d("Steps $steps")
    }

    fun checkForMatch(card: Card): Boolean {
        val number = card.number
        if (previousNumber?.number == null) {
            previousNumber = card
            return true
        } else if (previousNumber?.number == number) {
            matchedCards.add(card)
            previousNumber?.run {
                matchedCards.add(this)
            }
            if (matchedCards.size == itemCards.size) {
                _repoEventLiveData.value = ShowDialogEvent()
            }
            previousNumber = null
            current = null
            _repoEventLiveData.value = RemoveRunnableEvent()
            return true
        } else {
            Timber.d(" not a match")
            return false
        }
    }

    fun flipBackUnmatched() {
        if (waitForFlipBack) {
            waitForFlipBack = false
            previousNumber?.run {
                flipOutItemEvent(id)
            }
            current?.run {
                flipOutItemEvent(id)
                }
            previousNumber = null
            current = null
        }
    }

    var isRestartRequested = false

    val restart: () -> Unit = {
        isRestartRequested = true
        buildNumbers()
        previousNumber?.run {
            flipOutItemEvent(id)
        }
        current?.run {
            flipOutItemEvent(id)
        }
        previousNumber = null
        current = null
        matchedCards.forEach {
            flipOutItemEvent(it.id)
        }
        matchedCards.clear()
        steps = 0
        if (itemCards.none { it.isFlipped }) {
            isRestartRequested = false
            Timber.d("itemCards all animation completed $itemCards")
            _repoEventLiveData.value = RestartEvent()
        }
    }

    fun flipOutItemEvent(cardId: Int) {
        setCardAnimationState(cardId)
        _repoEventLiveData.value = FlipOutItemEvent(cardId)
    }

    fun flipInItemEvent(cardId: Int) {
        setCardAnimationState(cardId)
        _repoEventLiveData.value = FlipInItemEvent(cardId)
    }
    private fun setCardAnimationState(cardId: Int) {
        val clickedCard = itemCards.first { it.id == cardId }
        val index = itemCards.indexOf(clickedCard)
        clickedCard.isAnimationInProgress = true
        itemCards[index] = clickedCard
    }

    val onAnimationCompleted: (Int) -> Unit = { cardId ->
        Timber.d("onAnimationCompleted cardId $cardId")
        val clickedCard = itemCards.first { it.id == cardId }
        val index = itemCards.indexOf(clickedCard)
        clickedCard.isFlipped = !clickedCard.isFlipped
        clickedCard.isAnimationInProgress = false
        itemCards[index] = clickedCard
        if (isRestartRequested) {
            if (itemCards.map { it.isAnimationInProgress }.none { it }) {
                isRestartRequested = false
                Timber.d("itemCards all animation completed $itemCards")
                _repoEventLiveData.value = RestartEvent()
            }
        }
    }

    fun startAnimation(cardId: Int) {
        Timber.d("startAnimation cardId $cardId")
        val clickedCard = itemCards.first { it.id == cardId }
        val index = itemCards.indexOf(clickedCard)
        clickedCard.isAnimationInProgress = true
        itemCards[index] = clickedCard
    }
}

data class Card(var isFlipped: Boolean = false, val id: Int, var isAnimationInProgress: Boolean = false, val number: Int)

sealed class RepoEvent
class FlipInItemEvent(val cardId: Int) : RepoEvent()
class FlipOutItemEvent(val cardId: Int) : RepoEvent()
class RestartEvent(var message: String = "") : RepoEvent()
class StepUpdateEvent(val cardId: Int) : RepoEvent()
class RemoveRunnableEvent(var message: String = "") : RepoEvent()
class DelayRunnableEvent(var message: String = "") : RepoEvent()
class ShowDialogEvent(var message: String = "") : RepoEvent()

fun <T> MutableCollection<T>.replaceWith(elements: Collection<T>) {
    this.clear()
    this.addAll(elements)
}
