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
