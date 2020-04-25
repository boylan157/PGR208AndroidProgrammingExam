package no.kristiania.pgr208androidprogrammingexam.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import no.kristiania.pgr208androidprogrammingexam.R
import no.kristiania.pgr208androidprogrammingexam.activities.LocationDetailsActivity
import no.kristiania.pgr208androidprogrammingexam.activities.GoogleMapsActivity
import no.kristiania.pgr208androidprogrammingexam.database.model.Location
import java.util.*
import kotlin.collections.ArrayList

class LocationListAdapter(private val locationList: ArrayList<Location>,
                          private val context: Context): RecyclerView.Adapter<LocationListAdapter.ViewHolder>(), Filterable {

    var locationFilterList = ArrayList<Location>()

    init {
        locationFilterList = locationList
    }


    // Getting the view
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_row, parent, false)

        return ViewHolder(view)
    }


    // item count
    override fun getItemCount(): Int {
        return locationFilterList.size
    }

    // Dynamically changes the view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(locationFilterList[position])
    }

    // fetches widgets
    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var placeName = itemView.findViewById<TextView>(R.id.placeName)
        var pinBtn = itemView.findViewById<Button>(R.id.mapBtn)
        var card = itemView.findViewById<CardView>(R.id.cardList)


        fun bindView(location: Location){
            placeName.text = location.name

            // Adding link to LocationDetails page here and sending data
            card.setOnClickListener{
                var intent = Intent(context, LocationDetailsActivity::class.java)
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

    // code source for this and most of search function https://johncodeos.com/how-to-add-search-in-recyclerview-using-kotlin/ with some changes
    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    locationFilterList = locationList
                } else {
                    val resultList = ArrayList<Location>()
                    for (row in locationList) {
                        if (row.name!!.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        )
                            resultList.add(row)
                    }
                    locationFilterList = resultList
                }
                val filterResult = FilterResults()
                filterResult.values = locationFilterList
                return filterResult
            }
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                locationFilterList = results?.values as ArrayList<Location>
                notifyDataSetChanged()
            }
        }
    }
}


