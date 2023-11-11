package com.example.kredily.util.extensions

import android.os.Build
import androidx.annotation.ColorRes
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.FragmentActivity

fun FragmentActivity.updateSystemUIColor(
    @ColorRes statusBarColor: Int,
    @ColorRes navigationBarColor: Int,
    isAppearanceLightStatusBars: Boolean,
    isAppearanceLightNavigationBars: Boolean
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        window.statusBarColor = resources.getColor(statusBarColor, null)
        window.navigationBarColor = resources.getColor(navigationBarColor, null)
        val decorView = window.decorView
        val controller = WindowInsetsControllerCompat(window, decorView)
        controller.isAppearanceLightStatusBars = isAppearanceLightStatusBars
        controller.isAppearanceLightNavigationBars = isAppearanceLightNavigationBars
    }
}