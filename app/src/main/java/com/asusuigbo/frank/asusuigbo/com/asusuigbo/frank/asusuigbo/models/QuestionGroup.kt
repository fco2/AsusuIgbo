package com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models

data class QuestionGroup(var Question: String, var Options: ArrayList<String>,
                    var CorrectAnswer: String, var SelectedAnswer: String) {


    constructor(): this("", ArrayList(), "-1", "-1")
}