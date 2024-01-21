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
import com.portafolio.test_api_kotlin.databinding.ActivityMainBinding
import com.squareup.picasso.Picasso
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.Call
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //private val BASE_URL = "https://jsonplaceholder.typicode.com"
    private val BASE_URL = "https://api.frankfurter.app"

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

    /*-------------------------------------------------------------------------------------------*/
    override fun onStart() {
        super.onStart()
        getAllComments()
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