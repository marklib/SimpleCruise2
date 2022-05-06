package com.example.simplecruise;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.AnimatorRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.bumptech.glide.Glide;

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mStartText = itemView.findViewById(R.id.startText);
            mEndText = itemView.findViewById(R.id.endText);
            mDateText = itemView.findViewById(R.id.dateText);
            mItemImage = itemView.findViewById(R.id.image);

            itemView.findViewById(R.id.bookButton).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){

                }
            });
        }

        public void bindTo(CruiseItem currentItem) {
            mStartText.setText(currentItem.getStart());
            mEndText.setText(currentItem.getEnd());
            mDateText.setText(currentItem.getDate());
            Glide.with(mContext).load(currentItem.getImageResource()).into(mItemImage);
        }
    }
}


