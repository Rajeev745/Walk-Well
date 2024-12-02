package com.example.walkwell.home.bottom_nav

import android.graphics.PointF
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.BottomAppBar
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.IntOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

private const val CURVE_CIRCLE_RADIUS = 96

// the coordinates of the first curve
private val mFirstCurveStartPoint = PointF()
private val mFirstCurveControlPoint1 = PointF()
private val mFirstCurveControlPoint2 = PointF()
private val mFirstCurveEndPoint = PointF()

private val mSecondCurveControlPoint1 = PointF()
private val mSecondCurveControlPoint2 = PointF()
private var mSecondCurveStartPoint = PointF()
private var mSecondCurveEndPoint = PointF()

class BottomNavCurve(private val selectedIndex: Int, private val totalTabs: Int) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val curve = Path()
        val tabWidth = size.width / totalTabs
        val curveCenterX = (selectedIndex * tabWidth) + (tabWidth / 2)
        val curveDepth = CURVE_CIRCLE_RADIUS + (CURVE_CIRCLE_RADIUS / 15F)

        // the coordinates (x,y) of the start point before curve
        mFirstCurveStartPoint.set(
            curveCenterX - (CURVE_CIRCLE_RADIUS * 2) - (CURVE_CIRCLE_RADIUS / 3), curveDepth
        )

        // the coordinates (x,y) of the end point after curve
        mFirstCurveEndPoint.set(curveCenterX, 0F)

        // same thing for the second curve
        mSecondCurveStartPoint = mFirstCurveEndPoint
        mSecondCurveEndPoint.set(
            curveCenterX + (CURVE_CIRCLE_RADIUS * 2) + (CURVE_CIRCLE_RADIUS / 3), curveDepth
        )

        // the coordinates (x,y)  of the 1st control point on a cubic curve
        mFirstCurveControlPoint1.set(
            mFirstCurveStartPoint.x + curveDepth, mFirstCurveStartPoint.y
        )

        // the coordinates (x,y)  of the 2nd control point on a cubic curve
        mFirstCurveControlPoint2.set(
            mFirstCurveEndPoint.x - (CURVE_CIRCLE_RADIUS * 2) + CURVE_CIRCLE_RADIUS,
            mFirstCurveEndPoint.y
        )

        mSecondCurveControlPoint1.set(
            mSecondCurveStartPoint.x + (CURVE_CIRCLE_RADIUS * 2) - CURVE_CIRCLE_RADIUS,
            mSecondCurveStartPoint.y
        )

        mSecondCurveControlPoint2.set(
            mSecondCurveEndPoint.x - (curveDepth), mSecondCurveEndPoint.y
        )

        curve.moveTo(0f, curveDepth)
        curve.lineTo(mFirstCurveStartPoint.x, mFirstCurveStartPoint.y)
        curve.cubicTo(
            mFirstCurveControlPoint1.x,
            mFirstCurveControlPoint1.y,
            mFirstCurveControlPoint2.x,
            mFirstCurveControlPoint2.y,
            mFirstCurveEndPoint.x,
            mFirstCurveEndPoint.y
        )
        curve.cubicTo(
            mSecondCurveControlPoint1.x,
            mSecondCurveControlPoint1.y,
            mSecondCurveControlPoint2.x,
            mSecondCurveControlPoint2.y,
            mSecondCurveEndPoint.x,
            mSecondCurveEndPoint.y
        )
        curve.lineTo(size.width, curveDepth)
        curve.lineTo(size.width, size.height)
        curve.lineTo(0F, size.height)

        return Outline.Generic(path = curve)
    }
}
