package com.example.hungerspot.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.hungerspot.R
import com.example.hungerspot.SessionManagment
import com.example.hungerspot.ui.gallery.GalleryFragment

class HomeFragmentvolunt : Fragment() {


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_nav_home_volunteer, container, false)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sessionManagement = SessionManagment();
        activity?.let { sessionManagement.SessionManagement2(it) };
        var useridsss=sessionManagement.getSession();
        val ff="||";
        val list= useridsss?.split(ff);
        val userid= list?.get(0);
        val pincode= list?.get(1);
        val typesofuser= list?.get(2);

//        var foodupload=view.findViewById<Button>(R.id.idfooduploadbtn)
//        var request=view.findViewById<Button>(R.id.idrequests)
//        var mycontribution=view.findViewById<Button>(R.id.idmycontributes)

//        foodupload.setOnClickListener {
//            val gm:Fragment=GalleryFragment();
////            activity?.supportFragmentManager?.beginTransaction()?.replace(R.id.nav_gallery,hm)?.commit()
//            val Fragmentss: FragmentTransaction? = fragmentManager?.beginTransaction()
//            Fragmentss?.replace(R.id.nav_home,gm);
//            Fragmentss?.commit();
//
//        }


    }
}