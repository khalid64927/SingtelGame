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

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import com.khalid.hamid.githubrepos.R
import com.khalid.hamid.githubrepos.databinding.ItemMainBinding
import com.khalid.hamid.githubrepos.utilities.AppExecutors
import com.khalid.hamid.githubrepos.vo.RepoViewData

class RepoAdapter(
    private val dataBindingComponent: DataBindingComponent,
    appExecutors: AppExecutors,
    private val callback: ((RepoViewData) -> Unit)?
) : DataBoundListAdapter<RepoViewData, ItemMainBinding>(
    appExecutors = appExecutors,
    diffCallback = object : DiffUtil.ItemCallback<RepoViewData>() {
        override fun areItemsTheSame(oldItem: RepoViewData, newItem: RepoViewData): Boolean {
            return oldItem.uuid == newItem.uuid
        }

        override fun areContentsTheSame(oldItem: RepoViewData, newItem: RepoViewData): Boolean {
            return oldItem.imageUrl == newItem.imageUrl &&
                    oldItem.description == newItem.description
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ItemMainBinding {
        val binding = DataBindingUtil
            .inflate<ItemMainBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_main,
                parent,
                false,
                dataBindingComponent
            )
        binding.root.setOnClickListener {
            binding.repos?.let {
                callback?.invoke(it)
            }
        }
        return binding
    }

    override fun bind(binding: ItemMainBinding, item: RepoViewData) {
        binding.repos = item
    }
}
