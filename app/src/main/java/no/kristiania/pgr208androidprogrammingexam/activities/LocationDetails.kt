package no.kristiania.pgr208androidprogrammingexam.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_location_details.*
import no.kristiania.pgr208androidprogrammingexam.R
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
        var longitude = extras!!.get("longitude") as Double
        var latitude = extras.get("latitude") as Double

        // link to GoogleMapsActivity and sending data
        mapBtn.setOnClickListener{
            var intent = Intent(this, GoogleMapsActivity::class.java)
            intent.putExtra("longitude", longitude)
            intent.putExtra("latitude", latitude)
            startActivity(intent)
        }

        // changing view
        locationName.text = name

        fetchJson(url)

    }

    // Fetching Json properties
    fun fetchJson(url: String){
        val jsonObject = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener {
                    response ->
                    try {
                        //Getting place object
                        val placeObject = response.getJSONObject("place")
                        //getting comments property
                        var comment = placeObject.getString("comments")
                        //removing html tags
                        comment = Html.fromHtml(comment).toString()
                        if(comment.isEmpty()){
                            commentText.text = "No comment available"
                        } else {
                            commentText.text = comment
                        }
                        //println(comment)
                        // Getting image link
                        var imgBanner = placeObject.getString("banner")

                        // setting value of imageview to banner from json
                        if(TextUtils.isEmpty(imgBanner)){
                            imageBanner.setImageResource(android.R.drawable.ic_menu_report_image)
                        } else {
                            Glide.with(this).load(imgBanner).into(imageBanner)
                        }

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
