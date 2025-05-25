package com.aZina0.circulationmaze.ui.theme
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.aZina0.compose.backgroundDark
import com.aZina0.compose.backgroundDarkHighContrast
import com.aZina0.compose.backgroundDarkMediumContrast
import com.aZina0.compose.backgroundLight
import com.aZina0.compose.backgroundLightHighContrast
import com.aZina0.compose.backgroundLightMediumContrast
import com.aZina0.compose.errorContainerDark
import com.aZina0.compose.errorContainerDarkHighContrast
import com.aZina0.compose.errorContainerDarkMediumContrast
import com.aZina0.compose.errorContainerLight
import com.aZina0.compose.errorContainerLightHighContrast
import com.aZina0.compose.errorContainerLightMediumContrast
import com.aZina0.compose.errorDark
import com.aZina0.compose.errorDarkHighContrast
import com.aZina0.compose.errorDarkMediumContrast
import com.aZina0.compose.errorLight
import com.aZina0.compose.errorLightHighContrast
import com.aZina0.compose.errorLightMediumContrast
import com.aZina0.compose.inverseOnSurfaceDark
import com.aZina0.compose.inverseOnSurfaceDarkHighContrast
import com.aZina0.compose.inverseOnSurfaceDarkMediumContrast
import com.aZina0.compose.inverseOnSurfaceLight
import com.aZina0.compose.inverseOnSurfaceLightHighContrast
import com.aZina0.compose.inverseOnSurfaceLightMediumContrast
import com.aZina0.compose.inversePrimaryDark
import com.aZina0.compose.inversePrimaryDarkHighContrast
import com.aZina0.compose.inversePrimaryDarkMediumContrast
import com.aZina0.compose.inversePrimaryLight
import com.aZina0.compose.inversePrimaryLightHighContrast
import com.aZina0.compose.inversePrimaryLightMediumContrast
import com.aZina0.compose.inverseSurfaceDark
import com.aZina0.compose.inverseSurfaceDarkHighContrast
import com.aZina0.compose.inverseSurfaceDarkMediumContrast
import com.aZina0.compose.inverseSurfaceLight
import com.aZina0.compose.inverseSurfaceLightHighContrast
import com.aZina0.compose.inverseSurfaceLightMediumContrast
import com.aZina0.compose.onBackgroundDark
import com.aZina0.compose.onBackgroundDarkHighContrast
import com.aZina0.compose.onBackgroundDarkMediumContrast
import com.aZina0.compose.onBackgroundLight
import com.aZina0.compose.onBackgroundLightHighContrast
import com.aZina0.compose.onBackgroundLightMediumContrast
import com.aZina0.compose.onErrorContainerDark
import com.aZina0.compose.onErrorContainerDarkHighContrast
import com.aZina0.compose.onErrorContainerDarkMediumContrast
import com.aZina0.compose.onErrorContainerLight
import com.aZina0.compose.onErrorContainerLightHighContrast
import com.aZina0.compose.onErrorContainerLightMediumContrast
import com.aZina0.compose.onErrorDark
import com.aZina0.compose.onErrorDarkHighContrast
import com.aZina0.compose.onErrorDarkMediumContrast
import com.aZina0.compose.onErrorLight
import com.aZina0.compose.onErrorLightHighContrast
import com.aZina0.compose.onErrorLightMediumContrast
import com.aZina0.compose.onPrimaryContainerDark
import com.aZina0.compose.onPrimaryContainerDarkHighContrast
import com.aZina0.compose.onPrimaryContainerDarkMediumContrast
import com.aZina0.compose.onPrimaryContainerLight
import com.aZina0.compose.onPrimaryContainerLightHighContrast
import com.aZina0.compose.onPrimaryContainerLightMediumContrast
import com.aZina0.compose.onPrimaryDark
import com.aZina0.compose.onPrimaryDarkHighContrast
import com.aZina0.compose.onPrimaryDarkMediumContrast
import com.aZina0.compose.onPrimaryLight
import com.aZina0.compose.onPrimaryLightHighContrast
import com.aZina0.compose.onPrimaryLightMediumContrast
import com.aZina0.compose.onSecondaryContainerDark
import com.aZina0.compose.onSecondaryContainerDarkHighContrast
import com.aZina0.compose.onSecondaryContainerDarkMediumContrast
import com.aZina0.compose.onSecondaryContainerLight
import com.aZina0.compose.onSecondaryContainerLightHighContrast
import com.aZina0.compose.onSecondaryContainerLightMediumContrast
import com.aZina0.compose.onSecondaryDark
import com.aZina0.compose.onSecondaryDarkHighContrast
import com.aZina0.compose.onSecondaryDarkMediumContrast
import com.aZina0.compose.onSecondaryLight
import com.aZina0.compose.onSecondaryLightHighContrast
import com.aZina0.compose.onSecondaryLightMediumContrast
import com.aZina0.compose.onSurfaceDark
import com.aZina0.compose.onSurfaceDarkHighContrast
import com.aZina0.compose.onSurfaceDarkMediumContrast
import com.aZina0.compose.onSurfaceLight
import com.aZina0.compose.onSurfaceLightHighContrast
import com.aZina0.compose.onSurfaceLightMediumContrast
import com.aZina0.compose.onSurfaceVariantDark
import com.aZina0.compose.onSurfaceVariantDarkHighContrast
import com.aZina0.compose.onSurfaceVariantDarkMediumContrast
import com.aZina0.compose.onSurfaceVariantLight
import com.aZina0.compose.onSurfaceVariantLightHighContrast
import com.aZina0.compose.onSurfaceVariantLightMediumContrast
import com.aZina0.compose.onTertiaryContainerDark
import com.aZina0.compose.onTertiaryContainerDarkHighContrast
import com.aZina0.compose.onTertiaryContainerDarkMediumContrast
import com.aZina0.compose.onTertiaryContainerLight
import com.aZina0.compose.onTertiaryContainerLightHighContrast
import com.aZina0.compose.onTertiaryContainerLightMediumContrast
import com.aZina0.compose.onTertiaryDark
import com.aZina0.compose.onTertiaryDarkHighContrast
import com.aZina0.compose.onTertiaryDarkMediumContrast
import com.aZina0.compose.onTertiaryLight
import com.aZina0.compose.onTertiaryLightHighContrast
import com.aZina0.compose.onTertiaryLightMediumContrast
import com.aZina0.compose.outlineDark
import com.aZina0.compose.outlineDarkHighContrast
import com.aZina0.compose.outlineDarkMediumContrast
import com.aZina0.compose.outlineLight
import com.aZina0.compose.outlineLightHighContrast
import com.aZina0.compose.outlineLightMediumContrast
import com.aZina0.compose.outlineVariantDark
import com.aZina0.compose.outlineVariantDarkHighContrast
import com.aZina0.compose.outlineVariantDarkMediumContrast
import com.aZina0.compose.outlineVariantLight
import com.aZina0.compose.outlineVariantLightHighContrast
import com.aZina0.compose.outlineVariantLightMediumContrast
import com.aZina0.compose.primaryContainerDark
import com.aZina0.compose.primaryContainerDarkHighContrast
import com.aZina0.compose.primaryContainerDarkMediumContrast
import com.aZina0.compose.primaryContainerLight
import com.aZina0.compose.primaryContainerLightHighContrast
import com.aZina0.compose.primaryContainerLightMediumContrast
import com.aZina0.compose.primaryDark
import com.aZina0.compose.primaryDarkHighContrast
import com.aZina0.compose.primaryDarkMediumContrast
import com.aZina0.compose.primaryLight
import com.aZina0.compose.primaryLightHighContrast
import com.aZina0.compose.primaryLightMediumContrast
import com.aZina0.compose.scrimDark
import com.aZina0.compose.scrimDarkHighContrast
import com.aZina0.compose.scrimDarkMediumContrast
import com.aZina0.compose.scrimLight
import com.aZina0.compose.scrimLightHighContrast
import com.aZina0.compose.scrimLightMediumContrast
import com.aZina0.compose.secondaryContainerDark
import com.aZina0.compose.secondaryContainerDarkHighContrast
import com.aZina0.compose.secondaryContainerDarkMediumContrast
import com.aZina0.compose.secondaryContainerLight
import com.aZina0.compose.secondaryContainerLightHighContrast
import com.aZina0.compose.secondaryContainerLightMediumContrast
import com.aZina0.compose.secondaryDark
import com.aZina0.compose.secondaryDarkHighContrast
import com.aZina0.compose.secondaryDarkMediumContrast
import com.aZina0.compose.secondaryLight
import com.aZina0.compose.secondaryLightHighContrast
import com.aZina0.compose.secondaryLightMediumContrast
import com.aZina0.compose.surfaceBrightDark
import com.aZina0.compose.surfaceBrightDarkHighContrast
import com.aZina0.compose.surfaceBrightDarkMediumContrast
import com.aZina0.compose.surfaceBrightLight
import com.aZina0.compose.surfaceBrightLightHighContrast
import com.aZina0.compose.surfaceBrightLightMediumContrast
import com.aZina0.compose.surfaceContainerDark
import com.aZina0.compose.surfaceContainerDarkHighContrast
import com.aZina0.compose.surfaceContainerDarkMediumContrast
import com.aZina0.compose.surfaceContainerHighDark
import com.aZina0.compose.surfaceContainerHighDarkHighContrast
import com.aZina0.compose.surfaceContainerHighDarkMediumContrast
import com.aZina0.compose.surfaceContainerHighLight
import com.aZina0.compose.surfaceContainerHighLightHighContrast
import com.aZina0.compose.surfaceContainerHighLightMediumContrast
import com.aZina0.compose.surfaceContainerHighestDark
import com.aZina0.compose.surfaceContainerHighestDarkHighContrast
import com.aZina0.compose.surfaceContainerHighestDarkMediumContrast
import com.aZina0.compose.surfaceContainerHighestLight
import com.aZina0.compose.surfaceContainerHighestLightHighContrast
import com.aZina0.compose.surfaceContainerHighestLightMediumContrast
import com.aZina0.compose.surfaceContainerLight
import com.aZina0.compose.surfaceContainerLightHighContrast
import com.aZina0.compose.surfaceContainerLightMediumContrast
import com.aZina0.compose.surfaceContainerLowDark
import com.aZina0.compose.surfaceContainerLowDarkHighContrast
import com.aZina0.compose.surfaceContainerLowDarkMediumContrast
import com.aZina0.compose.surfaceContainerLowLight
import com.aZina0.compose.surfaceContainerLowLightHighContrast
import com.aZina0.compose.surfaceContainerLowLightMediumContrast
import com.aZina0.compose.surfaceContainerLowestDark
import com.aZina0.compose.surfaceContainerLowestDarkHighContrast
import com.aZina0.compose.surfaceContainerLowestDarkMediumContrast
import com.aZina0.compose.surfaceContainerLowestLight
import com.aZina0.compose.surfaceContainerLowestLightHighContrast
import com.aZina0.compose.surfaceContainerLowestLightMediumContrast
import com.aZina0.compose.surfaceDark
import com.aZina0.compose.surfaceDarkHighContrast
import com.aZina0.compose.surfaceDarkMediumContrast
import com.aZina0.compose.surfaceDimDark
import com.aZina0.compose.surfaceDimDarkHighContrast
import com.aZina0.compose.surfaceDimDarkMediumContrast
import com.aZina0.compose.surfaceDimLight
import com.aZina0.compose.surfaceDimLightHighContrast
import com.aZina0.compose.surfaceDimLightMediumContrast
import com.aZina0.compose.surfaceLight
import com.aZina0.compose.surfaceLightHighContrast
import com.aZina0.compose.surfaceLightMediumContrast
import com.aZina0.compose.surfaceVariantDark
import com.aZina0.compose.surfaceVariantDarkHighContrast
import com.aZina0.compose.surfaceVariantDarkMediumContrast
import com.aZina0.compose.surfaceVariantLight
import com.aZina0.compose.surfaceVariantLightHighContrast
import com.aZina0.compose.surfaceVariantLightMediumContrast
import com.aZina0.compose.tertiaryContainerDark
import com.aZina0.compose.tertiaryContainerDarkHighContrast
import com.aZina0.compose.tertiaryContainerDarkMediumContrast
import com.aZina0.compose.tertiaryContainerLight
import com.aZina0.compose.tertiaryContainerLightHighContrast
import com.aZina0.compose.tertiaryContainerLightMediumContrast
import com.aZina0.compose.tertiaryDark
import com.aZina0.compose.tertiaryDarkHighContrast
import com.aZina0.compose.tertiaryDarkMediumContrast
import com.aZina0.compose.tertiaryLight
import com.aZina0.compose.tertiaryLightHighContrast
import com.aZina0.compose.tertiaryLightMediumContrast
import com.aZina0.ui.theme.AppTypography

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable() () -> Unit
) {
  val colorScheme = when {
      dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
          val context = LocalContext.current
          if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
      }

      darkTheme -> darkScheme
      else -> lightScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = AppTypography,
    content = content
  )
}

