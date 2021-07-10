/*
 * Copyright 2021 Mohammed Khalid Hamid.
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
