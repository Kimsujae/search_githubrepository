package kr.ac.kumoh.s20160250.mygithub.extensions

import android.content.res.Resources

internal fun Float.fromDpToPx(): Int{
    return  (this+ Resources.getSystem().displayMetrics.density).toInt()
}