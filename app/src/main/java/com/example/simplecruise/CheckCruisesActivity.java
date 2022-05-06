package com.example.simplecruise;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.SearchView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class CheckCruisesActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth mAuth;

    private RecyclerView mRecyclerView;
    private ArrayList<CruiseItem> mItemList;
    private CruiseItemAdapter mAdapter;

    private FirebaseFirestore mFirestore;
    private CollectionReference mItems;

    private int gridNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_cruises);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            //
        }
        else{
            finish();
        }

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, gridNumber));
        mItemList = new ArrayList<>();

        mAdapter = new CruiseItemAdapter(this, mItemList);
        mRecyclerView.setAdapter(mAdapter);

        mFirestore = FirebaseFirestore.getInstance();
        mItems = mFirestore.collection("Items");

        queryData();
    }

    private void queryData(){
        mItemList.clear();

        mItems.orderBy("start").limit(10).get().addOnSuccessListener(queryDocumentSnapshots -> {
            for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                CruiseItem item = document.toObject(CruiseItem.class);
                mItemList.add(item);
            }

            if(mItemList.size() == 0){
                initalizeData();
                queryData();
            }

            mAdapter.notifyDataSetChanged();
        });


    }

    private void initalizeData(){
        String[] itemsStart = getResources().getStringArray(R.array.cruise_item_starts);
        String[] itemsEnd = getResources().getStringArray(R.array.cruise_item_ends);
        String[] itemsDate = getResources().getStringArray(R.array.cruise_item_dates);
        TypedArray itemsImageResource = getResources().obtainTypedArray(R.array.cruise_item_images);


        for(int i = 0; i < itemsStart.length; i++){
            mItems.add(new CruiseItem(itemsStart[i], itemsEnd[i], itemsDate[i], itemsImageResource.getResourceId(i, 0)));
        }

        itemsImageResource.recycle();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.cruise_list_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_bar);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.log_out:
                return true;
            case R.id.bookings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void bookCruise(CruiseItem item){

    }
}