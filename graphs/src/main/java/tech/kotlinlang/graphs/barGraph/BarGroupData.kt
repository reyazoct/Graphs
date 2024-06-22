package tech.kotlinlang.graphs.barGraph

data class BarGroupData(
    val barDataList: List<BarData<BarType>>,
    val xValue: String
)