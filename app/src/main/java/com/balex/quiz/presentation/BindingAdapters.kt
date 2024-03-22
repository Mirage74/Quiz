package com.balex.quiz.presentation

import android.graphics.Bitmap
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.balex.quiz.R

interface OnOptionClickListener {
    fun onOptionClick(numUserAnswer: Int)
}

@BindingAdapter("numberAsText")
fun bindNumberAsText(textView: TextView, number: Int) {
    textView.text = number.toString()
}

private fun getNumUserAnswerFromString(id: Int): Int {
    return when(id) {
        R.id.capital1 -> 1
        R.id.capital2 -> 2
        R.id.capital3 -> 3
        R.id.capital4 -> 4
        else -> {
            throw RuntimeException("Not correct user's answer number in fun getNumUserAnswerFromString")
        }
    }

}

@BindingAdapter("onOptionClickListener")
fun bindOnOptionClickListener(button: Button, clickListener: OnOptionClickListener) {
    button.setOnClickListener {
        clickListener.onOptionClick(getNumUserAnswerFromString(button.id))
    }
}

@BindingAdapter("srcBitmap")
fun setBitmapToImageView(imageView: ImageView, bitmap: Bitmap) {
    imageView.setImageBitmap(bitmap)
}

