package com.asusuigbo.frank.asusuigbo.models

data class QuestionGroup(var Question: String, var Options: ArrayList<OptionInfo>,
                         var CorrectAnswer: String, var LessonFormat: String = "SingleSelect")