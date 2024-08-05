package tech.kotlinlang.graphs.barGraph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import tech.kotlinlang.graphs.barGraphImpl.BarGraphImpl

@Composable
fun BarGraph(
    modifier: Modifier = Modifier,
    barGroupDataList: List<BarGroupData>,
    graphSettings: GraphSettings = GraphSettings(),
    transformValue: (Float) -> String = { it.toString() },
    yLabel: String? = null,
) {
    BarGraphImpl(
        modifier = modifier,
        barGroupDataList = barGroupDataList,
        graphSettings = graphSettings,
        transformValue = transformValue,
        yLabel = yLabel,
    )
}
