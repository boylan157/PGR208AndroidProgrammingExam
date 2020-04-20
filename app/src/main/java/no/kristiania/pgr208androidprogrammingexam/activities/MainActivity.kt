package no.kristiania.pgr208androidprogrammingexam.activities

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import kotlinx.android.synthetic.main.activity_main.*
import no.kristiania.pgr208androidprogrammingexam.LocationDatabaseHelper
import no.kristiania.pgr208androidprogrammingexam.R
import no.kristiania.pgr208androidprogrammingexam.adapters.LocationListAdapter
import no.kristiania.pgr208androidprogrammingexam.model.Location
import org.json.JSONException
import org.json.JSONObject
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {



    // Declarations
    var volleyRequest: RequestQueue? = null
    val url = "https://www.noforeignland.com/home/api/v1/places"
    var locationList: ArrayList<Location>? = null
    var locationAdapter: LocationListAdapter? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var dbHandler: LocationDatabaseHelper? = null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationList = ArrayList<Location>()


        // Getting Json
        volleyRequest = Volley.newRequestQueue(this)


        fetchJson(url)

        dbHandler = LocationDatabaseHelper(this)


        // Inserting locations
        locationList = dbHandler!!.readLocations()
        var testList: ArrayList<Location> = dbHandler!!.readLocations()

        for (l in locationList!!)
            println(l.name)


        locationAdapter = LocationListAdapter(locationList!!, this)
        layoutManager = LinearLayoutManager(this)
        // setting up list/recyclerView
        recyclerViewListId.layoutManager = layoutManager
        recyclerViewListId.adapter = locationAdapter

        locationAdapter!!.notifyDataSetChanged()

    }



    // Fetching Json properties
    fun fetchJson(url: String){
        val jsonObjectReq = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener {
                response: JSONObject ->
                try {

                    // Array that contains all locations
                    val featuresArray = response.getJSONArray("features")

                    // iterate through features array
                    for(i in 0 until featuresArray.length() -1){

                        // Get properties object
                        val propertiesObj = featuresArray.getJSONObject(i).getJSONObject("properties")
                        val name = propertiesObj.getString("name")
                        val id = propertiesObj.getString("id")

                        // Get geometry object
                        val geometryObj = featuresArray.getJSONObject(i).getJSONObject("geometry")
                        val coordinatesArray = geometryObj.getJSONArray("coordinates")

                        // Iterate through coordinates array
                        for(i in 0 until coordinatesArray.length() -1){
                            val longitude = coordinatesArray[0] as Double
                            val latitude = coordinatesArray[1] as Double


                            //initializing location object
                            val location = Location(id, name, longitude, latitude)


                            //Save locations to database

                            //saveToDB(location)


                        }
                    }
                } catch (e: JSONException){
                   e.printStackTrace()
                }
        },
        Response.ErrorListener {
            error: VolleyError ->
            try {
                println("Error: " + error.toString())
            } catch (e: JSONException){
                e.printStackTrace()
            }
        })

        //Adds request to queue
        volleyRequest!!.add(jsonObjectReq)
    }


    fun saveToDB(location: Location){
        dbHandler!!.createLocation(location)
    }

}

