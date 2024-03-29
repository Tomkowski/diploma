package com.tomitive.avia.interfaces

import androidx.constraintlayout.motion.widget.MotionLayout

/**
 * interfejs nadpisujący nieużywane w aplikacji metody interfejsu [MotionLayout.TransitionListener]
 */
interface TransitionChangeListener: MotionLayout.TransitionListener {

    override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
    }

    override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {
    }
}