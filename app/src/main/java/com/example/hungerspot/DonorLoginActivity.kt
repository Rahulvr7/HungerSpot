package com.example.hungerspot

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class DonorLoginActivity : AppCompatActivity() {

lateinit var reffsfinal:DatabaseReference;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_login);
        var hvalue:String?=null
        var kvalue:String?=null;

        var btnslogin=findViewById<Button>(R.id.btnloginvoluntid);
        var usernamess=findViewById<EditText>(R.id.usernamevoluntidl);
        var passwordss=findViewById<EditText>(R.id.passwordvoluntidl);

        var didnthaveacnt=findViewById<TextView>(R.id.donthaveacnt);



        didnthaveacnt.setOnClickListener {
            var intent3=Intent(this,DonorRegisterActivity::class.java);
            startActivity(intent3);
        }

        btnslogin.setOnClickListener {

            val reffsforpincode=FirebaseDatabase.getInstance().getReference("Donor");
            reffsforpincode.addValueEventListener(object:ValueEventListener{
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (h in snapshot.children){
                        Log.i("checksss",h.key.toString());
                        val reffs2=FirebaseDatabase.getInstance().getReference("Donor").child(h.key.toString());
                        reffs2.addValueEventListener(object:ValueEventListener{
                            override fun onCancelled(error: DatabaseError) {}
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (k in snapshot.children){
                                    Log.i("checkss2",k.key.toString());
                                    if(usernamess.text.toString()==k.key.toString()+"@gmail.com"){
                                        hvalue=h.key.toString();
                                        kvalue=k.key.toString();
                                        Log.i("hvalue and kvalue are :",hvalue+" "+kvalue)
                                        reffsfinal= FirebaseDatabase.getInstance().getReference("Donor").child(h.key.toString()).child(k.key.toString())
                                        verifications(hvalue!!, kvalue!!,usernamess.text.toString(),passwordss.text.toString());
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

    fun verifications(Hvalue:String,Kvalue:String,userid:String,pswd:String){



        val reffs = Hvalue.let { it1 -> Kvalue.let { it2 -> FirebaseDatabase.getInstance().getReference("Donor").child(it1).child(it2) } };

        var userinfo=userid;
        var passwordinfo=pswd;
        val emailsplitter2=userinfo.indexOf("@");
        val finalstring2=userinfo.substring(0,emailsplitter2);




        reffs.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                var userfromdb:String?=null;
                var pswdfromdb:String?=null;
                var pincodes:String?=null;

                for(h in dataSnapshot.children){
                    if(h.key.toString()=="email"){
                        userfromdb=h.value.toString();

                    }
                    else if(h.key.toString()=="password"){
                        pswdfromdb=h.value.toString();
                    }else if(h.key.toString()=="pincode"){
                        pincodes=h.value.toString()
                    }
                }
                val username=dataSnapshot.child("name").value.toString();

                if(userinfo==userfromdb && passwordinfo==pswdfromdb){

                    val user= User(Kvalue,username, Hvalue,"Donor");
                    val sessionManagement = SessionManagment();
                    sessionManagement.SessionManagement2(this@DonorLoginActivity);
                    sessionManagement.saveSession(user);
                    movetomainactivity();


                }else{

                    Toast.makeText(applicationContext,"userid or password is incorrect",Toast.LENGTH_SHORT).show();

                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }


    fun movetomainactivity(){
        val intent:Intent= Intent(this,DonorMainActivity::class.java);
        startActivity(intent);


    }
}