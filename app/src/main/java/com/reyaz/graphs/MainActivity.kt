package com.reyaz.graphs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.reyaz.graphs.ui.theme.GraphsTheme
import tech.kotlinlang.graphs.barGraph.BarData
import tech.kotlinlang.graphs.barGraph.BarGraph
import tech.kotlinlang.graphs.barGraph.BarGroupData
import tech.kotlinlang.graphs.barGraph.BarType
import tech.kotlinlang.graphs.barGraph.GraphSettings

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GraphsTheme {
                val malesBarType = object : BarType {
                    override val color: Color
                        get() = Color.Green
                }
                val femalesBarType = object : BarType {
                    override val color: Color
                        get() = Color.Blue
                }
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    BarGraph(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(2F),
                        barGroupDataList = listOf(
                            BarGroupData(
                                barDataList = listOf(
                                    BarData(
                                        value = -100F,
                                        type = malesBarType
                                    ),
                                    BarData(
                                        value = -50F,
                                        type = femalesBarType,
                                    )
                                ),
                                xValue = "2019",
                            ),
                            BarGroupData(
                                barDataList = listOf(
                                    BarData(
                                        value = -60F,
                                        type = malesBarType
                                    ),
                                    BarData(
                                        value = -50F,
                                        type = femalesBarType,
                                    )
                                ),
                                xValue = "2020",
                            ),
                            BarGroupData(
                                barDataList = listOf(
                                    BarData(
                                        value = 60F,
                                        type = malesBarType
                                    ),
                                    BarData(
                                        value = -80F,
                                        type = femalesBarType,
                                    )
                                ),
                                xValue = "2020",
                            )

                        ),
                        graphSettings = GraphSettings(
                            gradientToZero = true,
                            zeroLineColor = Color.Black,
                        ),
                    )
                }
            }
        }
    }
}