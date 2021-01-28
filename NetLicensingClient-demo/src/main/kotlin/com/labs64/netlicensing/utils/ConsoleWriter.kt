package com.labs64.netlicensing.utils

import com.labs64.netlicensing.domain.vo.Page
import java.lang.System.out

class ConsoleWriter {

    fun writeMessage(msg: String) {
        out.println(msg)
        out.println()
    }

    fun writeException(msg: String, ex: Exception) {
        out.println(msg)
        ex.printStackTrace()
        out.println()
    }

    fun writeObject(msg: String, obj: Any?) {
        out.println(msg)
        out.println(obj)
        out.println()
    }

    fun writePage(msg: String, page: Page<*>?) {
        out.println(msg)
        if (page != null && page.hasContent()) {
            for (any in page.content) {
                out.println(any)
            }
        }
        out.println()
    }
}
