package com.asusuigbo.frank.asusuigbo

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.asusuigbo.frank.asusuigbo.fragments.profile.ProfileFragment
import com.asusuigbo.frank.asusuigbo.fragments.WeeklyNewsFragment
import com.asusuigbo.frank.asusuigbo.fragments.lessons.LessonsFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpBottomNavigation()
    }

    private fun setUpBottomNavigation(){
        bottomNavigationView = findViewById(R.id.bottom_nav_view_id)

        val menu: Menu = bottomNavigationView.menu
        bottomNavigationView.itemIconTintList = null
        selectNavMenuItem(menu.getItem(0))

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            selectNavMenuItem(item)
            false
        }
    }

    private fun selectNavMenuItem(menuItem: MenuItem){
        menuItem.isChecked = true

        when(menuItem.itemId){
            R.id.home_icon_id -> initializeFragment(LessonsFragment())
            R.id.newest_icon_id -> initializeFragment(WeeklyNewsFragment())
            R.id.profile_icon_id -> initializeFragment(ProfileFragment())
            else -> initializeFragment(LessonsFragment())
        }
    }

    private fun initializeFragment(fragment: Fragment){
        val fm: FragmentManager = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.replace(R.id.frame_layout_id, fragment)
        ft.commit()
    }
}
