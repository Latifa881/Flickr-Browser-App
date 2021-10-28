package com.example.flickrbrowserapp


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.net.URL

//val API_KEY="fb68d28f6932960f3e6316e21de3495c"
//var tags="dogs"
//val URL="https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=fb68d28f6932960f3e6316e21de3495c&tags=cat&format=json&nojsoncallback=1&extras=url_s&safe_search=1"
class MainActivity : AppCompatActivity() {

    lateinit var rvMain: RecyclerView
    lateinit var etSearch: EditText
    lateinit var btSearch: ImageView
    lateinit var pbProgress: ProgressBar
    val detailsArray = ArrayList<photoDetails>()
    val API_KEY = "fb68d28f6932960f3e6316e21de3495c"
    var tags = "cookies"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvMain = findViewById(R.id.rvMain)
        etSearch = findViewById(R.id.etSearch)
        btSearch = findViewById(R.id.ivSearch)
        pbProgress = findViewById(R.id.pbProgress)
        bottomNavigationView()
        rvMain.adapter = RecyclerViewAdapter(detailsArray, findViewById(R.id.ivImage), rvMain, findViewById(R.id.constraintLayout),this)
        rvMain.layoutManager = LinearLayoutManager(this)
        requestApi_withRetrofit()
        btSearch.setOnClickListener {
            val text = etSearch.text.toString()
            if (text.isNotEmpty()) {
                tags = text
                etSearch.text.clear()
                detailsArray.clear()
                //JSON without retrofit
                // requestApi_withoutRetrofit()
                //JSON with retrofit
                requestApi_withRetrofit()
                // Hide Keyboard
                val imm = ContextCompat.getSystemService(this, InputMethodManager::class.java)
                imm?.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)

            } else {
                Toast.makeText(this@MainActivity, "Search field is empty", Toast.LENGTH_SHORT)
                    .show()
            }
        }


    }
    fun bottomNavigationView(){
        var bottomNavigationView=findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.search -> startActivity(Intent(this, MainActivity::class.java))
                R.id.liked->startActivity(Intent(this,LikedActivity::class.java))
            }
            true
        }
    }
    fun requestApi_withRetrofit() {
        val apiInterface = APIClient.getClient()?.create(APIInterface::class.java)
        pbProgress.visibility = View.VISIBLE
        rvMain.visibility = View.GONE
        if (apiInterface != null) {

            apiInterface.getPhotos("?method=flickr.photos.search&api_key=$API_KEY&tags=$tags&format=json&nojsoncallback=1&extras=url_s&safe_search=3")
                ?.enqueue(object : Callback<Flickr?> {
                    override fun onResponse(call: Call<Flickr?>?, response: Response<Flickr?>) {
                        Log.d("TAG", response.code().toString() + "")
                        val resource: Flickr? = response.body()
                        val listOfPhotos = resource?.photos?.photo!!
                        Log.d("Photo size:", listOfPhotos.size.toString())
                        if (listOfPhotos.size == 0) {
                            Toast.makeText(
                                this@MainActivity,
                                "No Images Found for $tags",
                                Toast.LENGTH_SHORT
                            )
                        }
                        for (i in 0 until listOfPhotos.size)
                            detailsArray.add(
                                photoDetails(
                                    listOfPhotos[i].url_s.toString(),
                                    listOfPhotos[i].title.toString()
                                )
                            )
                        pbProgress.visibility = View.GONE
                        rvMain.visibility = View.VISIBLE
                        rvMain.adapter!!.notifyDataSetChanged()


                    }

                    override fun onFailure(call: Call<Flickr?>, t: Throwable?) {
                        call.cancel()
                    }
                })
        }
    }

    fun requestApi_withoutRetrofit() {
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
                "https://www.flickr.com/services/rest/?method=flickr.photos.search&api_key=$API_KEY&tags=$tags&format=json&nojsoncallback=1&extras=url_s&safe_search=3"
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
                    detailsArray.add((photoDetails(imageLink, title)))
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