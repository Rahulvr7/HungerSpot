package com.example.hungerspot

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class VolunteerRegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_volunteer_register);

        var username=findViewById<EditText>(R.id.usernamevoluntid);
        var phno=findViewById<EditText>(R.id.phnovoluntid);
        var emails=findViewById<EditText>(R.id.emailvoluntid);
        var dob=findViewById<EditText>(R.id.dobvoluntid);
//        var gender=findViewById<EditText>(R.id.gendervoluntid);
        var addresss=findViewById<EditText>(R.id.addressvoluntid);
        var landmarks=findViewById<EditText>(R.id.landmarkvoluntid);
        var pswd=findViewById<EditText>(R.id.pswdvoluntid);
        var cpswd=findViewById<EditText>(R.id.cpswdvoluntid);
        var pinc=findViewById<EditText>(R.id.pincodeid)
        var regbtns=findViewById<Button>(R.id.regbtnvoluntid);




        var haveacnt=findViewById<TextView>(R.id.haveanaccountid);
        haveacnt.setOnClickListener {
            var intent6=Intent(this,VolunteerLoginActivity::class.java);
            startActivity(intent6);
        }

        regbtns.setOnClickListener {


            val namesn = username.text.toString().trim();
            val phnosn = phno.text.toString().trim();
            val emailsn = emails.text.toString().trim();
            val addresssn = addresss.text.toString().trim();
            val landmarksn = landmarks.text.toString().trim();
            val dobsn=dob.text.toString().trim();
            val pincodesn = pinc.text.toString().trim();
            val passwordsn = pswd.text.toString().trim();
            val cpasswordsn = cpswd.text.toString().trim();



            if(cpasswordsn.equals(passwordsn)){
                val donorreg = volunteerclass(namesn, phnosn,emailsn,addresssn,landmarksn,dobsn,pincodesn,passwordsn);
                val reffs = FirebaseDatabase.getInstance().getReference("Volunteer");

                val stremail=emails.text.toString();
                var emailsplitter=stremail.indexOf("@");
                val finalstring=stremail.substring(0,emailsplitter);

                reffs.child(pincodesn).child(finalstring).setValue(donorreg).addOnCompleteListener{
                    Toast.makeText(this,"Registered Successfully", Toast.LENGTH_SHORT).show();

                }.addOnCanceledListener {
                    Toast.makeText(this,"Registeration Failed", Toast.LENGTH_SHORT).show();

                }


            }else{
                Toast.makeText(this,"Passwords are not matching", Toast.LENGTH_SHORT).show();

            }


        }



    }
}