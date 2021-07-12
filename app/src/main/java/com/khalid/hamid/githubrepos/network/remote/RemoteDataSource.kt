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

package com.khalid.hamid.githubrepos.network.remote

import com.khalid.hamid.githubrepos.network.GitHubService
import com.khalid.hamid.githubrepos.network.Result
import com.khalid.hamid.githubrepos.network.Result.Error_
import com.khalid.hamid.githubrepos.network.Result.Success
import com.khalid.hamid.githubrepos.vo.GitRepos
import javax.inject.Inject
import retrofit2.HttpException
import timber.log.Timber

class RemoteDataSource @Inject constructor(
    private val gitHubService: GitHubService
) {

    suspend fun fetchRepos(): Result<GitRepos> {
        Timber.d("fetchRespos")
        try {
            val dataResponse = gitHubService.fetchRepos()
            Timber.d("data response is  " + dataResponse.body().toString())

            if (dataResponse.isSuccessful) {
                Timber.d("dataResponse.isSuccessful")
                val default = GitRepos("", emptyList(), 0)
                return Success(dataResponse.body() ?: default)
            } else {
                Timber.d("dataResponse.Error_")
                return Error_(Exception(dataResponse?.message() ?: "Unknown error"))
            }
        } catch (httpException: HttpException) {
            Timber.d("dataResponse.Error_")
            httpException.printStackTrace()
            return Error_(httpException)
        } catch (unkown: Exception) {
            Timber.d("dataResponse.Error_")
            unkown.printStackTrace()
            return Error_(unkown)
        }
    }
}
