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

package com.khalid.hamid.githubrepos.ui

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import com.khalid.hamid.githubrepos.R

class CardItem @JvmOverloads constructor(
    val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(mContext, attrs, defStyleAttr) {
    private var cardClickListener: OnClickListener? = null
    var isAnimationInProgress = false

    var onAnimationCompleted :(Int)->Unit = {}

    fun flipCard(context: Context, viewToBringInFront: View, inVisibleView: View, onAnimationCompleted : (Int)->Unit = {}) {
        try {
            isAnimationInProgress = true
            viewToBringInFront.visible()
            val scale = context.resources.displayMetrics.density
            val cameraDist = 8000 * scale
            viewToBringInFront.cameraDistance = cameraDist
            inVisibleView.cameraDistance = cameraDist
            val flipOutAnimatorSet =

                AnimatorInflater.loadAnimator(
                    context,
                    R.animator.out_animation
                ) as AnimatorSet
            flipOutAnimatorSet.setTarget(inVisibleView)
            val flipInAnimatorSet =
                AnimatorInflater.loadAnimator(
                    context,
                    R.animator.in_animation
                ) as AnimatorSet
            flipInAnimatorSet.setTarget(viewToBringInFront)
            flipOutAnimatorSet.start()
            flipInAnimatorSet.start()
            flipInAnimatorSet.doOnEnd {
                inVisibleView.gone()
                viewToBringInFront.visible()
                isAnimationInProgress = false
                onAnimationCompleted(this.id)
            }
        } catch (e: Exception) {
            // logHandledException(e)
        }
    }

    fun View.visible() {
        this.visibility = View.VISIBLE
    }

    fun View.gone() {
        this.visibility = View.GONE
    }
}
