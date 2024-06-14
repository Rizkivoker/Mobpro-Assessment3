package org.d3if3168.appassessment3

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3168.appassessment3.ui.widget.ProfilDialog
import org.d3if3168.appassessment3.ui.widget.TopAppBarWidget
import org.d3if3168.appassessment3.system.database.MainViewModel
import org.d3if3168.appassessment3.system.database.model.User
import org.d3if3168.appassessment3.system.network.UserDataStore
import org.d3if3168.appassessment3.system.network.signIn
import org.d3if3168.appassessment3.system.network.signOut
import org.d3if3168.appassessment3.system.util.SettingsDataStore
import org.d3if3168.appassessment3.ui.theme.AppAssessment3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseApp()
        }
    }
}

@Composable
fun BaseApp(
    navController: NavHostController = rememberNavController(),
    context: Context = LocalContext.current,
    systemViewModel: MainViewModel = viewModel()
) {
    val dataStore = SettingsDataStore(context)
    val userStore = UserDataStore(context)
    val appTheme by dataStore.layoutFlow.collectAsState(true)
    var showDialog by remember { mutableStateOf(false) }
    val user by userStore.userFlow.collectAsState(User())
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
            bottomBar = {
                //BottomBarWidget(navController)
            },
        ) { paddingValues ->
            Modifier.padding(paddingValues)
            //NavigationGraph(navController, apiProfile, modifier = Modifier.padding(paddingValues))

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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppAssessment3Theme {
        BaseApp()
    }
}