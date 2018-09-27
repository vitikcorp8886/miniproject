package com.example.vitikcorp.miniproject.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vitikcorp.miniproject.Interface.ItemClickListener;
import com.example.vitikcorp.miniproject.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView txtProductName;
    public ImageView ProImageView;

    private ItemClickListener itemClickListener;
    public ProductViewHolder(View itemView){
        super(itemView);
        txtProductName=(TextView)itemView.findViewById(R.id.product_name);
        ProImageView=(ImageView)itemView.findViewById(R.id.product_image);

        itemView.setOnClickListener(this);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view){
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
