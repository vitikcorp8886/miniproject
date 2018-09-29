package com.example.vitikcorp.miniproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.vitikcorp.miniproject.Common.Common;
import com.example.vitikcorp.miniproject.Model.Request;
import com.example.vitikcorp.miniproject.ViewHolder.OrderViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.vitikcorp.miniproject.Cart.*;

import org.w3c.dom.Text;

//import static com.example.vitikcorp.miniproject.Cart.od;

public class OrderStatus extends AppCompatActivity {


    public RecyclerView recyclerView;
    public RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;
    FirebaseRecyclerAdapter adapter;
    //TextView NoOrder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);

        database= FirebaseDatabase.getInstance();
        requests=database.getReference("PendingOrders");

      //  NoOrder=(TextView)findViewById(R.id.noorder);
       // NoOrder.setVisibility(View.VISIBLE);
        recyclerView=(RecyclerView)findViewById(R.id.listOrder);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //if(getIntent()==null)
           loadOrders(Common.currentUser.getPhone());
        //else
          // loadOrders(getIntent().getStringExtra("userPhone"));


    }
    private void loadOrders(String phone){

        adapter =new FirebaseRecyclerAdapter<Request,OrderViewHolder>(
                Request.class,
                R.layout.order_layout,
                OrderViewHolder.class,
                requests.orderByChild("phone").equalTo(phone)
        ){
            @Override
            protected void populateViewHolder(OrderViewHolder viewHolder,Request model,int position){
                viewHolder.txtOrderId.setText(adapter.getRef(position).getKey());
                viewHolder.txtOrderStatus.setText(Common.convertCodeToStatus(model.getStatus()));
                viewHolder.txtOrderAddress.setText(model.getAddress());
                viewHolder.txtOrderPhone.setText(model.getPhone());
            }
        };
        recyclerView.setAdapter(adapter);

    }
}
