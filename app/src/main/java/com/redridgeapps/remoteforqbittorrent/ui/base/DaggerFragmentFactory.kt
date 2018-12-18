package com.redridgeapps.remoteforqbittorrent.ui.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import javax.inject.Inject
import javax.inject.Provider
import javax.inject.Singleton

@Singleton
class DaggerFragmentFactory @Inject constructor(
        private val creators: Map<Class<out BaseFragment>, @JvmSuppressWildcards Provider<BaseFragment>>
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String, args: Bundle?): Fragment {

        val modelClass = Class.forName(className).asSubclass(BaseFragment::class.java)

        val creator = creators[modelClass] ?: creators.asIterable().firstOrNull {
            modelClass.isAssignableFrom(it.key)
        }?.value

        creator ?: throw IllegalArgumentException("Unknown Fragment class $modelClass")

        return creator.get()
    }
}