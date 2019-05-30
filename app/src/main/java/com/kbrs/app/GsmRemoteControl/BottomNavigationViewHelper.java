package com.kbrs.app.GsmRemoteControl;

import android.annotation.SuppressLint;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import java.lang.reflect.Field;

class BottomNavigationViewHelper {
    @SuppressLint("RestrictedApi")
    static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            /*Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);*/
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                //noinspection RestrictedApi
                //item.setShiftingMode(false);
                //item.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED); // 28.0.0
                // set once again checked value, so view will be updated
                //noinspection RestrictedApi
                item.setChecked(item.getItemData().isChecked());
            }
        }/* catch (NoSuchFieldException e) {
            Log.e("BottomNavViewHelper", "Unable to get shift mode field", e);
        } catch (IllegalAccessException e) {
            Log.e("BottomNavViewHelper", "Unable to change value of shift mode", e);
        }*/
        catch (Exception e){
            Log.e("BottomNavViewHelper", "Unable to get shift mode field", e);
        }
    }
}