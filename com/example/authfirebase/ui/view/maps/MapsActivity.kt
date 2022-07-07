package com.example.authfirebase.ui.view.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import com.example.authfirebase.R
import com.example.authfirebase.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val ojekBro = LatLng(-6.9388637,107.7609764)
    private val jatos = LatLng(-6.9352104,107.7718115)
    private val unpad = LatLng(-6.9281188,107.7725299)
    private val ciseke = LatLng(-6.9372353,107.7761778)

    private var locationArrayList: ArrayList<LatLng>? = null

    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        // Current Marker
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Multiple Marker
        locationArrayList = ArrayList()

        locationArrayList!!.add(ojekBro)
        locationArrayList!!.add(jatos)
        locationArrayList!!.add(unpad)
        locationArrayList!!.add(ciseke)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        setUpMap()
        // Draw Route
//        val URL = getDirectionURL(ojekBro, jatos)
//        GetDirection(URL).execute()

        // Add a marker in Sydney and move the camera
        for (i in locationArrayList!!.indices){
            mMap.addMarker(MarkerOptions().position(locationArrayList!![i]).title("Marker in Sydney"))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locationArrayList!![i]))
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    ojekBro,
                    14f
                )
            )
            mMap.addPolyline(
                PolylineOptions()
                    .add(ojekBro)
                    .add(jatos)
                    .add(unpad)
                    .add(ciseke)
                    .width(4f)
                    .color(Color.RED)
            )
            mMap.addCircle(
                CircleOptions()
                    .center(ojekBro)
                    .radius(500.0)
                    .strokeWidth(2f)
                    .strokeColor(Color.RED)
                    .fillColor(Color.argb(30, 150, 50, 50))
            )
        }
    }

    private fun setUpMap(){

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
            return
        }
        mMap.isMyLocationEnabled = true
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null){
                lastLocation = location
                val currentLatLong = ojekBro
                placeMarkerOnMap(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 14f))
            }
        }
    }

    private fun placeMarkerOnMap(currentLatLong : LatLng){
        val markerOption = MarkerOptions().position(currentLatLong)
        markerOption.title("$currentLatLong")
        mMap.addMarker(markerOption)
    }

    override fun onMarkerClick(p0: Marker?) = false

//    private fun getDirectionURL(origin: LatLng, dest: LatLng) : String {
//        return "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}&destination=${dest.latitude},${dest.longitude}&sensor=false&mode=driving"
//    }
//
//    inner class GetDirection(private val url: String) : AsyncTask<Void, Void, List<List<LatLng>>>(){
//        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
//            val client = OkHttpClient()
//            val request = Request.Builder().url(url).build()
//            val response = client.newCall(request).execute()
//            val data = response.body.string()
//            val result = ArrayList<List<LatLng>>()
//            try {
//                val respObj = Gson().fromJson(data, GoogleMapsDTO::class.java)
//                val path = ArrayList<LatLng>()
//
//                for (i in 0 until respObj.routes[0].legs[0].steps.size){
////                    val startLatLng = LatLng(
////                        respObj.routes[0].legs[0].steps[i].start_location.lat.toDouble(),
////                        respObj.routes[0].legs[0].steps[i].start_location.lng.toDouble()
////                    )
////                    path.add(startLatLng)
////
////                    val endlatLng = LatLng(
////                        respObj.routes[0].legs[0].steps[i].end_location.lat.toDouble(),
////                        respObj.routes[0].legs[0].steps[i].end_location.lng.toDouble()
////                    )
//                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
//                }
//                result.add(path)
//            }
//            catch (e: Exception){
//                e.printStackTrace()
//            }
//            return result
//        }
//
//
//        override fun onPostExecute(result: List<List<LatLng>>) {
//            val lineOption = PolylineOptions()
//            for (i in result.indices){
//                lineOption.addAll(result[i])
//                lineOption.width(10f)
//                lineOption.color(Color.BLUE)
//                lineOption.geodesic(true)
//            }
//            googleMap.addPolyline(lineOption)
//        }
//    }
//
//    fun decodePolyline(encoded: String): List<LatLng>{
//        val poly = ArrayList<LatLng>()
//        var index = 0
//        val len = encoded.length
//        var lat = 0
//        var lng = 0
//
//        while (index < len){
//            var b: Int
//            var shift = 0
//            var result = 0
//            do {
//                b = encoded[index++].code - 63
//                result = result or (b and 0x1f shl shift)
//                shift += 5
//            } while (b >= 0x20)
//
//            val dlat = (result shr 1).inv()
//            lat += dlat
//            shift = 0
//            result = 0
//
//            do {
//                b = encoded[index++].code - 63
//                result = result or (b and 0x1f shl shift)
//                shift += 5
//            } while (b >= 0x20)
//
//            val dlng = (result shr 1).inv()
//            lng += dlng
//
//            val latLng = LatLng((lat.toDouble() / 1E5), (lng.toDouble() / 1E5))
//            poly.add(latLng)
//            }
//        return poly
//
//    }
}