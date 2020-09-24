package com.ravypark.cyberwrecker.utils

import android.content.res.Resources

val Float.px: Float get() = (this * Resources.getSystem().displayMetrics.density)

val Int.px: Int get() = ((this * Resources.getSystem().displayMetrics.density).toInt())

val Float.dp: Float get() = (this / Resources.getSystem().displayMetrics.density)

val Int.dp: Int get() = ((this / Resources.getSystem().displayMetrics.density).toInt())