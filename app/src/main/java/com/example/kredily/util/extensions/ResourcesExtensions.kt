package com.example.kredily.util.extensions

import android.content.res.Resources

val Resources.shortAnimTime
    get():Long = getInteger(android.R.integer.config_shortAnimTime).toLong()

val Resources.mediumAnimTime
    get():Long = getInteger(android.R.integer.config_mediumAnimTime).toLong()

val Resources.longAnimTime
    get():Long = getInteger(android.R.integer.config_longAnimTime).toLong()