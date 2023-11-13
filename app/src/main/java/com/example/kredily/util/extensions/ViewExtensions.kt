package com.example.kredily.util.extensions

import android.transition.Slide
import android.transition.TransitionManager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.GravityInt

fun View.visible() {
    if (!isVisible()) visibility = View.VISIBLE
}

fun View.invisible() {
    if (!isInvisible()) visibility = View.INVISIBLE
}

fun View.gone() {
    if (!isGone()) visibility = View.GONE
}

fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

fun View.isInvisible(): Boolean {
    return visibility == View.INVISIBLE
}

fun View.isGone(): Boolean {
    return visibility == View.GONE
}

fun View.visibleWithSlide(
    parent: ViewGroup,
    @GravityInt slideEdge: Int = Gravity.BOTTOM,
    duration: Long = 300
) {
    val slideTransition = Slide(slideEdge).setDuration(duration).addTarget(this)
    TransitionManager.beginDelayedTransition(parent, slideTransition)
    visible()
}

fun View.goneWithSlide(
    parent: ViewGroup,
    @GravityInt slideEdge: Int = Gravity.BOTTOM,
    duration: Long = 300
) {
    val slideTransition = Slide(slideEdge).setDuration(duration).addTarget(this)
    TransitionManager.beginDelayedTransition(parent, slideTransition)
    gone()
}

fun View.invisibleWithSlide(
    parent: ViewGroup,
    @GravityInt slideEdge: Int = Gravity.BOTTOM,
    duration: Long = 300
) {
    val slideTransition = Slide(slideEdge).setDuration(duration).addTarget(this)
    TransitionManager.beginDelayedTransition(parent, slideTransition)
    invisible()
}