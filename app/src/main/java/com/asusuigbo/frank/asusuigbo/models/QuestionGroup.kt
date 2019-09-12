package com.asusuigbo.frank.asusuigbo.models

data class QuestionGroup(var Question: String, var Options: ArrayList<String>,
                         var CorrectAnswer: String, var LessonFormat: String = "SingleSelect")