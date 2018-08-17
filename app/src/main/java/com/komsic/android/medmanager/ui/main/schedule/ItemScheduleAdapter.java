package com.komsic.android.medmanager.ui.main.schedule;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.komsic.android.medmanager.R;

import java.util.List;

public class ItemScheduleAdapter extends
        RecyclerView.Adapter<ItemScheduleAdapter.ItemScheduleViewHolder> {

    private List<String> medNames;

    public ItemScheduleAdapter(List<String> medNames) {
        this.medNames = medNames;
    }

    @NonNull
    @Override
    public ItemScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemScheduleViewHolder(LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.item_med_name_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ItemScheduleViewHolder holder, int position) {
        if (medNames != null) {
            String medName = medNames.get(position);
            holder.scheduleCheckBox.setText(medName);
        }
    }

    @Override
    public int getItemCount() {
        return medNames.size();
    }

    class ItemScheduleViewHolder extends RecyclerView.ViewHolder {
        CheckBox scheduleCheckBox;

        ItemScheduleViewHolder(View itemView) {
            super(itemView);

            scheduleCheckBox = itemView.findViewById(R.id.checkbox);
        }
    }

}
