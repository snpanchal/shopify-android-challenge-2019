package com.example.shopifyandroid.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shopifyandroid.DetailsActivity;
import com.example.shopifyandroid.R;
import com.example.shopifyandroid.models.Collection;

import java.util.Map;

public class CollectionsAdapter extends RecyclerView.Adapter<CollectionsAdapter.CollectionsViewHolder> {

    public static final String COLLECTION_NAME = "collection_name";
    public static final String COLLECTION_ID = "collection_id";
    public static final String COLLECTION_HTML = "collection_html";
    public static final String COLLECTION_IMAGE = "collection_image";

    private Context context;
    private String[] collections;
    private Map<String, Collection> collectionsMap;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public CollectionsAdapter(Context context, String[] collections, Map<String, Collection> collectionsMap) {
        this.context = context;
        this.collections = collections;
        this.collectionsMap = collectionsMap;
    }


    @Override
    public CollectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection, parent, false);
        return new CollectionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionsViewHolder holder, int i) {
        String collectionName = collections[i];
        Collection collection = collectionsMap.get(collectionName);
        holder.setCollectionName(collectionName);
        holder.setClickAction(context, collectionName, collection.getId(), collection.getBodyHtml(), collection.getImageLink());
    }

    @Override
    public int getItemCount() {
        return collections.length;
    }

    static class CollectionsViewHolder extends RecyclerView.ViewHolder {

        View rowView;
        TextView tvCollectionName;

        CollectionsViewHolder(@NonNull View itemView) {
            super(itemView);
            rowView = itemView;
            tvCollectionName = rowView.findViewById(R.id.collection_name);
        }

        void setCollectionName(String collectionName) {
            tvCollectionName.setText(collectionName);
        }

        void setClickAction(final Context context, final String collectionName, final String collectionId, final String bodyHtml, final String imageLink) {
            tvCollectionName.setOnClickListener(view -> {
                // Send collection details to DetailsActivity
                Intent detailsIntent = new Intent(context, DetailsActivity.class);
                detailsIntent.putExtra(COLLECTION_NAME, collectionName);
                detailsIntent.putExtra(COLLECTION_ID, collectionId);
                detailsIntent.putExtra(COLLECTION_HTML, bodyHtml);
                detailsIntent.putExtra(COLLECTION_IMAGE, imageLink);
                context.startActivity(detailsIntent);
            });
        }
    }
}
