package com.redridgeapps.remoteforqbittorrent.ui.base

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import javax.inject.Inject
import javax.inject.Singleton

@Navigator.Name("fragment")
@Singleton
class CustomFragmentNavigator(
        private val fragmentFactory: FragmentFactory,
        context: Context,
        manager: FragmentManager,
        containerId: Int
) : FragmentNavigator(context, manager, containerId) {

    override fun instantiateFragment(
            context: Context,
            fragmentManager: FragmentManager,
            className: String,
            args: Bundle?
    ): Fragment {
        return fragmentFactory.instantiate(context.classLoader, className, args)
    }
}

@SuppressLint("ValidFragment")
class CustomNavHostFragment @Inject constructor(
        private val fragmentFactory: FragmentFactory
) : NavHostFragment() {

    override fun createFragmentNavigator() = CustomFragmentNavigator(
            fragmentFactory,
            requireContext(),
            childFragmentManager,
            id
    )
}
