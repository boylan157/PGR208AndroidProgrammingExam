package no.danboy.pgr208androidprogrammingexam.activities

import android.os.Bundle
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
import no.danboy.pgr208androidprogrammingexam.R
import no.danboy.pgr208androidprogrammingexam.adapters.LocationListAdapter
import no.danboy.pgr208androidprogrammingexam.model.Location
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {



    // Declaration
    var volleyRequest: RequestQueue? = null
    val url = "https://www.noforeignland.com/home/api/v1/places"
    var locationList: ArrayList<Location>? = null
    var locationAdapter: LocationListAdapter? = null
    var layoutManager: RecyclerView.LayoutManager? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        locationList = ArrayList<Location>()

        //Setting up recyclerView




        // Getting Json
        volleyRequest = Volley.newRequestQueue(this)
        fetchJson(url)
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
                        var name = propertiesObj.getString("name")
                        var id = propertiesObj.getString("id")

                        // Get geometry object
                        val geometryObj = featuresArray.getJSONObject(i).getJSONObject("geometry")
                        var coordinatesArray = geometryObj.getJSONArray("coordinates")

                        // Iterate through coordinates array
                        for(i in 0 until coordinatesArray.length() -1){
                            var longitude = coordinatesArray[0] as Double
                            var latitude = coordinatesArray[1] as Double


                            //initializing location object
                            var location = Location(name, id, longitude, latitude)


                            // adding locations to list
                            locationList!!.add(location)

                            //
                            locationAdapter = LocationListAdapter(locationList!!, this)
                            layoutManager = LinearLayoutManager(this)

                            // setting up list/recyclerView
                            recyclerViewListId.layoutManager = layoutManager
                            recyclerViewListId.adapter = locationAdapter
                        }

                        locationAdapter!!.notifyDataSetChanged()
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

}






//class Geometry