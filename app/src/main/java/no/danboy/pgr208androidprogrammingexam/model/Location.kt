package no.danboy.pgr208androidprogrammingexam.model

class Location (val name: String, val id: String, val longitude: Double, val latitude: Double){

    var detailsLink: String = "https://www.noforeignland.com/home/api/v1/place?id=" + id

}