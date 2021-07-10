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

package com.khalid.hamid.githubrepos.ui

import android.app.Application
import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.khalid.hamid.githubrepos.R
import com.khalid.hamid.githubrepos.databinding.FragmentRepoBinding
import com.khalid.hamid.githubrepos.databinding.ItemCardBinding
import com.khalid.hamid.githubrepos.di.Injectable
import com.khalid.hamid.githubrepos.testing.OpenForTesting
import com.khalid.hamid.githubrepos.utilities.*
import javax.inject.Inject
import kotlinx.android.synthetic.main.item_card.view.*
import timber.log.Timber

@OpenForTesting
class RepoFragment : Fragment(), Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var app: Application
    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)
    var binding by autoCleared<FragmentRepoBinding>()
    val vm: RepoViewModel by viewModels {
        viewModelFactory
    }
    lateinit var itemCards: MutableList<ItemCardBinding>
    val handler = Handler()

    val runnable = Runnable {
       vm.flipBackUnmatched()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        itemCards = mutableListOf(
            binding.card1, binding.card2, binding.card3, binding.card4, binding.card5, binding.card6, binding.card7, binding.card8, binding.card9, binding.card10, binding.card11, binding.card12
        )
        binding.toolbarStepsCount.text = "0"
        binding.toolbarRestart.setOnClickListener {
        //    restart()
            vm.restart.invoke()
        }

        vm.itemCards.clear()
        itemCards.forEachIndexed { index, it ->
            it.cardBack.tv_number.text = vm.CARD_PAIRS_VALUE[index].toString()
            val cardId = it.clItem.id
            val number = it.clItem.card_back.cl_card_back.tv_number.text.toString().toInt()
            Timber.d(" cardId $cardId number : $number")
            vm.itemCards.add(Card(id = cardId, number = number))
        }

        itemCards.forEachIndexed { index, it ->
            it.clItem.setOnClickListener {
                Timber.d("clicked id ${it.id}")
                vm.onCardClicked(it.id)
            }
        }

        vm.repoEventLiveData.observe(viewLifecycleOwner) {
            when(it){
                is FlipOutItemEvent -> {
                    val cardId = (it as FlipOutItemEvent).cardId
                    Timber.d("flip in ID $cardId")

                    val item = itemCards.first { it.clItem.id == cardId }
                    item.flipOut()
                }
                is FlipInItemEvent -> {
                    itemCards.map { it.clItem.id }.forEach { cardId -> Timber.d("all ids $cardId") }

                    val cardId = (it as FlipInItemEvent).cardId
                    Timber.d("flip in ID $cardId")

                    val item = itemCards.first { it.clItem.id == cardId }
                    item.flipIn()
                }
                is RestartEvent -> {
                    binding.toolbarStepsCount.text = "0"
                }
                is DelayRunnableEvent -> {
                    handler.postDelayed(runnable, 1000)
                }
                is RemoveRunnableEvent -> {
                    handler.removeCallbacks(runnable)
                }
                is StepUpdateEvent -> {
                    binding.toolbarStepsCount.text = vm.steps.toString()
                }
                is ShowDialogEvent -> {
                    showDialog()
                }
            }

        }

    }

    fun ItemCardBinding.isFlipped() = cardBack.visibility == View.VISIBLE

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val dataBinding = DataBindingUtil.inflate<FragmentRepoBinding>(
            inflater,
            R.layout.fragment_repo,
            container,
            false
        )
        Timber.d("onCreateView")
        setHasOptionsMenu(true)
        binding = dataBinding
        return binding.repoRoot
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_sort_name -> true
            // sort adapter by stars
            R.id.menu_sort_star -> true
            else -> false
    }


    fun ItemCardBinding.flipIn() {
        clItem.flipCard(this.root.context, cardBack, cardFront, vm.onAnimationCompleted)
    }

    fun ItemCardBinding.flipOut() {
        clItem.flipCard(this.root.context, cardFront, cardBack, vm.onAnimationCompleted)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        Timber.d("onCreateOptionsMenu")
        inflater.inflate(R.menu.arrange_list, menu)
    }

    fun showDialog() = activity?.run {
        val dialog = GameCompletionDialog(vm.steps, vm.restart )
        dialog.show(supportFragmentManager, "")
    }

    /**
     * Created to be able to override in tests
     */
    open fun navController() = findNavController()
}

class GameCompletionDialog(
    private val steps: Int,
    private val playAgain: (() -> Unit)? = null
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val message = String.format(resources.getString(R.string.dialog_game_complete), steps)
            builder.setTitle(R.string.congratulations)
            builder.setMessage(message)
                .setPositiveButton(R.string.try_again
                ) { _, _ ->
                    playAgain?.invoke()
                }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
