package tech.kotlinlang.graphs.barGraph

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import kotlin.math.abs

@Composable
fun BarGraph(
    modifier: Modifier = Modifier,
    barGroupDataList: List<BarGroupData>,
    graphSettings: GraphSettings = GraphSettings(),
    transformValue: (Float) -> String = { it.toString() },
    yLabel: String? = null,
) {
    val maximumValue = if (barGroupDataList.isEmpty()) 0F
    else barGroupDataList.maxOf { barGroupData ->
        barGroupData.barDataList.maxOf {
            it.value
        }
    }

    val minimumValue = if (barGroupDataList.isEmpty()) 0F
    else barGroupDataList.minOf { barGroupData ->
        barGroupData.barDataList.minOf {
            it.value
        }
    }

    val maxHeight: Float
    val minHeight: Float

    val type: Boolean?
    if (maximumValue >= 0F && minimumValue >= 0F) {
        type = true
        maxHeight = maximumValue
        minHeight = 0F
    } else if (maximumValue < 0F && minimumValue < 0F) {
        type = false
        maxHeight = 0F
        minHeight = minimumValue
    } else if (abs(maximumValue) > abs(minimumValue)) {
        type = null
        maxHeight = maximumValue
        minHeight = maximumValue * -1
    } else {
        type = null
        maxHeight = minimumValue * -1
        minHeight = minimumValue
    }

    val eachHeightGap = (maxHeight - minHeight) / (graphSettings.yAxisValueCount - 1)
    val textMeasurer = rememberTextMeasurer()
    Canvas(
        modifier = modifier,
    ) {
        val yAxisValueMeasure = textMeasurer.measure(
            text = "",
            maxLines = 1,
            style = graphSettings.yAxisValueStyle,
            softWrap = false,
        )

        val xAxisValueMeasure = textMeasurer.measure(
            text = "",
            maxLines = 1,
            style = graphSettings.xAxisValueStyle,
            softWrap = false,
        )

        // bar Label Start
        var totalBarTypeLabelHeight = 0F
        var totalXLabelTaken = 0F
        val barTypeList = barGroupDataList.flatMap { it.barDataList }.map { it.type }.distinct().reversed()
        barTypeList.forEachIndexed { index, barType ->
            val barTypeMeasure = textMeasurer.measure(
                text = barType.label,
                maxLines = 1,
                style = graphSettings.barLabelStyle,
                softWrap = false,
            )

            val radius = barTypeMeasure.size.height / 3F
            val totalBarTypeLabelWidthToBeTaken = radius + barTypeMeasure.size.height + barTypeMeasure.size.width

            var xLabelOffset = size.width - if (size.width - totalXLabelTaken > totalBarTypeLabelWidthToBeTaken) {
                totalXLabelTaken + totalBarTypeLabelWidthToBeTaken + if (totalXLabelTaken == 0F) 0F else graphSettings.barLabelGap.toPx()
            } else {
                totalBarTypeLabelHeight += barTypeMeasure.size.height.toFloat() + 4.dp.toPx()
                totalBarTypeLabelWidthToBeTaken
            }

            drawCircle(
                color = barType.color,
                radius = radius,
                center = Offset(
                    x = xLabelOffset + radius,
                    y = size.height - totalBarTypeLabelHeight - barTypeMeasure.size.height / 2,
                ),
            )
            xLabelOffset += barTypeMeasure.size.height
            drawText(
                textLayoutResult = barTypeMeasure,
                topLeft = Offset(
                    x = xLabelOffset,
                    y = size.height - totalBarTypeLabelHeight - barTypeMeasure.size.height,
                ),
            )
            totalXLabelTaken += totalBarTypeLabelWidthToBeTaken
            if (index == barTypeList.lastIndex) totalBarTypeLabelHeight += barTypeMeasure.size.height.toFloat() + 4.dp.toPx()
        }
        // bar Label End

        val totalYAxisValueHeight =
            size.height - xAxisValueMeasure.size.height - graphSettings.xAxisValueGraphGap.toPx() - totalBarTypeLabelHeight + (yAxisValueMeasure.size.height / 2)

        val eachYAxisValueGap =
            (totalYAxisValueHeight - (yAxisValueMeasure.size.height * graphSettings.yAxisValueCount)) / (graphSettings.yAxisValueCount - 1)

        var maxYAxisValueTextWidthResult = 0F
        val yAxisLabelOffset = if (yLabel != null) {
            val yLabelMeasuredText = textMeasurer.measure(
                text = AnnotatedString(yLabel),
                maxLines = 1,
                style = graphSettings.yLabelStyle.copy(
                    textAlign = TextAlign.Center
                ),
                constraints = Constraints.fixedWidth(totalYAxisValueHeight.toInt())
            )
            rotate(
                degrees = 270F,
                pivot = Offset(
                    0F,
                    totalYAxisValueHeight,
                )
            ) {
                drawText(
                    textLayoutResult = yLabelMeasuredText,
                    topLeft = Offset(
                        0F,
                        totalYAxisValueHeight,
                    ),
                )
            }
            yLabelMeasuredText.size.height.toFloat()
        } else 0F

        repeat(graphSettings.yAxisValueCount) { index ->
            val yAxisValueTextResult = textMeasurer.measure(
                text = AnnotatedString(transformValue(maxHeight - (eachHeightGap * index))),
                maxLines = 1,
                style = graphSettings.yAxisValueStyle,
                softWrap = false,
            )
            if (maxYAxisValueTextWidthResult < yAxisValueTextResult.size.width) {
                maxYAxisValueTextWidthResult = yAxisValueTextResult.size.width.toFloat()
            }
            val yOffset = (yAxisValueMeasure.size.height + eachYAxisValueGap) * index
            drawText(
                textLayoutResult = yAxisValueTextResult,
                topLeft = Offset(
                    x = yAxisLabelOffset + graphSettings.yLabelGapEnd.toPx(),
                    y = yOffset,
                ),
            )
        }
        maxYAxisValueTextWidthResult += yAxisLabelOffset

        val xAxisValueTextOffset = maxYAxisValueTextWidthResult + graphSettings.yAxisValueGraphGap.toPx()
        val totalXOffsetBeforeGraph = xAxisValueTextOffset + 0F

        val yAxisValueTextOffset = yAxisValueMeasure.size.height / 2
        val totalYOffsetBeforeGraph = yAxisValueTextOffset + 0F

        val totalGraphWidth = size.width - totalXOffsetBeforeGraph
        val totalGraphHeight =
            size.height - xAxisValueMeasure.size.height - graphSettings.xAxisValueGraphGap.toPx() - totalBarTypeLabelHeight - (yAxisValueMeasure.size.height / 2)

        val graphSize = Size(totalGraphWidth, totalGraphHeight)
        val heightRatio = graphSize.height / (maxHeight - minHeight)
        val totalBarGap = (graphSize.width * graphSettings.barGapRatio)
        val eachBarGap = totalBarGap / barGroupDataList.size

        val barGroupWidth = (graphSize.width - totalBarGap) / barGroupDataList.size

        val startOffsetForLine: Offset
        val endOffsetForLine: Offset
        when (type) {
            true -> {
                startOffsetForLine = Offset(
                    x = totalXOffsetBeforeGraph,
                    y = totalYOffsetBeforeGraph + totalGraphHeight,
                )
                endOffsetForLine = Offset(
                    x = totalXOffsetBeforeGraph + graphSize.width,
                    y = totalYOffsetBeforeGraph + totalGraphHeight
                )
            }

            false -> {
                startOffsetForLine = Offset(
                    x = totalXOffsetBeforeGraph,
                    y = totalYOffsetBeforeGraph,
                )
                endOffsetForLine = Offset(
                    x = totalXOffsetBeforeGraph + graphSize.width,
                    y = totalYOffsetBeforeGraph,
                )
            }

            null -> {
                startOffsetForLine = Offset(
                    x = totalXOffsetBeforeGraph,
                    y = totalYOffsetBeforeGraph + (totalGraphHeight / 2),
                )
                endOffsetForLine = Offset(
                    x = totalXOffsetBeforeGraph + graphSize.width,
                    y = totalYOffsetBeforeGraph + (totalGraphHeight / 2),
                )

            }
        }

        drawLine(
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round,
            color = graphSettings.zeroLineColor,
            start = startOffsetForLine,
            end = endOffsetForLine,
        )

        barGroupDataList.forEachIndexed { barGroupIndex, barGroupData ->
            val eachBarWidth = (barGroupWidth) / barGroupData.barDataList.size
            val barGroupOffset = totalXOffsetBeforeGraph + barGroupWidth * barGroupIndex + eachBarGap * (barGroupIndex + 0.5F)

            barGroupData.barDataList.forEachIndexed { barDataIndex, barData ->
                val barDataOffset = barGroupOffset + eachBarWidth * barDataIndex

                val barHeight: Float
                val barYOffset: Float

                when (type) {
                    true -> {
                        barHeight = heightRatio * barData.value
                        barYOffset = totalYOffsetBeforeGraph + (totalGraphHeight - barHeight)
                    }

                    false -> {
                        barHeight = heightRatio * barData.value * -1
                        barYOffset = totalYOffsetBeforeGraph
                    }

                    null -> {
                        barHeight = heightRatio * abs(barData.value)
                        barYOffset = totalYOffsetBeforeGraph + if (barData.value >= 0F) {
                            totalGraphHeight / 2 - barHeight
                        } else {
                            totalGraphHeight / 2
                        }
                    }
                }

                val cornerRadius = CornerRadius(graphSettings.cornerRoundness.toPx())
                val path = Path().apply {
                    addRoundRect(
                        RoundRect(
                            rect = Rect(
                                offset = Offset(
                                    x = barDataOffset,
                                    y = barYOffset,
                                ),
                                size = Size(
                                    width = eachBarWidth,
                                    height = barHeight,
                                ),
                            ),
                            topLeft = if (barData.value >= 0F) cornerRadius else CornerRadius.Zero,
                            topRight = if (barData.value >= 0F) cornerRadius else CornerRadius.Zero,
                            bottomLeft = if (barData.value >= 0F) CornerRadius.Zero else cornerRadius,
                            bottomRight = if (barData.value >= 0F) CornerRadius.Zero else cornerRadius,
                        )
                    )
                }
                if (graphSettings.gradientToZero) {
                    val colors = if (barData.value >= 0F) listOf(barData.type.color, Color.Transparent)
                    else listOf(Color.Transparent, barData.type.color)
                    drawPath(path, Brush.verticalGradient(colors))
                } else {
                    drawPath(path, barData.type.color)
                }
            }
            val xValueTextResult = textMeasurer.measure(
                text = AnnotatedString(barGroupData.xValue),
                maxLines = 1,
                style = graphSettings.xAxisValueStyle,
                softWrap = false,
            )
            drawText(
                textLayoutResult = xValueTextResult,
                topLeft = Offset(
                    x = barGroupOffset + (barGroupWidth - xValueTextResult.size.width) / 2,
                    y = totalYOffsetBeforeGraph + graphSize.height + graphSettings.xAxisValueGraphGap.toPx()
                )
            )
        }
    }
}
