package com.practicum.playlistmaker.presentation.audioplayer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.core.graphics.drawable.toBitmap
import com.practicum.playlistmaker.R
import java.lang.Integer.min

class PlaybackButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = 0,
    @StyleRes defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {

    private var imageRect = RectF(0f, 0f, 0f, 0f)
    private var isPlaying = false
    private var isClickEnabled = false
    private var imagePlayButtonBitmap: Bitmap? = null
    private var imagePauseButtonBitmap: Bitmap? = null

    var onTouchListener: (() -> Unit)? = null

    init {
        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PlaybackButtonView,
            defStyleAttr,
            defStyleRes
        ).apply {
            try {
                imagePlayButtonBitmap =
                    getDrawable(R.styleable.PlaybackButtonView_playButtonImageResId)?.toBitmap()
                imagePauseButtonBitmap =
                    getDrawable(R.styleable.PlaybackButtonView_pauseButtonImageResId)?.toBitmap()
            } finally {
                recycle()
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if(isClickEnabled){
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    return true
                }

                MotionEvent.ACTION_UP -> {
                    onTouchListener?.invoke()
                    updateButtonState()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    fun updateСlickAccessibility(isEnabled: Boolean) {
        isClickEnabled = isEnabled
    }

    fun updateButtonState() {
        isPlaying = !isPlaying
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = min(measuredWidth, measuredHeight)
        setMeasuredDimension(size, size)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        imageRect = RectF(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat())
    }

    override fun onDraw(canvas: Canvas) {
        val imageBitmap = if (isPlaying) imagePauseButtonBitmap else imagePlayButtonBitmap
        if (imageBitmap != null) {
            canvas.drawBitmap(imageBitmap, null, imageRect, null)
        }
    }
}