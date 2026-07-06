package com.ypm14.simpatik.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ═══════════════════════════════════════
// PIE CHART — pakai Canvas + drawArc
// ═══════════════════════════════════════
data class PieSlice(val label: String, val value: Float, val color: Color)

@Composable
fun SimplePieChart(
    slices: List<PieSlice>,
    modifier: Modifier = Modifier,
    size: Dp = 120.dp,
    strokeWidth: Dp = 40.dp
) {
    val total = slices.sumOf { it.value.toDouble() }.toFloat()
    val animatedProgress by animateFloatAsState(targetValue = 1f, animationSpec = tween(800), label = "pie")

    Canvas(modifier = modifier.size(size)) {
        val diameter = size.toPx()
        val stroke = strokeWidth.toPx()
        val arcSize = Size(diameter - stroke, diameter - stroke)
        val topLeft = Offset(stroke / 2, stroke / 2)
        var startAngle = -90f

        slices.forEach { slice ->
            val sweep = (slice.value / total) * 360f * animatedProgress
            drawArc(color = slice.color, startAngle = startAngle, sweepAngle = sweep,
                useCenter = false, topLeft = topLeft, size = arcSize, style = Stroke(width = stroke, cap = StrokeCap.Butt))
            startAngle += sweep
        }
    }
}

// ═══════════════════════════════════════
// LINE CHART — pakai Canvas + drawPath
// ═══════════════════════════════════════
@Composable
fun SimpleLineChart(
    dataPoints: List<Pair<String, Float>>, // (label, value)
    lineColor: Color,
    modifier: Modifier = Modifier,
    height: Dp = 140.dp
) {
    val animatedProgress by animateFloatAsState(targetValue = 1f, animationSpec = tween(600), label = "line")

    Canvas(modifier = modifier.fillMaxWidth().height(height).padding(end = 8.dp)) {
        if (dataPoints.isEmpty()) return@Canvas
        val maxVal = dataPoints.maxOfOrNull { it.second } ?: 1f
        val minVal = dataPoints.minOfOrNull { it.second } ?: 0f
        val range = (maxVal - minVal).coerceAtLeast(0.001f)
        val stepX = size.width / (dataPoints.size - 1).coerceAtLeast(1)
        val padY = 16f
        val chartH = size.height - padY * 2

        val path = Path()
        dataPoints.forEachIndexed { i, (_, v) ->
            val x = i * stepX
            val y = padY + chartH - ((v - minVal) / range) * chartH
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }

        // Animate draw length
        val finalX = (dataPoints.size - 1) * stepX
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 2.5f, cap = StrokeCap.Round, join = androidx.compose.ui.graphics.StrokeJoin.Round)
        )

        // Dots
        dataPoints.forEachIndexed { i, (_, v) ->
            val x = i * stepX
            val y = padY + chartH - ((v - minVal) / range) * chartH
            if (x <= finalX * animatedProgress) {
                drawCircle(color = lineColor, radius = 3f, center = Offset(x, y))
            }
        }

        // Y-axis labels (manual)
        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.parseColor("#6B7280")
            textSize = 24f
            isAntiAlias = true
        }
        drawContext.canvas.nativeCanvas.apply {
            drawText(maxVal.toInt().toString(), 2f, padY + 12f, paint)
            drawText(minVal.toInt().toString(), 2f, size.height - padY - 2f, paint)
        }
    }
}

// ═══════════════════════════════════════
// PROGRESS BAR
// ═══════════════════════════════════════
@Composable
fun SimpleProgressBar(
    progress: Float, // 0..1
    color: Color,
    modifier: Modifier = Modifier,
    height: Dp = 8.dp
) {
    val animated by animateFloatAsState(targetValue = progress, animationSpec = tween(800), label = "prog")
    Box(modifier = modifier.height(height).fillMaxWidth()) {
        // Background
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRoundRect(color = color.copy(alpha = 0.15f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(height.toPx() / 2))
        }
        // Progress
        Canvas(modifier = Modifier.fillMaxWidth().fillMaxHeight()) {
            val w = size.width * animated
            if (w > 0) {
                drawRoundRect(color = color, size = Size(w, size.height),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(height.toPx() / 2))
            }
        }
    }
}

// ═══════════════════════════════════════
// LEGEND
// ═══════════════════════════════════════
@Composable
fun ChartLegend(
    items: List<Pair<Color, String>>,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items.forEach { (color, label) ->
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                Canvas(modifier = Modifier.size(8.dp)) {
                    drawCircle(color = color)
                }
                Spacer(Modifier.width(4.dp))
                Text(label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
