package com.techbeloved.hymnbook.sheetmusic

import com.techbeloved.hymnbook.hymndetail.BY_NUMBER
import com.techbeloved.hymnbook.hymndetail.SortBy
import com.techbeloved.hymnbook.hymnlisting.TitleItem
import io.reactivex.Observable

/**
 * Handles some of the sheet music hymn listing and display use cases such as getting hymns from online
 *  source, that is hymns with sheet music. Downloading sheet music
 */
interface HymnUseCases {
    /**
     * List titles of hymns with available sheet music
     */
    fun hymnSheetMusicTitles(@SortBy sortBy: Int = BY_NUMBER): Observable<List<TitleItem>>

    /**
     * Loads indices of hymns that have available sheet music
     */
    fun hymnSheetMusicIndices(@SortBy sortBy: Int): Observable<List<Int>>

    /**
     * Loads the sheet music hymn detail from local repo
     */
    fun hymnSheetMusicDetail(hymnId: Int): Observable<SheetMusicState>

    /**
     * Downloads sheet music for a hymn given the id
     */
    fun downloadSheetMusic(hymnId: Int)
}
