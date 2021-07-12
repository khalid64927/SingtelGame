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

    var onAnimationCompleted: (Int) -> Unit = {}

    fun flipCard(context: Context, viewToBringInFront: View, inVisibleView: View, onAnimationCompleted: (Int) -> Unit = {}) {
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
