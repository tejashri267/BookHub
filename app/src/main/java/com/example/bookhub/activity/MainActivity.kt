package com.example.bookhub.activity

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.bookhub.*
import com.example.bookhub.fragment.AboutappFragment
import com.example.bookhub.fragment.DashboardFragment
import com.example.bookhub.fragment.FavouritesFragment
import com.example.bookhub.fragment.ProfileFragment
import com.google.android.material.navigation.NavigationView

lateinit var drawerLayout: DrawerLayout
lateinit var coordinatorLayout: CoordinatorLayout
lateinit var toolbar: Toolbar
lateinit var frameLayout: FrameLayout
lateinit var navigationView: NavigationView
var previousMenuItem:MenuItem? = null
private lateinit var preferenceManager: PreferenceManager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        preferenceManager = PreferenceManager(this@MainActivity)
        setContentView(R.layout.activity_main2)
        drawerLayout = findViewById(R.id.drawerlayout)
        coordinatorLayout = findViewById(R.id.coordinatorlayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.framelayout)
        navigationView = findViewById(R.id.navigationview)
        setUpToolbar()
        openDashboard()

//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.framelayout, DashboardFragment())
//                .commit();
//
//            navigationView.setCheckedItem(R.id.dashboard)
//
//        }


        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it




            when (it.itemId){
                R.id.dashboard -> {
                    openDashboard()
                    drawerLayout.closeDrawers()
                }

                R.id.favourites -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.framelayout, FavouritesFragment())
                        .commit();
                    supportActionBar?.title="Favourites"
                    drawerLayout.closeDrawers()

                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.framelayout, ProfileFragment())
                        .commit();
                    supportActionBar?.title="Profile"
                    drawerLayout.closeDrawers()
                }

                R.id.aboutapp -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.framelayout, AboutappFragment())
                        .commit();
                    supportActionBar?.title="About App"
                    drawerLayout.closeDrawers()
                }
                R.id.logout ->{
                    val builder = AlertDialog.Builder(this)
                    builder.setMessage("Do you want to exit ?")
                    builder.setTitle("Alert !")
                    builder.setCancelable(false)
                    builder.setPositiveButton("Yes") {
                       dialog, which -> preferenceManager.clearPreferences()
                        finish()
                            }
                    builder.setNegativeButton("No") {
                            dialog, which -> dialog.cancel()
                    }
                    val alertDialog = builder.create()
                    alertDialog.show()
                }
            }
            return@setNavigationItemSelectedListener true
        }

    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }


    fun openDashboard(){
        val fragment= DashboardFragment()
        val transaction=supportFragmentManager.beginTransaction()
        transaction.replace(R.id.framelayout,DashboardFragment())
        transaction.commit()
        supportActionBar?.title="Dashboard"
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.framelayout)

        when(frag){
            !is DashboardFragment ->openDashboard()

            else-> super.onBackPressed()
        }
    }
    }