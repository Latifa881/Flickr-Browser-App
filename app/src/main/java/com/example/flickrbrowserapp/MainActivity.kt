package com.example.flickrbrowserapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

//val API_KEY="fb68d28f6932960f3e6316e21de3495c"
//var tags="dogs"
//val URL="https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=$API_KEY&tags=$tags&format=json&nojsoncallback=1&extras=url_s"
class MainActivity : AppCompatActivity() {

    lateinit var rvMain: RecyclerView
    lateinit var etSearch: EditText
    lateinit var btSearch: Button
    lateinit var pbProgress: ProgressBar
    val detailsArray = ArrayList<Details>()
    val API_KEY = "fb68d28f6932960f3e6316e21de3495c"
    var tags = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvMain = findViewById(R.id.rvMain)
        etSearch = findViewById(R.id.etSearch)
        btSearch = findViewById(R.id.btSearch)
        pbProgress = findViewById(R.id.pbProgress)
        rvMain.adapter = RecyclerViewAdapter(
            detailsArray,
            findViewById(R.id.ivImage),
            rvMain,
            findViewById(R.id.linearLayout)
        )
        rvMain.layoutManager = LinearLayoutManager(this)
        btSearch.setOnClickListener {
            val text = etSearch.text.toString()
            if (text.isNotEmpty()) {
                tags = text
                etSearch.text.clear()
                detailsArray.clear()
                Log.d("Constants.tags", tags)
                requestApi()

            } else {
                Toast.makeText(this@MainActivity, "Search field is empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // Hide Keyboard
        val imm = ContextCompat.getSystemService(this, InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }

    fun requestApi() {
        CoroutineScope(Dispatchers.IO).launch {
            updateStatus(0)
            val data = async {
                fetchData()
            }.await()

            if (data.isNotEmpty()) {
                setDataToRV(data)
                updateStatus(1)
            } else {
                Toast.makeText(this@MainActivity, "Something Went wrong", Toast.LENGTH_SHORT)
                    .show()
                Log.d("OOPs: ", "Something wrong")
            }

        }
    }

    private fun fetchData(): String {

        var response = ""
        try {

            var URL =
                "https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=$API_KEY&tags=$tags&format=json&nojsoncallback=1&extras=url_s"
            Log.d("Constants.URL", URL)
            response = URL(URL).readText(Charsets.UTF_8)

        } catch (e: Exception) {
            println("Error $e")

        }
        return response

    }

    private suspend fun setDataToRV(data: String) {
        withContext(Dispatchers.Main)
        {

            val jsonObject = JSONObject(data)
            val photos = jsonObject.getJSONObject("photos")
            val photo = photos.getJSONArray("photo")
            if (photo.length() <= 0) {
                Toast.makeText(this@MainActivity, "No Images Found for $tags", Toast.LENGTH_SHORT)
                    .show()
            } else {

                for (i in 0 until photo.length()) {
                    val title = photo.getJSONObject(i).getString("title")
                    val imageLink = photo.getJSONObject(i).getString("url_s")
                    detailsArray.add((Details(imageLink, title)))
                    Log.d("FLIKER title: ", title)
                    Log.d("FLIKER imageLink: ", imageLink)

                }

            }
            rvMain.adapter!!.notifyDataSetChanged()
            
        }

    }

    private suspend fun updateStatus(state: Int) {
        withContext(Dispatchers.Main) {
            when (state) {
                0 -> {
                    pbProgress.visibility = View.VISIBLE
                    rvMain.visibility = View.GONE
                }
                1 -> {
                    pbProgress.visibility = View.GONE
                    rvMain.visibility = View.VISIBLE
                }
            }
        }
    }
}