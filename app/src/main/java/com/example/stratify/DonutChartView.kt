package com.example.stratify

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View

class DonutChartView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs) {

    var positivePercent: Float = 70f
    var negativePercent: Float = 30f
    var totalReviews: Int = 1200

    // Lebar garis lebih tipis
    private val strokeWidth = 15f

    private val bgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#E0E0E0") // abu muda background chart
        style = Paint.Style.STROKE
        this.strokeWidth = this@DonutChartView.strokeWidth
    }

    private val positivePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#4CAF50") // hijau positif
        style = Paint.Style.STROKE
        this.strokeWidth = this@DonutChartView.strokeWidth
        strokeCap = Paint.Cap.ROUND
    }

    private val negativePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#F44336") // merah negatif
        style = Paint.Style.STROKE
        this.strokeWidth = this@DonutChartView.strokeWidth
        strokeCap = Paint.Cap.ROUND
    }

    private val mainTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 24f
        textAlign = Paint.Align.CENTER
        typeface = Typeface.DEFAULT_BOLD
    }

    private val subTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        textSize = 14f
        textAlign = Paint.Align.CENTER
    }

    private val labelPositivePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#4CAF50")
        textSize = 18f
        textAlign = Paint.Align.LEFT
        typeface = Typeface.DEFAULT_BOLD
    }

    private val labelNegativePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#F44336")
        textSize = 18f
        textAlign = Paint.Align.LEFT
        typeface = Typeface.DEFAULT_BOLD
    }

    private var animatedSweep = 0f

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        setBackgroundColor(Color.TRANSPARENT)
        startAnimation()
    }

    private fun startAnimation() {
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 1500
        animator.addUpdateListener {
            animatedSweep = it.animatedFraction
            invalidate()
        }
        animator.start()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val cx = width / 3f
        val cy = height / 2f
        val radius = ((width.coerceAtMost(height) / 2f) - strokeWidth * 1.5f) * 0.9f
        val rect = RectF(cx - radius, cy - radius, cx + radius, cy + radius)

        // background circle
        canvas.drawArc(rect, 0f, 360f, false, bgPaint)

        val totalPercent = (positivePercent + negativePercent).coerceAtMost(100f)
        val positiveAngle = 360f * (positivePercent / totalPercent)
        val negativeAngle = 360f * (negativePercent / totalPercent)

        // hijau positif
        canvas.drawArc(rect, 270f, positiveAngle * animatedSweep, false, positivePaint)
        // merah negatif
        canvas.drawArc(rect, 270f + positiveAngle, negativeAngle * animatedSweep, false, negativePaint)

        // Posisi teks agar di tengah secara vertikal
        val mainTextY = cy - ((mainTextPaint.descent() + mainTextPaint.ascent()) / 2)
        val subTextY = cy + ((-mainTextPaint.descent() - mainTextPaint.ascent())/2) + subTextPaint.textSize

        // Teks tengah
        canvas.drawText(totalReviews.toString(), cx, mainTextY, mainTextPaint)
        canvas.drawText("Total Review", cx, subTextY, subTextPaint)

        // label kanan
        val rightTextX = cx + radius + 30f
        canvas.drawText("${positivePercent.toInt()}% Positive", rightTextX, cy - 10f, labelPositivePaint)
        canvas.drawText("${negativePercent.toInt()}% Negative", rightTextX, cy + 20f, labelNegativePaint)
    }
}
