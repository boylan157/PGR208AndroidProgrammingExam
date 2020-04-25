package no.kristiania.pgr208androidprogrammingexam.activities

import android.graphics.Color
import android.os.Bundle
import android.widget.ImageView
import android.widget.SearchView
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
import no.kristiania.pgr208androidprogrammingexam.database.LocationDatabaseHelper
import no.kristiania.pgr208androidprogrammingexam.R
import no.kristiania.pgr208androidprogrammingexam.adapters.LocationListAdapter
import no.kristiania.pgr208androidprogrammingexam.database.model.Location
import org.json.JSONException
import org.json.JSONObject
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {



    // Declarations
    var volleyRequest: RequestQueue? = null
    private val url = "https://www.noforeignland.com/home/api/v1/places"
    var locationList: ArrayList<Location>? = null
    var locationAdapter: LocationListAdapter? = null
    var layoutManager: RecyclerView.LayoutManager? = null
    var dbHandler: LocationDatabaseHelper? = null
    var testList: ArrayList<Location>? = null





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationList = ArrayList<Location>()

        // Getting Json
        volleyRequest = Volley.newRequestQueue(this)

        dbHandler = LocationDatabaseHelper(this)

        testList = dbHandler!!.readLocations()



        //Uncomment this if u need to load in the data
        // this part of the code is redundant but i keep it so you can see
        if(testList.isNullOrEmpty()) {
            fetchJson(url)
        }



        // Inserting locations from database into list
        locationList = dbHandler!!.readLocations()

        location_search.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                locationAdapter!!.filter.filter(newText)
                return false
            }

        })




        locationAdapter = LocationListAdapter(locationList!!, this)
        layoutManager = LinearLayoutManager(this)
        // setting up list/recyclerView
        recyclerViewListId.layoutManager = layoutManager
        recyclerViewListId.adapter = locationAdapter

        locationAdapter!!.notifyDataSetChanged()
        locationList = dbHandler!!.readLocations()
    }



    // Fetching Json properties
    private fun fetchJson(url: String){
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

                            saveToDB(location)
                        }
                    }
                } catch (e: JSONException){
                   e.printStackTrace()
                }
        },
        Response.ErrorListener {
            error: VolleyError ->
            try {
                println("Error: $error")
            } catch (e: JSONException){
                e.printStackTrace()
            }
        })

        //Adds request to queue
        volleyRequest!!.add(jsonObjectReq)
    }


    private fun saveToDB(location: Location){
        dbHandler!!.createLocation(location)
    }

}

