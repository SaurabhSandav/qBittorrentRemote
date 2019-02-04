package com.redridgeapps.remoteforqbittorrent.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentFactory
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import com.redridgeapps.remoteforqbittorrent.R
import com.redridgeapps.remoteforqbittorrent.api.QBittorrentService.Filter
import com.redridgeapps.remoteforqbittorrent.databinding.ActivityMainBinding
import dagger.android.AndroidInjection
import javax.inject.Inject

class MainActivity : AppCompatActivity(),
        NavigationView.OnNavigationItemSelectedListener {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = fragmentFactory

        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        setSupportActionBar(binding.toolbar)
        setupNavigation()

        binding.navView.setNavigationItemSelectedListener(this@MainActivity)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
    }

    override fun onSupportNavigateUp() = navController.navigateUp(appBarConfiguration)

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)

        val filter: String = when (item.itemId) {
            R.id.item_filter_all -> Filter.ALL
            R.id.item_filter_downloading -> Filter.DOWNLOADING
            R.id.item_filter_completed -> Filter.COMPLETED
            R.id.item_filter_paused -> Filter.PAUSED
            R.id.item_filter_active -> Filter.ACTIVE
            R.id.item_filter_inactive -> Filter.INACTIVE
            else -> return item.onNavDestinationSelected(navController)
        }

        viewModel.setFilter(filter)
        return true
    }

    private fun setupNavigation() {
        navController = findNavController(R.id.nav_host_fragment)

        // Declare Top level destinations
        appBarConfiguration = AppBarConfiguration(
                setOf(R.id.torrentListFragment, R.id.configFragment),
                binding.drawerLayout
        )

        // Disable Up button for Top level destinations
        setupActionBarWithNavController(navController, appBarConfiguration)

        // Lock Drawer outside TorrentList screen
        navController.addOnDestinationChangedListener { _, destination, _ ->

            val lockMode =
                    if (destination.id == R.id.torrentListFragment) DrawerLayout.LOCK_MODE_UNLOCKED
                    else DrawerLayout.LOCK_MODE_LOCKED_CLOSED

            binding.drawerLayout.setDrawerLockMode(lockMode)
        }
    }
}
