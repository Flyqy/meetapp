package com.example.meetapp.stuff.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager

inline fun View.onTouchStartOrFirstMove(
    crossinline action: () -> Unit
) = setOnTouchListener(object : View.OnTouchListener {

    private var handled = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val current = event.action
        when {
            !handled && (current == MotionEvent.ACTION_DOWN || current == MotionEvent.ACTION_MOVE) -> action().also {
                handled = true
            }
            current == MotionEvent.ACTION_UP -> handled = false
        }
        return false
    }
})

fun View.hideSoftKeyboard() {
    val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}