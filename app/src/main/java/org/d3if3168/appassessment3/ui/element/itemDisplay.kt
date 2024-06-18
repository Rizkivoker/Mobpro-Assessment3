package org.d3if3168.appassessment3.ui.element

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import org.d3if3168.appassessment3.R
import org.d3if3168.appassessment3.system.database.model.Contact
import org.d3if3168.appassessment3.system.network.ContactAPI
import org.d3if3168.appassessment3.ui.screen.DetailScreen

@Composable
fun ListItem(
    contact: Contact,
    onDelete: (String) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var nama by remember { mutableStateOf("") }
    var alamat by remember { mutableStateOf("") }
    var nomor by remember { mutableStateOf("") }
    var showDetail by remember { mutableStateOf(false) }

    nama = contact.contactName
    alamat = contact.contactEmail
    nomor = contact.contactPhone

    if (showDialog) {
        DeleteDialog(
            openDialog = showDialog,
            onDismissRequest = { showDialog = false },
            onConfirmation = {
                onDelete(contact.id)
                showDialog = false
            }
        )
    }

    if (showDetail) {
        ContactDetails(contact, onDismissRequest = { showDetail = false })
    }

    Box(
        modifier = Modifier
            .clickable(onClick = { showDetail = true })
            .padding(4.dp)
            .border(1.dp, Color.Gray),
        contentAlignment = Alignment.BottomCenter
    ) {
        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(ContactAPI.imgUrl(contact.contactImageUrl))
                .crossfade(true)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            error = { painterResource(id = R.drawable.baseline_broken_image_24) },
            loading = { painterResource(id = R.drawable.baseline_broken_image_24) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .background(Color(red = 0f, green = 0f, blue = 0f, alpha = 0.5f))
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = contact.contactName,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = contact.contactEmail,
                    fontStyle = FontStyle.Italic,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
            IconButton(onClick = { showDialog = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "",
                    tint = Color.White
                )
            }
        }
    }
}