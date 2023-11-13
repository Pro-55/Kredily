package com.example.kredily.util.extensions

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.kredily.R

fun Fragment.glide() = Glide.with(this)

fun <T> RequestBuilder<T>.diskCacheStrategyAll(): RequestBuilder<T> =
    diskCacheStrategy(DiskCacheStrategy.ALL)

fun <T> RequestBuilder<T>.addProfilePlaceholder(context: Context): RequestBuilder<T> = placeholder(
    AppCompatResources.getDrawable(context, R.drawable.img_profile_placeholder)
)