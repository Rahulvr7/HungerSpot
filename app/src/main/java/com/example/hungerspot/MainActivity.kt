package com.example.hungerspot



import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sessionManagement = SessionManagment();
        sessionManagement.SessionManagement2(this);
        var useridsss=sessionManagement.getSession();
        val ff="||";
        val list= useridsss?.split(ff);


        var donorbtn=findViewById<Button>(R.id.donorbtnid);
        var volunteerbtn=findViewById<Button>(R.id.volunteerbtnid);

        donorbtn.setOnClickListener {
            var intent=Intent(this,DonorLoginActivity::class.java);
            startActivity(intent);
        }
        volunteerbtn.setOnClickListener {
            var intent2=Intent(this,VolunteerLoginActivity::class.java);
            startActivity(intent2);
        }






    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}