package com.asusuigbo.frank.asusuigbo.helpers

import com.asusuigbo.frank.asusuigbo.models.UserLesson
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LessonsHelper {
    companion object{
        fun createLessons() : ArrayList<UserLesson>{
            val userLessonList: ArrayList<UserLesson> = ArrayList()
            userLessonList.add(UserLesson("Lesson 1", "lesson_1", "True"))
            userLessonList.add(UserLesson("Lesson 2", "lesson_2"))
            userLessonList.add(UserLesson("Lesson 3", "lesson_3"))
            userLessonList.add(UserLesson("Lesson 4", "lesson_4"))
            userLessonList.add(UserLesson("Lesson 5", "lesson_5"))
            userLessonList.add(UserLesson("Lesson 6", "lesson_6"))
            userLessonList.add(UserLesson("Lesson 7", "lesson_7"))
            userLessonList.add(UserLesson("Lesson 8", "lesson_8"))
            userLessonList.add(UserLesson("Lesson 9", "lesson_9"))
            userLessonList.add(UserLesson("Lesson 10", "lesson_10"))
            userLessonList.add(UserLesson("Lesson 11", "lesson_11"))
            userLessonList.add(UserLesson("Lesson 12", "lesson_12"))
            userLessonList.add(UserLesson("Lesson 13", "lesson_13"))
            userLessonList.add(UserLesson("Lesson 14", "lesson_14"))
            userLessonList.add(UserLesson("Lesson 15", "lesson_15"))
            userLessonList.add(UserLesson("Lesson 16", "lesson_16"))
            userLessonList.add(UserLesson("Lesson 17", "lesson_17"))
            userLessonList.add(UserLesson("Lesson 18", "lesson_18"))
            userLessonList.add(UserLesson("Lesson 19", "lesson_19"))
            userLessonList.add(UserLesson("Lesson 20", "lesson_20"))
            userLessonList.add(UserLesson("Lesson 21", "lesson_21"))
            userLessonList.add(UserLesson("Lesson 22", "lesson_22"))
            userLessonList.add(UserLesson("Lesson 23", "lesson_23"))
            userLessonList.add(UserLesson("Lesson 24", "lesson_24"))
            userLessonList.add(UserLesson("Lesson 25", "lesson_25"))
            userLessonList.add(UserLesson("Lesson 26", "lesson_26"))
            userLessonList.add(UserLesson("Lesson 27", "lesson_27"))
            userLessonList.add(UserLesson("Lesson 28", "lesson_28"))
            userLessonList.add(UserLesson("Lesson 29", "lesson_29"))
            userLessonList.add(UserLesson("Lesson 30", "lesson_30"))
            userLessonList.add(UserLesson("Lesson 31", "lesson_31"))
            userLessonList.add(UserLesson("Lesson 32", "lesson_32"))
            return userLessonList
        }

        fun saveLessonsToFirebase(auth: FirebaseAuth, userLessonList: ArrayList<UserLesson>, language: String){
            val dbReference: DatabaseReference = FirebaseDatabase.getInstance().reference
            for((index, item) in userLessonList.withIndex()){
                dbReference.child("Users/${auth.currentUser!!.uid}/Language/${language}/Lessons/$index/LessonName").setValue(item.LessonName)
                dbReference.child("Users/${auth.currentUser!!.uid}/Language/${language}/Lessons/$index/LessonImage").setValue(item.LessonImage)
                dbReference.child("Users/${auth.currentUser!!.uid}/Language/${language}/Lessons/$index/Unlocked").setValue(item.Unlocked)
            }
        }
    }
}