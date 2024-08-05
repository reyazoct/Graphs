package tech.kotlinlang.graphs.barGraph

import androidx.annotation.FloatRange
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class GraphSettings(
    @FloatRange(from = 0.0, to = 1.0)
    val barGapRatio: Float = 0.25F,

    val gradientToZero: Boolean = false,

    val yAxisValueStyle: TextStyle = TextStyle.Default,

    val yAxisValueCount: Int = 5,

    val yAxisValueGraphGap: Dp = 4.dp,

    val xAxisValueStyle: TextStyle = TextStyle.Default,

    val xAxisValueGraphGap: Dp = 4.dp,

    val zeroLineColor: Color = Color(0x33F2F2F2),

    val yLabelStyle: TextStyle = TextStyle.Default,

    val barLabelStyle: TextStyle = TextStyle.Default,

    val yLabelGapEnd: Dp = 4.dp,

    val cornerRoundness: Dp = 8.dp,

    val barLabelGap: Dp = 12.dp,
)
