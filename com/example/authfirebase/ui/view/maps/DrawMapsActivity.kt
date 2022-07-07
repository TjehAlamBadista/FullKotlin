package com.example.authfirebase.ui.view.maps

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.authfirebase.R
import com.example.authfirebase.databinding.ActivityDrawMapsBinding
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_draw_maps.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class DrawMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding : ActivityDrawMapsBinding
    private var mMap : GoogleMap? = null
    lateinit var mapView : MapView
    private val MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey"
    lateinit var tvCurrentAddress: TextView

    var end_latitude = 0.0
    var end_longitude = 0.0
    var origin: MarkerOptions? = null
    var destination: MarkerOptions? = null
    var latitude = 0.0
    var longitude = 0.0

    private val DEFAULT_ZOOM = 14f
    private var fusedLocationProviderClient : FusedLocationProviderClient? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDrawMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mapView = binding.map1
        tvCurrentAddress = binding.tvAdd

        askPermissionLocation()

        var mapViewBundle : Bundle? = null
        if (savedInstanceState != null){
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY)
        }

        mapView.onCreate(mapViewBundle)
        mapView.getMapAsync(this)

        binding.btnSearch.setOnClickListener {
            searchArea()
        }
        binding.btnClear.setOnClickListener {
            mapView.onCreate(mapViewBundle)
            mapView.getMapAsync(this)
        }
    }

    private fun searchArea(){
        val tf_location = binding.etLocation
        val location = tf_location.text.toString()
        var addressList : List<Address>? = null
        val markerOptions = MarkerOptions()
        if (location != ""){
            val geocoder = Geocoder(applicationContext)
            try {
                addressList = geocoder.getFromLocationName(location, 5)
            }
            catch (e: IOException){
                e.printStackTrace()
            }
            if (addressList != null){
                for (i in addressList.indices){
                    val myAddress = addressList[i]
                    val latlng = LatLng(myAddress.latitude, myAddress.longitude)
                    markerOptions.position(latlng)
                    mMap!!.addMarker(markerOptions)
                    end_latitude = myAddress.latitude
                    end_longitude = myAddress.longitude
                    mMap!!.animateCamera(CameraUpdateFactory.newLatLng(latlng))
                    val mo = MarkerOptions()
                    mo.title("Distance")

                    val result = FloatArray(10)
                    Location.distanceBetween(
                        latitude,
                        longitude,
                        end_latitude,
                        end_longitude,
                        result
                    )
                    val s = String.format("%.1f", result[0] / 1000)

                    // Setting marker untuk menggambar rute antara dua point
                    origin = MarkerOptions().position(LatLng(latitude, longitude))
                        .title("HSR Layout").snippet("origin")
                    destination = MarkerOptions().position(LatLng(end_latitude, end_longitude))
                        .title(tf_location.text.toString())
                        .snippet("Distance = $s KM")

                    mMap!!.addMarker(destination)
                    mMap!!.addMarker(origin)

                    Toast.makeText(this@DrawMapsActivity, "Distance = $s KM", Toast.LENGTH_SHORT).show()

                    tvCurrentAddress!!.setText("Distance = $s KM")

                    // Get URL to the Google Direction API
                    val url : String = getDirectionUrl(origin!!.getPosition(), destination!!.getPosition())!!

                    val downloadTask: DownloadTask = DownloadTask()
                    downloadTask.execute(url)
                }
            }
        }
    }

    inner class DownloadTask: AsyncTask<String?, Void?, String>(){

        override fun onPostExecute(result: String) {
            super.onPostExecute(result)
            val parserTask = ParserTask()
            parserTask.execute(result)
        }

        override fun doInBackground(vararg url: String?): String {
            var data = ""
            try {
                data = downloadUrl(url[0].toString()).toString()
            }
            catch (e: java.lang.Exception){
                Log.d("Background Task", e.toString())
            }
            return data
        }
    }

    inner class ParserTask: AsyncTask<String?, Int?, List<List<HashMap<String, String>>>?>(){
        override fun doInBackground(vararg jsonData: String?): List<List<HashMap<String, String>>>? {
            var jObject: JSONObject
            var routes : List<List<HashMap<String, String>>>? = null
            try {
                jObject = JSONObject(jsonData[0])
                val parser = DataParser()
                routes = parser.parse(jObject)
            }
            catch (e: java.lang.Exception){
                e.printStackTrace()
            }
            return routes
        }

        override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
            val points = ArrayList<LatLng?>()
            val lineOptions = PolylineOptions()
//            if (result != null) {
//                if (result.isNotEmpty()){
                    for (i in result!!.indices){
                        val path = result[i]
                        for (j in path.indices){
                            val point = path[j]
                            val lat = point["lat"]!!.toDouble()
                            val lng = point["lng"]!!.toDouble()
                            val position = LatLng(lat, lng)
                            points.add(position)
                        }
                        lineOptions.addAll(points)
                        lineOptions.width(8f)
                        lineOptions.color(Color.RED)
                        lineOptions.geodesic(true)

                    }
                    if (points.size != 0)
                        mMap!!.addPolyline(lineOptions)
//                }
//            }
//            else{
//                Toast.makeText(this@DrawMapsActivity, "NullPointerException", Toast.LENGTH_LONG).show()
//            }
        }
    }

    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String?{
        var data = ""
        var iStream : InputStream? = null
        var urlConnection: HttpURLConnection? = null
        try {
            val url = URL(strUrl)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()
            iStream = urlConnection!!.inputStream
            val br = BufferedReader(InputStreamReader(iStream))
            val sb = StringBuffer()
            var line: String? = ""
            while (br.readLine().also { line = it } != null){
                sb.append(line)
            }
            data = sb.toString()
            br.close()
        }
        catch (e: java.lang.Exception){
            Log.d("Exception", e.toString())
        }
        finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }
        return data
    }

    private fun getDirectionUrl(origin: LatLng, dest: LatLng) : String? {
        // Rute Origin
        val strOrigin = "Origin" + origin.latitude + "," + origin.longitude

        // Rute Destination
        val str_dest = "Destination" + dest.latitude + "," + dest.longitude

        //Setting Transporting Mode
        val mode = "mode=driving"

        val parameters = "$strOrigin$str_dest$mode"

        val output = "json"

        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key="+getString(R.string.google_maps_key)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap?) {
        mapView.onResume()
        mMap = googleMap

        askPermissionLocation()

        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ){
            return
        }
        mMap!!.setMyLocationEnabled(true)
//        mMap!!.setOnCameraMoveListener(this)
//        mMap!!.setOnCameraMoveStartedListener(this)
//        mMap!!.setOnCameraIdleListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        askPermissionLocation()
        var mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY)
        if (mapViewBundle == null){
            mapViewBundle = Bundle()
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle)
        }

        mapView.onSaveInstanceState(mapViewBundle)
    }

    private fun askPermissionLocation(){
        askPermission(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ){
            getCurrentLocation()
//            mapView.getMapAsync(this@DrawMapsActivity)
        }.onDeclined{ e ->
            if (e.hasDenied()){
                e.denied.forEach {  }

                AlertDialog.Builder(this)
                    .setMessage("Please accpet our Permission!")
                    .setPositiveButton("yes"){_, _ ->
                        e.askAgain()
                    }
                    .setNegativeButton("no"){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }

            if (e.hasForeverDenied()){
                e.foreverDenied.forEach {  }
                e.goToSettings()
            }
        }
    }


    private fun getCurrentLocation(){
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@DrawMapsActivity)

        try {
            @SuppressLint("MissingPermission")
            val location = fusedLocationProviderClient!!.getLastLocation()

            location.addOnCompleteListener(object : OnCompleteListener<Location>{
                override fun onComplete(loc: Task<Location>){
                    if (loc.isSuccessful){
                        val currentLocation = loc.result as Location?
                        if (currentLocation != null){
                            moveCamera(
                                LatLng(currentLocation.latitude, currentLocation.longitude),
                                DEFAULT_ZOOM
                            )
                            latitude = currentLocation.latitude
                            longitude = currentLocation.longitude
                        }
                    }
                    else{
                        askPermissionLocation()
                    }
                }
            })
        }
        catch (se: Exception){
            Log.e("TAG", "Security Exception")
        }
    }
    private fun moveCamera(latlng: LatLng, zoom: Float){
        mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, zoom))
    }

    // Mengambil Address
//    override fun onLocationChanged(location: Location) {
//        val geocoder = Geocoder(this, Locale.getDefault())
//        var addresses : List<Address>? = null
//
//        try {
//            addresses = geocoder.getFromLocation(location!!.latitude, location!!.longitude, 1)
//        }
//        catch (e: IOException){
//            e.printStackTrace()
//        }
//        setAddress(addresses!![0])
//    }
//
//    private fun setAddress(addresses: Address){
//        if (addresses != null){
//            if (addresses.getAddressLine(0) != null){
//                tvCurrentAddress!!.setText(
//                    addresses.getAddressLine(0)
//                )
//            }
//            if (addresses.getAddressLine(1) != null){
//                tvCurrentAddress!!.setText(
//                    tvCurrentAddress.getText().toString() + addresses.getAddressLine(1)
//                )
//            }
//        }
//    }

//    override fun onCameraMove() {
//        TODO("Not yet implemented")
//    }
//
//    override fun onCameraMoveStarted(p0: Int) {
//        TODO("Not yet implemented")
//    }

    // Mengambil Address
//    override fun onCameraIdle() {
//        var addresses : List<Address>? = null
//        val geocoder = Geocoder(this, Locale.getDefault())
//        try {
//            addresses = geocoder.getFromLocation(mMap!!.getCameraPosition().target.latitude, mMap!!.getCameraPosition().target.longitude, 1)
//
//            setAddress(addresses!![0])
//        }
//        catch (e: IndexOutOfBoundsException){
//            e.printStackTrace()
//        }
//        catch (e: IOException){
//            e.printStackTrace()
//        }
//    }
}