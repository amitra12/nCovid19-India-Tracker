package com.aparajitamitra.ncovid19indiatracker

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan

class SpannableDeltaData(langName: String, langColor: String, start: Int) : SpannableString(langName) {
    init {
        setSpan(
            ForegroundColorSpan(Color.parseColor(langColor)),
            start,
            langName.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }
}