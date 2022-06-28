package com.bogdan801.additionalpoints.data.excel.report

import android.content.Context
import com.bogdan801.additionalpoints.data.excel.util.createCell
import com.bogdan801.additionalpoints.data.excel.util.createRow
import com.bogdan801.additionalpoints.data.mapper.toStudent
import com.bogdan801.additionalpoints.data.mapper.toStudentActivity
import com.bogdan801.additionalpoints.data.util.getLastDateOfMonth
import com.bogdan801.additionalpoints.data.util.getUkrainianMonthName
import com.bogdan801.additionalpoints.domain.model.Student
import com.bogdan801.additionalpoints.domain.model.StudentActivity
import com.bogdan801.additionalpoints.domain.repository.Repository
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.ss.util.PropertyTemplate
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook

suspend fun generateReport(months: List<String>, groupID: Int, context: Context, repository: Repository): XSSFWorkbook{
    val workbook = XSSFWorkbook()
    months.forEach{ month ->
        createMonthSheet(workbook, month, groupID, repository)
    }

    return workbook
}

suspend fun createMonthSheet(workbook: XSSFWorkbook, month: String, groupID: Int, repository: Repository){
    val sheet = workbook.createSheet(getUkrainianMonthName(month))
    val pt = PropertyTemplate()

    //column width
    sheet.setColumnWidth(0,  8 * 256)
    sheet.setColumnWidth(1, 30 * 256)
    sheet.setColumnWidth(2, 30 * 256)
    sheet.setColumnWidth(3, 20 * 256)
    sheet.setColumnWidth(4, 30 * 256)
    sheet.setColumnWidth(5,  8 * 256)
    sheet.setColumnWidth(6,  8 * 256)
    sheet.setColumnWidth(7, 15 * 256)

    //font
    val font = workbook.createFont()
    font.fontName = "Times New Roman"
    font.fontHeightInPoints = 12

    //styles
    val styleMain = workbook.createCellStyle()
    styleMain.wrapText = true
    styleMain.verticalAlignment = VerticalAlignment.CENTER
    styleMain.alignment = HorizontalAlignment.CENTER
    styleMain.setFont(font)

    val styleSecond = workbook.createCellStyle()
    styleSecond.wrapText = true
    styleSecond.verticalAlignment = VerticalAlignment.CENTER
    styleSecond.setFont(font)

    //HEADER
    createCell(createRow(sheet, 0, styleMain, 800), 0, "Перелік основних досягнень студентів у науковій, науково-технічній діяльності, громадському житті, творчій та спортивній діяльності, що враховуються для розрахунку додаткових балів при формуванні рейтингу успішності (01.$month - ${getLastDateOfMonth(month)}.$month)", cellStyle = styleMain)
    sheet.addMergedRegion(CellRangeAddress(0,0,0,7))
    pt.drawBorders(CellRangeAddress(0,0,0,7), BorderStyle.THIN, BorderExtent.OUTSIDE)

    //TITLES
    val titlesRow = createRow(sheet, 1, styleMain, 600)
    createCell(titlesRow, 0, "№ з/п",                cellStyle = styleMain)
    createCell(titlesRow, 1, "ПІП",                  cellStyle = styleMain)
    createCell(titlesRow, 2, "Захід",                cellStyle = styleMain)
    createCell(titlesRow, 3, "Дата проведення",      cellStyle = styleMain)
    createCell(titlesRow, 4, "Посилання на рейтинг", cellStyle = styleMain)
    createCell(titlesRow, 5, "Бал",                  cellStyle = styleMain)
    createCell(titlesRow, 6, "Сума балів",           cellStyle = styleMain)
    createCell(titlesRow, 7, "Підпис студента",      cellStyle = styleMain)
    pt.drawBorders(CellRangeAddress(1,1,0,7), BorderStyle.THIN, BorderExtent.ALL)

    //STUDENT BUDGET TYPE TITLE
    val budgetStudents = repository.getStudentsByGroupAndType(groupID, 0).map { it.toStudent() }
    if(budgetStudents.isNotEmpty()){
        createCell(createRow(sheet, 2, styleMain), 0, "Студенти, які  навчаються за державним замовленням", cellStyle = styleMain)
        sheet.addMergedRegion(CellRangeAddress(2,2,0,7))
        pt.drawBorders(CellRangeAddress(2,2,0,7), BorderStyle.THIN, BorderExtent.OUTSIDE)
    }

    //BUDGET STUDENTS LIST
    var rowIndex = 3
    budgetStudents.forEachIndexed { index, student ->
        val studentActivities = repository.getGetStudentActivitiesByMonth(student.studentID, month).map { it.toStudentActivity(repository) }
        rowIndex += writeStudent(rowIndex, index + 1, student, studentActivities, sheet, pt, listOf(styleMain, styleSecond))
    }

    //STUDENT CONTRACT TYPE TITLE
    val contractStudents = repository.getStudentsByGroupAndType(groupID, 1).map { it.toStudent() }
    if(contractStudents.isNotEmpty()){
        createCell(createRow(sheet, rowIndex, styleMain), 0, "Студенти, які  навчаються за умов договору", cellStyle = styleMain)
        sheet.addMergedRegion(CellRangeAddress(rowIndex,rowIndex,0,7))
        pt.drawBorders(CellRangeAddress(rowIndex,rowIndex,0,7), BorderStyle.THIN, BorderExtent.OUTSIDE)
        rowIndex++
    }

    //CONTRACT STUDENTS LIST
    contractStudents.forEachIndexed { index, student ->
        val studentActivities = repository.getGetStudentActivitiesByMonth(student.studentID, month).map { it.toStudentActivity(repository) }
        rowIndex += writeStudent(rowIndex, index + 1, student, studentActivities, sheet, pt, listOf(styleMain, styleSecond))
    }

    //apply borders
    pt.applyBorders(sheet)
}

fun writeStudent(rowIndex: Int, studentIndex: Int, student: Student, studentActivities: List<StudentActivity>, sheet: XSSFSheet, pt: PropertyTemplate, styles: List<XSSFCellStyle>): Int{
    studentActivities.forEachIndexed { index, activity ->
        val row = createRow(sheet, rowIndex + index, styles[0])
        if(index == 0){
            createCell(row, 0, valueDouble = studentIndex.toDouble(), cellStyle = styles[0])
            createCell(row, 1, student.fullName, cellStyle = styles[1])
            createCell(row, 6, String.format("%.2f", studentActivities.sumOf { it.value.toDouble() }), cellStyle = styles[0])
        }
        createCell(row, 2, activity.description, cellStyle = styles[1])
        createCell(row, 3, activity.date, cellStyle = styles[0])
        createCell(row, 4, activity.activityInformation.description + " - Блок ${activity.activityInformation.block} пункт ${activity.activityInformation.paragraph} рейтингу", cellStyle = styles[1])
        createCell(row, 5, activity.value.toString(), cellStyle = styles[0])

        //borders
        pt.drawBorders(CellRangeAddress(rowIndex + index,rowIndex + index,0,7), BorderStyle.THIN, BorderExtent.ALL)
    }

    //merging cells
    if(studentActivities.size > 1){
        sheet.addMergedRegion(CellRangeAddress(rowIndex,rowIndex + studentActivities.lastIndex,0,0))
        sheet.addMergedRegion(CellRangeAddress(rowIndex,rowIndex + studentActivities.lastIndex,1,1))
        sheet.addMergedRegion(CellRangeAddress(rowIndex,rowIndex + studentActivities.lastIndex,6,6))
    }

    return studentActivities.size
}
