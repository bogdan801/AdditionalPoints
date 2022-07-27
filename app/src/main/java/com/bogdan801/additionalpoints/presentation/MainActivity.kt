package com.bogdan801.additionalpoints.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.bogdan801.additionalpoints.data.excel.util.createExcelURILauncher
import com.bogdan801.additionalpoints.presentation.navigation.Navigation
import com.bogdan801.additionalpoints.presentation.theme.AdditionalPointsTheme
import dagger.hilt.android.AndroidEntryPoint
import org.apache.poi.xssf.usermodel.XSSFWorkbook

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val workbookState: MutableState<XSSFWorkbook> = mutableStateOf(XSSFWorkbook())

        val launcher = createExcelURILauncher(this){
            workbookState.value.write(contentResolver.openOutputStream(it))
            Toast.makeText(this, "Файл було записано", Toast.LENGTH_LONG).show()
        }

        setContent {
            AdditionalPointsTheme {
                Surface(modifier = Modifier.background(MaterialTheme.colors.background)) {
                    val navController = rememberNavController()
                    Navigation(navController = navController, launcher = launcher, workbook = workbookState)
                }
            }
        }
    }
}