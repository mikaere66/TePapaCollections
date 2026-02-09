package com.michaelrmossman.collections.util

import com.michaelrmossman.collections.R

object ResourceUtils {

    /* Used for DownloadButton()
       and ButtonWithIcon(). See
       ResultDetailsScreen() or
       QueryResultsList() respectively */

    /* For both res types, there is a
       4th resource in play, but is
       only used for DownloadButton() */

    val downloadDrawableIds = Triple(
        R.drawable.outline_downloading_24,
        R.drawable.outline_download_24,
        R.drawable.outline_download_off_24
    )

    val downloadStringIds = Triple(
        R.string.loading_anim,
        R.string.download_more,
        R.string.download_done
    )
}