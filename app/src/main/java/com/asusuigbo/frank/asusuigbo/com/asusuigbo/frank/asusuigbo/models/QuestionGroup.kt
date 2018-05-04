package com.asusuigbo.frank.asusuigbo.com.asusuigbo.frank.asusuigbo.models

class QuestionGroup(var Question: String, var Options: ArrayList<Option>,
                    var CorrectAnswer: Int, var SelectedAnswer: Int) {


    fun getCorrectAnswer(): Boolean {
        return this.CorrectAnswer == this.SelectedAnswer
    }
}