package com.asusuigbo.frank.asusuigbo.models

/**
 * Created by Frank on 3/27/2018.
 */
//TODO: change lessonComplete to canViewLesson
data class LessonInfo (var imageDrawableIndex: Int, var lessonKey: String,
                       var lessonComplete: String = "FALSE")