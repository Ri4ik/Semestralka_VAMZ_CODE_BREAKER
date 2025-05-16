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

@SuppressLint("RememberReturnType")
@Composable
fun AppLanguageWrapper(
    language: AppLanguage,
    content: @Composable () -> Unit
) {
    val baseContext = LocalContext.current
    val locale = when (language) {
        AppLanguage.SK -> Locale("sk")
        AppLanguage.EN -> Locale.ENGLISH
    }

    val localizedContext = remember(locale) {
        baseContext.createLocalizedContext(locale)
    }

    val layoutDirection = when (locale.language) {
        "ar", "he" -> LayoutDirection.Rtl
        else -> LayoutDirection.Ltr
    }

    CompositionLocalProvider(
        LocalContext provides localizedContext,
        LocalLayoutDirection provides layoutDirection
    ) {
        content()
    }
}
