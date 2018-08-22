package com.redridgeapps.remoteforqbittorrent

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.android.material.navigation.NavigationView
import com.redridgeapps.remoteforqbittorrent.api.QBittorrentService.Filter
import com.redridgeapps.remoteforqbittorrent.databinding.ActivityMainBinding
import com.redridgeapps.remoteforqbittorrent.repo.PreferenceRepository
import com.redridgeapps.remoteforqbittorrent.ui.base.DrawerActivityContract
import com.redridgeapps.remoteforqbittorrent.util.asMutable
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class MainActivity : AppCompatActivity(),
        HasSupportFragmentInjector,
        NavigationView.OnNavigationItemSelectedListener,
        DrawerActivityContract {

    @Inject
    lateinit var fragmentDispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var prefRepo: PreferenceRepository

    override val navigationItemSelectionsLiveData: LiveData<String> = MutableLiveData()

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        navController = findNavController(R.id.nav_host_fragment)

        setSupportActionBar(binding.toolbar)
        setupNavigation()
        setupDrawer()
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentDispatchingAndroidInjector

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else super.onBackPressed()
    }

    override fun onSupportNavigateUp() = binding.drawerLayout.navigateUp(navController)

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        binding.drawerLayout.closeDrawer(GravityCompat.START)

        val filter: String = when (item.itemId) {
            R.id.item_filter_all -> Filter.ALL
            R.id.item_filter_downloading -> Filter.DOWNLOADING
            R.id.item_filter_completed -> Filter.COMPLETED
            R.id.item_filter_paused -> Filter.PAUSED
            R.id.item_filter_active -> Filter.ACTIVE
            R.id.item_filter_inactive -> Filter.INACTIVE
            else -> TODO()
        }

        navigationItemSelectionsLiveData.asMutable().postValue(filter)
        return true
    }

    private fun setupNavigation() {
        try {
            navController.graph
        } catch (e: IllegalStateException) {
            val graphResId = if (!prefRepo.initialConfigFinished) R.navigation.config_graph else R.navigation.nav_graph
            navController.setGraph(graphResId)
        }

        navController.addOnNavigatedListener { _, destination ->
            if (destination.id == R.id.torrentListFragment)
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            else
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        }
    }

    private fun setupDrawer() {
        binding.navView.setNavigationItemSelectedListener(this)
        setupActionBarWithNavController(navController, binding.drawerLayout)
    }
}
