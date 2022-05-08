package com.example.simplecruise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MyCruiseItemAdapter extends RecyclerView.Adapter<MyCruiseItemAdapter.ViewHolder> implements Filterable {
    private ArrayList<CruiseItem> mCruiseItemsData;
    private ArrayList<CruiseItem> mCruiseItemsDataAll;
    private Context mContext;
    private int lastPosition = -1;

    MyCruiseItemAdapter(Context context, ArrayList<CruiseItem> itemsData){
        this.mCruiseItemsData = itemsData;
        this.mCruiseItemsDataAll = itemsData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyCruiseItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyCruiseItemAdapter.ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCruiseItemAdapter.ViewHolder holder, int position) {
        CruiseItem currentItem = mCruiseItemsData.get(position);

        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAbsoluteAdapterPosition();
        }

        holder.mUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("Bookings").get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                        BookingItem item = document.toObject(BookingItem.class);
                        if(item.getCruiseStart().equals(holder.mStartText) && item.getUserEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                            CollectionReference cf = FirebaseFirestore.getInstance().collection("Bookings");
                            cf.document(document.getId()).set(new BookingItem(item.getCruiseStart(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), Integer.parseInt(holder.mPeopleText.getText().toString())));
                            break;
                        }
                    }
                });
            }
        });

        holder.mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("Bookings").get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                        BookingItem item = document.toObject(BookingItem.class);
                        if(item.getCruiseStart().equals(holder.mStartText) && item.getUserEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                            CollectionReference cf = FirebaseFirestore.getInstance().collection("Bookings");
                            cf.document(document.getId()).delete();
                            break;
                        }
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return mCruiseItemsData.size();
    }

    @Override
    public Filter getFilter(){ return cruiseFilter; }

    private Filter cruiseFilter = new Filter(){
        @Override
        protected FilterResults  performFiltering(CharSequence charSequence){
            ArrayList<CruiseItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(charSequence == null || charSequence.length() == 0){
                results.count = mCruiseItemsDataAll.size();
                results.values = mCruiseItemsDataAll;
            }
            else{
                String filterPattern = charSequence.toString().toLowerCase().trim();

                for(CruiseItem item : mCruiseItemsDataAll){
                    if(item.getStart().toLowerCase().contains(filterPattern) || item.getEnd().toLowerCase().contains(filterPattern) || item.getDate().contains(filterPattern)){
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults){
            mCruiseItemsData = (ArrayList) filterResults.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView mStartText;
        private TextView mEndText;
        private TextView mDateText;
        private ImageView mItemImage;
        private TextView mPeopleText;
        private Button mUpdateButton;
        private Button mDeleteButton;
        private String uid;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mStartText = itemView.findViewById(R.id.startText);
            mEndText = itemView.findViewById(R.id.endText);
            mDateText = itemView.findViewById(R.id.dateText);
            mItemImage = itemView.findViewById(R.id.image);
            mPeopleText = itemView.findViewById(R.id.peopleNumber);
            mUpdateButton = itemView.findViewById(R.id.updateButton);
            mDeleteButton = itemView.findViewById(R.id.deleteButton);
        }

        public void bindTo(CruiseItem currentItem) {
            mStartText.setText(currentItem.getStart());
            mEndText.setText(currentItem.getEnd());
            mDateText.setText(currentItem.getDate());
            mPeopleText.setText("1");
            Glide.with(mContext).load(currentItem.getImageResource()).into(mItemImage);
        }

    }
}
