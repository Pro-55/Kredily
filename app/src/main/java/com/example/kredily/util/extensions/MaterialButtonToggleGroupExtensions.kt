package com.example.kredily.util.extensions

import com.google.android.material.button.MaterialButtonToggleGroup

fun MaterialButtonToggleGroup.setOnButtonCheckedListener(
    listener: MaterialButtonToggleGroup.OnButtonCheckedListener
) {
    removeOnButtonCheckedListener(listener)
    addOnButtonCheckedListener(listener)
}