package com.asusuigbo.frank.asusuigbo.models

import java.io.Serializable

class QuestionInfo(var Question: String, var Audio: String = ""): Serializable {
    constructor(): this("", "")
}