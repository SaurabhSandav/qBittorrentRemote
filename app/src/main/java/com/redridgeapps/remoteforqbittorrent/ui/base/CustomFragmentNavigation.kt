package com.redridgeapps.remoteforqbittorrent.ui.base

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.NavHostFragment
import arrow.core.Try
import arrow.core.getOrDefault
import com.redridgeapps.remoteforqbittorrent.di.PerFragment
import dagger.android.support.AndroidSupportInjection
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
        return Try { fragmentFactory.instantiate(context.classLoader, className, args) }
                .getOrDefault { super.instantiateFragment(context, fragmentManager, className, args) }
    }
}

@PerFragment
class CustomNavHostFragment : NavHostFragment() {

    @Inject
    lateinit var fragmentFactory: FragmentFactory

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun createFragmentNavigator() = CustomFragmentNavigator(
            fragmentFactory,
            requireContext(),
            childFragmentManager,
            id
    )
}
