package com.example.mimesa.screens

import android.view.View
import android.widget.FrameLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import com.example.mimesa.utils.LocationUtils
import com.google.android.gms.maps.SupportMapFragment

@Composable
fun MapScreen() {
    AndroidView(
        factory = { context ->
            val frameLayout = FrameLayout(context).apply { id = View.generateViewId() }
            val fragmentManager = (context as FragmentActivity).supportFragmentManager
            val mapFragment = SupportMapFragment.newInstance()

            frameLayout.post {
                fragmentManager.beginTransaction()
                    .replace(frameLayout.id, mapFragment, "map_fragment")
                    .commitNow()

                mapFragment.getMapAsync { googleMap ->
                    LocationUtils.configureMap(context, googleMap)
                }
            }

            frameLayout
        },
        modifier = Modifier.fillMaxSize()
    )
}
