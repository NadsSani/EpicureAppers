package com.nads.epicureapp

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.nads.epicureapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity(){
    private lateinit var appBarConfiguration : AppBarConfiguration
    private lateinit var navController:NavController
    private lateinit var  binding: ActivityMainBinding
    private lateinit var toolbar:Toolbar
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var drawerLayout:DrawerLayout
    private lateinit var navView:NavigationView
    private lateinit var listener:NavController.OnDestinationChangedListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        drawerLayout = binding.root.findViewById(R.id.drawer_layout)
        toolbar = binding.root.findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        val bottomNavigationViewMain = binding.root
            .findViewById<BottomNavigationView>(R.id.bottomNavigationViewmain)
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_to) as NavHostFragment
        navController = navHostFragment.navController
        navView = binding.root.findViewById(R.id.nav_view)
        setupWithNavController(
            binding.root.findViewById<NavigationView>(R.id.nav_view),
            navController
        )
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment3,
                R.id.settings_dest,
                R.id.addRecipes,
                R.id.somePage2
            ), drawerLayout
        )
          bottomNavigationViewMain.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.navigation, menu)
        return true
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.logouted){
            val sharedPref = this.getSharedPreferences(
                getString(R.string.epicureapp_file_key), Context.MODE_PRIVATE
            )
            with(sharedPref.edit()){
            clear()
            commit()
            }

            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .requestId()
                .build()
            val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
            mGoogleSignInClient.signOut()
            finish()
        }
        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item)
       }
}