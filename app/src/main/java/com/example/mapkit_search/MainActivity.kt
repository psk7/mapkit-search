package com.example.mapkit_search

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.mapview.MapView
import com.yandex.mapkit.search.*
import com.yandex.runtime.Error

const val API_KEY = "_INSERT_API_KEY_HERE_"

class MainActivity : AppCompatActivity() {
    private lateinit var searchManager: SearchManager
    private lateinit var searchSession: Session
    private val mapview: MapView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        MapKitFactory.setApiKey(API_KEY)
        MapKitFactory.initialize(this)

        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.button).setOnClickListener { geocode() }
    }

    private fun geocode() {
        /*
        mapview = (MapView)findViewById(R.id.mapview);
        mapview.getMap().move(
            new CameraPosition(new Point(55.751574, 37.573856), 11.0f, 0.0f, 0.0f),
            new Animation(Animation.Type.SMOOTH, 0),
            null);
        */

        if (API_KEY == "_INSERT_API_KEY_HERE_") {
            findViewById<TextView>(R.id.text).text = "API KEY is not set"
        } else {
            searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.ONLINE)
            val point = Point(55.734, 37.588)

            // При таком запросе первый объект в ответе будет «улица Льва Толстого, 16»
            searchSession = searchManager.submit(point, 16, SearchOptions(), object : Session.SearchListener {
                override fun onSearchResponse(response: Response) {
                    val addr = response.collection.children.firstOrNull()?.obj
                        ?.metadataContainer
                        ?.getItem(ToponymObjectMetadata::class.java)
                        ?.address

                    findViewById<TextView>(R.id.text).text = addr?.formattedAddress ?: "<NULL>"
                }

                override fun onSearchError(p0: Error) {
                    findViewById<TextView>(R.id.text).text = "onSearchError $p0"
                }
            })
        }
    }

    override fun onStop() {
        super.onStop()
        mapview?.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onStart() {
        super.onStart()
        mapview?.onStart()
        MapKitFactory.getInstance().onStart()
    }
}