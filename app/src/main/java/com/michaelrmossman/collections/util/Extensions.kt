package com.michaelrmossman.collections.util

import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.core.net.toUri
import com.michaelrmossman.collections.R
import com.michaelrmossman.collections.model.SearchResult
import java.text.NumberFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.ceil

/**
 * Extension functions used throughout the app
 */

fun Context.openUrlInBrowser(url: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = url.toUri()
    }
    startActivity(intent)
}

fun Int.formatWithComma(): String {
    return NumberFormat.getNumberInstance(
        Locale.getDefault()
    ).format(this)
}

fun List<SearchResult>.getResultsHashMap(): HashMap<Int, String> {
    return this.associateBy(
        keySelector    = { result -> result.id },
        valueTransform = { result -> result.title }
    ) as HashMap<Int, String>
}

fun List<SearchResult>.getSearchResult(itemId: Int): SearchResult? {
    return this.find { result ->
        result.id == itemId
    }
}

fun Long.parseMillisToKiwiDate(): String {
    return SimpleDateFormat(
        /* UK uses lowercase AM/PM */
        KIWI_UPDATE_FORMAT, Locale.UK
    ).format(Date(this))
}

fun String.capitalise(): String {
    return when (this.isBlank()) {
        true -> this
        else -> this.replaceFirstChar { string ->
            string.titlecase()
        }
    }
}

@Composable
fun String.fromHtml(): AnnotatedString {
    return AnnotatedString.Companion.fromHtml(
        htmlString = this,
        linkStyles = TextLinkStyles(
            style = SpanStyle(
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.primary
            )
        )
    )
}

fun String.parseKiwiDateToMillis(): Long {
    val dateFormat = SimpleDateFormat(
        KIWI_UPDATE_FORMAT, Locale.getDefault()
    )
    val date = dateFormat.parse(this)
    return date?.time ?: 0L // Note elvis op
}

fun String.parseStringDateToMillis(): Long {
    return try {
        val dateFormat = SimpleDateFormat(
            LAST_UPDATE_FORMAT, Locale.getDefault()
        )
        val date = dateFormat.parse(this)
        date?.time ?: 0L // Note elvis op
    }
    catch (exception: ParseException) {
        println(exception.message)
        0L
    }
}

fun String.replaceAngleBrackets(): String {
    return when (this.isBlank()) {
        true -> this
        else -> this
            .replace("<",String())
            .replace(">",String())
    }
}

fun String.replaceExtraneousParagraphs(): String {
    return when (this.isBlank()) {
        true -> this
        else -> this
            .replace(
                "\n<P>&nbsp;</P>",String(), ignoreCase = true
            )
            .replace(
                "\n<p><br></p>",String(), ignoreCase = true
            )
            .replace("<BR></P>","</P>", ignoreCase = true)
    }
}

/* ignoreCase doesn't seem to work */
fun String.replaceMacrons(): String {
    return when (this.isBlank()) {
        true -> this
        else -> this
            .replace("Ā","A")
            .replace("Ē","E")
            .replace("Ī","I")
            .replace("Ō","O")
            .replace("Ū","U")
        }
}