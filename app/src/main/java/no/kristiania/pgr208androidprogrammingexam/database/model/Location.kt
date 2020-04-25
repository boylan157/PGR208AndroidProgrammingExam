package no.kristiania.pgr208androidprogrammingexam.database.model

// Model class for database

class Location(){
    var id: String? = null
    var name: String? = null
    var longitude: Double? = null
    var latitude: Double? = null
    var detailsLink: String? = null

    constructor(id: String, name: String, longitude: Double, latitude: Double): this(){
        this.id = id
        this.name = name
        this.longitude = longitude
        this.latitude = latitude
        this.detailsLink = "https://www.noforeignland.com/home/api/v1/place?id=$id"
    }

    override fun toString(): String {
        return "id: $id, name: $name, longitude: $longitude, latitude: $latitude, detailsLink: $detailsLink"
    }
}



