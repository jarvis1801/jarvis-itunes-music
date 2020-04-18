package com.jarvis.itunesmusic.ui.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.constraintlayout.widget.ConstraintLayout
import com.jarvis.itunesmusic.R
import kotlinx.android.synthetic.main.custom_search_panel.view.*


class SearchPanel @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : ConstraintLayout(context, attrs, defStyleAttr) {

    private var mOnSearchBtnClickListener: OnSearchBtnClickListener? = null

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.custom_search_panel, this, true)

        btnSearch.setOnClickListener {
            mOnSearchBtnClickListener!!.onSearchClick(term = searchTerm.text.toString(), limit = "25")
        }

        searchTerm.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                btnSearch.performClick()
            }
            false
        }
    }

    fun setOnSearchBtnClickListener(onSearchBtnClickListener: OnSearchBtnClickListener) {
        mOnSearchBtnClickListener = onSearchBtnClickListener
    }

    fun release() {
        // todo
    }

    interface OnSearchBtnClickListener {
        fun onSearchClick(term: String, limit: String)
    }
}