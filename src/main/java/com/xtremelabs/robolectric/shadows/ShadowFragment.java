package com.xtremelabs.robolectric.shadows;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.xtremelabs.robolectric.internal.Implementation;
import com.xtremelabs.robolectric.internal.Implements;
import com.xtremelabs.robolectric.internal.RealObject;

import java.lang.reflect.Field;

/**
 * Shadow class for "Fragment".  Note that this is for the support package v4 version of "Fragment", not the android.app
 * one.
 */
@SuppressWarnings({"UnusedDeclaration"})
@Implements(Fragment.class)
public class ShadowFragment {
    @RealObject
    Fragment realFragment;

    private Bundle arguments;
    private FragmentActivity fragmentActivity;
    private View view;

    private int fragmentId;
    private String tag;

    private Fragment targetFragment;
    private boolean resumed;
    private boolean visible;

    @Implementation
    public void setArguments(Bundle bundle) {
        arguments = bundle;
    }

    @Implementation
    public Bundle getArguments() {
        return arguments;
    }

    @Implementation
    public FragmentActivity getActivity() {
        return fragmentActivity;
    }

    @Implementation
    public Resources getResources() {
        return getActivity().getResources();
    }

    @Implementation
    public final CharSequence getText(int resId) {
        return getResources().getText(resId);
    }

    @Implementation
    public final String getString(int resId) {
        return getResources().getString(resId);
    }

    @Implementation
    public final String getString(int resId, Object... formatArgs) {
        return getResources().getString(resId, formatArgs);
    }

    @Implementation
    public View getView() {
        return view;
    }

    @Implementation
    public FragmentManager getFragmentManager() {
        return getActivity().getSupportFragmentManager();
    }

    @Implementation
    public int getId() {
        return fragmentId;
    }

    @Implementation
    public String getTag() {
        return tag;
    }

    @Implementation
    public boolean isAdded() {
        return fragmentActivity != null;
    }

    @Implementation
    public boolean isVisible() {
        return isAdded() && visible;
    }

    @Implementation
    public boolean isHidden() {
        return !visible;
    }

    @Implementation
    public Fragment getTargetFragment() {
        return targetFragment;
    }

    @Implementation
    public void setTargetFragment(Fragment targetFragment, int requestCode) {
        this.targetFragment = targetFragment;
    }

    @Implementation
    public void onResume() {
        this.resumed = true;
    }

    public void resume() {
        realFragment.onResume();
    }

    @Implementation
    public void onPause() {
        this.resumed = false;
    }

    public void pause() {
        realFragment.onPause();
    }

    @Implementation
    public void startActivity(Intent intent) {
        if (fragmentActivity == null) {
            throw new IllegalStateException("Fragment " + this + " not attached to Activity");
        }
        fragmentActivity.startActivity(intent);
    }

    @Implementation
    public void startActivityForResult(Intent intent, int requestCode) {
        if (fragmentActivity == null) {
            throw new IllegalStateException("Fragment " + this + " not attached to Activity");
        }
        fragmentActivity.startActivityForResult(intent, requestCode);
    }

    @Implementation
    public boolean isResumed() {
        return resumed;
    }

    public void setActivity(FragmentActivity activity) {
        if (fragmentActivity != null) realFragment.onDetach();
        fragmentActivity = activity;
        visible = true;
        if (activity != null) realFragment.onAttach(activity);
        try {
            Field field = Fragment.class.getDeclaredField("mActivity");
            field.setAccessible(true);
            field.set(realFragment, activity);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to set mActivity field");
        }
    }

    public void createView() {
        final FragmentActivity activity = getActivity();
        view = realFragment.onCreateView(activity.getLayoutInflater(), null, null);
        realFragment.onViewCreated(view, null);
    }

    public void setFragmentId(int fragmentId) {
        this.fragmentId = fragmentId;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void show() {
        visible = true;
    }

    public void hide() {
        visible = false;
    }
}
