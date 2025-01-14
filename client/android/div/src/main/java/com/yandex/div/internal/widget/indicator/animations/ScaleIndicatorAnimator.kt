package com.yandex.div.internal.widget.indicator.animations

import android.animation.ArgbEvaluator
import android.graphics.Color
import android.graphics.RectF
import android.util.SparseArray
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import com.yandex.div.internal.widget.indicator.IndicatorParams
import kotlin.math.abs

internal class ScaleIndicatorAnimator(private val styleParams: IndicatorParams.Style) : IndicatorAnimator {

    private val colorEvaluator: ArgbEvaluator = ArgbEvaluator()
    private val itemsScale = SparseArray<Float>()
    private var itemsCount: Int = 0

    override fun getColorAt(position: Int) = calculateColor(
        getScaleAt(position),
        styleParams.inactiveShape.color,
        styleParams.activeShape.color
    )

    override fun onPageScrolled(position: Int, positionOffset: Float) {
        scaleIndicatorByOffset(position, 1f - positionOffset)
        if (position < itemsCount - 1) {
            scaleIndicatorByOffset(position + 1, positionOffset)
        } else {
            scaleIndicatorByOffset(0, positionOffset)
        }
    }

    override fun onPageSelected(position: Int) {
        itemsScale.clear()
        itemsScale.put(position, 1f)
    }

    override fun setItemsCount(count: Int) {
        itemsCount = count
    }

    override fun getSelectedItemRect(xOffset: Float, yOffset: Float): RectF? = null

    override fun getItemSizeAt(position: Int): IndicatorParams.ItemSize {
        return when (val activeShape = styleParams.activeShape) {
            is IndicatorParams.Shape.Circle -> {
                val inactiveShape = styleParams.inactiveShape as IndicatorParams.Shape.Circle
                IndicatorParams.ItemSize.Circle(
                    inactiveShape.itemSize.radius + (activeShape.itemSize.radius
                            - inactiveShape.itemSize.radius) * getScaleAt(position)
                )
            }
            is IndicatorParams.Shape.RoundedRect -> {
                val inactiveShape = styleParams.inactiveShape as IndicatorParams.Shape.RoundedRect
                IndicatorParams.ItemSize.RoundedRect(
                    inactiveShape.itemSize.itemWidth + (activeShape.itemSize.itemWidth
                            - inactiveShape.itemSize.itemWidth) * getScaleAt(position),
                    inactiveShape.itemSize.itemHeight + (activeShape.itemSize.itemHeight
                            - inactiveShape.itemSize.itemHeight) * getScaleAt(position),
                    inactiveShape.itemSize.cornerRadius + (activeShape.itemSize.cornerRadius
                            - inactiveShape.itemSize.cornerRadius) * getScaleAt(position)
                )
            }
        }
    }

    override fun getBorderColorAt(position: Int): Int {
        return when (val activeShape = styleParams.activeShape) {
            is IndicatorParams.Shape.RoundedRect -> {
                val inactiveShape = styleParams.inactiveShape as IndicatorParams.Shape.RoundedRect
                calculateColor(getScaleAt(position), inactiveShape.strokeColor, activeShape.strokeColor)
            }
            else -> Color.TRANSPARENT
        }
    }

    override fun getBorderWidthAt(position: Int): Float {
        return when (val activeShape = styleParams.activeShape) {
            is IndicatorParams.Shape.RoundedRect -> {
                val inactiveShape = styleParams.inactiveShape as IndicatorParams.Shape.RoundedRect
                inactiveShape.strokeWidth + (activeShape.strokeWidth
                        - inactiveShape.strokeWidth) * getScaleAt(position)
            }
            else -> 0f
        }
    }

    private fun scaleIndicatorByOffset(position: Int, offset: Float) {
        if (offset == 0f) {
            itemsScale.remove(position)
        } else {
            itemsScale.put(position, abs(offset))
        }
    }

    private fun getScaleAt(position: Int): Float = itemsScale.get(position, 0f)
    @ColorInt
    private fun calculateColor(@FloatRange(from = 0.0, to = 1.0) scaleOffset: Float, from: Int, to: Int): Int {
        return colorEvaluator.evaluate(scaleOffset, from, to) as Int
    }

}
