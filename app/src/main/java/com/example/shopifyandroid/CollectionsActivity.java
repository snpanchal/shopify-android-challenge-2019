package com.example.shopifyandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.shopifyandroid.adapters.CollectionsAdapter;

public class CollectionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);

        RecyclerView collectionsList = findViewById(R.id.collections_list);
        collectionsList.setHasFixedSize(true);
        collectionsList.setLayoutManager(new LinearLayoutManager(this));
        CollectionsAdapter adapter = new CollectionsAdapter(this);
        collectionsList.setAdapter(adapter);
    }
}
