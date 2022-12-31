package aum.application.project.formassignment.utils

import android.content.Context
import java.io.IOException
import java.io.InputStream

fun loadJSONFile(ctx:Context,filename:String): String? {
    return    ctx.assets.open(filename).bufferedReader().use { it.readText() }
}