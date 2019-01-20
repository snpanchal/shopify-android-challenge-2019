package com.example.shopifyandroid;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.shopifyandroid.adapters.CollectionsAdapter;
import com.example.shopifyandroid.models.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class CollectionsActivity extends AppCompatActivity {

    private static final String COLLECTIONS_URL = "https://shopicruit.myshopify.com/admin/custom_collections.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";

    // JSON Tags
    private static final String CUSTOM_COLLECTIONS_TAG = "custom_collections";
    private static final String TITLE_TAG = "title";
    private static final String ID_TAG = "id";
    private static final String BODY_HTML_TAG = "body_html";
    private static final String IMAGE_TAG = "image";
    private static final String SRC_TAG = "src";

    private DisposableObserver<String> collectionsObserver;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);

        getSupportActionBar().setTitle("Collections");

        // Use an observable to get collection data
        collectionsObserver = getCollectionsObservable()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new DisposableObserver<String>() {
                @Override
                public void onNext(String collectionsResult) {
                    Map<String, Collection> collectionsMap = new HashMap<>();
                    String[] collections = null;

                    try {
                        // Get list of collections to add to UI
                        JSONObject collectionsJson = new JSONObject(collectionsResult);
                        JSONArray customCollections = collectionsJson.getJSONArray(CUSTOM_COLLECTIONS_TAG);

                        int numCollections = customCollections.length();
                        collections = new String[numCollections];

                        // Extract data from JSON
                        for (int i = 0; i < numCollections; i++) {
                            JSONObject currentCollection = customCollections.getJSONObject(i);
                            String collectionTitle = currentCollection.getString(TITLE_TAG);
                            String collectionId = currentCollection.getString(ID_TAG);
                            String bodyHtml = currentCollection.getString(BODY_HTML_TAG);
                            String imageLink = currentCollection.getJSONObject(IMAGE_TAG).getString(SRC_TAG);
                            collectionsMap.put(collectionTitle, new Collection(collectionTitle, imageLink, bodyHtml, collectionId));
                            collections[i] = collectionTitle;
                        }
                    } catch (JSONException e) {
                        Toast.makeText(CollectionsActivity.this, "Error parsing data.", Toast.LENGTH_SHORT).show();
                    }


                    // Set up collection list RecyclerView
                    RecyclerView collectionsList = findViewById(R.id.collections_list);
                    collectionsList.setHasFixedSize(true);
                    collectionsList.setLayoutManager(new LinearLayoutManager(CollectionsActivity.this));
                    CollectionsAdapter adapter = new CollectionsAdapter(CollectionsActivity.this, collections, collectionsMap);
                    collectionsList.setAdapter(adapter);
                }

                @Override
                public void onError(Throwable e) {
                    Log.e("Error", e.getMessage(), e);
                }

                @Override
                public void onComplete() {}
            });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public Observable<String> getCollectionsObservable() {
        RequestCaller reqCaller = new RequestCaller();
        return Observable.defer(() -> Observable.just(reqCaller.makeRequest(COLLECTIONS_URL)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Prevent memory leaks by disposing observer
        if (collectionsObserver != null && !collectionsObserver.isDisposed()) {
            collectionsObserver.dispose();
        }
    }
}
