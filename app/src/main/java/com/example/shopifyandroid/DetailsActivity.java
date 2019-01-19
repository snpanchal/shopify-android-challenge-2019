package com.example.shopifyandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
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

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class DetailsActivity extends AppCompatActivity {

    // JSON Tags
    private static final String COLLECTS_TAG = "collects";
    private static final String PRODUCT_ID_TAG = "product_id";
    private static final String PRODUCTS_TAG = "products";
    private static final String TITLE_TAG = "title";
    private static final String IMAGE_TAG = "image";
    private static final String SRC_TAG = "src";
    private static final String VARIANTS_TAG = "variants";
    private static final String INVENTORY_QUANTITY_TAG = "inventory_quantity";

    private String collectionName;
    private DisposableObserver<String> productsObserver;
    private DisposableObserver<String> detailsObserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get collection details from Intent
        collectionName = getIntent().getStringExtra(CollectionsAdapter.COLLECTION_NAME);
        String collectionId = getIntent().getStringExtra(CollectionsAdapter.COLLECTION_ID);
        final String imageLink = getIntent().getStringExtra(CollectionsAdapter.COLLECTION_IMAGE);
        final String bodyHtml = getIntent().getStringExtra(CollectionsAdapter.COLLECTION_HTML);

        // Get products for current collection
        String productsEndpoint = getProductsEndpoint(collectionId);
        productsObserver = getObservable(productsEndpoint)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableObserver<String>() {
                @Override
                public void onNext(String productsResult) {
                    try {
                        // Extract data from JSON
                        JSONObject collectsObj = new JSONObject(productsResult);
                        JSONArray collects = collectsObj.getJSONArray(COLLECTS_TAG);

                        int numCollects = collects.length();
                        String[] productIds = new String[numCollects];

                        for (int i = 0; i < numCollects; i++) {
                            JSONObject currentCollect = collects.getJSONObject(i);
                            String productId = currentCollect.getString(PRODUCT_ID_TAG);
                            productIds[i] = productId;
                        }

                        getProductDetails(productIds, imageLink, bodyHtml);

                    } catch (JSONException e) {
                        Toast.makeText(DetailsActivity.this, "Error parsing data.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("Error", e.getMessage(), e);
                }

                @Override
                public void onComplete() {}
            });
    }

    private void getProductDetails(String[] productIds, String imageLink, String bodyHtml) {
        String detailsEndpoint = getProductDetailsEndpoint(productIds);
        detailsObserver = getObservable(detailsEndpoint)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableObserver<String>() {
                @Override
                public void onNext(String detailsResult) {
                    try {
                        // Extract data from JSON
                        JSONObject productsObj = new JSONObject(detailsResult);
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

                        loadViews(imageLink, bodyHtml, productDetails);
                    } catch (JSONException e) {
                        Toast.makeText(DetailsActivity.this, "Error parsing data.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("Error", e.getMessage(), e);
                }

                @Override
                public void onComplete() {}
            });
    }

    private void loadViews(String imageLink, String bodyHtml, Product[] productDetails) {
        // Load information into views
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

    public Observable<String> getObservable(String requestUrl) {
        RequestCaller reqCaller = new RequestCaller();
        return Observable.defer(() -> Observable.just(reqCaller.makeRequest(requestUrl)));
    }

    private String getProductsEndpoint(String collectionId) {
        return "https://shopicruit.myshopify.com/admin/collects.json?collection_id=" + collectionId + "&page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Dispose observers to avoid memory leaks
        if (productsObserver != null && !productsObserver.isDisposed()) {
            productsObserver.dispose();
        }
        if (detailsObserver != null && !detailsObserver.isDisposed()) {
            detailsObserver.dispose();
        }
    }
}
