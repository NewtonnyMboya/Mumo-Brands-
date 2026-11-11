package ke.infinitycyber.mumobrands.ui
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
val Navy=Color(0xFF090B67)
val Magenta=Color(0xFFE60078)
val RoyalBlue=Color(0xFF1769E8)
val DeepNavy=Color(0xFF10005D)
val Pale=Color(0xFFF4F5FF)
@Composable fun MumoTheme(content:@Composable ()->Unit) {
 MaterialTheme(colorScheme=lightColorScheme(primary=Magenta,secondary=RoyalBlue,tertiary=DeepNavy,background=Pale,surface=Color.White,onPrimary=Color.White,onSurface=Navy),typography=Typography(),content=content)
}