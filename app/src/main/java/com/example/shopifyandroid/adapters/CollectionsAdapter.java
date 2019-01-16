package com.example.shopifyandroid.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopifyandroid.DetailsActivity;
import com.example.shopifyandroid.R;
import com.example.shopifyandroid.RequestCaller;
import com.example.shopifyandroid.models.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CollectionsAdapter extends RecyclerView.Adapter<CollectionsAdapter.CollectionsViewHolder> {

    private static final String COLLECTIONS_URL = "https://shopicruit.myshopify.com/admin/custom_collections.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";
    public static final String COLLECTION_NAME = "collection_name";
    public static final String COLLECTION_ID = "collection_id";
    public static final String COLLECTION_HTML = "collection_html";
    public static final String COLLECTION_IMAGE = "collection_image";


    // JSON Tags
    private static final String CUSTOM_COLLECTIONS_TAG = "custom_collections";
    private static final String TITLE_TAG = "title";
    private static final String ID_TAG = "id";
    private static final String BODY_HTML_TAG = "body_html";
    private static final String IMAGE_TAG = "image";
    private static final String SRC_TAG = "src";

    private Context context;
    private String[] collections;
    private Map<String, Collection> collectionsMap;

    public CollectionsAdapter(Context context) {
        this.context = context;
        getCollectionData();
    }

    public void getCollectionData() {
        RequestCaller reqCaller = new RequestCaller();
        collectionsMap = new HashMap<>();
        try {
            String collectionsResult = reqCaller.execute(COLLECTIONS_URL).get();

            // Get list of collections to add to UI
            JSONObject collectionsJson = new JSONObject(collectionsResult);
            JSONArray customCollections = collectionsJson.getJSONArray(CUSTOM_COLLECTIONS_TAG);

            int numCollections = customCollections.length();
            collections = new String[numCollections];

            for (int i = 0; i < numCollections; i++) {
                JSONObject currentCollection = customCollections.getJSONObject(i);
                String collectionTitle = currentCollection.getString(TITLE_TAG);
                String collectionId = currentCollection.getString(ID_TAG);
                String bodyHtml = currentCollection.getString(BODY_HTML_TAG);
                String imageLink = currentCollection.getJSONObject(IMAGE_TAG).getString(SRC_TAG);
                collectionsMap.put(collectionTitle, new Collection(collectionTitle, imageLink, bodyHtml, collectionId));
                collections[i] = collectionTitle;
            }

        } catch (InterruptedException e) {
            Toast.makeText(context, "Something went wrong, process interrupted.", Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            Toast.makeText(context, "Task unexpectedly aborted.", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(context, "Data parsing error.", Toast.LENGTH_SHORT).show();
        }
    }
    @NonNull
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
            tvCollectionName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent detailsIntent = new Intent(context, DetailsActivity.class);
                    detailsIntent.putExtra(COLLECTION_NAME, collectionName);
                    detailsIntent.putExtra(COLLECTION_ID, collectionId);
                    detailsIntent.putExtra(COLLECTION_HTML, bodyHtml);
                    detailsIntent.putExtra(COLLECTION_IMAGE, imageLink);
                    context.startActivity(detailsIntent);
                }
            });
        }
    }
}
