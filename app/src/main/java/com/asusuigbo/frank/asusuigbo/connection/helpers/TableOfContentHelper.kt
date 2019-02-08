package com.asusuigbo.frank.asusuigbo.connection.helpers
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import com.asusuigbo.frank.asusuigbo.adapters.LessonInfoAdapter
import com.asusuigbo.frank.asusuigbo.models.LessonInfo
import com.google.firebase.database.*

class TableOfContentHelper {
    companion object {
        fun populateList(dataList: ArrayList<LessonInfo>, context: Context?, recyclerView: RecyclerView?, progressBar: ProgressBar?){
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val dbReference: DatabaseReference = database.getReference("TableOfContent/Items")
            val con = context!!.applicationContext

            dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (d in dataSnapshot.children){
                        val imageName: String = d.child("ImageDrawableIndex").value.toString()
                        val key = d.child("LessonKey").value.toString()
                        val resId: Int = con.resources.getIdentifier(imageName,"drawable", con.packageName)
                        val lessonComplete = d.child("LessonComplete").value.toString()
                        val item = LessonInfo(resId, key, lessonComplete)
                        dataList.add(item)
                    }
                    recyclerView!!.layoutManager = GridLayoutManager(context, 2)
                    recyclerView.hasFixedSize()
                    recyclerView.adapter = LessonInfoAdapter(dataList, context)

                    progressBar!!.visibility = View.GONE
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //do nothing
                }
            })
        }
    }
}
