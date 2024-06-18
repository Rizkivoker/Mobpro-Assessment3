package org.d3if3168.appassessment3.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import org.d3if3168.appassessment3.R

@Composable
fun DetailScreen(
    nama: String,
    alamat: String,
    nomor: String,
    navController: NavHostController
) {
    Column {
        Text(text = nama)
        Text(text = alamat)
        Text(text = nomor)
        Button(onClick = { navController.popBackStack() }) {
            Image(painter = painterResource(id = R.drawable.baseline_login_24), contentDescription = "")
        }
    }
}