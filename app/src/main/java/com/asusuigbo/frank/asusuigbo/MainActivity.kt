package com.asusuigbo.frank.asusuigbo

import android.app.Fragment
import android.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.asusuigbo.frank.asusuigbo.fragments.LessonsFragment
import com.asusuigbo.frank.asusuigbo.fragments.NewestFragment
import com.asusuigbo.frank.asusuigbo.fragments.ProfileFragment

class MainActivity : AppCompatActivity() {

    private var bottomNavigationView: BottomNavigationView? = null
    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar_main)
        toolbar!!.title = resources.getString(R.string.lessons_text)

        setUpBottomNavigation()
    }

    private fun setUpBottomNavigation(){
        bottomNavigationView = findViewById(R.id.bottom_nav_view_id)

        if(bottomNavigationView != null){ // Not Important - it is since it's nullable

            val menu: Menu = bottomNavigationView!!.menu
            selectNavMenuItem(menu.getItem(0))

            bottomNavigationView?.setOnNavigationItemSelectedListener { item ->
                selectNavMenuItem(item)
                false
            }
        }
    }

    private fun selectNavMenuItem(menuItem: MenuItem){
        menuItem.isChecked = true

        when(menuItem.itemId){
            R.id.home_icon_id -> initializeFragment(LessonsFragment(), "Lessons")
            R.id.newest_icon_id -> initializeFragment(NewestFragment(), "New Words")
            R.id.profile_icon_id -> initializeFragment(ProfileFragment(), "Profile")
            else -> initializeFragment(LessonsFragment(), "Lessons")
        }
    }

    private fun initializeFragment(fragment: Fragment, title: String){
        val fm: FragmentManager = fragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.frame_layout_id, fragment)
        ft.commit()
        toolbar!!.title = title
    }
}
