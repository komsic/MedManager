package com.komsic.android.medmanager.ui.detail.choose_day;

import android.support.v4.content.res.ResourcesCompat;
import android.view.View;

import com.komsic.android.medmanager.R;
import com.komsic.android.medmanager.data.DataManager;
import com.komsic.android.medmanager.ui.base.BasePresenter;

import java.util.Map;


/**
 * Created by komsic on 4/3/2018.
 */

public class ChooseDayDialogPresenter<V extends ChooseDayDialogMvpView> extends BasePresenter<V>
        implements ChooseDayDialogMvpPresenter<V> {

    public ChooseDayDialogPresenter(DataManager dataManager) {
        super(dataManager);
    }


    @Override
    public void onDismiss() {
        getMvpView().dismissDialog("");
    }

    public void init() {
        getMvpView().init(getDataManager().getDayStateMap());
    }

    public void setDayStateMap(Map<String, Boolean> dayStateMap) {
        getDataManager().setDayStateMap(dayStateMap);
    }

    void onClick(View v, int count) {
        if (getDataManager().getDayStateMap().get(DataManager.daysOfTheWeek[count])) {
            v.setBackground(ResourcesCompat.getDrawable(v.getContext().getResources(),
                    R.drawable.day_unselect, null));
            getDataManager().getDayStateMap().put(DataManager.daysOfTheWeek[count], false);
        } else {
            v.setBackground(ResourcesCompat.getDrawable(v.getContext().getResources(),
                    R.drawable.day_select, null));
            getDataManager().getDayStateMap().put(DataManager.daysOfTheWeek[count], true);
        }
    }
}
