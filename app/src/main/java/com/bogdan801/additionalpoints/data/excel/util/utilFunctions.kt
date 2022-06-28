package com.bogdan801.additionalpoints.data.excel.util

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import org.apache.poi.xssf.usermodel.XSSFCell
import org.apache.poi.xssf.usermodel.XSSFCellStyle
import org.apache.poi.xssf.usermodel.XSSFRow
import org.apache.poi.xssf.usermodel.XSSFSheet

fun createExcelURILauncher(activity: ComponentActivity, action: (uri: Uri) -> Unit): ActivityResultLauncher<String> = activity.registerForActivityResult(CreateSpecificTypeDocument("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
    if(it != null){
        action(it)
    }
}

fun createRow(sheet: XSSFSheet, rowNumber: Int, rowStyle: XSSFCellStyle? = null, height: Short? = null): XSSFRow {
    val row = sheet.createRow(rowNumber)
    if(rowStyle!=null) row.rowStyle = rowStyle
    if(height!=null) row.height = height

    return row
}

fun createCell(row: XSSFRow, columnIndex: Int, valueString: String? = null, valueDouble: Double? = null, cellStyle: XSSFCellStyle? = null): XSSFCell {
    val cell = row.createCell(columnIndex)
    if(valueString!=null) cell.setCellValue(valueString)
    if(valueDouble!=null) cell.setCellValue(valueDouble)
    if(cellStyle != null) cell.cellStyle = cellStyle

    return cell
}