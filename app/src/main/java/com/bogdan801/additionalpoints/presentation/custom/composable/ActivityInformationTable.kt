package com.bogdan801.additionalpoints.presentation.custom.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.bogdan801.additionalpoints.domain.model.ActivityInformation

@Composable
fun ActivityInformationTable(
    data: List<ActivityInformation>,
    columnWeights: ColumnWeights = ColumnWeights(),
    headerBackgroundColor: Color =  MaterialTheme.colors.secondaryVariant,
    contentBackgroundColor: Color = MaterialTheme.colors.onPrimary,
    headerTextColor: Color = MaterialTheme.colors.onBackground,
    contentTextColor: Color = MaterialTheme.colors.onBackground,
    headerBorderColor: Color = MaterialTheme.colors.onBackground,
    contentBorderColor: Color = MaterialTheme.colors.onBackground,
    headerBorderWidth: Dp = 1.dp,
    contentBorderWidth: Dp = 1.dp,
    isHeaderUpper: Boolean = false
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(contentBackgroundColor)
            .padding(0.dp)
    ) {
        //header
        Row(
            Modifier
                .background(headerBackgroundColor)
                .height(IntrinsicSize.Max)){
            var headerText = listOf("Блок", "Пункт", "Опис", "Бал")
            if(isHeaderUpper) headerText = headerText.map { it.uppercase() }
            TableCell(
                text = headerText[0],
                weight = columnWeights.blockWeight,
                borderWidth = headerBorderWidth,
                textColor = headerTextColor,
                borderColor = headerBorderColor
            )
            TableCell(
                text = headerText[1],
                weight = columnWeights.paragraphWeight,
                borderWidth = headerBorderWidth,
                textColor = headerTextColor,
                borderColor = headerBorderColor
            )
            TableCell(
                text = headerText[2],
                weight = columnWeights.descriptionWeight,
                borderWidth = headerBorderWidth,
                textColor = headerTextColor,
                borderColor = headerBorderColor
            )
            TableCell(
                text = headerText[3],
                weight = columnWeights.valueWeight,
                borderWidth = headerBorderWidth,
                textColor = headerTextColor,
                borderColor = headerBorderColor
            )
        }
        //content
        LazyColumn(Modifier.fillMaxSize()) {
            items(data) { activity ->
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)) {
                    TableCell(
                        text = activity.block,
                        weight = columnWeights.blockWeight,
                        borderWidth = contentBorderWidth,
                        textColor = contentTextColor,
                        borderColor = contentBorderColor
                    )
                    TableCell(
                        text = activity.paragraph,
                        weight = columnWeights.paragraphWeight,
                        borderWidth = contentBorderWidth,
                        textColor = contentTextColor,
                        borderColor = contentBorderColor
                    )
                    TableCell(
                        text = activity.description,
                        weight = columnWeights.descriptionWeight,
                        borderWidth = contentBorderWidth,
                        textColor = contentTextColor,
                        borderColor = contentBorderColor
                    )
                    TableCell(
                        text = activity.value.toString(),
                        weight = columnWeights.valueWeight,
                        borderWidth = contentBorderWidth,
                        textColor = contentTextColor,
                        borderColor = contentBorderColor
                    )
                }
            }
        }
    }
}

data class ColumnWeights(
    val blockWeight: Float = .15f,
    val paragraphWeight: Float = .18f,
    val descriptionWeight: Float = .52f,
    val valueWeight: Float = .15f
)

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float,
    borderWidth: Dp = 1.dp,
    borderColor: Color = Color.Black,
    textColor: Color = Color.Black
) {
    Text(
        text = text,
        modifier = Modifier
            .border(borderWidth / 2, borderColor)
            .fillMaxHeight()
            .weight(weight)
            .padding(8.dp),
        color = textColor
    )
}