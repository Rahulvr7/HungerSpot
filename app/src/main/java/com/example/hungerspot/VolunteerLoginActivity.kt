package com.example.hungerspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.*

class VolunteerLoginActivity : AppCompatActivity() {

    lateinit var reffsfinal: DatabaseReference;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_login);
        var hvalue: String? = null
        var kvalue: String? = null;

        var btnslogin = findViewById<Button>(R.id.btnloginvoluntid);
        var usernamess = findViewById<EditText>(R.id.usernamevoluntidl);
        var passwordss = findViewById<EditText>(R.id.passwordvoluntidl);

        var donthaveacnt = findViewById<TextView>(R.id.donthaveacntid);
        donthaveacnt.setOnClickListener {
            var intent5 = Intent(this, VolunteerRegisterActivity::class.java);
            startActivity(intent5);
        }


        btnslogin.setOnClickListener {

            val reffsforpincode = FirebaseDatabase.getInstance().getReference("Volunteer");
            reffsforpincode.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (h in snapshot.children) {
                        val reffs2 = FirebaseDatabase.getInstance().getReference("Volunteer").child(h.key.toString());
                        reffs2.addValueEventListener(object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {}
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (k in snapshot.children) {
                                    if (usernamess.text.toString() == k.key.toString() + "@gmail.com") {
                                        hvalue = h.key.toString();
                                        kvalue = k.key.toString();
                                        Log.i("hvalue and kvalue are :", hvalue + " " + kvalue)
                                        reffsfinal = FirebaseDatabase.getInstance().getReference("Volunteer").child(h.key.toString()).child(k.key.toString())
                                        verifications(hvalue!!, kvalue!!, usernamess.text.toString(), passwordss.text.toString());
                                        break;
                                    }
                                }
                            }

                        })


                    }
                }

            })


        }
    }

    fun verifications(Hvalue: String, Kvalue: String, userid: String, pswd: String) {


        val reffs = Hvalue.let { it1 -> Kvalue.let { it2 -> FirebaseDatabase.getInstance().getReference("Volunteer").child(it1).child(it2) } };

        var userinfo = userid;
        var passwordinfo = pswd;
        val emailsplitter2 = userinfo.indexOf("@");
        val finalstring2 = userinfo.substring(0, emailsplitter2);




        reffs.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                var userfromdb: String? = null;
                var pswdfromdb: String? = null;
                var pincodes: String? = null;

                for (h in dataSnapshot.children) {
                    if (h.key.toString() == "email") {
                        userfromdb = h.value.toString();

                    } else if (h.key.toString() == "password") {
                        pswdfromdb = h.value.toString();
                    } else if (h.key.toString() == "pincode") {
                        pincodes = h.value.toString()
                    }
                }
                val username = dataSnapshot.child("name").value.toString();

                if (userinfo == userfromdb && passwordinfo == pswdfromdb) {

                    val user = User(Kvalue, username, Hvalue,"Volunteer");
                    val sessionManagement = SessionManagment();
                    sessionManagement.SessionManagement2(this@VolunteerLoginActivity);
                    if (user != null) {
                        sessionManagement.saveSession(user)
                    };
                    movetomainactivity();


                } else {

                    Toast.makeText(applicationContext, "userid or password is incorrect", Toast.LENGTH_SHORT).show();

                }


            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }
    override fun onStart() {
        super.onStart();

//        val sessionManagement = SessionManagment();
//        sessionManagement.SessionManagement2(this@VolunteerLoginActivity);
//        var useridsss=sessionManagement.getSession();
//        val ff="||";
//        val list= useridsss?.split(ff);
//        val userid= list?.get(0);
//        val pincode= list?.get(1);
//        val typesofuser= list?.get(2);
//
//        if (userid != null) {
//            Log.i("idss",userid)
//        };
//
//        if(userid!=" "  && typesofuser=="Volunteer"){
//
//
//            movetomainactivity();
//
//        }
    }
    fun movetomainactivity(){
        val intent:Intent= Intent(this,DonorMainActivity::class.java);
        startActivity(intent);


    }
}