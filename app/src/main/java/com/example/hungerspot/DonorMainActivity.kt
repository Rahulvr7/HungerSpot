package com.example.hungerspot


import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


class DonorMainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val sessionManagement = SessionManagment();
        sessionManagement.SessionManagement2(this);
        var useridsss=sessionManagement.getSession();
        val ff="||";
        val list= useridsss?.split(ff);
        val userid= list?.get(0);
        val pincode= list?.get(1);
        val typesofuser= list?.get(2);





            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            val navView: NavigationView = findViewById(R.id.nav_view)
            val navController = findNavController(R.id.nav_host_fragment)
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            appBarConfiguration = AppBarConfiguration(setOf(
                    R.id.nav_home2, R.id.nav_gallery, R.id.nav_slideshow,R.id.nav_mycontribution,R.id.nav_mydetail), drawerLayout)
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController);

        val navigationView = findViewById<View>(R.id.nav_view) as NavigationView
        val hView = navigationView.getHeaderView(0)


//        val nav_icons=hView2.findViewById<View>(R.id.nav_home2) as ClipData.Item;

        val nav_user_name = hView.findViewById<View>(R.id.navpageaccounternameid) as TextView
        val nav_user_type = hView.findViewById<View>(R.id.navpageaccountertypeid) as TextView
        var nav_user_dpimage= hView.findViewById<View>(R.id.navpageaccountdpid) as ImageView

        var reffsff:DatabaseReference?=null;

        if(typesofuser=="Donor") {
            reffsff = FirebaseDatabase.getInstance().getReference("Donor").child(pincode.toString()).child(userid.toString());
            nav_user_type.text="Donor";

        }else if(typesofuser=="Volunteer"){
            reffsff = FirebaseDatabase.getInstance().getReference("Volunteer").child(pincode.toString()).child(userid.toString());
            nav_user_type.text="Volunteer";
            val menu = navigationView.menu
            menu.findItem(R.id.nav_gallery).setVisible(false);
            menu.findItem(R.id.nav_mycontribution).setVisible(false);

        }


        var reffs22 =
                FirebaseDatabase.getInstance().getReference(typesofuser.toString()).child(pincode.toString()).child(userid.toString()).child("Mydp");
        reffs22.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                for (h in snapshot.children){
                    Log.i("paths",h.key.toString())
                    if (h.key.toString()=="mydp"){
                        var imgurl=h.value.toString()
                        Picasso.get().load(imgurl).into(nav_user_dpimage);
                    }
                }
            }
        })


        reffsff?.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                for (h in snapshot.children){
                    if (h.key.toString()=="name"){
                        nav_user_name.text=h.value.toString();
                    }
                }
            }
        })
    }
    override fun onBackPressed(){

        var alert=AlertDialog.Builder(this);

        alert.setMessage("Are you sure? you want to exit?").setPositiveButton("Yes"){dialog: DialogInterface?, which: Int ->
//            finish()
              val Intents=Intent(this,MainActivity::class.java);
//              Intents.putExtra("secret1","toquit")
              startActivity(Intents);

        }.setNegativeButton("Stay"){dialog:DialogInterface, which -> }
        val alertDialog: AlertDialog = alert.create()
        // Set other dialog properties
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.donor_main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

//    override fun onPre(menu: Menu?): Boolean {
//        if (menu != null) {
//            Log.i("oppmenu",menu.get(0).title.toString())
//        };
//
//        return super.onPrepareOptionsMenu(menu)
//    }


}