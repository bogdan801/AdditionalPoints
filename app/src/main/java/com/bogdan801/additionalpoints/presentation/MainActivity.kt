package com.bogdan801.additionalpoints.presentation

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.bogdan801.additionalpoints.data.database.entities.GroupEntity
import com.bogdan801.additionalpoints.data.database.entities.StudentActivityEntity
import com.bogdan801.additionalpoints.data.database.entities.StudentEntity
import com.bogdan801.additionalpoints.data.excel.util.createExcelURILauncher
import com.bogdan801.additionalpoints.data.mapper.toActivityInformation
import com.bogdan801.additionalpoints.domain.model.ActivityInformation
import com.bogdan801.additionalpoints.domain.repository.Repository
import com.bogdan801.additionalpoints.presentation.custom.composable.ActivityInformationTable
import com.bogdan801.additionalpoints.presentation.theme.AdditionalPointsTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.apache.poi.ss.usermodel.BorderExtent
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.ss.util.PropertyTemplate
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var repository: Repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //lateinit var activityInfoList: List<ActivityInformation>
        runBlocking {
            //activityInfoList = repository.getAllActivities().first().map { it.toActivityInformation() }

            repository.insertGroup(GroupEntity(0, "КН19001б"))
            repository.insertGroup(GroupEntity(0, "КН19002б"))
            repository.insertStudent(StudentEntity(0, 1, "Дерига Богдан Григорович", false))
            repository.insertStudent(StudentEntity(0, 1, "Пахалюк Катерина Дмитрівна", false))
            repository.insertStudent(StudentEntity(0, 1, "Григурко Данило Батькович", true))
            repository.insertStudent(StudentEntity(0, 2, "Дарійчук Юрій Батькович", false))

            repository.insertStudentActivity(StudentActivityEntity(0, 1, 86, "інстаграм вікторина «Новорічна вікторина»", "28.12.2021", 0.6f))
            repository.insertStudentActivity(StudentActivityEntity(0, 2, 86, "інстаграм вікторина «Новорічна вікторина»", "31.12.2021", 0.8f))
        }

        val launcher = createExcelURILauncher(this){
            lateinit var workbook: XSSFWorkbook
            val context = this
            runBlocking{
                workbook = repository.generateReportWorkbook(listOf("12.2021"), context)
            }
            workbook.write(contentResolver.openOutputStream(it))

            Toast.makeText(this, "Workbook has been saved", Toast.LENGTH_LONG).show()
        }

        /*val workBook = XSSFWorkbook()
        val sheet = workBook.createSheet("Лютий")
        val row = sheet.createRow(0)
        val cell = row.createCell(0)
        cell.setCellValue("гигиги, я молодець")
        sheet.addMergedRegion(CellRangeAddress(0,1,0,2))
        val pt = PropertyTemplate()
        pt.drawBorders(CellRangeAddress(0,1,0,2), BorderStyle.THIN, BorderExtent.OUTSIDE)
        pt.applyBorders(sheet)
        cell.cellStyle.verticalAlignment = VerticalAlignment.CENTER
        cell.cellStyle.alignment = HorizontalAlignment.CENTER
        */

        setContent {
            AdditionalPointsTheme {
                /*ActivityInformationTable(
                    data = activityInfoList,
                    headerBackgroundColor = Color(0xFF436129),
                    headerTextColor = Color.White,
                    headerBorderWidth = 0.5.dp,
                    headerBorderColor = Color(0xFF92A760),
                    contentBorderWidth = 0.5.dp,
                    contentBorderColor = Color.Gray
                )*/

                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Button(onClick = {launcher.launch("file.xlsx") }) {
                        Text(text = "Create file")
                    }
                }

            }
        }
    }
}
