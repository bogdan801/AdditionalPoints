package com.bogdan801.additionalpoints.data.excel.report

import android.content.Context
import com.bogdan801.additionalpoints.data.util.getLastDateOfMonth
import com.bogdan801.additionalpoints.data.util.getUkrainianMonthName
import com.bogdan801.additionalpoints.domain.repository.Repository
import org.apache.poi.ss.usermodel.*
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.ss.util.PropertyTemplate
import org.apache.poi.xssf.usermodel.XSSFWorkbook

suspend fun generateReport(months: List<String>, context: Context, repository: Repository): XSSFWorkbook{
    val workbook = XSSFWorkbook()
    months.forEach{ month ->
        createMonthSheet(workbook, month, context, repository)
    }

    return workbook
}

suspend fun createMonthSheet(workbook: XSSFWorkbook, month: String, context: Context, repository: Repository){
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

    //style
    val styleMain = workbook.createCellStyle()
    styleMain.wrapText = true
    styleMain.verticalAlignment = VerticalAlignment.CENTER
    styleMain.alignment = HorizontalAlignment.CENTER
    styleMain.setFont(font)

    //HEADER
    //row
    val headerRow = sheet.createRow(0)
    headerRow.height = 800
    headerRow.rowStyle = styleMain

    //cell
    val headerCell = headerRow.createCell(0)
    headerCell.setCellValue("Перелік основних досягнень студентів у науковій, науково-технічній діяльності, громадському житті, творчій та спортивній діяльності, що враховуються для розрахунку додаткових балів при формуванні рейтингу успішності (01.$month - ${getLastDateOfMonth(month)}.$month)")
    sheet.addMergedRegion(CellRangeAddress(0,0,0,7))
    pt.drawBorders(CellRangeAddress(0,0,0,7), BorderStyle.THIN, BorderExtent.OUTSIDE)
    headerCell.cellStyle = styleMain

    //TITLES
    //row
    val titlesRow = sheet.createRow(1)
    titlesRow.height = 600
    titlesRow.rowStyle = styleMain

    //cells
    pt.drawBorders(CellRangeAddress(1,1,0,7), BorderStyle.THIN, BorderExtent.ALL)
    val titleNumCell = titlesRow.createCell(0)
    titleNumCell.setCellValue("№ з/п")
    titleNumCell.cellStyle = styleMain

    val titleNameCell = titlesRow.createCell(1)
    titleNameCell.setCellValue("ПІП")
    titleNameCell.cellStyle = styleMain

    val titleEventCell = titlesRow.createCell(2)
    titleEventCell.setCellValue("Захід")
    titleEventCell.cellStyle = styleMain

    val titleDateCell = titlesRow.createCell(3)
    titleDateCell.setCellValue("Дата проведення")
    titleDateCell.cellStyle = styleMain

    val titleActivityCell = titlesRow.createCell(4)
    titleActivityCell.setCellValue("Посилання на рейтинг")
    titleActivityCell.cellStyle = styleMain

    val titleValueCell = titlesRow.createCell(5)
    titleValueCell.setCellValue("Бал")
    titleValueCell.cellStyle = styleMain

    val titleSumCell = titlesRow.createCell(6)
    titleSumCell.setCellValue("Сума балів")
    titleSumCell.cellStyle = styleMain

    val titleSignatureCell = titlesRow.createCell(7)
    titleSignatureCell.setCellValue("Підпис студента")
    titleSignatureCell.cellStyle = styleMain

    //STUDENT BUDGET TYPE TITLE
    //row

    //cell

    pt.applyBorders(sheet)
}
