package com.bogdan801.additionalpoints.presentation.custom.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bogdan801.additionalpoints.R

@Composable
fun MenuDrawer(
    drawerHeaderHeight: Dp = 200.dp,
    headerColors: List<Color> = listOf(
        MaterialTheme.colors.secondaryVariant,
        MaterialTheme.colors.primary
    ),
    headerIconPainter: Painter? = null,
    headerIconTint: Color = MaterialTheme.colors.onPrimary,
    headerIconSize: Dp = 86.dp,
    headerIconOffset: Dp = (-12).dp,
    headerTitle: String = "",
    headerTitleColor: Color = MaterialTheme.colors.onPrimary,
    headerTitleTextStyle: TextStyle = MaterialTheme.typography.h3,
    menuItems: @Composable (ColumnScope.() -> Unit)? = null
) {
    Column(modifier = Modifier
        .fillMaxSize()
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(drawerHeaderHeight)
            .background(
                brush =
                Brush.linearGradient(
                    colors = headerColors,
                    start = Offset(0f, Float.POSITIVE_INFINITY),
                    end = Offset(Float.POSITIVE_INFINITY, 0f)
                )
            )
        ) {
            Column(modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
            ){
                if (headerIconPainter!=null){
                    Icon(
                        modifier = Modifier
                            .size(headerIconSize)
                            .offset(x = headerIconOffset),
                        painter = headerIconPainter,
                        contentDescription = null,
                        tint = headerIconTint
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Text(text = headerTitle, color = headerTitleColor, style = headerTitleTextStyle)
            }
        }
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            menuItems?.invoke(this)
        }
        Box(modifier = Modifier.fillMaxSize()){
            Text(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).align(Alignment.BottomCenter),
                text = "Розробили: Дерига Б.Г. та Пахалюк К.Д.\nФІТ. КН-19001б. 2022",
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = Color(0xFFC7C7C7)
            )
        }
    }
}