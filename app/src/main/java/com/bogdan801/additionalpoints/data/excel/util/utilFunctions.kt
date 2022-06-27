package com.bogdan801.additionalpoints.data.excel.util

import android.net.Uri
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher

fun createExcelURILauncher(activity: ComponentActivity, action: (uri: Uri) -> Unit): ActivityResultLauncher<String> = activity.registerForActivityResult(CreateSpecificTypeDocument("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
    if(it != null){
        action(it)
    }
}
