package com.example.hungerspot.ui.slideshow

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hungerspot.*
import com.example.hungerspot.R
import com.example.hungerspot.ui.home.orderViewHolder
import com.example.hungerspot.ui.home.requestViewHolder
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.*

class SlideshowFragment : Fragment() {

    private lateinit var slideshowViewModel: SlideshowViewModel
    var mRecyclerviewn: RecyclerView? =null;
    var mRecyclerviewn2: RecyclerView? =null;


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_slideshow, container, false)
        return root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {


        val sessionManagement = SessionManagment();
        activity?.let { sessionManagement.SessionManagement2(it) };
        var useridsss=sessionManagement.getSession();
        val ff="||";
        val list= useridsss?.split(ff);
        val userid= list?.get(0);
        val pincode= list?.get(1);
        val typesofuser= list?.get(2);

        super.onViewCreated(view, savedInstanceState);

        if(typesofuser=="Donor") {

            mRecyclerviewn = view.findViewById(R.id.accept_layout);
            mRecyclerviewn2 = view.findViewById(R.id.accept_layout2);

            mRecyclerviewn2?.visibility=View.GONE;
            mRecyclerviewn?.visibility=View.VISIBLE;


            val reffsforacpt = FirebaseDatabase.getInstance().getReference("Donor").child(pincode.toString()).child(userid.toString()).child("Accepts");

            reffsforacpt.keepSynced(true);
            logicRecyclerView(reffsforacpt, pincode.toString());

            mRecyclerviewn?.setHasFixedSize(true);
            mRecyclerviewn?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        }else if(typesofuser=="Volunteer"){
            mRecyclerviewn = view.findViewById(R.id.accept_layout);
            mRecyclerviewn2 = view.findViewById(R.id.accept_layout2);

            mRecyclerviewn2?.visibility=View.VISIBLE;
            mRecyclerviewn?.visibility=View.GONE;

            val reffsforacpt2 = FirebaseDatabase.getInstance().getReference("Volunteer").child(pincode.toString()).child(userid.toString()).child("Processing");

            reffsforacpt2.keepSynced(true);
            logicRecyclerView2(reffsforacpt2, pincode.toString());

            mRecyclerviewn2?.setHasFixedSize(true);
            mRecyclerviewn2?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }


    }

    fun logicRecyclerView(refs:DatabaseReference,pinc:String){
        val firebaseRecyclerOptions2 =
            FirebaseRecyclerOptions.Builder<Acceptcls>().setQuery(refs, Acceptcls::class.java).build()


        val firebaseRecyclerAdapter2 = object : FirebaseRecyclerAdapter<Acceptcls, AcceptViewHolder>(
            firebaseRecyclerOptions2
        ) {
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): AcceptViewHolder {
                val v: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.request_layout, parent, false)
                return AcceptViewHolder(v)
            }

            override fun onBindViewHolder(
                holder: AcceptViewHolder,
                position: Int,
                model: Acceptcls
            ) {
                holder.namesnn.setText(model.nameofvolunt);
                val refs=FirebaseDatabase.getInstance().getReference("Donor").child(pinc.toString()).child("MyContribution@@").child(model.idofdish.toString());
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
                    val intents=Intent(activity,DonorProcessActivity::class.java);
                        intents.putExtra("voluntprocessdetail",model.idofvolunt.toString());
                    intents.putExtra("voluntprocessdetail2",model.idofdish.toString());
                    startActivity(intents);
                }
            }
        }
        firebaseRecyclerAdapter2.startListening();
        mRecyclerviewn?.adapter = firebaseRecyclerAdapter2;

    }




    fun logicRecyclerView2(refs:DatabaseReference,pinc:String){
        val firebaseRecyclerOptions2 =
                FirebaseRecyclerOptions.Builder<Acceptcls>().setQuery(refs, Acceptcls::class.java).build()


        val firebaseRecyclerAdapter2 = object : FirebaseRecyclerAdapter<Acceptcls, AcceptViewHolder>(
                firebaseRecyclerOptions2
        ) {
            override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
            ): AcceptViewHolder {
                val v: View = LayoutInflater.from(parent.context)
                        .inflate(R.layout.request_layout, parent, false)
                return AcceptViewHolder(v)
            }

            override fun onBindViewHolder(
                    holder: AcceptViewHolder,
                    position: Int,
                    model: Acceptcls
            ) {
                holder.namesnn.setText(model.nameofvolunt);
                val refs=FirebaseDatabase.getInstance().getReference("Donor").child(pinc.toString()).child("MyContribution@@").child(model.idofdish.toString());
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
                    val intents=Intent(activity,VolunteerProcessActivity::class.java);
                    intents.putExtra("donprocessdetail",model.idofvolunt.toString());
                    intents.putExtra("donprocessdetail2",model.idofdish.toString());
                    startActivity(intents);
                }
            }
        }
        firebaseRecyclerAdapter2.startListening();
        mRecyclerviewn2?.adapter = firebaseRecyclerAdapter2;

    }

}

class AcceptViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
    var namesnn=itemView.findViewById<TextView>(R.id.requesternameid);
    var dishes=itemView.findViewById<TextView>(R.id.dishesname);
    var buttons=itemView.findViewById<Button>(R.id.btnforviewid);
}