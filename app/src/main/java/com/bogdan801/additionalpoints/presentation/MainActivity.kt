package com.bogdan801.additionalpoints.presentation

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.bogdan801.additionalpoints.R
import com.bogdan801.additionalpoints.data.database.entities.GroupEntity
import com.bogdan801.additionalpoints.data.database.entities.StudentActivityEntity
import com.bogdan801.additionalpoints.data.database.entities.StudentEntity
import com.bogdan801.additionalpoints.data.excel.report.AdditionalReportInfo
import com.bogdan801.additionalpoints.data.excel.util.createExcelURILauncher
import com.bogdan801.additionalpoints.domain.repository.Repository
import com.bogdan801.additionalpoints.presentation.custom.composable.CustomTextField
import com.bogdan801.additionalpoints.presentation.navigation.Navigation
import com.bogdan801.additionalpoints.presentation.screens.group.GroupScreen
import com.bogdan801.additionalpoints.presentation.screens.info.InfoScreen
import com.bogdan801.additionalpoints.presentation.theme.AdditionalPointsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*runBlocking {

            repository.insertGroup(GroupEntity(0, "КН19001б"))
            repository.insertGroup(GroupEntity(0, "КН19002б"))
            repository.insertStudent(StudentEntity(0, 1, "Дерига Богдан Григорович", false))
            repository.insertStudent(StudentEntity(0, 1, "Пахалюк Катерина Дмитрівна", false))
            repository.insertStudent(StudentEntity(0, 1, "Григурко Данило Батькович", true))
            repository.insertStudent(StudentEntity(0, 2, "Дарійчук Юрій Батькович", false))
            repository.insertStudentActivity(StudentActivityEntity(0, 1, 86, "інстаграм вікторина «Новорічна вікторина»", "28.12.2021", 0.6f))
            repository.insertStudentActivity(StudentActivityEntity(0, 1, 86, "інстаграм вікторина «Новорічна домівка»", "29.12.2021", 1f))
            repository.insertStudentActivity(StudentActivityEntity(0, 2, 86, "інстаграм вікторина «Новорічна вікторина»", "31.12.2021", 0.8f))
            repository.insertStudentActivity(StudentActivityEntity(0, 3, 86, "інстаграм вікторина «Новорічна вікторина»", "28.12.2021", 0.8f))
            repository.insertStudentActivity(StudentActivityEntity(0, 1, 86, "інстаграм вікторина «Вгадай серіал»", "29.01.2022", 1f))
            repository.insertStudentActivity(StudentActivityEntity(0, 2, 86, "інстаграм вікторина «Вгадай серіал»", "31.01.2022", 1f))
        }*/
        /*val launcher = createExcelURILauncher(this){
            lateinit var workbook: XSSFWorkbook
            runBlocking{
                workbook = repository.generateReportWorkbook(listOf("12.2021", "01.2022"), 1, AdditionalReportInfo("3", "ФІТ", "Григурко Д. Б.", "Лізка"))
            }
            workbook.write(contentResolver.openOutputStream(it))

            Toast.makeText(this, "Workbook has been saved", Toast.LENGTH_LONG).show()
        }*/

        /*runBlocking {
            repository.insertStudentActivity(StudentActivityEntity(0, 13, 86, "Інстаграм вікторина «Новорічна вікторина»", "28.12.2021", 0.6f))
            repository.insertStudentActivity(StudentActivityEntity(0, 13, 86, "Інстаграм вікторина «Новорічна домівка»", "29.12.2021", 1f))
            repository.insertStudentActivity(StudentActivityEntity(0, 14, 86, "Інстаграм вікторина «Новорічна вікторина»", "31.12.2021", 0.8f))
            repository.insertStudentActivity(StudentActivityEntity(0, 15, 86, "Інстаграм вікторина «Новорічна вікторина»", "28.12.2021", 0.8f))
            repository.insertStudentActivity(StudentActivityEntity(0, 13, 86, "Інстаграм вікторина «Вгадай серіал»", "29.01.2022", 1f))
            repository.insertStudentActivity(StudentActivityEntity(0, 14, 86, "Інстаграм вікторина «Вгадай серіал»", "31.01.2022", 1f))
        }*/

        setContent {
            AdditionalPointsTheme {
                Surface(modifier = Modifier.background(MaterialTheme.colors.background)) {
                    val navController = rememberNavController()
                    Navigation(navController = navController)
                }
                /*ActivityInformationTable(
                    data = activityInfoList,
                    headerBackgroundColor = Color(0xFF436129),
                    headerTextColor = Color.White,
                    headerBorderWidth = 0.5.dp,
                    headerBorderColor = Color(0xFF92A760),
                    contentBorderWidth = 0.5.dp,
                    contentBorderColor = Color.Gray
                )

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Button(onClick = {launcher.launch("file.xlsx") }) {
                        Text(text = "Create file")
                    }
                }*/


            }
        }
    }
}
