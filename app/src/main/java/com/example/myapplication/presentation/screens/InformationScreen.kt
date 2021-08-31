package com.example.myapplication.presentation.screens

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.myapplication.presentation.components.MapView
import com.example.myapplication.util.Constants.FINISHHOUR
import com.example.myapplication.util.Constants.SHOPEMAIL
import com.example.myapplication.util.Constants.SHOPNUMBER
import com.example.myapplication.util.Constants.STARTHOUR
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@Composable
fun InformationScreen(
    navController: NavController,
) {

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                phoneCall(SHOPNUMBER, context)
            } else {
                Toast.makeText(
                    context,
                    "The app requires phone call permission to be able to call the shop physical store.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {

            Row(
                modifier = Modifier.padding(top = 12.dp),
                horizontalArrangement = Arrangement.Start
            ) {
//                IconButton(
//                    onClick = {
//                        navController.popBackStack()
//                    },
//                ) {
                Icon(
                    modifier = Modifier
                        .align(CenterVertically)
                        .size(35.dp)
                        .clickable {
                            navController.popBackStack()
                        },
                    tint = MaterialTheme.colors.primary,
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                )
                // }

                Spacer(modifier = Modifier.width(11.dp))

                Text(
                    text = "About us",
                    style = MaterialTheme.typography.h2
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Contact us",
                style = MaterialTheme.typography.h3
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(modifier = Modifier.fillMaxWidth()) {

                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .align(CenterVertically),
                    tint = MaterialTheme.colors.primary,
                    imageVector = Icons.Filled.Call,
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    modifier = Modifier
                        .align(CenterVertically)
                        .clickable {
                            when (PackageManager.PERMISSION_GRANTED) {
                                ContextCompat.checkSelfPermission(
                                    context,
                                    Manifest.permission.CALL_PHONE
                                ) -> {
                                    // Some works that require permission
                                    phoneCall(SHOPNUMBER, context)
                                }
                                else -> {
                                    // Asking for permission
                                    launcher.launch(Manifest.permission.CALL_PHONE)
                                }
                            }
                        },
                    text = SHOPNUMBER,
                    style = MaterialTheme.typography.h4
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .align(CenterVertically),
                    tint = MaterialTheme.colors.primary,
                    imageVector = Icons.Filled.Email,
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    modifier = Modifier
                        .align(CenterVertically),
                    text = SHOPEMAIL,
                    style = MaterialTheme.typography.h4
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(text = "Follow us", style = MaterialTheme.typography.h3)

            Spacer(modifier = Modifier.height(14.dp))

            Row(modifier = Modifier.fillMaxWidth()) {

                Icon(
                    modifier = Modifier
                        .size(42.dp)
                        .align(CenterVertically),
                    tint = MaterialTheme.colors.primary,
                    imageVector = Icons.Filled.Facebook,
                    contentDescription = null,
                )

                Spacer(modifier = Modifier.width(15.dp))

                Text(
                    modifier = Modifier
                        .align(CenterVertically),
                    text = "ShopApp",
                    style = MaterialTheme.typography.h4
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(text = "Work Hours", style = MaterialTheme.typography.h3)

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "$STARTHOUR am - $FINISHHOUR pm",
                style = MaterialTheme.typography.h4
            )

            Spacer(modifier = Modifier.height(15.dp))

            MapView(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(300.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .padding(bottom = 15.dp),
                onReady = {
                    it.uiSettings.isZoomControlsEnabled = true

                    val shopLocation =  LatLng(35.70383845083055, -0.5613504002574305)

                    it.moveCamera(CameraUpdateFactory.newLatLngZoom(shopLocation,15f))
                    val markerOptions = MarkerOptions()
                        .title("Waiki Shop")
                        .position(shopLocation)
                    it.addMarker(markerOptions)
                }
            )
        }
    }
}

fun phoneCall(number: String, context: Context) {
    val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
    context.startActivity(intent)
}