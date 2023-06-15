package xget.dev.jet.presentation.main.device_config.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.DurationBasedAnimationSpec
import androidx.compose.animation.core.EaseInCirc
import androidx.compose.animation.core.EaseOutCirc
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.TabRowDefaults.Divider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.input.pointer.PointerIcon.Companion.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.coroutines.NonDisposableHandle.parent
import xget.dev.jet.ui.theme.JetBlue
import xget.dev.jet.ui.theme.JetBlue2

@Composable
fun SearchDeviceSteps() {
    val numberStep = 3
    var currentStep by rememberSaveable { mutableIntStateOf(4) }
    val titleList = arrayListOf(
        "Escaneo de\ndispositivos\ncercanos",
        "Registro del\n dispositivo en la \n nube",
        "Configuración\ndel dispositivo"
    )


    xget.dev.jet.presentation.main.device_config.components.Stepper(
        modifier = Modifier.width(380.dp),
        numberOfSteps = numberStep,
        currentStep = currentStep,
        stepDescriptionList = titleList,
        selectedColor = JetBlue2,
        unSelectedColor = Color(0xFFC9C9C9),
    )

}

@Preview(showBackground = true)
@Composable
fun SEarchDeviceSteps() {
    SearchDeviceSteps()
}

@Composable
fun Stepper(
    modifier: Modifier = Modifier,
    numberOfSteps: Int,
    currentStep: Int,
    stepDescriptionList: List<String> = List(numberOfSteps) { "" },
    unSelectedColor: Color = Color.LightGray,
    selectedColor: Color = JetBlue2,
    isRainbow: Boolean = false
) {

    val descriptionList = MutableList(numberOfSteps) { "" }

    stepDescriptionList.forEachIndexed { index, element ->
        if (index < numberOfSteps)
            descriptionList[index] = element
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (step in 1..numberOfSteps) {
            Step(
                modifier = Modifier.weight(1F),
                step = step,
                isCompete = step < currentStep,
                isCurrent = step == currentStep,
                isComplete = step == numberOfSteps,
                isRainbow = isRainbow,
                stepDescription = descriptionList[step - 1],
                unSelectedColor = unSelectedColor,
                selectedColor = selectedColor,
            )
        }
    }
}

@Composable
private fun Step(
    modifier: Modifier = Modifier,
    step: Int,
    isCompete: Boolean,
    isCurrent: Boolean,
    isComplete: Boolean,
    isRainbow: Boolean,
    stepDescription: String,
    unSelectedColor: Color,
    selectedColor: Color,
) {

    val rainBowColor = Brush.linearGradient(
        listOf(
            Color.Magenta,
            Color.Blue,
            Color.Cyan,
            Color.Green,
            Color.Yellow,
            Color.Red,
        )
    )

    val transition = updateTransition(isCompete, label = "")

    val innerCircleColor by transition.animateColor(label = "innerCircleColor") {
        if (it) selectedColor else unSelectedColor
    }
    val txtColor by transition.animateColor(label = "txtColor")
    { if (it || isCurrent) selectedColor else unSelectedColor }

    val color by transition.animateColor(label = "color")
    { if (it || isCurrent) selectedColor else Color.Gray }

    val borderStroke: BorderStroke = if (isRainbow) {
        BorderStroke(2.dp, rainBowColor)
    } else {
        BorderStroke(0.dp, color)
    }

    val textSize by remember { mutableStateOf(12.sp) }
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val offset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 130f,
        animationSpec = infiniteRepeatable(tween(10 * 100), repeatMode = RepeatMode.Reverse), label = ""
    )

    val brush = Brush.linearGradient(
        listOf(JetBlue2, White),
        start = Offset(offset, offset),
        end = Offset(offset , offset + 120f),
        tileMode = TileMode.Mirror
    )
    ConstraintLayout(modifier = modifier) {

        val (circle, txt, line) = createRefs()

        Surface(
            shape = CircleShape,
            border = if (isCurrent) BorderStroke(2.dp, brush) else null,
            color = innerCircleColor,
            modifier = Modifier
                .size(30.dp)
                .constrainAs(circle) {
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                }
        ) {

            Box(contentAlignment = Alignment.Center) {
                if (isCompete)
                    Icon(
                        imageVector = Icons.Default.Done, "done",
                        modifier = modifier.padding(4.dp),
                        tint = White
                    )
                else
                    Text(
                        text = step.toString(),
                        color = White,
                        fontSize = 15.sp
                    )
            }
        }

        Text(
            modifier = Modifier.constrainAs(txt) {
                top.linkTo(circle.bottom, margin = 3.dp)
                start.linkTo(circle.start)
                end.linkTo(circle.end)
                bottom.linkTo(parent.bottom)
            },
            fontSize = textSize,
            overflow = TextOverflow.Visible,
            textAlign = TextAlign.Center,
            text = stepDescription,
            fontWeight = FontWeight.Bold,
            color = if (isCompete) Black else Color(0xFFA4A4A4),
        )

        if (step != 3) {
            androidx.compose.material.Divider(
                modifier = Modifier.constrainAs(line) {
                    top.linkTo(circle.top)
                    bottom.linkTo(circle.bottom)
                    start.linkTo(circle.end)
                },
                color = innerCircleColor,
                thickness = 1.dp,
            )
        }

    }
}