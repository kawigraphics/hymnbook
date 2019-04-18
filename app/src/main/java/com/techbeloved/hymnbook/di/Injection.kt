package com.techbeloved.hymnbook.di

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.techbeloved.hymnbook.HymnbookApp
import com.techbeloved.hymnbook.data.repo.FirebaseRepo
import com.techbeloved.hymnbook.data.repo.HymnsRepository
import com.techbeloved.hymnbook.data.repo.HymnsRepositoryImp
import com.techbeloved.hymnbook.data.repo.OnlineRepo
import java.util.concurrent.Executors

const val WCCRM_HYMNS_COLLECTION = "wccrm"
object Injection {
    fun provideAppContext(): Application {
        return HymnbookApp.instance
    }

    fun provideRepository(): HymnsRepository {
        return HymnsRepositoryImp.getInstance(HymnbookApp.database)
    }

    fun provideOnlineRepo() = lazy<OnlineRepo> {
         FirebaseRepo(Executors.newSingleThreadExecutor(), FirebaseFirestore.getInstance(), WCCRM_HYMNS_COLLECTION)
    }
}