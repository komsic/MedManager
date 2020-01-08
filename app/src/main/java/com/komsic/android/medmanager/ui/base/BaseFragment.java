package com.komsic.android.medmanager.ui.base;

import android.content.Context;

import androidx.fragment.app.Fragment;

/**
 * Created by komsic on 4/2/2018.
 */

public abstract class BaseFragment extends Fragment implements MvpView {

    private BaseActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity activity = (BaseActivity) context;
            this.mActivity = activity;
            activity.onFragmentAttached();
        }
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }

    public interface Callback {

        void onFragmentAttached();

        void onFragmentDetached(String tag);

    }

}
