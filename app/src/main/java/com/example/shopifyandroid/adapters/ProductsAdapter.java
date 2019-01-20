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
    private String collectionImageLink;

    public ProductsAdapter(Product[] products, String collectionImageLink) {
        this.products = products;
        this.collectionImageLink = collectionImageLink;
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
        holder.setViews(product, collectionImageLink);
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

        public void setViews(Product product, String collectionImageLink) {
            CircleImageView productImage = rowView.findViewById(R.id.product_image);
            Picasso.get().load(product.getImageLink()).into(productImage);

            CircleImageView collectionImage = rowView.findViewById(R.id.detail_collection_image);
            Picasso.get().load(collectionImageLink).into(collectionImage);

            TextView productName = rowView.findViewById(R.id.product_name);
            productName.setText(product.getName());

            TextView productCollectionTitle = rowView.findViewById(R.id.product_collection_name);
            productCollectionTitle.setText(product.getCollectionTitle());

            TextView productInventory = rowView.findViewById(R.id.total_inventory);
            productInventory.setText("Inventory: " + product.getInventory());

            TextView productDescription = rowView.findViewById(R.id.product_description);
            productDescription.setText(product.getDescription());

            TextView productVendor = rowView.findViewById(R.id.product_vendor);
            productVendor.setText("Vendor: " + product.getVendor());
        }
    }
}
