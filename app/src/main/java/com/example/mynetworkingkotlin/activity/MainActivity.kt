package com.example.baseactivity.activity

import android.os.AsyncTask
import android.os.AsyncTask.execute
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.telecom.Call
import android.text.TextUtils
import android.util.AndroidException
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.example.myapplication.adapter.CustomAdapter
import com.example.myapplication.model.Player
import com.example.myapplication.model.PlayerDatas
import com.example.mynetworkingkotlin.R
import com.example.mynetworkingkotlin.helper.ServerUrl
import com.example.myrecyclerviewpinterest.helper.ApiService
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.RequestParams
import com.loopj.android.http.TextHttpResponseHandler
import cz.msebera.android.httpclient.Header
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers

import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.reflect.Member
import java.net.HttpCookie
import java.net.HttpURLConnection
import java.net.URL
import java.util.stream.Stream
import javax.security.auth.callback.Callback

open class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView();

        //apiUsingAsyncTask()   // bu yaxshimas
        //apiUsingOkHttp()
         //apiUsingAsyncHttp()
        //apiUsingVolley()    // ishlamadi ku
        // apiUsingFuel()
       // apiUsingRetrofit()


       /* val players = prepareMemberList()
        refreshAdapter(players)*/
    }


    private fun initView() {
        recyclerView.layoutManager = GridLayoutManager(this, 1)

    }

    private fun refreshAdapter(items: List<Player>) {
        val adapter = CustomAdapter(this, items)
        recyclerView.adapter = adapter
    }

    fun fireToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    fun processWithResponse(datas: PlayerDatas) {
        progressBar.visibility = View.VISIBLE
        if (datas == null) return
        val tennisList = datas.data
        val message = datas.message

        if (tennisList != null) refreshAdapter(tennisList)
        if (!TextUtils.isEmpty(message)) fireToast(message)
    }



    //---------------------------------//  Network api here  //-----------------------------------------//
    private fun apiUsingAsyncTask() {
        progressBar.visibility = View.VISIBLE
        LoadUsingAsyncTask().execute(ServerUrl.COMMON_BASE_URL)
    }


    private fun apiUsingOkHttp() {
        progressBar.visibility = View.VISIBLE

        val client = OkHttpClient()
        val request = okhttp3.Request.Builder().url(ServerUrl.COMMON_BASE_URL).build()
        client.newCall(request).enqueue(object : Callback, okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: IOException) {
                Log.d("mytest", "onFailure" + e.toString())
            }

            override fun onResponse(call: okhttp3.Call, response: Response) {
                val responeData = response?.body?.string()
                val datas = Gson().fromJson(responeData, PlayerDatas::class.java)
                runOnUiThread {
                    processWithResponse(datas)
                }
                Log.d("mytest", "onSuccess" + responeData)
        }
        /*     if (tennisList != null)refreshAdapter(tennisList)
             if (!TextUtils.isEmpty(message)) fireToast(message)*/
    })
    }



/*    private fun apiUsingVolley() {
        progressBar.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(this)
        val jsonObjectRequest = JsonObjectRequest(com.android.volley.Request.Method.GET, ServerUrl.COMMON_BASE_URL, null,
                Response.Listener { response ->
                    Log.d("mytest", "onSuccess" + response.toString())
                    val datas = Gson().fromJson(response.toString(), PlayerDatas::class.java)
                    processWithResponse(datas)
                },
                Response.ErrorListener { error ->
                        Log.d("mytest", "onFailure" + error.localizedMessage)
                }
            )
        queue.add(jsonObjectRequest)


    }*/

    private fun apiUsingFuel() {
        progressBar.visibility = View.VISIBLE

        Fuel.get(ServerUrl.COMMON_BASE_URL).responseString { request, response, result ->
            result.fold({ d ->
                Log.d("mytest", "onSuccess" + result.get())
                val datas = Gson().fromJson(result.get(), PlayerDatas::class.java)
                processWithResponse(datas)
            },{
                err ->
                Log.d("mytest", "onFailure" + err.localizedMessage)

            })
        }
    }

    private fun apiUsingAsyncHttp() {
        progressBar.visibility = View.VISIBLE

        val params = RequestParams()
        val client = AsyncHttpClient()
        client.post(ServerUrl.COMMON_BASE_URL, params, object : TextHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseString: String
            ) {
                Log.d("mytest", "onSuccess" + responseString)
                val datas = Gson().fromJson(responseString, PlayerDatas::class.java)
                processWithResponse(datas)
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?,
                throwable: Throwable?
            ) {
                Log.d("mytest", "onFailure" + responseString)
            }
        })

    }

    private fun apiUsingRetrofit() {

        progressBar.visibility = View.VISIBLE
        ApiService.create().loadDatas()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subcribe(
                { result ->
                    Log.d("mytest", "onSuccess" + result)
                    processWithResponse(result)
                },
                { error ->
                    Log.d("mytest", "onFailure" + error)

                }
            )


    }

    inner class LoadUsingAsyncTask(): AsyncTask<String,String,String>() {
        override fun doInBackground(vararg params: String?): String {
            try {
                val url = URL(params[0])
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.connectTimeout = 6000
                urlConnection.readTimeout = 6000
                var inString = streamToString(urlConnection.inputStream) // jeladigan malumot Stream holatida keladi bu metod bizga Stringga aylantiradi
                return inString
            }
            catch (ex: Exception){
                return ""
            }
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            if (!TextUtils.isEmpty(result)){
                val datas = Gson().fromJson(result, PlayerDatas::class.java)
                processWithResponse(datas)
            }
        }


    }


    fun streamToString(inputStream: InputStream): String{
        val bufferReader = BufferedReader(InputStreamReader(inputStream))
        var line: String
        var result = ""

        try {
            do {
                line= bufferReader.readLine()
                if (line != null) {
                    result += line
                }
            }while (line != null)
            inputStream.close()
        }catch(ex: Exception){
        }
        return result
    }
}

    /*private fun prepareMemberList(): ArrayList<Player> {
        val members = ArrayList<Player>()
        for (i in 0..29) {
            members.add(Player("Makhmudov" + i, "Abdulloh" + i))
        }
        return members
    }*/



