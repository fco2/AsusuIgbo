package com.asusuigbo.frank.asusuigbo.models
import java.io.Serializable

class UserLesson (var LessonName: String, var LessonImage: String = "", var Unlocked: String = "False") : Serializable {
    constructor(): this("", "", "False"){}

}