package com.example.hungerspot

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class mycontribution : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    lateinit var mRecyclerview:RecyclerView
    lateinit var mDatabase: DatabaseReference

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mycontribution, container, false)
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


        mRecyclerview=view.findViewById(R.id.idlistview);
        Log.i("yessss",pincode+" "+userid);
        mDatabase=FirebaseDatabase.getInstance().getReference("Donor").child(pincode.toString()).child(userid.toString()).child("MyContribution");
        mDatabase.keepSynced(true);
        mRecyclerview.setHasFixedSize(true);
        mRecyclerview.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        logicRecyclerView();

    }
    fun logicRecyclerView(){



        val firebaseRecyclerOptions=FirebaseRecyclerOptions.Builder<Contribution>()
            .setQuery(mDatabase,Contribution::class.java)
            .build();


        val firebaseRecyclerAdapter=object : FirebaseRecyclerAdapter<Contribution,contributeViewHolder>(firebaseRecyclerOptions){
            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): contributeViewHolder {
                val v:View=LayoutInflater.from(parent.context).inflate(R.layout.list_layout,parent,false)
                return contributeViewHolder(v)
            }

            override fun onBindViewHolder(
                holder: contributeViewHolder,
                position: Int,
                model: Contribution
            ) {
                holder.notess.setText(model.notes);
                Picasso.get().load(model.imgurl).into(holder.imgs)
                holder.from.setText(model.timefrom);
                holder.till.setText(model.timetill);

            }

        }
        firebaseRecyclerAdapter.startListening();
        mRecyclerview.adapter=firebaseRecyclerAdapter;
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            mycontribution().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
class contributeViewHolder(itemView:View):RecyclerView.ViewHolder(itemView){
    var imgs=itemView.findViewById<ImageView>(R.id.idimg);
    var notess=itemView.findViewById<TextView>(R.id.idnotess);
    var from=itemView.findViewById<TextView>(R.id.idfrom);
    var till=itemView.findViewById<TextView>(R.id.idtill);

}

