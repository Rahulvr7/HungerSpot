package com.example.hungerspot

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlin.math.log

class ContentDetailVolunteerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_details_volunteer)

        var fromtime:String?=null;
        var tilltime:String?=null;
        var notes:String?=null;
        var address:String?=null;
        var landmark:String?=null;
        var usernamesn:String?=null;
        var useridsn:String?=null;


        var nameforvolunt:String?=null;
        var phnoforvolunt:String?=null;

        var myname:String?=null;




        var uploader1=findViewById<TextView>(R.id.uploadertextid2);
        var fromtime1=findViewById<TextView>(R.id.fromtextid2);
        var tilltime1=findViewById<TextView>(R.id.Tilltextid2);
        var notes1=findViewById<TextView>(R.id.notestextid2);
        var address1=findViewById<TextView>(R.id.addresstextid2);
        var landmark1=findViewById<TextView>(R.id.landmarktextid2);
        var dishesname=findViewById<TextView>(R.id.dishesnameid);

        var btnsreq=findViewById<Button>(R.id.btnrequestid);

        var imgdishes=findViewById<ImageView>(R.id.imgdishesid);







        val sessionManagement = SessionManagment();
        sessionManagement.SessionManagement2(this) ;
        var useridsss=sessionManagement.getSession();
        val ff="||";
        val list= useridsss?.split(ff);
        val userid= list?.get(0);
        val pincode= list?.get(1);
        val typesofuser= list?.get(2);

        this.setTitle("Dishes detail");



        val intents2=intent;
        val idofcontent=intents2.getStringExtra("productid");
        val namofcontent=FirebaseDatabase.getInstance().getReference("Donor").child(pincode.toString()).child("MyContribution@@").child(idofcontent.toString());

        namofcontent.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                for (h in snapshot.children){
                    if(h.key.toString()=="notes"){
                        dishesname.text=h.value.toString();
                    }
                    else if(h.key.toString()=="imgurl"){
                        Picasso.get().load(h.value.toString()).into(imgdishes);
                    }
                }
            }

        })







        Log.i("chhh",idofcontent.toString()+userid.toString()+pincode.toString()+typesofuser.toString());

        val reffs=FirebaseDatabase.getInstance().getReference("Donor").child(pincode.toString()).child("MyContribution@@").child(idofcontent.toString());

        reffs.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                for(h in snapshot.children){
                    if(h.key.toString()=="usernameforuploads"){
                        usernamesn=h.value.toString();
                        uploader1.text=usernamesn;

                    }else if(h.key.toString()=="timefrom"){
                        fromtime=h.value.toString();
                        fromtime1.text=fromtime;

                    }else if(h.key.toString()=="timetill"){
                        tilltime=h.value.toString();
                        tilltime1.text=tilltime;



                    }else if(h.key.toString()=="notes"){
                        notes=h.value.toString();
                        notes1.text=notes;



                    }else if(h.key.toString()=="address"){
                        address=h.value.toString();
                        address1.text=address;



                    }else if(h.key.toString()=="landmark"){
                        landmark=h.value.toString();
                        landmark1.text=landmark;


                    }else if(h.key.toString()=="useridforuploads"){
                        useridsn=h.value.toString();
                    }

                }
            }

        })



        uploader1.setOnClickListener {
            val intss=Intent(this,DonorAccountViewerActivity::class.java);
            intss.putExtra("donordetails",useridsn.toString());
            Log.i("telln",useridsn.toString())
            startActivity(intss)
        }

        val reffss=FirebaseDatabase.getInstance().getReference("Volunteer").child(pincode.toString()).child(userid.toString()).child("name");
        reffss.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                myname=snapshot.value.toString();
                Toast.makeText(this@ContentDetailVolunteerActivity,myname.toString(),Toast.LENGTH_SHORT).show();
            }

        })
        btnsreq.setOnClickListener{

            var mydetailss=FirebaseDatabase.getInstance().getReference("Volunteer").child(pincode.toString()).child(userid.toString());
            mydetailss.addValueEventListener(object:ValueEventListener{
                override fun onCancelled(error: DatabaseError) {}
                @SuppressLint("LongLogTag")
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(h in snapshot.children){
                        if(h.key.toString()=="name"){
                            nameforvolunt=h.value.toString();
                            var reffs=FirebaseDatabase.getInstance().getReference("Donor").child(pincode.toString()).child(useridsn.toString()).child("Requests").push();
                            var detailsofvolunt=requests(nameforvolunt,userid,idofcontent);
                            reffs.setValue(detailsofvolunt).addOnCompleteListener {
                                btnsreq.text="UnSend";
                                Log.i("userids",userid.toString())
                                Toast.makeText(this@ContentDetailVolunteerActivity,"Requested "+userid.toString(),Toast.LENGTH_SHORT).show();
                                val userss= myname?.let { it1 -> User(userid.toString(), it1, pincode.toString(),"Volunteer") };
                                val sessionManagement = SessionManagment();
                                sessionManagement.SessionManagement2(this@ContentDetailVolunteerActivity);
                                if (userss != null) {
                                    sessionManagement.saveSession(userss)
                                };
                                val intent:Intent= Intent(this@ContentDetailVolunteerActivity,DonorMainActivity::class.java);
                                startActivity(intent);
                                Log.i("authsafter contentdetail",userid.toString());
                            }
                        }
                    }
                }
            })
        }
    }
}

class requests(var names:String?,var ids2:String?,var idofdishes:String?);