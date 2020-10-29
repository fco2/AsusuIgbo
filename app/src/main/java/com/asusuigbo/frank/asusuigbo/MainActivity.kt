package com.asusuigbo.frank.asusuigbo

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.asusuigbo.frank.asusuigbo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

//Hilt Fragments must be attached to an @AndroidEntryPoint Activity.
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.navController
        bottomNavigationView.setupWithNavController(navController)
        bottomNavigationView.itemIconTintList = null
        bottomNavigationView.setOnNavigationItemReselectedListener {/* NO-OP */ }

        navController.addOnDestinationChangedListener{_, destination, _ ->
            //set active language here since it should be authenticated by here and also toolbar visibility
            when(destination.id) {
                R.id.allLessonsFragment, R.id.profileFragment, R.id.myLanguagesFragment,
                R.id.weeklyNewsFragment  -> {
                    viewModel.activeLanguage.observe(this, {lang ->
                        //Timber.d("CHUKA - destination ${destination.id} | viewModel: ${viewModel.activeLanguage.value} | lang: $lang")
                        binding.layoutToolbar.toolbarMain.visibility = View.VISIBLE
                        this.binding.layoutToolbar.currentLanguage.text = lang
                    })
                }
                else -> { binding.layoutToolbar.toolbarMain.visibility = View.GONE}
            }

            //set bottomNavigationView visibility
            when(destination.id){
                R.id.allLessonsFragment, R.id.profileFragment,
                    R.id.weeklyNewsFragment -> {
                    //Make toolbar and bottom nav visible
                    bottomNavigationView.visibility = View.VISIBLE
                }
                else -> {
                    bottomNavigationView.visibility = View.GONE
                }
            }
            //set toolbar main text and set visibility of currentLanguage to the right of toolbar
            when(destination.id){
                R.id.allLessonsFragment ->{ setToolBarTexts(getString(R.string.lessons_text), true) }
                R.id.profileFragment -> { setToolBarTexts(getString(R.string.profile_text), false)}
                R.id.weeklyNewsFragment -> { setToolBarTexts(getString(R.string.weekly_news), true)}
                R.id.myLanguagesFragment -> { setToolBarTexts(getString(R.string.my_languages), false)
                }
            }
        }
    }

    private fun setToolBarTexts(text: String, isLangVisible: Boolean){
        binding.layoutToolbar.toolbarText.text = text
        binding.layoutToolbar.currentLanguage.visibility = if(isLangVisible) View.VISIBLE else View.GONE
    }

}
