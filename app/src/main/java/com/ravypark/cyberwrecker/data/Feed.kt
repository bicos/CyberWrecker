package com.ravypark.cyberwrecker.data

import android.text.format.DateUtils
import com.google.firebase.firestore.IgnoreExtraProperties
import java.text.ParseException

@IgnoreExtraProperties
data class Feed(
    val author: String = "",
    val bbsId: String = "",
    val bbsName: String = "",
    val content: String = "",
    val cp: String = "",
    val cpName: String = "",
    val createdAt: String = "",
    val scrappedAt: String = "",
    val images: List<String> = emptyList(),
    val likeCnt: Int = 0,
    val postId: String = "",
    val replyCnt: Int = 0,
    val title: String = "",
    val uid: String = "",
    val url: String = "",
    val viewCnt: Int = 0
) {

    fun getImage() = if (images.isNullOrEmpty()) null else images.first()

    fun getItemId(): String = "${bbsId}_${postId}"

    fun getDesc(): String {
        var desc = cpName

        if (viewCnt > 0) {
            desc += " · 조회수 ${String.format("%,d",viewCnt)}"
        }

        if (createdAt.isNotEmpty()) {
            desc += getTime()
        }

        return desc
    }

    private fun getTime(): String {
        try {
            val time = DEFAULT_DATE_FORMAT.parse(createdAt)?.time ?: System.currentTimeMillis()
            val now = System.currentTimeMillis()
            return " · " + DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS).toString()
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return ""
    }
}