package com.techbeloved.hymnbook

import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.techbeloved.hymnbook.databinding.ActivityHymnbookBinding
import com.techbeloved.hymnbook.di.Injection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class HymnbookActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHymnbookBinding
    private lateinit var navController: NavController

    private val viewModel: HymnbookViewModel by lazy {
        val factory = HymnbookViewModel.Factory(Injection.provideHymnbookUseCases)
        ViewModelProviders.of(this, factory)[HymnbookViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hymnbook)
        setupNightMode()

        navController = findNavController(R.id.mainNavHostFragment)

        binding.bottomNavigationMain.setupWithNavController(navController)
        binding.bottomNavigationMain.setOnNavigationItemSelectedListener { item ->
            if (navController.currentDestination?.id != item.itemId) {
                item.onNavDestinationSelected(navController)
                true
            } else false
        }

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            // Hide the bottom navigation in detail view
            when (destination.id) {
                R.id.detailPagerFragment,
                R.id.sheetMusicPagerFragment -> if (binding.bottomNavigationMain.isVisible) {
                    binding.bottomNavigationMain.visibility = View.INVISIBLE
                }
                else -> if (!binding.bottomNavigationMain.isVisible) binding.bottomNavigationMain.visibility = View.VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return (navController.currentDestination?.id != item.itemId && item.onNavDestinationSelected(navController))
                || super.onOptionsItemSelected(item)
    }

    private val disposables: CompositeDisposable = CompositeDisposable()
    private fun setupNightMode() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val rxPreferences = RxSharedPreferences.create(sharedPreferences)

        val nightModePreference: Preference<Boolean> = rxPreferences.getBoolean(getString(R.string.pref_key_enable_night_mode), false)
        val disposable = nightModePreference.asObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { enable ->

                    // If already in night mode, do nothing, and otherwise
                    when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
                        Configuration.UI_MODE_NIGHT_NO -> {
                            if (enable) delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                        }
                        Configuration.UI_MODE_NIGHT_YES -> {
                            if (!enable) delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        }
                        Configuration.UI_MODE_NIGHT_UNDEFINED -> {
                            if (!enable) delegate.setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                        }
                    }
                }
        disposables.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!disposables.isDisposed) disposables.dispose()
        viewModel.updateAppFirstStart(false)
        //unregisterReceiver(onDownloadComplete)
    }
}
