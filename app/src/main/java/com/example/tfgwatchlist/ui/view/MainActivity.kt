package com.example.tfgwatchlist.ui.view

import android.os.Bundle
import android.transition.Visibility
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.tfgwatchlist.R
import com.example.tfgwatchlist.core.datastore.AuthDataStore
import com.example.tfgwatchlist.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val authDataStore = AuthDataStore(applicationContext)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val navHostFragment = supportFragmentManager.findFragmentById(binding.navHostFragment.id) as NavHostFragment
        val navController = navHostFragment.navController
        lifecycleScope.launch {
            authDataStore.isLoggedIn.collect { isLogged ->
                if(isLogged){
                    navController.navigate(R.id.yourWatchlistFragment)
                } else {
                    navController.navigate(R.id.loginScreenFragment)
                }
            }
        }

        binding.bottomNavigationView.setupWithNavController(navController)
    }

    fun showBottomNav(show:Boolean){
        if(show){
            binding.bottomNavigationView.isVisible = true
        } else {
            binding.bottomNavigationView.isVisible = false
        }

    }
}