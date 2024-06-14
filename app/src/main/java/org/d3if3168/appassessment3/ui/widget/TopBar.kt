package org.d3if3168.appassessment3.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import org.d3if3168.appassessment3.R
import org.d3if3168.appassessment3.system.database.model.User
import org.d3if3168.appassessment3.ui.theme.BrownBg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarWidget(
    title: String,
    user: User,
    appTheme: Boolean,
    showDialog: Boolean,
    onShowDialogChange: (Boolean) -> Unit,
    onAppThemeChange: (Boolean) -> Unit
) {
    CenterAlignedTopAppBar(
        modifier = Modifier.background(BrownBg),
        title = {
            Text(title, style = MaterialTheme.typography.titleMedium)
        },
        navigationIcon = {
            IconButton(onClick = { onAppThemeChange(!appTheme) }) {
                Icon(
                    painter = if (appTheme) painterResource(id = R.drawable.baseline_dark_mode_24) else painterResource(
                        id = R.drawable.baseline_light_mode_24
                    ),
                    contentDescription = ""
                )
            }
        },
        actions = {
            IconButton(
                onClick = { onShowDialogChange(!showDialog) }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_login_24),
                    contentDescription = null,
                    modifier = Modifier.size(30.dp)
                )
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(user.photoUrl)
                        .crossfade(true)
                        .build(),
                    error = { painterResource(id = R.drawable.baseline_account_circle_24) },
                    contentDescription = null
                )
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
        )
    )
}
