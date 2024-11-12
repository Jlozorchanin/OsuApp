package com.example.osuapp.graph

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

data class ChartStyle(
    val chartLineColor : Color,
    val unselectedColor : Color,
    val selectedColor : Color,
    val helperLinesThickness : Float,
    val axisLinesThickness : Float,
    val labelFontSize : TextUnit,
    val minYLabelSpacing : Dp,
    val verticalPadding : Dp,
    val horizontalPadding : Dp,
    val xAxisLabelSpacing : Dp
)