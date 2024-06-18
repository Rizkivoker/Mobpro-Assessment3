package org.d3if3168.appassessment3.ui.screen

import android.content.res.Configuration
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import org.d3if3168.appassessment3.R


@Composable
fun DisplayAlertDialog(
    openDilog: Boolean,
    onDismissRequest: () -> Unit,
    onConfirmRequest: () -> Unit
) {
    if (openDilog) {
        AlertDialog(
            onDismissRequest = { onDismissRequest() },
            text = { Text(text = stringResource(id = R.string.pesan_hapus))},
            confirmButton = {
                TextButton(
                    onClick = { onConfirmRequest() }
                ) {
                    Text(text = stringResource(id = R.string.tombol_hapus))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onDismissRequest() }
                ) {
                    Text(text = stringResource(id = R.string.tombol_batal))
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun DialogPreview() {
    DisplayAlertDialog(
        openDilog = true,
        onDismissRequest = { },
        onConfirmRequest = { }
    )
}