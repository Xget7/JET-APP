package xget.dev.jet.presentation.splash.components.jetvectorsandicons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object Vectors {


    fun welcomeScreenTopVector(): ImageVector {
        return Builder(
            name = "Vector 4", defaultWidth = 550.dp, defaultHeight = 563.0.dp,
            viewportWidth = 390.0f, viewportHeight = 463.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF0003FF)), stroke = null, fillAlpha = 0.62f,
                strokeLineWidth = 0.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(0.0f, 454.0f)
                lineTo(390.0f, 364.674f)
                lineTo(390.0f, 0.0f)
                lineTo(0.051f, 0.0f)
                lineTo(0.0f, 15.61f)
                lineTo(0.0f, 454.0f)
                close()
            }
            path(
                fill = SolidColor(Color(0x00000000)), stroke = SolidColor(Color(0xFF90E0EF)),
                strokeLineWidth = 1.0f, strokeLineCap = Butt, strokeLineJoin = Miter,
                strokeLineMiter = 4.0f, pathFillType = NonZero
            ) {
                moveTo(0.0f, 454.0f)
                lineTo(390.0f, 364.674f)
                lineTo(390.0f, 0.0f)
                lineTo(0.01f, 0.0f)
                lineTo(0.0f, 15.61f)
                lineTo(0.0f, 454.0f)
                close()
            }
        }.build()
    }

    fun welcomeScreenBottom(): ImageVector {
        return Builder(
            name = "WelcomeScreenBottom", defaultWidth = 204.0.dp,
            defaultHeight = 100.0.dp, viewportWidth = 234.0f, viewportHeight = 120.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF2E2F73)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(43.101f, 164.794f)
                arcToRelative(68.0f, 176.5f, 119.18f, true, false, 66.308f, -118.74f)
                arcToRelative(68.0f, 176.5f, 119.18f, true, false, -66.308f, 118.74f)
                close()
            }
        }
            .build()
    }


}

