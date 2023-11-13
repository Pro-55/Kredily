package com.example.kredily.util.extensions

import android.text.TextWatcher
import android.widget.TextView

fun TextView.setTextChangedListener(watcher: TextWatcher) {
    removeTextChangedListener(watcher)
    addTextChangedListener(watcher)
}