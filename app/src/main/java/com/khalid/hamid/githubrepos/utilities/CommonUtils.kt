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

import timber.log.Timber

/**
 * Convenience function
 *
 * @param error(e: Throwable): optional function parameter. Pass this if you need
 *   to handle error scenario
 * @param block(instance: <AnyObject>): function with object on which runSafe is called
 */

inline fun <T> T?.letSafe(
    error: (Throwable) -> Unit = { /*no-op*/ },
    block: (T) -> Unit
) {
    try {
        if (this != null) return block(this)
    } catch (e: Exception) {
        Timber.e(e)
        error(e)
    }
}

/**
 * Convenience function
 *
 * @param error(e: Throwable): optional function parameter. Pass this if you need
 *   to handle error scenario
 * @param block(instance: <AnyObject>): function with object on which alsoSafe is called
 */
public inline fun <T> T.alsoSafe(
    error: (Throwable) -> Unit = { /*no-op*/ },
    block: (T) -> Unit
): T {
    try {
        block(this)
    } catch (e: Exception) {
        Timber.e(e)
        error(e)
    }
    return this
}

/**
 * Convenience function
 *
 * @param error(e: Throwable): optional function parameter. Pass this if you need
 *   to handle error scenario
 * @param block(instance: <AnyObject>): function with object on which runSafe is called
 */
inline fun <T, R> T?.runSafe(
    error: (Throwable) -> Unit = { /*no-op*/ },
    block: T.() -> R
): R? {
    try {
        if (this != null) {
            return block()
        }
    } catch (e: Exception) {
        Timber.e(e)
        error(e)
        return null
    }
    return null
}

inline fun String?.isNotEqual(other: String?, ignoreCase: Boolean = false) = !(this.equals(other, ignoreCase))
