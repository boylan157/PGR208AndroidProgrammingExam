package no.danboy.pgr208androidprogrammingexam.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_location_details.*
import no.danboy.pgr208androidprogrammingexam.R
import org.json.JSONException

class LocationDetails : AppCompatActivity() {

    // Declaration
    var volleyRequest: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_details)

        //Getting Json
        volleyRequest = Volley.newRequestQueue(this)


        // Getting data
        var extras = intent.extras
        var url = extras!!.get("link").toString()  // url for second Json
        var name = extras.get("name").toString()

        // changing view
        locationName.text = name

        fetchJson(url)

    }

    fun fetchJson(url: String){
        val jsonObject = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
                    response ->
                    try {
                        //Getting place object
                        val placeObject = response.getJSONObject("place")
                        //getting comments property
                        var comment = placeObject.getString("comments")
                        //println(comment)
                        var imgBanner = placeObject.getString("banner")
                        println(imgBanner)

                    } catch (e: JSONException){e.printStackTrace()}

                },
                Response.ErrorListener {
                    error: VolleyError? ->
                    try {
                        println("error: " + error.toString())
                    } catch (e: JSONException){ e.printStackTrace()}
                })
        volleyRequest!!.add(jsonObject)
    }
}
