package com.example.hungerspot

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.widget.Toast

public class SessionManagment{

      var sharedpref:SharedPreferences?=null;
     lateinit var editor:SharedPreferences.Editor;
    var SHARED_PREF_NAME="session";
    var SESSION_KEY="";
    var SESSION_PINCODE="";



    fun SessionManagement2(context:Context){
        sharedpref=context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor= sharedpref?.edit()!!;
    }
    fun saveSession(user:User){
        val ids: String? =user.ids;
        val pincs:String?=user.pincodes;
        val types:String?=user.typesofuser;
        var alls=ids+"||"+pincs+"||"+types;
        editor.putString(SESSION_KEY,alls).commit();



    }


    fun  getSession(): String? {
        var userids= sharedpref?.getString(SESSION_KEY," ");

        return userids;
    }
    fun getpincode():String?{
        var userpincode=sharedpref?.getString(SESSION_PINCODE," ");
        return userpincode;
    }
    fun removeSession(){
        editor.putString(SESSION_KEY," ").commit();
    }

}