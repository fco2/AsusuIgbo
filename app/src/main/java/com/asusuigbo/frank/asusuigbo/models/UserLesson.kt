package com.asusuigbo.frank.asusuigbo.models
import java.io.Serializable

class UserLesson (var LessonName: String,
                  var LessonImage: String = "",
                  var Unlocked: String = "False",
                  var Index: Int = 0) : Serializable {
    constructor(): this("", "", "False"){}

}