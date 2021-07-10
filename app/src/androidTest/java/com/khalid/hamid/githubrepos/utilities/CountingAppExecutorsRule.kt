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

package com.khalid.hamid.githubrepos.utilities

import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.IdlingResource
import com.khalid.hamid.githubrepos.testing.CountingAppExecutors
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.TimeUnit
import org.junit.rules.TestWatcher
import org.junit.runner.Description

/**
 * A Junit rule that registers an espresso idling resource which counts all tasks that are submitted
 * via [AppExecutors].
 */
class CountingAppExecutorsRule : TestWatcher() {
    // give it a unique id to workaround an espresso bug where you cannot register/unregister
    // an idling resource w/ the same name.
    private val id = UUID.randomUUID().toString()
    private val countingAppExecutors = CountingAppExecutors {
        callbacks.forEach {
            it.onTransitionToIdle()
        }
    }
    val appExecutors = countingAppExecutors.appExecutors
    private val callbacks = CopyOnWriteArrayList<IdlingResource.ResourceCallback>()
    private val idlingResource: IdlingResource = object : IdlingResource {
        override fun getName() = "App Executors Idling Resource $id"

        override fun isIdleNow() = countingAppExecutors.taskCount() == 0

        override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
            callbacks.add(callback)
        }
    }

    override fun starting(description: Description?) {
        IdlingRegistry.getInstance().register(idlingResource)
        super.starting(description)
    }

    override fun finished(description: Description?) {
        countingAppExecutors.drainTasks(10, TimeUnit.SECONDS)
        callbacks.clear()
        IdlingRegistry.getInstance().unregister(idlingResource)
        super.finished(description)
    }
}
