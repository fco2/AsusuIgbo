package com.asusuigbo.frank.asusuigbo.connection.helpers
import androidx.recyclerview.widget.GridLayoutManager
import android.view.View
import com.asusuigbo.frank.asusuigbo.adapters.LessonInfoAdapter
import com.asusuigbo.frank.asusuigbo.fragments.LessonsFragment
import com.asusuigbo.frank.asusuigbo.models.LessonInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class TableOfContentHelper {
    companion object {
        fun populateList(lessonsFragment: LessonsFragment){
            val database: FirebaseDatabase = FirebaseDatabase.getInstance()
            val dbReference: DatabaseReference = database.getReference("TableOfContent")
            val con = lessonsFragment.contextData!!.applicationContext

            dbReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (d in dataSnapshot.children){
                        val imageName: String = d.child("ImageDrawableIndex").value.toString()
                        val key = d.child("LessonKey").value.toString()
                        val resId: Int = con.resources.getIdentifier(imageName,"mipmap", con.packageName)
                        val item = LessonInfo(resId, key)
                        lessonsFragment.dataList.add(item)
                    }
                    val auth = FirebaseAuth.getInstance()
                    val lessonsActivatedDbRef = database.reference.child("Users")
                        .child(auth.currentUser!!.uid)

                    lessonsActivatedDbRef.addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(p0: DataSnapshot) {
                            val viewableLessons = p0.child("LessonsCompleted").value
                                .toString().toInt()

                            val glm = GridLayoutManager(lessonsFragment.contextData, 2)
                            //this block below makes the recyclerView staggered
                            glm.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                                override fun getSpanSize(position: Int): Int { //Index begins at 1
                                    return if(position % 3 == 0 || position % 3 == 1)
                                        1
                                    else
                                        glm.spanCount
                                }
                            }
                            lessonsFragment.recyclerView.layoutManager = glm
                            lessonsFragment.recyclerView.hasFixedSize()
                            lessonsFragment.recyclerView.adapter = LessonInfoAdapter(lessonsFragment.dataList,
                                    lessonsFragment.contextData!!, viewableLessons)

                            lessonsFragment.progressBar.visibility = View.GONE
                        }
                        override fun onCancelled(p0: DatabaseError) {
                        }
                    })
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    //do nothing
                }
            })
        }
    }
}
