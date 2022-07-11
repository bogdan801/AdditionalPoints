package com.bogdan801.additionalpoints.data.excel.report

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

suspend fun generateReport(months: List<String>, groupID: Int, repository: Repository, additionalInfo: AdditionalReportInfo?): XSSFWorkbook{
    val workbook = XSSFWorkbook()
    months.forEach{ month ->
        createMonthSheet(workbook, month, groupID, repository)
    }

    if(additionalInfo!=null){
        createGeneralSheet(workbook, months, groupID, repository, additionalInfo)
    }

    return workbook
}

fun writeStudentToMonthSheet(rowIndex: Int, studentIndex: Int, student: Student, studentActivities: List<StudentActivity>, sheet: XSSFSheet, pt: PropertyTemplate, styles: List<XSSFCellStyle>): Int{
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
        rowIndex += writeStudentToMonthSheet(rowIndex, index + 1, student, studentActivities, sheet, pt, listOf(styleMain, styleSecond))
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
        rowIndex += writeStudentToMonthSheet(rowIndex, index + 1, student, studentActivities, sheet, pt, listOf(styleMain, styleSecond))
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

    val sheet = workbook.createSheet("Загальний")
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
    createCell(createRow(sheet, 0, styleTitle, 600), 0, "Список бакалаврів ${additionalInfo.course}-го року навчання(${additionalInfo.faculty}, $groupName)", cellStyle = styleTitle)

    //TITLES
    val titlesRow = createRow(sheet, 1, styleBold)

    var colIndex = 0
    createCell(titlesRow, colIndex, "ПІП студента", cellStyle = styleBold)
    colIndex++
    months.forEach { month ->
        createCell(titlesRow, colIndex, "Рейтинг за ${getUkrainianMonthName(month)}", cellStyle = styleBold)
        colIndex++
    }
    createCell(titlesRow, colIndex, "Загальний рейтинг", cellStyle = styleBold)
    colIndex++
    createCell(titlesRow, colIndex, "Нараховано балів", cellStyle = styleBold)

    sheet.addMergedRegion(CellRangeAddress(0,0,0, colIndex))
    pt.drawBorders(CellRangeAddress(0,0,0, colIndex), BorderStyle.THIN, BorderExtent.OUTSIDE)
    pt.drawBorders(CellRangeAddress(1,1,0, colIndex), BorderStyle.THIN, BorderExtent.ALL)
    for(i in 1..colIndex){
        sheet.setColumnWidth(i,  10 * 256)
    }

    //STUDENT BUDGET TYPE TITLE
    var rowNumber = 2
    val budgetStudents = repository.getStudentsByGroupAndType(groupID, 0).map { it.toStudent() }
    if(budgetStudents.isNotEmpty()){
        createCell(createRow(sheet, rowNumber, styleRegular), 0, "Студенти, які  навчаються за державним замовленням", cellStyle = styleRegular)
        sheet.addMergedRegion(CellRangeAddress(rowNumber,rowNumber,0, colIndex))
        pt.drawBorders(CellRangeAddress(rowNumber,rowNumber,0,colIndex), BorderStyle.THIN, BorderExtent.OUTSIDE)
        rowNumber++
    }

    //BUDGET STUDENTS LIST
    budgetStudents.forEach { student ->
        val studentRow = createRow(sheet, rowNumber, styleRegularLeftAlign)
        createCell(studentRow, 0, student.fullName, cellStyle = styleRegularLeftAlign)

        var sum = 0.0
        months.forEachIndexed { index, month ->
            val studentActivities = repository.getGetStudentActivitiesByMonth(student.studentID, month).map { it.toStudentActivity(repository) }
            val value = studentActivities.sumOf { it.value.toDouble() }
            createCell(studentRow, 1 + index, String.format("%.2f", value), cellStyle = styleRegular)
            sum += value
        }

        createCell(studentRow, colIndex-1, String.format("%.2f", sum), cellStyle = styleRegular)
        createCell(studentRow, colIndex, String.format("%.2f", if(sum>10) 10 else sum), cellStyle = styleRegular)

        pt.drawBorders(CellRangeAddress(rowNumber,rowNumber,0,colIndex), BorderStyle.THIN, BorderExtent.ALL)
        rowNumber++
    }

    //STUDENT CONTRACT TYPE TITLE
    val contractStudents = repository.getStudentsByGroupAndType(groupID, 1).map { it.toStudent() }
    if(contractStudents.isNotEmpty()){
        createCell(createRow(sheet, rowNumber, styleRegular), 0, "Студенти, які  навчаються за умов договору", cellStyle = styleRegular)
        sheet.addMergedRegion(CellRangeAddress(rowNumber,rowNumber,0,colIndex))
        pt.drawBorders(CellRangeAddress(rowNumber, rowNumber,0,colIndex), BorderStyle.THIN, BorderExtent.OUTSIDE)
        rowNumber++
    }

    //CONTRACT STUDENTS LIST
    contractStudents.forEach { student ->
        val studentRow = createRow(sheet, rowNumber, styleRegularLeftAlign)
        createCell(studentRow, 0, student.fullName, cellStyle = styleRegularLeftAlign)

        var sum = 0.0
        months.forEachIndexed { index, month ->
            val studentActivities = repository.getGetStudentActivitiesByMonth(student.studentID, month).map { it.toStudentActivity(repository) }
            val value = studentActivities.sumOf { it.value.toDouble() }
            createCell(studentRow, 1 + index, String.format("%.2f", value), cellStyle = styleRegular)
            sum += value
        }

        createCell(studentRow, colIndex-1, String.format("%.2f", sum), cellStyle = styleRegular)
        createCell(studentRow, colIndex, String.format("%.2f", sum), cellStyle = styleRegular)

        pt.drawBorders(CellRangeAddress(rowNumber,rowNumber,0,colIndex), BorderStyle.THIN, BorderExtent.ALL)
        rowNumber++
    }

    //SIGNATURES
    rowNumber++
    val signatureTitleRow = createRow(sheet, rowNumber, styleRegularLeftAlign)
    createCell(signatureTitleRow, 0, "Староста групи $groupName", cellStyle = styleRegularLeftAlign)
    createCell(signatureTitleRow, 3, "Куратор групи $groupName", cellStyle = styleRegularLeftAlign)
    rowNumber++
    val signatureNameRow = createRow(sheet, rowNumber, styleRegularLeftAlign)
    createCell(signatureNameRow, 0, additionalInfo.headOfGroup, cellStyle = styleRegularLeftAlign)
    createCell(signatureNameRow, 1, "_______", cellStyle = styleRegularLeftAlign)
    createCell(signatureNameRow, 3, additionalInfo.curator, cellStyle = styleRegularLeftAlign)
    createCell(signatureNameRow, 5, "_______", cellStyle = styleRegularLeftAlign)
    rowNumber++
    val signatureLastRow = createRow(sheet, rowNumber, styleRegular)
    createCell(signatureLastRow, 1, "(підпис)", cellStyle = styleRegular)
    createCell(signatureLastRow, 5, "(підпис)", cellStyle = styleRegular)


    //apply borders
    pt.applyBorders(sheet)
}
