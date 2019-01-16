package com.example.shopifyandroid.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shopifyandroid.R;
import com.example.shopifyandroid.models.Product;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductViewHolder> {

    private Product[] products;

    public ProductsAdapter(Product[] products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_details, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int i) {
        Product product = products[i];
        holder.setImage(product.getImageLink());
        holder.setName(product.getName());
        holder.setCollectionTitle(product.getCollectionTitle());
        holder.setInventory(product.getInventory());
    }

    @Override
    public int getItemCount() {
        return products.length;
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {

        View rowView;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            rowView = itemView;
        }

        public void setImage(String imageLink) {
            CircleImageView productImage = rowView.findViewById(R.id.product_image);
            Picasso.get().load(imageLink).into(productImage);
        }

        public void setName(String name) {
            TextView productName = rowView.findViewById(R.id.product_name);
            productName.setText(name);
        }

        public void setCollectionTitle(String collectionTitle) {
            TextView productCollectionTitle = rowView.findViewById(R.id.product_collection_name);
            productCollectionTitle.setText(collectionTitle);
        }

        public void setInventory(int inventory) {
            TextView productInventory = rowView.findViewById(R.id.total_inventory);
            productInventory.setText("Inventory: " + inventory);
        }
    }
}
