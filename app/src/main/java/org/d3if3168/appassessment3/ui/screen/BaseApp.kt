package org.d3if3168.appassessment3.ui.screen

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3168.appassessment3.R
import org.d3if3168.appassessment3.system.database.MainViewModel
import org.d3if3168.appassessment3.system.database.model.Contact
import org.d3if3168.appassessment3.system.database.model.User
import org.d3if3168.appassessment3.system.network.ContactAPI
import org.d3if3168.appassessment3.system.network.ContactStatus
import org.d3if3168.appassessment3.system.network.UserDataStore
import org.d3if3168.appassessment3.system.network.signIn
import org.d3if3168.appassessment3.system.network.signOut
import org.d3if3168.appassessment3.system.util.SettingsDataStore
import org.d3if3168.appassessment3.ui.element.AddForm
import org.d3if3168.appassessment3.ui.element.ListItem
import org.d3if3168.appassessment3.ui.theme.AppAssessment3Theme
import org.d3if3168.appassessment3.ui.widget.ProfilDialog
import org.d3if3168.appassessment3.ui.widget.TopAppBarWidget

@Composable
fun BaseApp(
    navController: NavHostController,
    onNavigateToScreen: (String, String, String) -> Unit,
) {
    val context = LocalContext.current
    val dataStore = SettingsDataStore(context)
    val viewModel: MainViewModel = viewModel()
    val userStore = UserDataStore(context)
    val appTheme by dataStore.layoutFlow.collectAsState(true)
    var showDialog by remember { mutableStateOf(false) }
    val user by userStore.userFlow.collectAsState(User())

    var addContact by remember { mutableStateOf(false) }
    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(contract = CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
        if (bitmap != null) addContact = true
    }

    AppAssessment3Theme(darkTheme = appTheme) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            topBar = {
                TopAppBarWidget(
                    title = stringResource(id = R.string.app_name),
                    user = user,
                    appTheme = appTheme,
                    showDialog = showDialog,
                    onShowDialogChange = { showDialog = it },
                    onAppThemeChange = { newTheme ->
                        CoroutineScope(Dispatchers.IO).launch {
                            dataStore.saveLayout(!appTheme)
                        }
                    }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    val options = CropImageContractOptions(
                        null, CropImageOptions(
                            imageSourceIncludeGallery = true,
                            imageSourceIncludeCamera = true,
                            fixAspectRatio = true
                        )
                    )
                    launcher.launch(options)
                }) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.people),
                            contentDescription = null
                        )
                        Text(text = stringResource(id = R.string.tambahKontak))
                    }
                }
            }

        ) { paddingValues ->
            ScreenContent(
                viewModel = viewModel,
                user = user,
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                onNavigateToScreen
            )

            if (addContact) {
                AddForm(
                    bitmap = bitmap,
                    onDismissRequest = { addContact = false },
                    onConfirmation = { nama, noTelp, email, tanggalLahir ->
                        viewModel.addContact(
                            user.email,
                            nama,
                            noTelp,
                            email,
                            tanggalLahir,
                            bitmap!!
                        )
                        addContact = false
                    }
                )
            }

            // LaunchedEffect to handle sign-in if needed
            LaunchedEffect(showDialog) {
                if (showDialog && user.email.isEmpty()) {
                    CoroutineScope(Dispatchers.IO).launch {
                        signIn(context, userStore)
                    }
                }
            }

            // Display the dialog if showDialog is true
            if (showDialog && user.email.isNotEmpty()) {
                ProfilDialog(user = user, onDismissRequest = { showDialog = false }) {
                    CoroutineScope(Dispatchers.IO).launch {
                        signOut(context, userStore)
                    }
                    showDialog = false
                }
            }

        }
    }
}

@Composable
fun ScreenContent(
    viewModel: MainViewModel,
    user: User,
    modifier: Modifier,
    navController: NavHostController,
    onNavigateToScreen: (String, String, String) -> Unit,
) {
    val status by viewModel._status.collectAsState()
    val contactData by viewModel.contactData.observeAsState()

    when (status) {
        ContactStatus.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Loading...",
                    fontSize = MaterialTheme.typography.bodyLarge.fontSize,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        ContactStatus.SUCCESS -> {
            val filteredContact = contactData?.filter { it.email == user.email }
            LazyVerticalGrid(
                modifier = modifier
                    .fillMaxSize()
                    .padding(4.dp),
                columns = GridCells.Fixed(2)
            ) {
                items(filteredContact!!) { contact ->
                    ListItem(contact, onDelete = { viewModel.deleteContact(it) })
                }
            }
        }

        ContactStatus.FAILED -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(id = R.string.error))
                Button(
                    onClick = { viewModel.getAllContacts() },
                    modifier = Modifier.padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text(text = stringResource(id = R.string.try_again))
                }
            }
        }

    }
}

private fun getCroppedImage(
    resolver: ContentResolver,
    result: CropImageView.CropResult
): Bitmap? {
    if (!result.isSuccessful) {
        Log.e("IMAGE", "Error: ${result.error}")
        return null
    }

    val uri = result.uriContent ?: return null

    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        MediaStore.Images.Media.getBitmap(resolver, uri)
    } else {
        val source = ImageDecoder.createSource(resolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppAssessment3Theme {
        BaseApp(rememberNavController()) { nama, alamat, nomor ->

        }
    }
}