package com.example.vitikcorp.miniproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.vitikcorp.miniproject.Common.Common;
import com.example.vitikcorp.miniproject.Interface.ItemClickListener;
import com.example.vitikcorp.miniproject.Model.Product;
import com.example.vitikcorp.miniproject.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference productList;

    String categoryId = "";
    FirebaseRecyclerAdapter<Product, ProductViewHolder> adapter;

    //search concept coding
    FirebaseRecyclerAdapter<Product, ProductViewHolder> searchAdapter;
    List<String> suggestList = new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        //initialising
        database = FirebaseDatabase.getInstance();
        productList = database.getReference("Menu");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_product);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        if (getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty() && categoryId != null) {
            if(Common.isConnectedToInternet(getBaseContext()))
                loadListProduct(categoryId);
            else
            {
                Toast.makeText(ProductList.this, "Please check your network connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        materialSearchBar = (MaterialSearchBar) findViewById(R.id.searchBar);
        materialSearchBar.setHint("Enter product");
        //materialSearchBar.setSpeechMode(false);
        loadsuggestion();

        materialSearchBar.setLastSuggestions(suggestList);
        materialSearchBar.setCardViewElevation(10);
        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<String> suggest = new ArrayList<String>();
                for (String search : suggestList) {
                    if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase())) ;
                    suggest.add(search);
                }
                materialSearchBar.setLastSuggestions(suggest);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled)
                    recyclerView.setAdapter(adapter);
            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {

            }
        });

    }

    private void loadsuggestion() {
        productList.orderByChild("MenuId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Product item = postSnapshot.getValue(Product.class);
                    suggestList.add(item.getName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadListProduct(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(Product.class, R.layout.product_item, ProductViewHolder.class,
                productList.orderByChild("MenuId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {
                viewHolder.txtProductName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.ProImageView);

                final Product local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //starting productdetail activity
                        Intent prodetail = new Intent(ProductList.this, ProductDetail.class);
                        prodetail.putExtra("ProductId", adapter.getRef(position).getKey());
                        startActivity(prodetail);
                    }
                });
            }
        };


        recyclerView.setAdapter(adapter);

    }

    private void startSearch(CharSequence text) {
        searchAdapter = new FirebaseRecyclerAdapter<Product, ProductViewHolder>(Product.class, R.layout.product_item,
                ProductViewHolder.class,
                productList.orderByChild("Name").equalTo(text.toString())) {

            @Override
            // protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {
            protected void populateViewHolder(ProductViewHolder viewHolder, Product model, int position) {
                viewHolder.txtProductName.setText(model.getName());
                Picasso.with(getBaseContext()).load(model.getImage()).into(viewHolder.ProImageView);

                final Product local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //starting productdetail activity
                        Intent prodetail = new Intent(ProductList.this, ProductDetail.class);
                        prodetail.putExtra("ProductId", searchAdapter.getRef(position).getKey());
                        startActivity(prodetail);
                    }
                });
                recyclerView.setAdapter(searchAdapter);
            }


        };
    }
}