package com.example.simplecruise;

import android.content.Context;
import android.text.InputType;
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

import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class CruiseItemAdapter extends RecyclerView.Adapter<CruiseItemAdapter.ViewHolder> implements Filterable {

    private ArrayList<CruiseItem> mCruiseItemsData;
    private ArrayList<CruiseItem> mCruiseItemsDataAll;
    private Context mContext;
    private int lastPosition = -1;

    CruiseItemAdapter(Context context, ArrayList<CruiseItem> itemsData){
        this.mCruiseItemsData = itemsData;
        this.mCruiseItemsDataAll = itemsData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CruiseItemAdapter.ViewHolder holder, int position) {
        CruiseItem currentItem = mCruiseItemsData.get(position);

        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPosition){
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAbsoluteAdapterPosition();
        }

        holder.mBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore.getInstance().collection("Bookings").get().addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot document : queryDocumentSnapshots){
                        BookingItem item = document.toObject(BookingItem.class);
                        if(item.getCruiseStart().equals(holder.mStartText) && item.getUserEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                            CollectionReference cf = FirebaseFirestore.getInstance().collection("Bookings");
                            cf.document(document.getId()).set(new BookingItem(item.getCruiseStart(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), Integer.parseInt(holder.mPeopleText.getText().toString())+item.getPeople()));
                            break;
                        }
                    }
                });
                FirebaseFirestore.getInstance().collection("Bookings").add(new BookingItem(holder.mStartText.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail(), Integer.parseInt(holder.mPeopleText.getText().toString())));
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
        private Button mBook;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mStartText = itemView.findViewById(R.id.startText);
            mEndText = itemView.findViewById(R.id.endText);
            mDateText = itemView.findViewById(R.id.dateText);
            mItemImage = itemView.findViewById(R.id.image);
            mPeopleText = itemView.findViewById(R.id.peopleNumber);
            mBook = itemView.findViewById(R.id.bookButton);
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


