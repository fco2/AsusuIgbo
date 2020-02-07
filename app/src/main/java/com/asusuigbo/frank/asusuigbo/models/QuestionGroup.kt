package com.asusuigbo.frank.asusuigbo.models

import java.io.Serializable

class QuestionGroup(var QuestionInfo: QuestionInfo,
                         var Options: MutableList<OptionInfo>,
                         var CorrectAnswer: String,
                         var QuestionFormat: String = "SingleSelect") : Serializable{
    constructor(): this(QuestionInfo("",""),
                        mutableListOf<OptionInfo>(),
            "",
            "SingleSelect")
}