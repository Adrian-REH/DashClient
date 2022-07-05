package com.example.administraciondashboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import java.io.File

object ArchivoController {
    fun selectArchivo(activiti: Activity, code:Int){
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.setType("*/*");
        activiti.startActivityForResult(intent,code)
    }
    fun SendArchivo(context: Context, id: Long,uri:Uri){
        val file= File(context.filesDir, id.toString())
        val byte = context.contentResolver.openInputStream(uri)?.readBytes()!!
        file.writeBytes(byte)

    }
}