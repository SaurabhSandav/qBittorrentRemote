package com.redridgeapps.remoteforqbittorrent.ui.base

import androidx.lifecycle.LiveData

interface DrawerActivityContract {

    val navigationItemSelectionsLiveData: LiveData<String>
}
