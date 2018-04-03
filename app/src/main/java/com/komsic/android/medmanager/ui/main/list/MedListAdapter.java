package com.komsic.android.medmanager.ui.main.list;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.model.Med;
import com.komsic.android.medmanager.ui.detail.DetailActivity;
import com.komsic.android.medmanager.util.CalendarUtil;

import java.util.ArrayList;
/**
 * Created by komsic on 4/2/2018.
 */

public class MedListAdapter extends
        RecyclerView.Adapter<MedListAdapter.MedListViewHolder> {

    private final Context mContext;
    private ArrayList<Med> mMedList;

    public MedListAdapter(Context context) {
        this.mContext = context;
        mMedList = new ArrayList<>();
    }

    @Override
    public MedListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MedListViewHolder(LayoutInflater
                .from(mContext)
                .inflate(R.layout.item_med_list, parent, false));
    }

    @Override
    public void onBindViewHolder(MedListViewHolder holder, int position) {
        final Med currentMed = mMedList.get(position);
        holder.textMedName.setText(currentMed.name);
        holder.textStartDate.setText(String.format("%s - %s",
                mContext.getString(R.string.msg_start_date), CalendarUtil.getDateInString(currentMed.startDate)));
        holder.textEndDate.setText(String.format("%s - %s",
                mContext.getString(R.string.msg_end_date), CalendarUtil.getDateInString(currentMed.endDate)));

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
        return mMedList.size();
    }

    public void addMed(Med newMed) {
        mMedList.add(newMed);
        notifyItemInserted(mMedList.size() - 1);
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
}
