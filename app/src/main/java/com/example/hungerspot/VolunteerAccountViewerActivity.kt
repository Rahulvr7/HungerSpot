package com.example.hungerspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class VolunteerAccountViewerActivity : AppCompatActivity() {

    var nameofddd:String?=null;
    var myname:String?=null;



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_account_viewer)

        var namevolunt=findViewById<TextView>(R.id.namevoluntid2);
        var mailidvolunt=findViewById<TextView>(R.id.Mailvoluntid2);
        var gendervolunt=findViewById<TextView>(R.id.gendervoluntid2);
        var dobvolunt=findViewById<TextView>(R.id.dobvoluntids2);
        var addressvolunt=findViewById<TextView>(R.id.addressvoluntid2);
        var landmarkvolunt=findViewById<TextView>(R.id.landmarkvoluntid2);
        var phnovolunt=findViewById<TextView>(R.id.phnovoluntid2);
        var pincodevolunt=findViewById<TextView>(R.id.pincodevoluntid2);

        var btnaccept=findViewById<Button>(R.id.btnforacceptid);
        var btnreject=findViewById<Button>(R.id.btnforrejectid);
        var volunterdp=findViewById<ImageView>(R.id.volunteerdpids);
        var ratingtxtl=findViewById<TextView>(R.id.ratingavgid2);

        this.setTitle("Volunteer Details");


        val sessionManagement = SessionManagment();
        sessionManagement.SessionManagement2(this) ;
        var useridsss=sessionManagement.getSession();
        val ff="||";
        val list= useridsss?.split(ff);
        val userid= list?.get(0);
        val pincode= list?.get(1);
        val typesofuser= list?.get(2);

        var temprate:Float=0F;
        var tempnof:Int=0;


        val intents3=intent;
        val idofvolunt=intents3.getStringExtra("volunteerdetails");
        val idofdishes=intents3.getStringExtra("volunteerdetails2");

        var refforrating=FirebaseDatabase.getInstance().getReference("Volunteer").child(pincode.toString()).child(idofvolunt.toString()).child("Ratings@@");
        refforrating.addValueEventListener(object :ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.key.toString()=="Ratings@@"){
                    for (h in snapshot.children){
                        if(h.key.toString()=="avgrating"){
                            temprate=h.value.toString().toFloat()
                        }else if(h.key.toString()=="noofclient"){
                            tempnof=h.value.toString().toInt();
                        }
                    }
                }else{
                    ratingtxtl.text="Not Yet Rated";
                }

            }

        })
        Handler().postDelayed({
            Log.i("finalshowdown",temprate.toString()+tempnof.toString());
            tempnof.toFloat();
            var avgfinder=temprate/tempnof;
            ratingtxtl.text=avgfinder.toString();

        },2000)


        var reffs22 =
                FirebaseDatabase.getInstance().getReference("Volunteer").child(pincode.toString()).child(idofvolunt.toString()).child("Mydp");
        reffs22.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.i("path2223s","dfdfdfdfdfdfdfdf")

                for (h in snapshot.children){
                    Log.i("path222s",h.key.toString())
                    if (h.key.toString()=="mydp"){
                        var imgurl=h.value.toString()
                        Picasso.get().load(imgurl).into(volunterdp);
                    }
                }
            }
        })

        var reffs= FirebaseDatabase.getInstance().getReference("Volunteer").child(pincode.toString()).child(idofvolunt.toString());
        reffs.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                for (h in snapshot.children){
                    if(h.key.toString()=="name"){
                        namevolunt.text=h.value.toString();
                    }else if(h.key.toString()=="email"){
                        mailidvolunt.text=h.value.toString();
                    }else if(h.key.toString()=="gender"){
                        gendervolunt.text=h.value.toString();
                    }else if(h.key.toString()=="dob"){
                        dobvolunt.text=h.value.toString();
                    }else if(h.key.toString()=="address"){
                        addressvolunt.text=h.value.toString();
                    }else if(h.key.toString()=="landmark"){
                        landmarkvolunt.text=h.value.toString();
                    }else if(h.key.toString()=="phno"){
                        phnovolunt.text=h.value.toString();
                    }else if(h.key.toString()=="pincode"){
                        pincodevolunt.text=h.value.toString();
                    }
                }
            }

        })

        val refforaccept=FirebaseDatabase.getInstance().getReference("Donor").child(pincode.toString()).child(userid.toString()).child("Accepts").push();
        val reffsfortemp=FirebaseDatabase.getInstance().getReference("Volunteer").child(pincode.toString()).child(idofvolunt.toString());

        val reffsfordonorname=FirebaseDatabase.getInstance().getReference("Donor").child(pincode.toString()).child(userid.toString());
        reffsfordonorname.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                for(h in snapshot.children){
                    if (h.key.toString()=="name"){
                        nameofddd=h.value.toString();

                    }
                }
            }
        })
        val reffss=FirebaseDatabase.getInstance().getReference("Volunteer").child(pincode.toString()).child(userid.toString()).child("name");
        reffss.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                myname=snapshot.value.toString();
            }

        })

        btnaccept.setOnClickListener{

            processdetails(idofdishes.toString(),userid.toString(),pincode.toString(),idofvolunt.toString());

            reffsfortemp.addValueEventListener(object:ValueEventListener{
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(h in snapshot.children){
                        if(h.key.toString()=="name"){
                            acceptdetails(h.value.toString(),idofdishes.toString(),idofvolunt.toString(),refforaccept);
                        }
                    }
                }

            })
            val reffss3=FirebaseDatabase.getInstance().getReference("Donor").child(pincode.toString()).child(userid.toString()).child("Requests");
            reffss3.addValueEventListener(object:ValueEventListener{
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (h in snapshot.children){
                        for(k in h.children){
                            if (k.key.toString()=="idofdishes"){
                                Log.i("abdekh",k.key.toString());
                                if(k.value.toString()==idofdishes.toString()){
                                    reffss3.child(h.key.toString()).removeValue();

                                }
                            }
                        }

                    }
                }

            })
            val userss= myname?.let { it1 -> User(userid.toString(), it1, pincode.toString(),"Donor") };
            Log.i("auths",userid.toString());


            val sessionManagement = SessionManagment();
            sessionManagement.SessionManagement2(this@VolunteerAccountViewerActivity);
            if (userss != null) {
                sessionManagement.saveSession(userss)
            };
            val intent: Intent = Intent(this@VolunteerAccountViewerActivity,DonorMainActivity::class.java);
            startActivity(intent);
            Log.i("authsafter",userid.toString());


        }
        btnreject.setOnClickListener{
            val reffss3=FirebaseDatabase.getInstance().getReference("Donor").child(pincode.toString()).child(userid.toString()).child("Requests");
            reffss3.addValueEventListener(object:ValueEventListener{
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (h in snapshot.children){
                        for(k in h.children){
                            if (k.key.toString()=="idofdishes"){
                                Log.i("abdekh",k.key.toString());
                                if(k.value.toString()==idofdishes.toString()){
                                    reffss3.child(h.key.toString()).removeValue();

                                }
                            }
                        }

                    }
                }

            })

        }

    }
    fun acceptdetails(nameofvo1:String?,idofdishe1:String?,idofvo1:String?,reffsforac:DatabaseReference){
        var nameofvo=nameofvo1;
        val details=accepts(idofdishe1.toString(),idofvo1.toString(),nameofvo1);
        reffsforac.setValue(details).addOnCompleteListener {
            Toast.makeText(this,"Accecpted",Toast.LENGTH_SHORT).show();
        }
    }
    fun processdetails(idofdis:String?,useri:String?,pinc:String?,idofvo:String?){
        Log.i("bhaiiiii",nameofddd.toString());
        finalprocessupload(idofdis,useri,nameofddd,pinc,idofvo);
    }


    fun finalprocessupload(idofdis: String?,useri:String?,nameofdonor:String?,pinc:String?,idofvo: String?){
        val acceptpost=accepts(idofdis.toString(),useri.toString(),nameofdonor);
        val voluntprocess=FirebaseDatabase.getInstance().getReference("Volunteer").child(pinc.toString()).child(idofvo.toString()).child("Processing").push();
        voluntprocess.setValue(acceptpost).addOnCompleteListener {

        }
    }

}
class accepts(var idofdish:String?,var idofvolunt:String?,var nameofvolunt:String?);

class processss(var idofdishe:String?,var idofdonor:String?,var nameofdonor:String?);