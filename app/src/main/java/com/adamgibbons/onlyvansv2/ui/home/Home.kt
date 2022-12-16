package com.adamgibbons.onlyvansv2.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.adamgibbons.onlyvansv2.R
import com.adamgibbons.onlyvansv2.databinding.HomeBinding
import com.adamgibbons.onlyvansv2.databinding.NavHeaderMainBinding
import com.adamgibbons.onlyvansv2.ui.login.LoggedInViewModel
import com.adamgibbons.onlyvansv2.ui.login.Login

class Home : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: HomeBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navHeaderBinding : NavHeaderMainBinding
    private lateinit var headerView : View
    private lateinit var loggedInViewModel: LoggedInViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = HomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        drawerLayout = binding.drawerLayout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.nav_gallery
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        val navView = binding.navView
        navView.setupWithNavController(navController)
        headerView = binding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderMainBinding.bind(headerView)
    }

    public override fun onStart() {
        super.onStart()
        loggedInViewModel = ViewModelProvider(this)[LoggedInViewModel::class.java]
        loggedInViewModel.liveFirebaseUser.observe(this, Observer { firebaseUser ->
            if (firebaseUser != null) {
//                updateNavHeader(firebaseUser)
            }
        })

        loggedInViewModel.loggedOut.observe(this, Observer { loggedOut ->
            if (loggedOut) {
                startActivity(Intent(this, Login::class.java))
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun signOut(item: MenuItem) {
        loggedInViewModel.logOut()
        val intent = Intent(this, Login::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

}