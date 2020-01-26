package com.asusuigbo.frank.asusuigbo.models

import java.io.Serializable

class QuestionGroup(var Question: QuestionInfo,
                         var Options: MutableList<OptionInfo>,
                         var CorrectAnswer: String,
                         var LessonFormat: String = "SingleSelect") : Serializable{
    constructor(): this(QuestionInfo("",""),
                        mutableListOf<OptionInfo>(),
            "",
            "SingleSelect")
}