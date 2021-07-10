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

package com.khalid.hamid.githubrepos.vo

import com.google.gson.annotations.SerializedName

data class Owner(
    @SerializedName("account_id")
    val accountId: String? = "",
    @SerializedName("nickname")
    val nickname: String? = "",
    @SerializedName("links")
    val links: Links?,
    @SerializedName("display_name")
    val displayName: String? = "",
    @SerializedName("type")
    val type: String? = "",
    @SerializedName("uuid")
    val uuid: String? = ""
)

data class Commits(
    @SerializedName("href")
    val href: String = ""
)

data class Mainbranch(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("type")
    val type: String = ""
)

data class Branches(
    @SerializedName("href")
    val href: String = ""
)

data class GitRepos(
    @SerializedName("next")
    val next: String = "",
    @SerializedName("values")
    val values: List<ValuesItem>?,
    @SerializedName("pagelen")
    val pagelen: Int = 0
)

data class Html(
    @SerializedName("href")
    val href: String = ""
)

data class Self(
    @SerializedName("href")
    val href: String = ""
)

data class Avatar(
    @SerializedName("href")
    val href: String = ""
)

data class Source(
    @SerializedName("href")
    val href: String = ""
)

data class Watchers(
    @SerializedName("href")
    val href: String = ""
)

data class Project(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("links")
    val links: Links,
    @SerializedName("type")
    val type: String = "",
    @SerializedName("uuid")
    val uuid: String = "",
    @SerializedName("key")
    val key: String = ""
)

data class CloneItem(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("href")
    val href: String = ""
)

data class ValuesItem(
    @SerializedName("owner")
    val owner: Owner,
    @SerializedName("updated_on")
    val updatedOn: String = "",
    @SerializedName("is_private")
    val isPrivate: Boolean = false,
    @SerializedName("website")
    val website: String = "",
    @SerializedName("workspace")
    val workspace: Workspace,
    @SerializedName("fork_policy")
    val forkPolicy: String = "",
    @SerializedName("project")
    val project: Project,
    @SerializedName("description")
    val description: String = "",
    @SerializedName("language")
    val language: String = "",
    @SerializedName("type")
    val type: String = "",
    @SerializedName("uuid")
    val uuid: String = "",
    @SerializedName("has_issues")
    val hasIssues: Boolean = false,
    @SerializedName("mainbranch")
    val mainbranch: Mainbranch,
    @SerializedName("has_wiki")
    val hasWiki: Boolean = false,
    @SerializedName("full_name")
    val fullName: String = "",
    @SerializedName("size")
    val size: Int = 0,
    @SerializedName("created_on")
    val createdOn: String = "",
    @SerializedName("name")
    val name: String = "",
    @SerializedName("links")
    val links: Links,
    @SerializedName("scm")
    val scm: String = "",
    @SerializedName("slug")
    val slug: String = ""
)
data class RepoViewData(
    val imageUrl: String,
    val name: String,
    val language: String,
    val timestamp: String,
    val size: String,
    val description: String,
    val uuid: String
)

fun ValuesItem.toViewData(): RepoViewData {
    val des = if (description.isBlank()) description else "Description :$description"
    val timestamp = if (description.isBlank()) description else "Description :$description"
    return RepoViewData(
        imageUrl = owner.links?.avatar?.href.orEmpty(),
        name = owner.displayName.orEmpty(),
        language = language,
        timestamp = createdOn,
        size = (size / 1000).toString() + " MB",
        description = description,
        uuid = uuid

    )
}

data class Links(
    @SerializedName("forks")
    val forks: Forks,
    @SerializedName("watchers")
    val watchers: Watchers,
    @SerializedName("source")
    val source: Source,
    @SerializedName("avatar")
    val avatar: Avatar,
    @SerializedName("branches")
    val branches: Branches,
    @SerializedName("pullrequests")
    val pullrequests: Pullrequests,
    @SerializedName("tags")
    val tags: Tags,
    @SerializedName("downloads")
    val downloads: Downloads,
    @SerializedName("clone")
    val clone: List<CloneItem>?,
    @SerializedName("commits")
    val commits: Commits,
    @SerializedName("self")
    val self: Self,
    @SerializedName("html")
    val html: Html,
    @SerializedName("hooks")
    val hooks: Hooks
)

data class Pullrequests(
    @SerializedName("href")
    val href: String = ""
)

data class Hooks(
    @SerializedName("href")
    val href: String = ""
)

data class Downloads(
    @SerializedName("href")
    val href: String = ""
)

data class Tags(
    @SerializedName("href")
    val href: String = ""
)

data class Workspace(
    @SerializedName("name")
    val name: String = "",
    @SerializedName("links")
    val links: Links,
    @SerializedName("type")
    val type: String = "",
    @SerializedName("uuid")
    val uuid: String = "",
    @SerializedName("slug")
    val slug: String = ""
)

data class Forks(
    @SerializedName("href")
    val href: String = ""
)
