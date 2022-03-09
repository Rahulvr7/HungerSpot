package com.example.hungerspot

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.*
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import org.w3c.dom.Text


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class Mydetails : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    var filepathfordp: Uri? =null;
     var imgurl:String?=null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mydetails, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var namess=view.findViewById<TextView>(R.id.idname);
        var emailss=view.findViewById<TextView>(R.id.idemail);
        var addresss=view.findViewById<TextView>(R.id.idaddress);
        var landmarks=view.findViewById<TextView>(R.id.idlandmark);
        var phnoss=view.findViewById<TextView>(R.id.idphno);
        var pincodess=view.findViewById<TextView>(R.id.idpincode);
        var logoutbtnss=view.findViewById<Button>(R.id.idlogoutbtn)
        var dpimage= view.findViewById<ImageView>(R.id.imgfordpid);
        var btnuploads=view.findViewById<Button>(R.id.imageuploadbtnid);



        val sessionManagement = SessionManagment();
        activity?.let { sessionManagement.SessionManagement2(it) };
        var useridsss=sessionManagement.getSession();
        val ff="||";
        val list= useridsss?.split(ff);
        val userid= list?.get(0);
        val pincode= list?.get(1);
        val typesofuser= list?.get(2);

        var reffs22 =
                FirebaseDatabase.getInstance().getReference(typesofuser.toString()).child(pincode.toString()).child(userid.toString()).child("Mydp");
        reffs22.addValueEventListener(object:ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}
            override fun onDataChange(snapshot: DataSnapshot) {
                for (h in snapshot.children){
                    Log.i("paths",h.key.toString())
                    if (h.key.toString()=="mydp"){
                        imgurl=h.value.toString()
                        Picasso.get().load(imgurl).into(dpimage);
                    }
                }
            }
        })

        if(typesofuser=="Donor") {
            var reffs =
                FirebaseDatabase.getInstance().getReference("Donor").child(pincode.toString())
                    .child(userid.toString());
            reffs.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (h in snapshot.children) {
                        if (h.key.toString() == "name") {
                            namess.text = h.value.toString();
                        } else if (h.key.toString() == "email") {
                            emailss.text =
                                h.value.toString();
                        } else if (h.key.toString() == "address") {
                            addresss.text =
                                h.value.toString();
                        } else if (h.key.toString() == "landmark") {
                            landmarks.text =
                                h.value.toString();
                        } else if (h.key.toString() == "phno") {
                            phnoss.text =
                                h.value.toString();
                        } else if (h.key.toString() == "pincode") {
                            pincodess.text =
                                h.value.toString();
                        }

                    }
                }

            })
        }else if(typesofuser=="Volunteer"){

            var dob=view.findViewById<TextView>(R.id.iddobs)
            dob.visibility=View.VISIBLE;

            var reffs =
                FirebaseDatabase.getInstance().getReference("Volunteer").child(pincode.toString())
                    .child(userid.toString());

            reffs.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {}

                override fun onDataChange(snapshot: DataSnapshot) {
                    for (h in snapshot.children) {
                        if (h.key.toString() == "name") {
                            namess.text = h.value.toString();
                        } else if (h.key.toString() == "email") {
                            emailss.text =
                                h.value.toString();
                        } else if (h.key.toString() == "address") {
                            addresss.text =
                                h.value.toString();
                        } else if (h.key.toString() == "landmark") {
                            landmarks.text =
                               h.value.toString();
                        } else if (h.key.toString() == "phno") {
                            phnoss.text =
                                h.value.toString();
                        } else if (h.key.toString() == "pincode") {
                            pincodess.text =
                                h.value.toString();
                        }
                        else if (h.key.toString() == "dob") {
                            dob.text =
                                h.value.toString();
                        }
                    }
                }
            })
        }

        dpimage?.setOnClickListener {
            startFileChooser();
        }
        logoutbtnss.setOnClickListener {
            sessionManagement.removeSession();
            var intentn=Intent(activity,MainActivity::class.java);
            startActivity(intentn);
        }
        btnuploads.setOnClickListener {
            var imgreffs= FirebaseStorage.getInstance().reference.child("Mydp").child(typesofuser.toString()).child(userid.toString()).child("${userid.toString()}.${filepathfordp?.let { it1 -> getExtension(it1) }}");

            filepathfordp?.let { it1 ->
                imgreffs.putFile(it1).addOnSuccessListener(object: OnSuccessListener<UploadTask.TaskSnapshot> {
                    override fun onSuccess(p0: UploadTask.TaskSnapshot?) {
                        imgreffs.downloadUrl.addOnSuccessListener(object :OnSuccessListener<Uri>{
                            override fun onSuccess(p0: Uri?) {
                                imgurl=p0.toString();
                                val ff=mydp(imgurl);
                                val uploaderf=FirebaseDatabase.getInstance().getReference(typesofuser.toString()).child(pincode.toString()).child(userid.toString()).child("Mydp");
                                uploaderf.setValue(ff).addOnCompleteListener {
                                    Toast.makeText(activity,"Uploaded",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    };
                })
            };
        }
    }
    fun startFileChooser(){

        var i=Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i,"Choose pics"),111);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==111 && resultCode== Activity.RESULT_OK && data!=null){
            filepathfordp=data.data!!;
            val bitmap= MediaStore.Images.Media.getBitmap(activity?.contentResolver,filepathfordp);
            view?.findViewById<ImageView>(R.id.imgfordpid)?.setImageBitmap(bitmap);
        }
    }
    private fun getExtension(uri:Uri):String?{
        val cr: ContentResolver?=activity?.contentResolver;
        val mimTypeMap= MimeTypeMap.getSingleton();
        return mimTypeMap.getExtensionFromMimeType(cr?.getType(uri))
    }
    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Mydetails().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

class mydp(var Mydp:String?);