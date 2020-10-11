package com.example.tabata.domain

class AuthorName(val authorName: String) {

    var errorMsg: String = ""
    val ERROR_MSG_001: String = "著者名は英数字のみです"

    companion object {
        fun valueToUpdate(value: String): AuthorName {
            return AuthorName(value)
        }
    }

    fun parseToSave(): Update {

        var regex = Regex("^[0-9a-zA-Z ]+$")

        if (regex.containsMatchIn(this.authorName)) {
            return Update.OK
        } else {
            errorMsg = ERROR_MSG_001
            return Update.NG
        }

    }


}