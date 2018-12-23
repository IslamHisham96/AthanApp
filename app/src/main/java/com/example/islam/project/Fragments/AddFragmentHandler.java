package com.example.islam.project.Fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.example.islam.project.R;

public class AddFragmentHandler {
    private final FragmentManager fragmentManager;
    private final int fragmentFrame;

    public AddFragmentHandler(FragmentManager fragmentManager, int fragmentFrame) {
        this.fragmentManager = fragmentManager;
        this.fragmentFrame = fragmentFrame;
    }

    public void add(MyFragment fragment) {
        //don't add a fragment of the same type on top of itself.
        MyFragment currentFragment = getCurrentFragment();
        if (currentFragment != null) {
            if (currentFragment.getClass() == fragment.getClass()) {
                Log.w("Fragment Manager", "Tried to add a fragment of the same type to the backstack. This may be done on purpose in some circumstances but generally should be avoided.");
                return;
            }
        }

        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.slide_out_right)
                .replace(fragmentFrame, fragment, fragment.getTitle())
                .addToBackStack(fragment.getTitle())
                .commit();
    }

    @Nullable
    public MyFragment getCurrentFragment() {
        if (fragmentManager.getBackStackEntryCount() == 0) {
            return null;
        }
        FragmentManager.BackStackEntry currentEntry = fragmentManager.getBackStackEntryAt(fragmentManager.getBackStackEntryCount() - 1);

        String tag = currentEntry.getName();
        Fragment fragment = fragmentManager.findFragmentByTag(tag);
        return (MyFragment) fragment;
    }
}