package com.utils

object EmptySpace {
    fun emptyAndSpace(text: String): Boolean {
        var bool = false
        for (c in text) {
            if (c != ' ') {
                bool = true
                break
            }
        }
        return bool && text != " "
    }
}