package com.example.vitikcorp.miniproject;

import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.vitikcorp.miniproject.Common.Common;
import com.example.vitikcorp.miniproject.Database.Database;
import com.example.vitikcorp.miniproject.Model.Order;
import com.example.vitikcorp.miniproject.Model.Product;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProductDetail extends AppCompatActivity {

    TextView P_name, P_price;
    ImageView P_image;
    FloatingActionButton btnCart;
    ElegantNumberButton counterButton;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String productId="";

    Product currProduct;

    FirebaseDatabase database;
    DatabaseReference productdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        database= FirebaseDatabase.getInstance();
        productdb=database.getReference("Menu");

        counterButton= (ElegantNumberButton)findViewById(R.id.number_button);
        btnCart=(FloatingActionButton)findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                        productId,
                        currProduct.getName(),
                        counterButton.getNumber(),
                        currProduct.getPrice()
                ));

                Toast.makeText(ProductDetail.this, "Added to Cart", Toast.LENGTH_SHORT).show();
            }
        });

        P_name=(TextView)findViewById(R.id.product_name);
        P_price=(TextView)findViewById(R.id.product_price);
        P_image=(ImageView)findViewById(R.id.img_prod);

        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);

        if(getIntent()!=null)
            productId=getIntent().getStringExtra("ProductId");
        if(!productId.isEmpty())
        {
            if(Common.isConnectedToInternet(getBaseContext()))
                getDetailProduct(productId);
            else
            {
                Toast.makeText(ProductDetail.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }
    private void getDetailProduct(String productId){
        productdb.child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currProduct=dataSnapshot.getValue(Product.class);

                Picasso.with(getBaseContext()).load(currProduct.getImage()).into(P_image);

                collapsingToolbarLayout.setTitle(currProduct.getName());
                P_name.setText(currProduct.getName());
                P_price.setText(currProduct.getPrice());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
