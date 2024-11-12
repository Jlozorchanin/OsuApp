package com.example.osuapp.graph

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.toColor
import com.example.compose.OsuAppTheme
import kotlin.math.roundToInt
import kotlin.random.Random

@Composable
fun LineGraph(
    dataPoints : List<DataPoint>,
    visibleDataPointsIndices : IntRange = 1..89, // for scrolling
    style: ChartStyle,
    modifier : Modifier = Modifier,
    selectedDataPoint: DataPoint? = null,
    onSelectedDataPoint : (DataPoint) -> Unit = {},
    onXLabelWidthChange : (Float) -> Unit = {},
    showHelperLines : Boolean = true
) {
    val textStyle = LocalTextStyle.current.copy(
        fontSize = style.labelFontSize
    )
    val visibleDataPoints : List<DataPoint> = remember(dataPoints,visibleDataPointsIndices) {
        dataPoints
    }
    val maxYValue = remember(visibleDataPoints) {
        visibleDataPoints.maxOfOrNull { it.y } ?: 0f
    }
    val minYValue = remember(visibleDataPoints) {
        visibleDataPoints.minOfOrNull { it.y } ?: 0f
    }
    val measurer = rememberTextMeasurer()
    var xLabelWidth by remember {
        mutableFloatStateOf(0f)
    }
    LaunchedEffect(xLabelWidth) {
        onXLabelWidthChange(xLabelWidth)
    }
    val selectedDataPointIndex = remember(selectedDataPoint) {
        dataPoints.indexOf(selectedDataPoint)
    }
    var drawPoints by remember {
        mutableStateOf(listOf<DataPoint>())
    }
    var isShowingDataPoint by remember {
        mutableStateOf(selectedDataPoint != null)
    }

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(drawPoints,xLabelWidth){
                detectHorizontalDragGestures{change, _ ->
                    val newSelectedDataPointIndex = getSelectedDataPointIndex(
                        touchOffsetX = change.position.x,
                        triggerWidth = xLabelWidth,
                        drawPoints = drawPoints
                    )
                    isShowingDataPoint =
                        (newSelectedDataPointIndex + visibleDataPointsIndices.first) in
                                visibleDataPointsIndices
                    if(isShowingDataPoint){
                        onSelectedDataPoint(dataPoints[newSelectedDataPointIndex])
                    }
                }
            }
    ) {
        val minLabelSpacingYPX = style.minYLabelSpacing.toPx()
        val verticalPaddingPx = style.verticalPadding.toPx()
        val horizontalPaddingPx = style.horizontalPadding.toPx()
        val xAxisLabelSpacing = style.xAxisLabelSpacing.toPx()

        val xLabelTextLayoutResults = visibleDataPoints.map { measurer.measure(
            text = it.xLabel,
            style = textStyle.copy(textAlign = TextAlign.Center)
        ) }

        val maxXLabelWidth = xLabelTextLayoutResults.maxOfOrNull { it.size.width } ?: 0
        val maxXLabelHeight = xLabelTextLayoutResults.maxOfOrNull { it.size.height } ?: 0

        val maxXLabelLineCount = xLabelTextLayoutResults.maxOfOrNull { it.lineCount } ?: 0
        val xLabelLineHeight = if(maxXLabelLineCount > 0) {
            maxXLabelHeight / maxXLabelLineCount
        } else 0

        val viewPortHeightPx = size.height -
                (maxXLabelHeight + 2 * verticalPaddingPx
                        + xLabelLineHeight + xAxisLabelSpacing)

        // Y_LABEL calc

        val labelViewPortHeightPx = viewPortHeightPx + xLabelLineHeight
        val labelCountExcludingLastLabel =
            ((labelViewPortHeightPx / (xLabelLineHeight + minLabelSpacingYPX)) ).toInt()
        val valueInc = (maxYValue - minYValue) / labelCountExcludingLastLabel

        val yLabels = (0..labelCountExcludingLastLabel)

        val yLabelTextLayoutResults = yLabels.map {
            measurer.measure(
                text = it.toString(),
                style = textStyle
            )
        }
        val maxYLabelWidth = yLabelTextLayoutResults.maxOfOrNull { it.size.width } ?: 0


        val viewPortTopY = verticalPaddingPx + xLabelLineHeight + 10f
        val viewPortRightX = size.width
        val viewPortBottomY = viewPortTopY + viewPortHeightPx
        val viewPortLeftX = 2f * horizontalPaddingPx + maxYLabelWidth


        xLabelWidth = size.width / 90
        xLabelTextLayoutResults.forEachIndexed { index, result ->
            val x = xAxisLabelSpacing / 2f +
                    xLabelWidth * index + viewPortLeftX
            if (selectedDataPointIndex == index){
                val valueLabel = - visibleDataPoints[index].y
                val valueResult = measurer.measure(
                    text = "#${valueLabel.toInt().toString()}",
                    style = textStyle.copy(
                        color = style.selectedColor,
                    ),
                    maxLines = 1
                )
                val days = measurer.measure(
                    text = when(index){
                        88 -> "Now"
                        87 -> "1d ago"
                        else -> "${90 - index}d ago"
                    },
                    style = textStyle.copy(
                        color = style.selectedColor,
                    ),

                )
                val textPosX = if(selectedDataPointIndex == visibleDataPointsIndices.last){
                    x - valueResult.size.width /2f - viewPortLeftX
                }
                else {
                    if (selectedDataPointIndex in 1..7){
                        x - valueResult.size.width /2f + viewPortLeftX * 0.3f
                    } else {
                        x - valueResult.size.width /2f - viewPortLeftX * 0.3f
                    }

                } + result.size.width /9f
                val isTextIsVisible =
                    (size.width - textPosX).roundToInt() in 0..size.width.roundToInt()
                if(isTextIsVisible){
                    drawText(
                        textLayoutResult = valueResult,
                        topLeft = Offset(
                            x = textPosX,
                            y = viewPortTopY - valueResult.size.height - 15f
                        ),
                    )
                    drawText(
                        textLayoutResult = days,
                        topLeft = Offset(
                            x = textPosX,
                            y = viewPortBottomY + days.size.height - 32f
                        ),
                    )
                }

            }

        }


        val heightReqForLabels = xLabelLineHeight * (labelCountExcludingLastLabel + 1)
        val remainingHeightForLabels = labelViewPortHeightPx - heightReqForLabels


        drawPoints = visibleDataPointsIndices.map{ it ->
            val x = (it - visibleDataPointsIndices.first) *
                    xLabelWidth + xLabelWidth /2f + viewPortLeftX

            val ratio = (dataPoints[it].y - minYValue) / (maxYValue - minYValue)
            val y = viewPortBottomY - (ratio * viewPortHeightPx)
            DataPoint(
                x = x,
                y = y,
                xLabel = dataPoints[it].xLabel
            )
        }

        val conPoints1 = mutableListOf<DataPoint>()
        val conPoints2 = mutableListOf<DataPoint>()
        for ( i in 1 until drawPoints.size){
            val p0 = drawPoints[i-1]
            val p1 = drawPoints[i]

            val x = (p1.x + p0.x)/2
            val y1 = p0.y
            val y2 = p1.y

            conPoints1.add(DataPoint(x,y1,""))
            conPoints2.add(DataPoint(x,y2,""))
        }

        val linePath = Path().apply {
            if(drawPoints.isNotEmpty()){
                moveTo(drawPoints.first().x,drawPoints.first().y)

                for ( i in 1 until drawPoints.size){
                    cubicTo(
                        x1 = conPoints1[i-1].x,
                        y1 = conPoints1[i-1].y,
                        x2 = conPoints2[i-1].x,
                        y2 = conPoints2[i-1].y,
                        x3 = drawPoints[i].x,
                        y3 = drawPoints[i].y
                    )
                }
            }
        }
        drawPath(
            path = linePath,
            color = style.chartLineColor,
            style = Stroke(width = 11f, cap = StrokeCap.Round)
        )

        drawPoints.forEachIndexed { index, point ->
            val offset = Offset(
                x = point.x,
                y = point.y
            )
            if (isShowingDataPoint) {

                if (selectedDataPointIndex == index){
                    drawCircle(
                        color = Color.DarkGray,
                        radius = 25f,
                        center = offset
                    )
                    drawCircle(
                        color = style.selectedColor,
                        radius = 25f,
                        center = offset,
                        style = Stroke(
                            width = 5f
                        )
                    )

                }
            }
        }
    }

}

private fun getSelectedDataPointIndex(
    touchOffsetX : Float,
    triggerWidth : Float,
    drawPoints : List<DataPoint>
) : Int {
    val triggerRangeLeft = touchOffsetX - triggerWidth /2f
    val triggerRangeRight = touchOffsetX + triggerWidth /2f

    return drawPoints.indexOfFirst {
        it.x in triggerRangeLeft..triggerRangeRight
    }

}

@Preview(widthDp = 1000)
@Composable
private fun LineChartPrev() {
    OsuAppTheme() {
        val rankHistory = remember {
            (1..90).map {
                Random.nextInt(-200000,400000)
            }
        }

        val style = ChartStyle(
            chartLineColor = Color.Black,
            unselectedColor = Color(0xFF7C7C7C),
            selectedColor = Color.Black,
            helperLinesThickness = 3f,
            axisLinesThickness = 5f,
            labelFontSize = 14.sp,
            verticalPadding = 8.dp,
            horizontalPadding = 1.dp,
            minYLabelSpacing = 1.dp,
            xAxisLabelSpacing = 0.dp
        )
        val dataPoints = remember {
            rankHistory.mapIndexed { index, result ->
                DataPoint(
                    x = index.toFloat(),
                    y = result.toFloat(),
                    xLabel = result.toString()
                )
            }
        }
        LineGraph(
            showHelperLines = true,
            dataPoints = dataPoints,
            style = style,
            visibleDataPointsIndices = 1..89,
            modifier = Modifier
                .width(700.dp)
                .height(300.dp)
                .background(Color.White),
            selectedDataPoint = dataPoints[6],

            )

    }
}
