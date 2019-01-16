package com.example.shopifyandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shopifyandroid.adapters.CollectionsAdapter;
import com.example.shopifyandroid.adapters.ProductsAdapter;
import com.example.shopifyandroid.models.Product;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailsActivity extends AppCompatActivity {

    // JSON Object Tags
    private static final String COLLECTS_TAG = "collects";
    private static final String PRODUCT_ID_TAG = "product_id";
    private static final String PRODUCTS_TAG = "products";
    private static final String TITLE_TAG = "title";
    private static final String IMAGE_TAG = "image";
    private static final String SRC_TAG = "src";
    private static final String VARIANTS_TAG = "variants";
    private static final String INVENTORY_QUANTITY_TAG = "inventory_quantity";

    private String collectionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        collectionName = getIntent().getStringExtra(CollectionsAdapter.COLLECTION_NAME);
        String collectionId = getIntent().getStringExtra(CollectionsAdapter.COLLECTION_ID);
        String imageLink = getIntent().getStringExtra(CollectionsAdapter.COLLECTION_IMAGE);
        String bodyHtml = getIntent().getStringExtra(CollectionsAdapter.COLLECTION_HTML);

        String[] productIds = getProductsInCollection(collectionId);
        Product[] productDetails = getDetailsOfProducts(productIds);

        RecyclerView productsList = findViewById(R.id.products_list);
        productsList.setHasFixedSize(true);
        productsList.setLayoutManager(new LinearLayoutManager(this));
        ProductsAdapter adapter = new ProductsAdapter(productDetails);
        productsList.setAdapter(adapter);

        View collectionDetails = findViewById(R.id.collection_details);
        CircleImageView collectionImage = collectionDetails.findViewById(R.id.collection_image);
        TextView tvCollectionName = collectionDetails.findViewById(R.id.detail_collection_name);
        TextView tvBodyHtml = collectionDetails.findViewById(R.id.body_html);

        Picasso.get().load(imageLink).into(collectionImage);
        tvCollectionName.setText(collectionName);
        tvBodyHtml.setText(bodyHtml);
    }

    private String getProductsEndpoint(String collectionId) {
        return "https://shopicruit.myshopify.com/admin/collects.json?collection_id=" + collectionId + "&page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";
    }

    private String[] getProductsInCollection(String collectionId) {
        RequestCaller reqCaller = new RequestCaller();
        String endpoint = getProductsEndpoint(collectionId);
        try {
            String result = reqCaller.execute(endpoint).get();

            JSONObject collectsObj = new JSONObject(result);
            JSONArray collects = collectsObj.getJSONArray(COLLECTS_TAG);

            int numCollects = collects.length();
            String[] productIds = new String[numCollects];

            for (int i = 0; i < numCollects; i++) {
                JSONObject currentCollect = collects.getJSONObject(i);
                String productId = currentCollect.getString(PRODUCT_ID_TAG);
                productIds[i] = productId;
            }

            return productIds;

        } catch (InterruptedException e) {
            Toast.makeText(this, "Something went wrong, process interrupted.", Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            Toast.makeText(this, "Task unexpectedly aborted.", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(this, "Data parsing error.", Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    private String getProductDetailsEndpoint(String[] productIds) {
        StringBuilder productIdsBuilder = new StringBuilder();
        for (int i = 0; i < productIds.length; i++) {
            productIdsBuilder.append(productIds[i]);
            if (i != productIds.length - 1) {
                productIdsBuilder.append(",");
            }
        }
        String productIdsQuery = productIdsBuilder.toString();

        return "https://shopicruit.myshopify.com/admin/products.json?ids=" + productIdsQuery + "&page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";
    }

    private int calculateInventory(JSONArray variants) throws JSONException {
        int inventory = 0;
        for (int i = 0; i < variants.length(); i++) {
            JSONObject currentVariant = variants.getJSONObject(i);
            inventory += currentVariant.getInt(INVENTORY_QUANTITY_TAG);
        }

        return inventory;
    }

    private Product[] getDetailsOfProducts(String[] productIds) {
        RequestCaller reqCaller = new RequestCaller();
        String endpoint = getProductDetailsEndpoint(productIds);

        try {
            String result = reqCaller.execute(endpoint).get();

            JSONObject productsObj = new JSONObject(result);
            JSONArray products = productsObj.getJSONArray(PRODUCTS_TAG);

            int numProducts = products.length();
            Product[] productDetails = new Product[numProducts];

            for (int i = 0; i < numProducts; i++) {
                JSONObject currentProduct = products.getJSONObject(i);
                String name = currentProduct.getString(TITLE_TAG);
                String imageLink = currentProduct.getJSONObject(IMAGE_TAG).getString(SRC_TAG);
                int inventory = calculateInventory(currentProduct.getJSONArray(VARIANTS_TAG));
                productDetails[i] = new Product(name, inventory, collectionName, imageLink);
            }

            return productDetails;

        } catch (InterruptedException e) {
            Toast.makeText(this, "Something went wrong, process interrupted.", Toast.LENGTH_SHORT).show();
        } catch (ExecutionException e) {
            Toast.makeText(this, "Task unexpectedly aborted.", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            Toast.makeText(this, "Data parsing error.", Toast.LENGTH_SHORT).show();
        }

        return null;
    }


}
