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

package com.khalid.hamid.githubrepos.network

import com.khalid.hamid.githubrepos.network.Result.Success
import com.khalid.hamid.githubrepos.network.remote.RemoteDataSource
import com.khalid.hamid.githubrepos.utilities.EspressoIdlingResource
import com.khalid.hamid.githubrepos.vo.GitRepos
import javax.inject.Inject
import timber.log.Timber

/**
 * This will return data from either DB or get from network
*/
class RepositoryImple @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : BaseDataSource {

    override suspend fun fetchRepos(): Result<GitRepos> {
        val fetchedData = remoteDataSource.fetchRepos()
        Timber.d("fetched data ${EspressoIdlingResource.countingIdlingResource.getCounterVal()}")
        when (fetchedData) {
            is Success -> {
                Timber.d("Success ${EspressoIdlingResource.countingIdlingResource.getCounterVal()}")
                return fetchedData
            }

            else -> {
                Timber.d("Error ${EspressoIdlingResource.countingIdlingResource.getCounterVal()}")
                return fetchedData
            }
        }
    }
}
