package xget.dev.jet.presentation.main.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun OptionsCard(
    options: List<ProfileOptionsData>,
    onClicked: (Int) -> Unit
) {
    Card(
        modifier = Modifier
            .height(79.dp * options.size)
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            for ((index, i) in options.withIndex()) {
                OptionItem(
                    title = i.title,
                    description = i.description
                ) {
                    onClicked(i.id)
                }
                if (index != options.lastIndex){
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(modifier = Modifier.fillMaxWidth(1f))
                    Spacer(modifier = Modifier.height(12.dp))
                }


            }
        }
    }

}

data class ProfileOptionsData(
    val title: String,
    val description: String,
    val id: Int
)