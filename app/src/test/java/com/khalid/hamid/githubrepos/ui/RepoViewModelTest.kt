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

import com.khalid.hamid.githubrepos.BaseUnitTest
import com.khalid.hamid.githubrepos.network.BaseRepository
import com.khalid.hamid.githubrepos.vo.GitRepos
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertFalse
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
        // Then
        assertTrue(subject.CARD_PAIRS_VALUE.size == 12)
    }

    @Test
    fun `verify buildNumbers always generates new values`() {
        // Given
        subject.buildNumbers()
        val setValues1 = mutableSetOf<Int>()
        val setValues2 = mutableSetOf<Int>()
        setValues1.addAll(subject.CARD_PAIRS_VALUE)
        // When
        subject.buildNumbers()
        setValues2.addAll(subject.CARD_PAIRS_VALUE)
        // Then
        assertTrue(setValues1 != setValues2 && setValues1.size == setValues2.size)
    }

    @Test
    fun `verify that onCardClicked flips the card`() = runBlockingTest {
        // Given
        subject.buildNumbers()
        subject.itemCards = mutableListOf(
            Card(id = 112, number = 1)
        )
        val card = subject.itemCards[0]
        // When
        subject.onCardClicked(card.id)
        // Then
        assertTrue(card.isAnimationInProgress)
    }

    @Test
    fun `verify that onCardClicked dosent do anything if it is already flipped`() = runBlockingTest {
        // Given
        subject.buildNumbers()
        subject.itemCards = mutableListOf(
            Card(id = 112, number = 1, isFlipped = true)
        )
        val card = subject.itemCards[0]
        // When
        subject.onCardClicked(card.id)
        // Then
        assertFalse(card.isAnimationInProgress)
        assertTrue(subject.matchedCards.isEmpty())
    }

    @Test
    fun `verify that onCardClicked dosent do anything if waiting on flip back animation `() = runBlockingTest {
        // Given
        subject.buildNumbers()
        subject.itemCards = mutableListOf(
            Card(id = 112, number = 1)
        )
        subject.waitForFlipBack = true

        val card = subject.itemCards[0]
        // When
        subject.onCardClicked(card.id)
        // Then
        assertFalse(card.isAnimationInProgress)
        assertTrue(subject.matchedCards.isEmpty())
    }

    @Test
    fun `verify that onCardClicked dosent do anything if waiting on flip in animation `() = runBlockingTest {
        // Given
        subject.buildNumbers()
        subject.itemCards = mutableListOf(
            Card(id = 112, number = 1, isAnimationInProgress = true)
        )
        val card = subject.itemCards[0]
        // When
        subject.onCardClicked(card.id)
        // Then
        assertTrue(card.isAnimationInProgress)
        assertFalse(card.isFlipped)
        assertTrue(subject.matchedCards.isEmpty())
    }

    @Test
    fun `verify that when onCardClicked twice called on matching number updates matchedList`() = runBlockingTest {
        // Given
        subject.buildNumbers()
        subject.itemCards = mutableListOf(
            Card(id = 112, number = 1),
            Card(id = 113, number = 1)
        )
        val card1 = subject.itemCards[0]
        val card2 = subject.itemCards[1]
        // When
        subject.onCardClicked(card1.id)
        // Then
        assertTrue(subject.previousNumber != null)
        subject.onCardClicked(card2.id)

        // Then
        assertTrue(subject.matchedCards.size == 2)
    }

    @Test
    fun `verify that when onCardClicked twice called on different number matchedList is not updated`() = runBlockingTest {
        // Given
        subject.buildNumbers()
        subject.itemCards = mutableListOf(
            Card(id = 112, number = 1),
            Card(id = 113, number = 2)
        )
        val card1 = subject.itemCards[0]
        val card2 = subject.itemCards[1]
        // When
        subject.onCardClicked(card1.id)
        // Then
        assertTrue(subject.previousNumber != null)
        subject.onCardClicked(card2.id)

        // Then
        assertTrue(subject.matchedCards.size == 0)
    }

    @Test
    fun `verify that two cards are flipped back`() {
        subject.buildNumbers()
        subject.itemCards = mutableListOf(
            Card(id = 112, number = 1),
            Card(id = 113, number = 2)
        )
        val card1 = subject.itemCards[0]
        val card2 = subject.itemCards[1]
        // When
        subject.onCardClicked(card1.id)
        subject.onCardClicked(card2.id)

        // Then
        assertTrue(subject.previousNumber != null)
        assertTrue(subject.current != null)

        // When
        subject.flipBackUnmatched()

        // Then
        assertTrue(subject.previousNumber == null)
        assertTrue(subject.current == null)
    }
}
