package com.techbeloved.hymnbook.data

import android.content.res.Resources
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.techbeloved.hymnbook.R
import io.reactivex.Observable
import io.reactivex.Single

class SharedPreferencesRepoImp(private val rxPreferences: RxSharedPreferences,
                               private val resources: Resources) : SharedPreferencesRepo {
    override fun isFirstStart(): Observable<Boolean> {
        val firstStartPref = rxPreferences.getBoolean(resources.getString(R.string.pref_key_first_start), true)
        return firstStartPref.asObservable()
    }

    override fun setFirstStart(firstStart: Boolean) {
        val firstStartPref = rxPreferences.getBoolean(resources.getString(R.string.pref_key_first_start))
        firstStartPref.set(firstStart)
    }

    override fun detailFontSize(): Observable<Float> {
        val fontSizePref = rxPreferences.getFloat(resources.getString(R.string.pref_key_detail_font_size))
        return fontSizePref.asObservable()
    }

    override fun updateDetailFontSize(newSize: Float) {
        val fontSizePref = rxPreferences.getFloat(resources.getString(R.string.pref_key_detail_font_size))
        fontSizePref.set(newSize)
    }

    override fun isNightModeActive(): Observable<Boolean> {
        val nightModePref = rxPreferences.getBoolean(resources.getString(R.string.pref_key_enable_night_mode))
        return nightModePref.asObservable()
    }

    override fun setNightModeActive(value: Boolean) {
        val nightModePref = rxPreferences.getBoolean(resources.getString(R.string.pref_key_enable_night_mode))
        nightModePref.set(value)
    }

    override fun midiFilesReady(): Observable<Boolean> {
        val midiFilesReadyPref = rxPreferences.getBoolean(resources.getString(R.string.pref_key_hymn_midi_files_ready))
        return midiFilesReadyPref.asObservable()
    }

    override fun updateMidiFilesReady(value: Boolean) {
        val midiFilesReadyPref = rxPreferences.getBoolean(resources.getString(R.string.pref_key_hymn_midi_files_ready))
        midiFilesReadyPref.set(value)
    }

    override fun midiArchiveVersion(version: Int) {
        val midiArchiveVersionPref = rxPreferences.getInteger(resources.getString(R.string.pref_key_saved_midi_version))
        midiArchiveVersionPref.set(version)
    }

    override fun midiArchiveVersion(): Single<Int> {
        val midiArchiveVersionPref = rxPreferences.getInteger(resources.getString(R.string.pref_key_saved_midi_version), 0)
        return midiArchiveVersionPref.asObservable().firstOrError()
    }
}

const val DEFAULT_PREFERENCE_NAME = "hymnbook_preferences"