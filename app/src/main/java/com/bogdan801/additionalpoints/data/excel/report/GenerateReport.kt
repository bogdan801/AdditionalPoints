package com.bogdan801.additionalpoints.data.excel.report

import com.bogdan801.additionalpoints.data.excel.util.createCell
import com.bogdan801.additionalpoints.data.excel.util.createRow
import com.bogdan801.additionalpoints.data.mapper.toStudent
import com.bogdan801.additionalpoints.data.mapper.toStudentActivity
import com.bogdan801.additionalpoints.data.util.getLastDateOfMonth
import com.bogdan801.additionalpoints.data.util.getUkrainianMonthName
import com.bogdan801.additionalpoints.domain.model.CurrentStudyYearBorders
import com.bogdan801.additionalpoints.domain.model.Student
import com.bogdan801.additionalpoints.domain.model.StudentActivity
import com.bogdan801.additionalpoints.domain.repository.Repository
import kotlinx.coroutines.flow.first
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.ss.util.PropertyTemplate
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFSheet
import org.apache.poi.xssf.usermodel.XSSFWorkbook

suspend fun generateReport(months: List<String>, groupID: Int, repository: Repository, additionalInfo: AdditionalReportInfo?, withGeneralSheet: Boolean = true): XSSFWorkbook{
    val workbook = XSSFWorkbook()
    months.forEach{ month ->
        createMonthSheet(workbook, month, groupID, repository, additionalInfo)
    }

    if(additionalInfo!=null && withGeneralSheet){
        createGeneralSheet(workbook, months, groupID, repository, additionalInfo)
    }

    return workbook
}

fun writeStudentToMonthSheet(rowIndex: Int, month: String, studentIndex: Int, student: Student, sheet: XSSFSheet, pt: PropertyTemplate, styles: List<XSSFCellStyle>, borders: CurrentStudyYearBorders?): Int{
    val monthsMap = student.getActivitiesByMonths(borders)
    val studentActivities: List<StudentActivity> = monthsMap[month] ?: listOf()

    if(studentActivities.isEmpty()) {
        val row = createRow(sheet, rowIndex , styles[0])
        createCell(row, 0, valueDouble = studentIndex.toDouble(), cellStyle = styles[0])
        createCell(row, 1, student.fullName, cellStyle = styles[1])
        pt.drawBorders(CellRangeAddress(rowIndex,rowIndex,0,7), BorderStyle.THIN, BorderExtent.ALL)
        return 1
    }

    studentActivities.forEachIndexed { index, activity ->
        val row = createRow(sheet, rowIndex + index, styles[0])
        if(index == 0){
            createCell(row, 0, valueDouble = studentIndex.toDouble(), cellStyle = styles[0])
            createCell(row, 1, student.fullName, cellStyle = styles[1])
            createCell(row, 6, String.format("%.2f", studentActivities.sumOf { it.value.toDouble() }), cellStyle = styles[0])
        }
        createCell(row, 2, activity.description, cellStyle = styles[1])
        createCell(row, 3, activity.date, cellStyle = styles[0])
        createCell(row, 4, activity.activityInformation.description + " - ???????? ${activity.activityInformation.block} ?????????? ${activity.activityInformation.paragraph} ????????????????", cellStyle = styles[1])
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

suspend fun createMonthSheet(workbook: XSSFWorkbook, month: String, groupID: Int, repository: Repository, additionalInfo: AdditionalReportInfo?){
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
    createCell(createRow(sheet, 0, styleMain, 800), 0, "?????????????? ???????????????? ?????????????????? ?????????????????? ?? ????????????????, ??????????????-?????????????????? ????????????????????, ???????????????????????? ??????????, ?????????????? ???? ???????????????????? ????????????????????, ???? ???????????????????????? ?????? ???????????????????? ???????????????????? ?????????? ?????? ???????????????????? ???????????????? ???????????????????? (01.$month - ${getLastDateOfMonth(month)}.$month)", cellStyle = styleMain)
    sheet.addMergedRegion(CellRangeAddress(0,0,0,7))
    pt.drawBorders(CellRangeAddress(0,0,0,7), BorderStyle.THIN, BorderExtent.OUTSIDE)

    //TITLES
    val titlesRow = createRow(sheet, 1, styleMain, 600)
    createCell(titlesRow, 0, "??? ??/??",                cellStyle = styleMain)
    createCell(titlesRow, 1, "??????",                  cellStyle = styleMain)
    createCell(titlesRow, 2, "??????????",                cellStyle = styleMain)
    createCell(titlesRow, 3, "???????? ????????????????????",      cellStyle = styleMain)
    createCell(titlesRow, 4, "?????????????????? ???? ??????????????", cellStyle = styleMain)
    createCell(titlesRow, 5, "??????",                  cellStyle = styleMain)
    createCell(titlesRow, 6, "???????? ??????????",           cellStyle = styleMain)
    createCell(titlesRow, 7, "???????????? ????????????????",      cellStyle = styleMain)
    pt.drawBorders(CellRangeAddress(1,1,0,7), BorderStyle.THIN, BorderExtent.ALL)

    //STUDENTS
    val students = repository.getStudentWithActivitiesJunction().first().map { it.toStudent(repository) }.filter { student -> student.groupID == groupID}

    //STUDENT BUDGET TYPE TITLE
    val budgetStudents = students.filter { !it.isContract }

    if(budgetStudents.isNotEmpty()){
        createCell(createRow(sheet, 2, styleMain), 0, "????????????????, ??????  ???????????????????? ???? ?????????????????? ??????????????????????", cellStyle = styleMain)
        sheet.addMergedRegion(CellRangeAddress(2,2,0,7))
        pt.drawBorders(CellRangeAddress(2,2,0,7), BorderStyle.THIN, BorderExtent.OUTSIDE)
    }

    //BUDGET STUDENTS LIST
    var rowIndex = 3
    budgetStudents.forEachIndexed { index, student ->
        rowIndex += writeStudentToMonthSheet(rowIndex, month, index + 1, student, sheet, pt, listOf(styleMain, styleSecond), additionalInfo?.borders)
    }

    //STUDENT CONTRACT TYPE TITLE
    val contractStudents = students.filter { it.isContract }
    if(contractStudents.isNotEmpty()){
        createCell(createRow(sheet, rowIndex, styleMain), 0, "????????????????, ??????  ???????????????????? ???? ???????? ????????????????", cellStyle = styleMain)
        sheet.addMergedRegion(CellRangeAddress(rowIndex,rowIndex,0,7))
        pt.drawBorders(CellRangeAddress(rowIndex,rowIndex,0,7), BorderStyle.THIN, BorderExtent.OUTSIDE)
        rowIndex++
    }

    //CONTRACT STUDENTS LIST
    contractStudents.forEachIndexed { index, student ->
        rowIndex += writeStudentToMonthSheet(rowIndex, month, index + 1, student, sheet, pt, listOf(styleMain, styleSecond), additionalInfo?.borders)
    }

    //apply borders
    pt.applyBorders(sheet)
}

suspend fun createGeneralSheet(
    workbook: XSSFWorkbook,
    months: List<String>,
    groupID: Int,
    repository: Repository,
    additionalInfo: AdditionalReportInfo
) {
    val groupName = repository.getGroupNameByID(groupID)

    val sheet = workbook.createSheet("??????????????????")
    val pt = PropertyTemplate()

    //column width
    sheet.setColumnWidth(0,  20 * 256)

    //font
    val fontTitle = workbook.createFont()
    fontTitle.fontName = "Times New Roman"
    fontTitle.fontHeightInPoints = 12

    val fontBold = workbook.createFont()
    fontBold.fontName = "Times New Roman"
    fontBold.bold = true
    fontBold.fontHeightInPoints = 10

    val fontRegular = workbook.createFont()
    fontRegular.fontName = "Times New Roman"
    fontRegular.fontHeightInPoints = 10

    //styles
    val styleTitle = workbook.createCellStyle()
    styleTitle.wrapText = true
    styleTitle.alignment = HorizontalAlignment.CENTER
    styleTitle.verticalAlignment = VerticalAlignment.CENTER
    styleTitle.setFont(fontTitle)

    val styleBold = workbook.createCellStyle()
    styleBold.wrapText = true
    styleBold.alignment = HorizontalAlignment.CENTER
    styleBold.verticalAlignment = VerticalAlignment.CENTER
    styleBold.setFont(fontBold)

    val styleRegular = workbook.createCellStyle()
    styleRegular.wrapText = true
    styleRegular.alignment = HorizontalAlignment.CENTER
    styleRegular.verticalAlignment = VerticalAlignment.CENTER
    styleRegular.setFont(fontRegular)

    val styleRegularLeftAlign = workbook.createCellStyle()
    styleRegularLeftAlign.wrapText = true
    styleRegularLeftAlign.verticalAlignment = VerticalAlignment.CENTER
    styleRegularLeftAlign.setFont(fontRegular)

    //HEADER
    val degree = when(additionalInfo.degree){
        Degree.Bachelor -> "????????????????????"
        Degree.Master -> "??????????????????"
    }
    createCell(createRow(sheet, 0, styleTitle, 600), 0, "???????????? $degree ${additionalInfo.course}-???? ???????? ????????????????(${additionalInfo.faculty}, $groupName)", cellStyle = styleTitle)

    //TITLES
    val titlesRow = createRow(sheet, 1, styleBold)

    var colIndex = 0
    createCell(titlesRow, colIndex, "?????? ????????????????", cellStyle = styleBold)
    colIndex++
    months.forEach { month ->
        createCell(titlesRow, colIndex, "?????????????? ???? ${getUkrainianMonthName(month)}", cellStyle = styleBold)
        colIndex++
    }
    createCell(titlesRow, colIndex, "?????????????????? ??????????????", cellStyle = styleBold)
    colIndex++
    createCell(titlesRow, colIndex, "???????????????????? ??????????", cellStyle = styleBold)

    sheet.addMergedRegion(CellRangeAddress(0,0,0, colIndex))
    pt.drawBorders(CellRangeAddress(0,0,0, colIndex), BorderStyle.THIN, BorderExtent.OUTSIDE)
    pt.drawBorders(CellRangeAddress(1,1,0, colIndex), BorderStyle.THIN, BorderExtent.ALL)
    for(i in 1..colIndex){
        sheet.setColumnWidth(i,  10 * 256)
    }

    //STUDENTS
    val students = repository.getStudentWithActivitiesJunction().first().map { it.toStudent(repository) }.filter { student -> student.groupID == groupID}

    //STUDENT BUDGET TYPE TITLE
    var rowNumber = 2
    val budgetStudents = students.filter { !it.isContract }
    if(budgetStudents.isNotEmpty()){
        createCell(createRow(sheet, rowNumber, styleRegular), 0, "????????????????, ??????  ???????????????????? ???? ?????????????????? ??????????????????????", cellStyle = styleRegular)
        sheet.addMergedRegion(CellRangeAddress(rowNumber,rowNumber,0, colIndex))
        pt.drawBorders(CellRangeAddress(rowNumber,rowNumber,0,colIndex), BorderStyle.THIN, BorderExtent.OUTSIDE)
        rowNumber++
    }



    //BUDGET STUDENTS LIST
    budgetStudents.forEach { student ->
        val studentRow = createRow(sheet, rowNumber, styleRegularLeftAlign)
        createCell(studentRow, 0, student.fullName, cellStyle = styleRegularLeftAlign)

        var sum = 0.0
        val studentMonthsMap = student.getActivitiesByMonths(additionalInfo.borders)
        months.forEachIndexed { index, month ->
            val studentActivities = studentMonthsMap[month] ?: listOf()
            val value = studentActivities.sumOf { it.value.toDouble() }
            createCell(studentRow, 1 + index, String.format("%.2f", value), cellStyle = styleRegular)
            sum += value
        }

        createCell(studentRow, colIndex-1, String.format("%.2f", sum), cellStyle = styleRegular)
        createCell(studentRow, colIndex, String.format("%.2f", if(sum>10) 10f else sum), cellStyle = styleRegular)

        pt.drawBorders(CellRangeAddress(rowNumber,rowNumber,0,colIndex), BorderStyle.THIN, BorderExtent.ALL)
        rowNumber++
    }

    //STUDENT CONTRACT TYPE TITLE
    val contractStudents = students.filter { it.isContract }
    if(contractStudents.isNotEmpty()){
        createCell(createRow(sheet, rowNumber, styleRegular), 0, "????????????????, ??????  ???????????????????? ???? ???????? ????????????????", cellStyle = styleRegular)
        sheet.addMergedRegion(CellRangeAddress(rowNumber,rowNumber,0,colIndex))
        pt.drawBorders(CellRangeAddress(rowNumber, rowNumber,0,colIndex), BorderStyle.THIN, BorderExtent.OUTSIDE)
        rowNumber++
    }

    //CONTRACT STUDENTS LIST
    contractStudents.forEach { student ->
        val studentRow = createRow(sheet, rowNumber, styleRegularLeftAlign)
        createCell(studentRow, 0, student.fullName, cellStyle = styleRegularLeftAlign)

        var sum = 0.0
        val studentMonthsMap = student.getActivitiesByMonths(additionalInfo.borders)
        months.forEachIndexed { index, month ->
            val studentActivities = studentMonthsMap[month] ?: listOf()
            val value = studentActivities.sumOf { it.value.toDouble() }
            createCell(studentRow, 1 + index, String.format("%.2f", value), cellStyle = styleRegular)
            sum += value
        }

        createCell(studentRow, colIndex-1, String.format("%.2f", sum), cellStyle = styleRegular)
        createCell(studentRow, colIndex, String.format("%.2f", if(sum>10) 10f else sum), cellStyle = styleRegular)

        pt.drawBorders(CellRangeAddress(rowNumber,rowNumber,0,colIndex), BorderStyle.THIN, BorderExtent.ALL)
        rowNumber++
    }

    //SIGNATURES
    rowNumber++
    val signatureTitleRow = createRow(sheet, rowNumber, styleRegularLeftAlign)
    createCell(signatureTitleRow, 0, "???????????????? ?????????? $groupName", cellStyle = styleRegularLeftAlign)
    createCell(signatureTitleRow, 3, "?????????????? ?????????? $groupName", cellStyle = styleRegularLeftAlign)
    rowNumber++
    val signatureNameRow = createRow(sheet, rowNumber, styleRegularLeftAlign)
    createCell(signatureNameRow, 0, additionalInfo.headOfGroup, cellStyle = styleRegularLeftAlign)
    createCell(signatureNameRow, 1, "_______", cellStyle = styleRegularLeftAlign)
    createCell(signatureNameRow, 3, additionalInfo.curator, cellStyle = styleRegularLeftAlign)
    createCell(signatureNameRow, 5, "_______", cellStyle = styleRegularLeftAlign)
    rowNumber++
    val signatureLastRow = createRow(sheet, rowNumber, styleRegular)
    createCell(signatureLastRow, 1, "(????????????)", cellStyle = styleRegular)
    createCell(signatureLastRow, 5, "(????????????)", cellStyle = styleRegular)


    //apply borders
    pt.applyBorders(sheet)
}