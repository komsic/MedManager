package com.komsic.android.medmanager.ui.base;

import android.content.Context;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by komsic on 4/2/2018.
 */

public abstract class BaseDialog extends DialogFragment implements DialogMvpView{
    private BaseActivity mActivity;

    @Override
    public void dismissDialog(String tag) {
        dismiss();
        getBaseActivity().onFragmentDetached(tag);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            BaseActivity mActivity = (BaseActivity) context;
            this.mActivity = mActivity;
            mActivity.onFragmentAttached();
        }
    }

    public void show(FragmentManager fragmentManager, String tag) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Fragment prevFragment = fragmentManager.findFragmentByTag(tag);
        if (prevFragment != null) {
            transaction.remove(prevFragment);
        }
        transaction.addToBackStack(null);
        show(transaction, tag);
    }

    @Override
    public void onDetach() {
        mActivity = null;
        super.onDetach();
    }

    public BaseActivity getBaseActivity() {
        return mActivity;
    }
}
