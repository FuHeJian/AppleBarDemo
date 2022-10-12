package com.example.applebardemo

import android.view.View
import androidx.lifecycle.*
import androidx.savedstate.*

class KeyboardViewLifecycleOwner :
    LifecycleOwner, ViewModelStoreOwner, SavedStateRegistryOwner {

    fun onCreate() {
        savedStateRegistryController.performRestore(null)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
    }

    fun onResume() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
    }

    override val savedStateRegistry: SavedStateRegistry
        get() =  savedStateRegistryController.savedStateRegistry

    fun onPause() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    }

    fun onDestroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        store.clear()
    }

    /**
    Compose uses the Window's decor view to locate the
    Lifecycle/ViewModel/SavedStateRegistry owners.
    Therefore, we need to set this class as the "owner" for the decor view.
     */
    fun attachToDecorView(decorView: View?) {
        if (decorView == null) return

        ViewTreeLifecycleOwner.set(decorView, this)
        ViewTreeViewModelStoreOwner.set(decorView, this)
        decorView.setViewTreeSavedStateRegistryOwner(this)
//        ViewTreeSavedStateRegistryOwner.set(decorView, this)
    }

    // LifecycleOwner methods
    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    // ViewModelStore methods
    private val store = ViewModelStore()
    override fun getViewModelStore(): ViewModelStore = store

    // SavedStateRegistry methods
    private val savedStateRegistryController = SavedStateRegistryController.create(this)

}
