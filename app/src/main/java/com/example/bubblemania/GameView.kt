package com.example.bubblemania

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import kotlin.math.PI
import kotlin.math.sin
class GameView(context: Context, private val gameTask: GameTask) : View(context) {

    private var myPaint: Paint = Paint()
    private var speed = 1
    private var time = 0
    private var score = 0
    private var BlueBubbleT = 1 // Initialize Blue Bubble in the middle lane
    private val otherBubble = ArrayList<HashMap<String, Any>>()
    private var isRunning = false

    private var viewWidth = 0
    private var viewHeight = 0
    private var laneWidth = 0

    private lateinit var BlueBubble: Drawable
    private lateinit var RedBubble: Drawable

    init {
        myPaint = Paint()
        loadDrawables()
    }

    private fun loadDrawables() {
        BlueBubble = context.getDrawable(R.drawable.blue)!!
        RedBubble = context.getDrawable(R.drawable.red)!!
    }

    private fun updateScoreAndSpeed() {
        score++
        speed = 1 + score / 8
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (canvas == null) return

        viewWidth = width
        viewHeight = height
        laneWidth = viewWidth / 3


        myPaint.color = Color.argb(255, 52, 73, 85)
        canvas.drawRect(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat(), myPaint)


        val waveAmplitude = 20
        val waveFrequency = 50
        val waveOffset = 0

        myPaint.strokeWidth = 3f
        for (i in 0 until viewWidth step waveFrequency) {
            val waveHeight = waveAmplitude * sin((i + waveOffset).toDouble() * 2 * PI / waveFrequency).toFloat()
            canvas.drawLine(
                i.toFloat(), viewHeight / 2f + waveHeight,
                (i + waveFrequency).toFloat(), viewHeight / 2f - waveHeight, myPaint
            )
        }

        // Update time and Other Bubbles
        if (isRunning) {
            time += 10 + speed
            if (time % 700 < 10 + speed) {
                val map = HashMap<String, Any>()
                map["lane"] = (0..2).random()
                map["startTime"] = time
                otherBubble.add(map)
            }
        }

        // Draw the BlueBubble
        val bubbleWidth = laneWidth / 2
        val bubbleHeight = bubbleWidth * 1
        val bubbleX = BlueBubbleT * laneWidth + (laneWidth - bubbleWidth) / 2
        val bubbleY = viewHeight - bubbleHeight
        BlueBubble.setBounds(bubbleX, bubbleY, bubbleX + bubbleWidth, bubbleY + bubbleHeight)
        BlueBubble.draw(canvas)

        // Draw other bubbles and handle collisions
        val indicesToRemove = mutableListOf<Int>()
        for (i in otherBubble.indices) {
            val bubbleBX = otherBubble[i]["lane"] as Int * laneWidth + (laneWidth - bubbleWidth) / 2
            var bubbleBY = time - (otherBubble[i]["startTime"] as Int)
            bubbleBY *= speed // Speed up the bubbles movement
            if (bubbleBY < -bubbleHeight) { // Remove bubbles that have passed beyond the top of the screen
                indicesToRemove.add(i)
                updateScoreAndSpeed()
            } else {
                val otherBubbleWidth = laneWidth / 2
                val otherBubbleHeight = otherBubbleWidth * 1
                RedBubble.setBounds(bubbleBX, bubbleBY, bubbleBX + otherBubbleWidth, bubbleBY + otherBubbleHeight)
                RedBubble.draw(canvas)
                if (isRunning && otherBubble[i]["lane"] as Int == BlueBubbleT && bubbleBY > bubbleY - otherBubbleHeight && bubbleBY < bubbleY + bubbleHeight) {
                    // Game over if a collision occurs
                    gameTask.closeGame(score)
                } else if (isRunning && bubbleBY >= bubbleY + bubbleHeight && bubbleBY < bubbleY + 2 * bubbleHeight) {
                    // Score increases
                    score += 1
                }
            }
        }

        // Remove red bubbles outside the loop
        indicesToRemove.forEach { index ->
            otherBubble.removeAt(index)
        }

        // Draw score and speed
        myPaint.color = Color.WHITE
        myPaint.textSize = 40f
        canvas.drawText("Score : $score", 80f, 80f, myPaint)
        canvas.drawText("Speed : $speed", 380f, 80f, myPaint)

        // Redraw the view
        if (isRunning) {
            invalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            when (it.action) {
                MotionEvent.ACTION_DOWN -> {
                    val x1 = it.x
                    BlueBubbleT = when {
                        x1 < laneWidth -> 0
                        x1 < laneWidth * 2 -> 1
                        else -> 2
                    }
                }
            }
        }
        return true
    }

    fun startAnimation() {
        isRunning = true
        invalidate()
    }

    fun stopAnimation() {
        isRunning = false
    }
}
