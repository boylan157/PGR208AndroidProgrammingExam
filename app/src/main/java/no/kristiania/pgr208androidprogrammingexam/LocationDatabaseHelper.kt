package no.kristiania.pgr208androidprogrammingexam

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import no.kristiania.pgr208androidprogrammingexam.model.*

class LocationDatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    override fun onCreate(db: SQLiteDatabase?) {
        var CREATE_LOCATION_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY," +
                                            KEY_LOCATION_NAME + " TEXT," +
                                            KEY_LOCATION_LONGITUDE + " DOUBLE," +
                                            KEY_LOCATION_LATITUDE + " DOUBLE," +
                                            KEY_LOCATION_DETAILSLINK + " TEXT)"

        db?.execSQL(CREATE_LOCATION_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")

        onCreate(db)
    }

    // CRUD

    //CREATE
    fun createLocation(location: Location){
        var db: SQLiteDatabase = writableDatabase

        var values = ContentValues()
        values.put(KEY_ID, location.id)
        values.put(KEY_LOCATION_NAME, location.name)
        values.put(KEY_LOCATION_LONGITUDE, location.longitude)
        values.put(KEY_LOCATION_LATITUDE, location.latitude)
        values.put(KEY_LOCATION_DETAILSLINK, location.detailsLink)

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    //READ
    fun readALocation(id: Int): Location {
        var db: SQLiteDatabase = writableDatabase

        // cursor is for reading from database, it knows how to fetch data
        var cursor: Cursor = db.query(
            TABLE_NAME,
            arrayOf(
                KEY_ID,
                KEY_LOCATION_NAME,
                KEY_LOCATION_LONGITUDE,
                KEY_LOCATION_LATITUDE,
                KEY_LOCATION_DETAILSLINK
            ),
            "$KEY_ID=?",
            arrayOf(id.toString()),
            null,
            null,
            null,
            null
        )

        if (cursor != null)
            cursor.moveToFirst()

        var location = Location()
        location.id = cursor.getString(cursor.getColumnIndex(KEY_ID))
        location.name = cursor.getString(cursor.getColumnIndex(KEY_LOCATION_NAME))
        location.longitude = cursor.getDouble(cursor.getColumnIndex(KEY_LOCATION_LONGITUDE))
        location.latitude = cursor.getDouble(cursor.getColumnIndex(KEY_LOCATION_LATITUDE))
        location.detailsLink = cursor.getString(cursor.getColumnIndex(KEY_LOCATION_DETAILSLINK))

        return location
    }

    fun readLocations(): ArrayList<Location>{
        var db: SQLiteDatabase = readableDatabase
        var list: ArrayList<Location> = ArrayList()

        var selectAll = "SELECT * FROM $TABLE_NAME"

        var cursor: Cursor = db.rawQuery(selectAll, null)

        if(cursor.moveToFirst()){
            do{
                var location = Location()
                location.id = cursor.getString(cursor.getColumnIndex(KEY_ID))
                location.name = cursor.getString(cursor.getColumnIndex(KEY_LOCATION_NAME))
                location.longitude = cursor.getDouble(cursor.getColumnIndex(KEY_LOCATION_LONGITUDE))
                location.latitude = cursor.getDouble(cursor.getColumnIndex(KEY_LOCATION_LATITUDE))
                location.detailsLink = cursor.getString(cursor.getColumnIndex(KEY_LOCATION_DETAILSLINK))

                list.add(location)

            } while (cursor.moveToNext())
        }
        return list
    }

    //UPDATE

    fun updateLocation(location: Location): Int{
        var db: SQLiteDatabase = writableDatabase

        var values = ContentValues()
        values.put(KEY_ID, location.id)
        values.put(KEY_LOCATION_NAME, location.name)
        values.put(KEY_LOCATION_LONGITUDE, location.longitude)
        values.put(KEY_LOCATION_LATITUDE, location.latitude)
        values.put(KEY_LOCATION_DETAILSLINK, location.detailsLink)

        return db.update(TABLE_NAME, values, "$KEY_ID=?", arrayOf(location.id.toString()))
    }

    //DELETE

    fun deleteLocation(location: Location){
        var db: SQLiteDatabase = writableDatabase
        db.delete(TABLE_NAME, "$KEY_ID=?", arrayOf(location.id.toString()))

        db.close()
    }

    fun getLocationsCount(): Int{
        var db: SQLiteDatabase = readableDatabase
        var countQuery = "SELECT * FROM $TABLE_NAME"
        var cursor: Cursor = db.rawQuery(countQuery, null)

        return cursor.count
    }
}



















