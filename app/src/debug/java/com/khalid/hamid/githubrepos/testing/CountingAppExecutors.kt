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

package com.khalid.hamid.githubrepos.testing

import com.khalid.hamid.githubrepos.utilities.AppExecutors
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

class CountingAppExecutors(idleCallback: (() -> Unit)? = null) {

    private val lock = java.lang.Object()

    private var taskCount = 0

    val appExecutors: AppExecutors

    init {
        val increment: () -> Unit = {
            synchronized(lock) {
                taskCount++
            }
        }
        val decrement: () -> Unit = {
            synchronized(lock) {
                taskCount--
                if (taskCount == 0) {
                    lock.notifyAll()
                }
            }
            idleCallback?.let {
                if (taskCount == 0) {
                    it.invoke()
                }
            }
        }
        appExecutors = AppExecutors(
            CountingExecutor(increment, decrement),
            CountingExecutor(increment, decrement),
            CountingExecutor(increment, decrement)
        )
    }

    fun taskCount() = synchronized(lock) {
        taskCount
    }

    fun drainTasks(time: Int, timeUnit: TimeUnit) {
        val end = System.currentTimeMillis() + timeUnit.toMillis(time.toLong())
        while (true) {
            synchronized(lock) {
                if (taskCount == 0) {
                    return
                }
                val now = System.currentTimeMillis()
                val remaining = end - now
                if (remaining > 0) {
                    lock.wait(remaining)
                } else {
                    throw TimeoutException("could not drain tasks")
                }
            }
        }
    }

    private class CountingExecutor(
        private val increment: () -> Unit,
        private val decrement: () -> Unit
    ) : Executor {

        private val delegate = Executors.newSingleThreadExecutor()

        override fun execute(command: Runnable) {
            increment()
            delegate.execute {
                try {
                    command.run()
                } finally {
                    decrement()
                }
            }
        }
    }
}
