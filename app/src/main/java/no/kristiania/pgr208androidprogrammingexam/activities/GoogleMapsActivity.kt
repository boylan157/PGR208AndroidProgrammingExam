package no.kristiania.pgr208androidprogrammingexam.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.*

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import no.kristiania.pgr208androidprogrammingexam.R

class GoogleMapsActivity : AppCompatActivity() {


    // Alot of code here taken from https://www.youtube.com/watch?v=suwq7Nta3oM with some changes

    lateinit var mapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_maps)

        // Getting data
        var extras = intent.extras
        var longitude = extras!!.get("longitude") as Double
        var latitude = extras.get("latitude") as Double

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment

        // when map is ready call this function
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it

            //setting where location is and adding marker
            val location = LatLng(latitude, longitude)
            googleMap.addMarker(MarkerOptions().position(location))

            // making zoom animation
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
        })
    }
}
