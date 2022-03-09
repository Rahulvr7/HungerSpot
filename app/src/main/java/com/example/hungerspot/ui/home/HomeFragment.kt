package com.example.hungerspot.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hungerspot.*
import com.example.hungerspot.R
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.squareup.picasso.Picasso


class HomeFragment : Fragment() {


    lateinit var refsforfinal: DatabaseReference;

     var mRecyclerview2: RecyclerView?=null

    var mRecyclerview3: RecyclerView?=null




    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)

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

        if(typesofuser=="Volunteer"){

//            Toast.makeText(activity,typesofuser.toString(),Toast.LENGTH_SHORT).show()
//            foodupload.visibility=View.GONE;
//            request.visibility=View.GONE;
//            mycontribution.visibility=View.GONE;
//            mRecyclerview2?.visibility=View.VISIBLE;
//            mRecyclerview3?.visibility=View.GONE;

            mRecyclerview2=view.findViewById(R.id.idlistview2);
            mRecyclerview2?.visibility=View.VISIBLE;
            mRecyclerview3=view.findViewById(R.id.idlistview3);
            mRecyclerview3?.visibility=View.GONE;
//
//            mRecyclerview2=view.findViewById(R.id.idlistview2);


            var refsamepincode=FirebaseDatabase.getInstance().getReference("Donor").child(pincode.toString()).child("MyContribution@@");
            refsamepincode.keepSynced(true);
            logicRecyclerView(refsamepincode);

            mRecyclerview2?.setHasFixedSize(true);
            mRecyclerview2?.layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)



        }
        else if(typesofuser=="Donor"){
            mRecyclerview2=view.findViewById(R.id.idlistview2);
            mRecyclerview2?.visibility=View.GONE;
            mRecyclerview3=view.findViewById(R.id.idlistview3);
            mRecyclerview3?.visibility=View.VISIBLE;




            var refsamepincode2=FirebaseDatabase.getInstance().getReference("Donor").child(pincode.toString()).child(userid.toString()).child("Requests");
            refsamepincode2.keepSynced(true);
            logicRecyclerView2(refsamepincode2,pincode.toString());

            mRecyclerview3?.setHasFixedSize(true);
            mRecyclerview3?.layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false)
        }
    }




    fun logicRecyclerView(refs:DatabaseReference){
            val firebaseRecyclerOptions =
                FirebaseRecyclerOptions.Builder<orders>().setQuery(refs, orders::class.java).build()


            val firebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<orders, orderViewHolder>(
                firebaseRecyclerOptions
            ) {
                override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): orderViewHolder {
                    val v: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_layout, parent, false)
                    return activity?.let { orderViewHolder(v, it) }!!
                }

                override fun onBindViewHolder(
                    holder: orderViewHolder,
                    position: Int,
                    model: orders
                ) {
                    holder.notess.setText(model.notes);
                    Picasso.get().load(model.imgurl).into(holder.imgs)

                    holder.from.setText(model.timefrom);
                    holder.till.setText(model.timetill);


                    holder.notess.setOnClickListener{
                        val intents=Intent(context,ContentDetailVolunteerActivity::class.java);
                        intents.putExtra("productid",getRef(position).key);

                        startActivity(intents);
//                        Toast.makeText(activity,"you selected "+getRef(position).key,Toast.LENGTH_SHORT).show();
                    }
                }

            }
            firebaseRecyclerAdapter.startListening();
            mRecyclerview2?.adapter = firebaseRecyclerAdapter;

    }




    fun logicRecyclerView2(refs:DatabaseReference,pinc:String){
        val firebaseRecyclerOptions2 =
            FirebaseRecyclerOptions.Builder<Requestcls>().setQuery(refs, Requestcls::class.java).build()


        val firebaseRecyclerAdapter2 = object : FirebaseRecyclerAdapter<Requestcls, requestViewHolder>(
            firebaseRecyclerOptions2
        ) {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): requestViewHolder {
                val v: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.request_layout, parent, false)
                return requestViewHolder(v)
            }

            override fun onBindViewHolder(
                holder: requestViewHolder,
                position: Int,
                model: Requestcls
            ) {
                holder.namesnn.setText(model.names);
                val refs=FirebaseDatabase.getInstance().getReference("Donor").child(pinc.toString()).child("MyContribution@@").child(model.idofdishes.toString());
                refs.addValueEventListener(object:ValueEventListener{
                    override fun onCancelled(error: DatabaseError) {}
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (h in snapshot.children){
                            Log.i("abbb",h.key.toString());
                            if (h.key.toString()=="notes"){
                                holder.dishes.setText(h.value.toString());
                            }
                        }
                    }
                })
                holder.buttons.setOnClickListener{
                    Toast.makeText(activity,"hello "+model.ids2,Toast.LENGTH_SHORT).show();
                    val intents=Intent(activity,VolunteerAccountViewerActivity::class.java);
                    intents.putExtra("volunteerdetails",model.ids2.toString());
                    intents.putExtra("volunteerdetails2",model.idofdishes.toString());
                    startActivity(intents);
                }
            }
        }
        firebaseRecyclerAdapter2.startListening();
        mRecyclerview3?.adapter = firebaseRecyclerAdapter2;

    }
}


class orderViewHolder(itemView:View,act:Activity):RecyclerView.ViewHolder(itemView) {
    var imgs=itemView.findViewById<ImageView>(R.id.idimg);
    var notess=itemView.findViewById<TextView>(R.id.idnotess);
    var from=itemView.findViewById<TextView>(R.id.idfrom);
    var till=itemView.findViewById<TextView>(R.id.idtill);



}

class requestViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
    var namesnn=itemView.findViewById<TextView>(R.id.requesternameid);
    var dishes=itemView.findViewById<TextView>(R.id.dishesname);
    var buttons=itemView.findViewById<Button>(R.id.btnforviewid);
}