package com.bogdan801.additionalpoints.presentation.custom.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CustomTopAppBar(
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colors.primary,
    contentColor: Color = MaterialTheme.colors.onPrimary,
    elevation: Dp = 4.dp,
    iconButton: @Composable (BoxScope.() -> Unit)? = null,
    title: @Composable (BoxScope.() -> Unit)? = null,
    titlePadding: Dp = 50.dp,
    action: @Composable (BoxScope.() -> Unit)? = null
) {
    TopAppBar(
        modifier = modifier,
        backgroundColor = backgroundColor,
        contentColor = contentColor,
        elevation = elevation
    ) {
        Box(modifier = Modifier.fillMaxSize()){
            if(iconButton!=null){
                Box(modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterStart),
                    contentAlignment = Alignment.CenterStart
                ){
                    iconButton(this)
                }
            }

            if(title!=null){
                Box(modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = titlePadding)
                ){
                    title(this)
                }
            }

            if(action!=null){
                Box(modifier = Modifier
                    .fillMaxHeight()
                    .align(Alignment.CenterEnd),
                    contentAlignment = Alignment.CenterEnd
                ){
                    action(this)
                }
            }
        }
    }
}