package org.d3if3168.appassessment3.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3168.appassessment3.R
import org.d3if3168.appassessment3.system.database.MainViewModel
import org.d3if3168.appassessment3.ui.theme.AppAssessment3Theme

/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(navController: NavHostController, id: Long? = null) {

    var nama by remember { mutableStateOf("") }
    var ttl by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var notelp by remember { mutableStateOf("") }
    var zodiak by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }
    var mainModel = MainViewModel()

    val contactData by mainModel.contactData.observeAsState()

    LaunchedEffect(true) {
        if (id != null) {
            nama = contactData.nama
            ttl = data.ttl
            email = data.email
            notelp = data.notelp
            zodiak = data.zodiak
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null)
                        Text(text = stringResource(id = R.string.app_name))
                    else
                        Text(text = stringResource(id = R.string.edit))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(
                        onClick = {
                            if (nama.isEmpty()) {
                                Toast.makeText(
                                    navController.context,
                                    R.string.nama_kosong,
                                    Toast.LENGTH_LONG
                                ).show()
                                return@IconButton
                            }
                            if (ttl.isEmpty()) {
                                Toast.makeText(
                                    navController.context,
                                    R.string.ttl_kosong,
                                    Toast.LENGTH_LONG
                                ).show()
                                return@IconButton
                            }
                            if (email.isEmpty()) {
                                Toast.makeText(
                                    navController.context,
                                    R.string.email_kosong,
                                    Toast.LENGTH_LONG
                                ).show()
                                return@IconButton
                            }
                            if (notelp.isEmpty()) {
                                Toast.makeText(
                                    navController.context,
                                    R.string.notelp_kosong,
                                    Toast.LENGTH_LONG
                                ).show()
                                return@IconButton
                            }
                            if (zodiak.isEmpty()) {
                                Toast.makeText(
                                    navController.context,
                                    R.string.zodiak_kosong,
                                    Toast.LENGTH_LONG
                                ).show()
                                return@IconButton
                            }

                            if (id == null) {
                                viewModel.insert(nama, ttl, email, notelp, zodiak)
                            } else {
                                viewModel.update(nama, ttl, email, notelp, zodiak, id)
                            }
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(id = R.string.simpan),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    if (id != null) {
                        DeleteAction {
                            showDialog = true
                        }
                        DisplayAlertDialog(
                            openDilog = showDialog,
                            onDismissRequest = { showDialog = false },
                        ) {
                            showDialog = false
                            viewModel.deleteOutfitsDaoById(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    ) { padding ->
        FormContact(
            nama = nama,
            onNamaChange = { nama = it },
            ttl = ttl,
            onTtlChange = { ttl = it },
            email = email,
            onEmailChange = { email = it },
            notelp = notelp,
            onNotelpChange = { notelp = it },
            zodiak = zodiak,
            onZodiakChange = { zodiak = it },
            modifier = Modifier.padding(padding)
        )
    }
}


@Composable
fun FormContact(
    nama: String, onNamaChange: (String) -> Unit,
    ttl: String, onTtlChange: (String) -> Unit,
    email: String, onEmailChange: (String) -> Unit,
    notelp: String, onNotelpChange: (String) -> Unit,
    zodiak: String, onZodiakChange: (String) -> Unit,

    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = nama,
            onValueChange = { onNamaChange(it) },
            label = { Text(text = stringResource(id = R.string.nama)) },
            singleLine = true,
            leadingIcon = { Icon(painterResource(id = R.drawable.namaewa), contentDescription = null)},
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = ttl,
            onValueChange = { onTtlChange(it) },
            label = { Text(text = stringResource(id = R.string.ttl)) },
            singleLine = true,
            leadingIcon = { Icon(painterResource(id = R.drawable.place), contentDescription = null)},
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = email,
            onValueChange = { onEmailChange(it) },
            label = { Text(text = stringResource(id = R.string.email)) },
            singleLine = true,
            leadingIcon = { Icon(painterResource(id = R.drawable.email), contentDescription = null)},
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Text
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = notelp,
            onValueChange = { onNotelpChange(it) },
            label = { Text(text = stringResource(id = R.string.notelp)) },
            singleLine = true,
            leadingIcon = { Icon(painterResource(id = R.drawable.phone), contentDescription = null)},
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Number
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = zodiak,
            onValueChange = { onZodiakChange(it) },
            label = { Text(text = stringResource(id = R.string.zodiak)) },
            singleLine = true,
            leadingIcon = { Icon(painterResource(id = R.drawable.zodiac), contentDescription = null)},
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(id = R.string.lainnya),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text(text = stringResource(id = R.string.hapus)) },
                onClick = {
                    delete()
                    expanded = false
                }
            )
        }

    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DetailScreenPreview() {
    Assessment2Theme {
        DetailScreen(rememberNavController(), viewModel = viewModel())
    }
}*/
