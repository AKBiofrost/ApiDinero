package com.portafolio.test_api_kotlin

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.example.Dinero
import com.example.exampleapikotlin.interfaz.APIService
import com.example.exampleapikotlin.interfaz.Comments
import com.portafolio.test_api_kotlin.adapter.ListElement
import com.portafolio.test_api_kotlin.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.Response
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.portafolio.test_api_kotlin.adapter.CustomAdapter
import com.portafolio.test_api_kotlin.adapter.ItemsViewModel
import com.portafolio.test_api_kotlin.adapter.presentacion
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //private val BASE_URL = "https://jsonplaceholder.typicode.com"
    private val BASE_URL = "https://api.frankfurter.app"

    private var valor="dinero"
    private var moneda="USD"

    val arrayList = ArrayList<presentacion>()//Creating an empty arraylist

   // val element: List<ListElement> = TODO()
    //https://www.frankfurter.app/docs/


    private val TAG = "Kotlin"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun init() {

        // getting the recyclerview by its id
        val recyclerview = findViewById<RecyclerView>(R.id.recycler_activity_main)

        // this creates a vertical layout Manager
        recyclerview.layoutManager = LinearLayoutManager(this)

        // ArrayList of class ItemsViewModel
        val data = ArrayList<ItemsViewModel>()

        // This loop will create 20 Views containing
        // the image with the count of view
        for (i in arrayList) {
            Log.e(TAG, "init: "+i.moneda)
            data.add(ItemsViewModel(R.drawable.ic_home_black_24dp, i.moneda+":" + i.valor))
        }

        // This will pass the ArrayList to our Adapter
        val adapter = CustomAdapter(data)

        // Setting the Adapter with the recyclerview
        recyclerview.adapter = adapter

    }

    /*-------------------------------------------------------------------------------------------*/
    override fun onStart() {
        super.onStart()
        getAllComments()
        init( )
    }

    private fun getAllComments() {


        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(APIService::class.java)

        api.getComments().enqueue(object : Callback<Dinero> {
            override fun onResponse(
                call: Call<Dinero>,
                response: Response<Dinero>
            ) {
                if (response.isSuccessful) {
                    val resCode = response.code()
                    Log.e(TAG, "code: $resCode")
                    Log.e(TAG, "response: $response")
                    val i = 0
                    Log.e(TAG, "-------------------------------------------------------")
                    Log.i(TAG, "-Monto: " + response.body()?.amount)
                    Log.i(TAG, "-Fecha: " + response.body()?.date)
                    Log.i(TAG, "-Base: " + response.body()?.base)
                    Log.i(TAG, "-TASA USD: " + response.body()?.rates?.USD)


                    valor=response.body()?.rates?.USD.toString()

                    arrayList.add(presentacion(response.body()?.rates?.BGN.toString(),"BGN"))
                    arrayList.add(presentacion(response.body()?.rates?.USD.toString(),"USD"))
                    Log.e(TAG, "-------------------------------------------------------")
                    /*
                     for (i in response.body()?.indices!!) {
                         Log.i(TAG, "-$i " + response.body()?.get(i)?.title)

                     }
                    */

                }

            }

            override fun onFailure(call: Call<Dinero>, t: Throwable) {

            }
        })
    }



}