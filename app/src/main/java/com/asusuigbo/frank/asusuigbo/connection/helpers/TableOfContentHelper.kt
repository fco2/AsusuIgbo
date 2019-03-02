package com.asusuigbo.frank.asusuigbo.connection.helpers
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.asusuigbo.frank.asusuigbo.adapters.LessonInfoAdapter
import com.asusuigbo.frank.asusuigbo.fragments.LessonsFragment
import com.asusuigbo.frank.asusuigbo.models.LessonInfo
import com.google.firebase.database.*

class TableOfContentHelper {
    companion object {
        fun populateList(lessonsFragment: LessonsFragment){
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val dbReference: DatabaseReference = database.getReference("TableOfContent/Items")
            val con = lessonsFragment.contextData!!.applicationContext

            dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (d in dataSnapshot.children){
                        val imageName: String = d.child("ImageDrawableIndex").value.toString()
                        val key = d.child("LessonKey").value.toString()
                        val resId: Int = con.resources.getIdentifier(imageName,"mipmap", con.packageName)
                        val lessonComplete = d.child("LessonComplete").value.toString()
                        val item = LessonInfo(resId, key, lessonComplete)
                        lessonsFragment.dataList.add(item)
                    }
                    //use forLoop to modify list data for lesson complete
                    lessonsFragment.recyclerView.layoutManager = GridLayoutManager(lessonsFragment.contextData, 2)
                    lessonsFragment.recyclerView.hasFixedSize()
                    lessonsFragment.recyclerView.adapter = LessonInfoAdapter(lessonsFragment.dataList,
                            lessonsFragment.contextData!!)

                    lessonsFragment.progressBar.visibility = View.GONE
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //do nothing
                }
            })
        }
    }
}
