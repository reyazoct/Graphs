package tech.kotlinlang.graphs.barGraph

data class BarData<T: BarType>(
    val value: Float,
    val type: T,
)