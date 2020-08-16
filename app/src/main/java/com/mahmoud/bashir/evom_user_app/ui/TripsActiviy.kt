package com.mahmoud.bashir.evom_user_app.ui

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mahmoud.bashir.evom_user_app.R
import com.mahmoud.bashir.evom_user_app.Storage.SharedPrefranceManager
import com.mahmoud.bashir.evom_user_app.pojo.SendRequest.trip_details_Model
import io.paperdb.Paper
import java.util.*
import kotlin.collections.ArrayList


class TripsActiviy : AppCompatActivity() {

    lateinit var toolbar : Toolbar
    lateinit var rec_trips : RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var  recAdpt : RecyclerAdapter

    lateinit var reference : DatabaseReference
    lateinit var auth : FirebaseAuth
    lateinit var userPhone : String
    lateinit var mlist : ArrayList<trip_details_Model>
    lateinit var tripModel : trip_details_Model
    lateinit var rel_big : RelativeLayout


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trips__activiy)

        Paper.init(this)

        rel_big = findViewById(R.id.rel_big)
        if (Paper.book().read<String>("language").equals("ar")){
            rel_big.setBackgroundResource(R.drawable.back_common_color)
        }else if (Paper.book().read<String>("language").equals("en")){
            rel_big.setBackgroundResource(R.drawable.txt_color)
        }


        rec_trips = findViewById(R.id.rec_trips)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)


        linearLayoutManager = LinearLayoutManager(this)
        rec_trips.layoutManager = linearLayoutManager

        //init firebase
        auth = FirebaseAuth.getInstance()
        reference = FirebaseDatabase.getInstance().reference.child("History")

        mlist = ArrayList<trip_details_Model>()

        userPhone = SharedPrefranceManager.getInastance(this).userPhone
        reference.child("Customers").child(userPhone).addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                for(snapshot in p0.children){
                    val date : String = snapshot.child("time").value.toString()
                    val userlat : Double = snapshot.child("dest_lat").value.toString().toDouble()
                    val userlng : Double = snapshot.child("dest_lng").value.toString().toDouble()
                    val userdestlat : Double = snapshot.child("dest_lat").value.toString().toDouble()
                    val userdestlng : Double = snapshot.child("dest_lng").value.toString().toDouble()

                    val d = date.split(" ").map {it.trim()}
                    tripModel = trip_details_Model(d[0],d[1],userlat,userlng,userdestlat,userdestlng,"30")
                    mlist.add(tripModel)

                   // Toast.makeText(this@TripsActiviy,""+userlat,Toast.LENGTH_LONG).show()

                }

                recAdpt = RecyclerAdapter(mlist,this@TripsActiviy)
                rec_trips.adapter = recAdpt
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}


class RecyclerAdapter(val mlist : ArrayList<trip_details_Model> = ArrayList<trip_details_Model>(),val context : Context) : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>()  {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val date_txt = itemView.findViewById<TextView>(R.id.date_txt)
        val time_txt = itemView.findViewById<TextView>(R.id.time_txt)
        val userplace_txt = itemView.findViewById<TextView>(R.id.userplace_txt)
        val userdestination_txt = itemView.findViewById<TextView>(R.id.userdestination_txt)
        val txt_tripPrice = itemView.findViewById<TextView>(R.id.txt_tripPrice)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = parent.inflate(R.layout.single_item_trips, false)
        return ViewHolder(inflatedView)

    }

    override fun getItemCount(): Int {
       return mlist.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tripDetailsModel = mlist.get(position)

        holder.date_txt.setText(tripDetailsModel.date)
        holder.time_txt.setText(tripDetailsModel.time)
        holder.userplace_txt.setText(getAddressName(tripDetailsModel.userplace_lat,tripDetailsModel.userplace_lng))
        holder.userdestination_txt.setText("Zag")
        holder.txt_tripPrice.setText("30")

        //Toast.makeText(context,""+tripDetailsModel.userplace_lat+" ---" +tripDetailsModel.userplace_lng,Toast.LENGTH_LONG).show()
        getAddressName(tripDetailsModel.userplace_lat,tripDetailsModel.userplace_lng)
    }

    fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }


    fun getAddressName(latitude: Double, longitude: Double): String? {

        Paper.init(context)
        var result = ""
        var lang = "en"
        if (Paper.book().read<String>("language").equals("ar")){
            lang = "ar"
        }else if (Paper.book().read<String>("language").equals("en")){
            lang = "en"
        }



            /*val mLocale = Locale(lang)
            val geocoder: Geocoder
            var fulladdress:String =""
            val addresses: List<Address>?
            geocoder = Geocoder(context, Locale.getDefault())

            addresses = geocoder.getFromLocation(latitude, longitude, 1) // Here 1 represent max location result to returned, by documents it recommended 1 to 5


            val address = addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

            val city = addresses[0].locality
            val state = addresses[0].adminArea
            val country = addresses[0].countryName
            val postalCode = addresses[0].postalCode
            val knownName = addresses[0].featureName // Only if available else return NULL
            fulladdress = city+"-"+state*/

        return ""
    }

/*
    fun getAddressName(latitude: Double, longitude: Double): String? {
        Paper.init(context)
        var result = ""
        var lang = "en"
        if (Paper.book().read<String>("language").equals("ar")){
            lang = "ar"
        }else if (Paper.book().read<String>("language").equals("en")){
            lang = "en"
        }

        val mLocale = Locale(lang)
        val geocoder = Geocoder(context, mLocale)

        // LogCat: Display language = English
        Log.i("Display language = ", "" + mLocale.getDisplayLanguage())

        // Log.i("geocoder geocoder = ", "" + geocoder.toString());
        try {
            val listAddresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)
            if (null != listAddresses && listAddresses.size > 0) {
                val _Location: String = listAddresses[0].getAddressLine(0)
                // Log.i("_Location = ", "" + _Location);

                val address: Address = listAddresses[0]
                Log.i("address = ", "" + address)
                result = address.getSubAdminArea() + "-" + address.getThoroughfare()
                Log.i("result = ", "" + result)
                //Toast.makeText(context, "Your Location  NAME is -" + result , Toast.LENGTH_LONG).show();
            }


        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }


  */
}
