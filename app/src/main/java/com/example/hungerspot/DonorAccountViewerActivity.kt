package com.example.hungerspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class DonorAccountViewerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_account_viewer);


        val sessionManagement = SessionManagment();
        sessionManagement.SessionManagement2(this) ;
        var useridsss=sessionManagement.getSession();
        val ff="||";
        val list= useridsss?.split(ff);
        val userid= list?.get(0);
        val pincode= list?.get(1);
        val typesofuser= list?.get(2);

        val intentsss=intent;
        val donorids=intentsss.getStringExtra("donordetails");
        this.setTitle("Donor Details");
        var uploaderdp=findViewById<ImageView>(R.id.uploaderdpid);


        var reffs223 = FirebaseDatabase.getInstance().getReference("Donor").child(pincode.toString()).child(donorids.toString()).child("Mydp");
        reffs223.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                for (h in snapshot.children){
                    Log.i("paths",h.key.toString())
                    if (h.key.toString()=="mydp"){
                        var imgurl=h.value.toString()
                        Picasso.get().load(imgurl).into(uploaderdp);
                    }
                }
            }
        })

        val reffs22=FirebaseDatabase.getInstance().getReference("Donor").child(pincode.toString()).child(donorids.toString());
        reffs22.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                for (h in snapshot.children){
                    Log.i("sssss",h.key.toString())
                    if (h.key.toString()=="name"){
                        findViewById<TextView>(R.id.nmtxtfid).text=h.value.toString();
                    }else if(h.key.toString()=="email"){
                        findViewById<TextView>(R.id.mailtxtfid).text=h.value.toString();
                    }
                    else if(h.key.toString()=="address"){
                        findViewById<TextView>(R.id.addresstxtfid).text=h.value.toString();
                    }
                    else if(h.key.toString()=="landmark"){
                        findViewById<TextView>(R.id.landmarktxtfid).text=h.value.toString();
                    }
                    else if(h.key.toString()=="phno"){
                        findViewById<TextView>(R.id.phnotxtfid).text=h.value.toString();
                    }
                    else if(h.key.toString()=="pincode"){
                        findViewById<TextView>(R.id.pincodetxtfid).text=h.value.toString();
                    }
                }
            }

        })



    }
}