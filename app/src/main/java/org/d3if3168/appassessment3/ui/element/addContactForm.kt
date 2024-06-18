package org.d3if3168.appassessment3.ui.element

import android.app.DatePickerDialog
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import org.d3if3168.appassessment3.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

@Composable
fun AddForm(
    bitmap: Bitmap?,
    onDismissRequest: () -> Unit,
    onConfirmation: (String, String, String, String) -> Unit,
) {
    var nama by remember { mutableStateOf("") }
    var noTelp by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }

    var selectedDate by remember { mutableStateOf<Date?>(null) }
    var dateString by remember {
        mutableStateOf(SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()))
    }
    val context = LocalContext.current

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val screenHeight = configuration.screenHeightDp.dp
    Dialog(
        onDismissRequest = { onDismissRequest() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    bitmap = bitmap!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(10.dp))
                        .width(screenWidth)
                        .heightIn(max = screenHeight)
                        .aspectRatio(1f)
                )
                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    label = { Text(text = stringResource(id = R.string.nama)) },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Words,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                )
                OutlinedTextField(
                    value = noTelp,
                    onValueChange = { noTelp = it },
                    label = { Text(text = stringResource(id = R.string.notelp)) },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text(text = stringResource(id = R.string.email)) },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .fillMaxWidth()
                )
                ///////////////////////////////////////////////////
                OutlinedButton(
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .height(48.dp)
                        .fillMaxWidth(),
                    onClick = {
                        val calendar = Calendar.getInstance()
                        val datePickerDialog = DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                val date = GregorianCalendar(year, month, dayOfMonth).time
                                selectedDate = date
                                dateString = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        )
                        // Set the maximum date to today
                        datePickerDialog.datePicker.maxDate = calendar.timeInMillis
                        datePickerDialog.show()
                    }
                ) {
                    Text(
                        text = dateString,
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.round_calendar),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
                ///////////////////////////////////////////////////
                OutlinedButton(
                    onClick = { onDismissRequest() },
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .height(48.dp)
                        .fillMaxWidth(),
                ) {
                    Text("Close")
                }
                OutlinedButton(
                    onClick = { onConfirmation(nama, noTelp, email, dateString) },
                    enabled = nama.isNotEmpty() && noTelp.isNotEmpty() && email.isNotEmpty(),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .height(48.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.simpan))
                }

            }
        }
    }
}