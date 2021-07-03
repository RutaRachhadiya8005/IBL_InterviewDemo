package com.example.demoexample.utils

import android.app.Activity
import com.example.demoexample.R

class AnimationTranslate {
    fun previewAnimation(activity: Activity) {
        activity.overridePendingTransition(R.anim.enter_anim_rtl, R.anim.exit_anim_rtl)
    }

    fun nextAnimation(activity: Activity) {
        activity.overridePendingTransition(R.anim.anim_right, R.anim.anim_left)
    }


}
