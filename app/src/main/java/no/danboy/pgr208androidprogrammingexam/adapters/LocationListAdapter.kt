package no.danboy.pgr208androidprogrammingexam.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import no.danboy.pgr208androidprogrammingexam.R
import no.danboy.pgr208androidprogrammingexam.activities.LocationDetails
import no.danboy.pgr208androidprogrammingexam.activities.GoogleMapsActivity
import no.danboy.pgr208androidprogrammingexam.model.Location

class LocationListAdapter(private val locationList: ArrayList<Location>,
                          private val context: Context): RecyclerView.Adapter<LocationListAdapter.ViewHolder>() {


    // Getting the view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_row, parent, false)

        return ViewHolder(view)
    }


    // item count
    override fun getItemCount(): Int {
        return locationList.size
    }

    // Dynamically changes the view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(locationList[position])
    }

    // fetches widgets
    inner class ViewHolder(val v: View): RecyclerView.ViewHolder(v){

        var placeName = itemView.findViewById<TextView>(R.id.placeName)
        var pinBtn = itemView.findViewById<Button>(R.id.mapBtn)
        var card = itemView.findViewById<CardView>(R.id.cardList)


        fun bindView(location: Location){
            placeName.text = location.name

            // Adding link to LocationDetails page here and sending data
            card.setOnClickListener{
                var intent = Intent(context, LocationDetails::class.java)
                intent.putExtra("name", location.name)
                intent.putExtra("link", location.detailsLink)
                intent.putExtra("longitude", location.longitude)
                intent.putExtra("latitude", location.latitude)
                context.startActivity(intent)
            }

            // Adding link to GoogleMapsActivity page here and sending data
            pinBtn.setOnClickListener{
                var intent = Intent(context, GoogleMapsActivity::class.java)
                intent.putExtra("longitude", location.longitude)
                intent.putExtra("latitude", location.latitude)
                context.startActivity(intent)


            }
        }
    }

}


