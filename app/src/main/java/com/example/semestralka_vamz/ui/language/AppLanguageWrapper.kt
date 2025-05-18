import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import com.example.semestralka_vamz.data.model.AppLanguage
import java.util.Locale
import com.example.semestralka_vamz.ui.language.createLocalizedContext

// Obalený composable komponent, ktorý nastavuje jazyk a smer rozloženia UI
@SuppressLint("RememberReturnType")
@Composable
fun AppLanguageWrapper(
    language: AppLanguage,               // Jazyk vybraný používateľom
    content: @Composable () -> Unit      // Obsah, ktorý má byť vykreslený s daným jazykom
) {
    val baseContext = LocalContext.current

    // Výber Locale na základe enum hodnoty jazyka
    val locale = when (language) {
        AppLanguage.SK -> Locale("sk")        // Slovenčina
        AppLanguage.EN -> Locale.ENGLISH      // Angličtina
    }

    // Vytvorenie lokalizovaného kontextu pre daný jazyk
    val localizedContext = remember(locale) {
        baseContext.createLocalizedContext(locale)
    }

    // Určenie smeru rozloženia (LTR alebo RTL) podľa jazyka
    val layoutDirection = when (locale.language) {
        "ar", "he" -> LayoutDirection.Rtl     // Sprava doľava pre arabčinu, hebrejčinu
        else -> LayoutDirection.Ltr           // Zľava doprava pre ostatné jazyky
    }

    // Poskytnutie lokalizovaného kontextu a smeru layoutu potomkom
    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalLayoutDirection provides layoutDirection
    ) {
        content()
    }
}
