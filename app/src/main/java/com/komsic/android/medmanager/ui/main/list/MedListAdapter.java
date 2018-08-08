package com.komsic.android.medmanager.ui.main.list;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.model.Med;
import com.komsic.android.medmanager.ui.detail.DetailActivity;
import com.komsic.android.medmanager.util.CalendarUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by komsic on 4/2/2018.
 */

public class MedListAdapter extends RecyclerView.Adapter<MedListAdapter.MedListViewHolder>
        implements Filterable{

    private final Context mContext;
    private ArrayList<Med> mMedList;
    private ArrayList<Med> filteredList;
    private MedFilter mMedFilter;

    public MedListAdapter(Context context) {
        this.mContext = context;
        mMedList = new ArrayList<>();
        filteredList = mMedList;

    }

    @Override
    public MedListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MedListViewHolder(LayoutInflater
                .from(mContext)
                .inflate(R.layout.item_med_list, parent, false));
    }

    @Override
    public void onBindViewHolder(MedListViewHolder holder, int position) {
        final Med currentMed = filteredList.get(position);

        holder.textMedName.setText(currentMed.name);

        holder.textStartDate.setText(String.format("%s - %s",
                mContext.getString(R.string.msg_start_date),
                CalendarUtil.getDateInString(currentMed.startDate)));

        holder.textEndDate.setText(String.format("%s - %s",
                mContext.getString(R.string.msg_end_date),
                CalendarUtil.getDateInString(currentMed.endDate)));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("key", currentMed.id);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void addMedList(List<Med> newMedList) {
        if (newMedList != null) {
            mMedList.clear();
            mMedList.addAll(newMedList);
            filteredList = mMedList;
            notifyItemInserted(filteredList.size() - 1);
        }
//        if (!mMedList.contains(newMed)) {
//            mMedList.add(newMed);
//            filteredList = mMedList;
//            notifyItemInserted(filteredList.size() - 1);
//        }
    }

    public void sortMedList() {
        Collections.sort(filteredList);
        Collections.sort(mMedList);
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {
        if (mMedFilter == null) {
            mMedFilter = new MedFilter();
        }
        return mMedFilter;
    }

    public class MedListViewHolder extends RecyclerView.ViewHolder {
        TextView textMedName;
        TextView textStartDate;
        TextView textEndDate;
        public MedListViewHolder(View itemView) {
            super(itemView);

            textMedName = itemView.findViewById(R.id.text_item_list_med_name);
            textStartDate = itemView.findViewById(R.id.text_start_date);
            textEndDate = itemView.findViewById(R.id.text_end_date);
        }
    }

    public class MedFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Med> tempList = new ArrayList<>();

                for (Med med : mMedList) {
                    if (med.name.toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(med);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = mMedList.size();
                filterResults.values = mMedList;
            }

            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            filteredList = (ArrayList<Med>) results.values;

            notifyDataSetChanged();
        }
    }
}
