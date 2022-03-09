package com.example.hungerspot

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.hungerspot.ui.gallery.foodupload
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask

class VolunteerProcessActivity : AppCompatActivity() {
    var REQUEST_PHONE_CALL=1;
    var phns:String?=null;
    lateinit var filepath:Uri
    lateinit var filepath2:Uri
    lateinit var imgurl:String
    lateinit var imgurl2:String

    var imgid1id: ImageView? =null;
     var imgid2id:ImageView?=null;
    var myname:String?=null;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_process);

        imgid1id=findViewById<ImageView>(R.id.imgid1);
        imgid2id=findViewById<ImageView>(R.id.imgid2);
        var btnfiledsubmit=findViewById<Button>(R.id.btnsubmitimgfromvoluntid);



        var namefield=findViewById<TextView>(R.id.donornameproid);
        var callbtnfield=findViewById<Button>(R.id.btncalldonorid);
        val intss=intent;
        val voluntsid=intss.getStringExtra("donprocessdetail");
        val dishesids=intss.getStringExtra("donprocessdetail2");

        val sessionManagement = SessionManagment();
        sessionManagement.SessionManagement2(this) ;
        var useridsss=sessionManagement.getSession();
        val ff="||";
        val list= useridsss?.split(ff);
        val userid= list?.get(0);
        val pincode= list?.get(1);
        val typesofuser= list?.get(2);

        this.setTitle("Delivery Details")


        val reffsfortemp=
                FirebaseDatabase.getInstance().getReference("Donor").child(pincode.toString()).child(voluntsid.toString());
        reffsfortemp.addValueEventListener(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                for(h in snapshot.children){
                    if(h.key.toString()=="name"){
                        namefield.text=h.value.toString();
                    }
                }
            }
        })

        callbtnfield.setOnClickListener {
            val reffsfortemp2=
                    FirebaseDatabase.getInstance().getReference("Donor").child(pincode.toString()).child(voluntsid.toString());
            reffsfortemp2.addValueEventListener(object: ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(h in snapshot.children){
                        if(h.key.toString()=="phno"){
                            phns=h.value.toString();
                            if(ActivityCompat.checkSelfPermission(applicationContext,android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
                                ActivityCompat.requestPermissions(this@VolunteerProcessActivity, arrayOf(android.Manifest.permission.CALL_PHONE),REQUEST_PHONE_CALL)
                            }else{
                                startcall(h.value.toString());
                            }
                        }
                    }
                }
            })
        }

        val reffstemp=FirebaseDatabase.getInstance().getReference("Volunteer").child(pincode.toString()).child(userid.toString()).child("Finishedwork@");

        reffstemp.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                for (h in snapshot.children){
                    if (h.key.toString()=="dishid"){
                        if(h.value.toString()==dishesids.toString()){
                            imgid1id?.visibility=View.GONE;
                            imgid2id?.visibility=View.GONE;
                            btnfiledsubmit.visibility=View.GONE;
                            findViewById<TextView>(R.id.submitedtextid).visibility=View.VISIBLE;
                            findViewById<TextView>(R.id.topictextids).visibility=View.GONE;
                        }
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

        imgid1id?.setOnClickListener{
            startFileChooser();
        }
        imgid2id?.setOnClickListener{
            startFileChooser2();
        }
        btnfiledsubmit.setOnClickListener {
            var imgreffs= FirebaseStorage.getInstance().reference.child("Workdonebyvolunteer").child(userid.toString()).child("${userid.toString()+voluntsid.toString()}.${getExtension(filepath)}");
            imgreffs.putFile(filepath).addOnSuccessListener(object: OnSuccessListener<UploadTask.TaskSnapshot> {
                override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
                    imgreffs.downloadUrl.addOnSuccessListener(object :OnSuccessListener<Uri>{
                        override fun onSuccess(p0: Uri?) {
                            imgurl=p0.toString();
                            var imgreffs2=FirebaseStorage.getInstance().reference.child("Workdonebyvolunteer").child(userid.toString()).child("${userid.toString()+dishesids.toString()}.${getExtension(filepath2)}");
                            imgreffs2.putFile(filepath2).addOnSuccessListener(object :OnSuccessListener<UploadTask.TaskSnapshot>{
                                override fun onSuccess(p1: UploadTask.TaskSnapshot?) {
                                    imgreffs2.downloadUrl.addOnSuccessListener(object:OnSuccessListener<Uri>{
                                        @SuppressLint("LongLogTag")
                                        override fun onSuccess(p1: Uri?) {
                                            imgurl2=p1.toString();
                                            val finished1=workfinish(imgurl,imgurl2,voluntsid.toString(),dishesids.toString(),userid.toString());
                                            val finishondonorref=FirebaseDatabase.getInstance().getReference("Donor").child(pincode.toString()).child(voluntsid.toString()).child("Finishedwork@").push();
                                            val finishonvoluntref=FirebaseDatabase.getInstance().getReference("Volunteer").child(pincode.toString()).child(userid.toString()).child("Finishedwork@").push();
                                            finishondonorref.setValue(finished1).addOnSuccessListener {
                                            }
                                            finishonvoluntref.setValue(finished1).addOnSuccessListener {
//                                                Toast.makeText(this@VolunteerProcessActivity,"Image Uploaded",Toast.LENGTH_SHORT).show();
                                                val userss= myname?.let { it1 -> User(userid.toString(), it1, pincode.toString(),"Volunteer") };

                                                val sessionManagement = SessionManagment();
                                                sessionManagement.SessionManagement2(this@VolunteerProcessActivity);
                                                if (userss != null) {
                                                    sessionManagement.saveSession(userss)
                                                };
                                                val intent:Intent= Intent(this@VolunteerProcessActivity,DonorMainActivity::class.java);
                                                startActivity(intent);
                                                Log.i("authsafter volunteerprocess",userid.toString());
                                            }
                                        }
                                    })




                                }

                            });



                        }

                    })
                }

            })
        }



    }
    fun startFileChooser(){

        var i=Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Choose pics"),111);
    }
    fun startFileChooser2(){

        var i=Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Choose pics"),112);
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==111 && resultCode== Activity.RESULT_OK && data!=null){
            filepath=data.data!!;
            var bitmap= MediaStore.Images.Media.getBitmap(this.contentResolver,filepath);
            imgid1id?.setImageBitmap(bitmap);
        }else if (requestCode==112 && resultCode== Activity.RESULT_OK && data!=null){
            filepath2=data.data!!;
            var bitmap= MediaStore.Images.Media.getBitmap(this.contentResolver,filepath2);
            imgid2id?.setImageBitmap(bitmap);
        }
    }
    private fun getExtension(uri:Uri):String?{
        val cr:ContentResolver?=this.contentResolver;
        val mimTypeMap= MimeTypeMap.getSingleton();
        return mimTypeMap.getExtensionFromMimeType(cr?.getType(uri))
    }




    @SuppressLint("MissingPermission")
    fun startcall(strr:String?){
        val callintent= Intent(Intent.ACTION_CALL);
        callintent.data= Uri.parse("tel:"+strr);
        startActivity(callintent);
    }
    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        if(requestCode==REQUEST_PHONE_CALL){
            startcall(phns);
        }
    }
}

class workfinish(var imgurl1:String?,var imgurl2:String?,var donorid:String?,var dishid:String?,var volunterid:String?);