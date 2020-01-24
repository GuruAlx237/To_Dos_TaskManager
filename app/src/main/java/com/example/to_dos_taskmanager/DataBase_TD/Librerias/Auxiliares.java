package com.example.to_dos_taskmanager.DataBase_TD.Librerias;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;

public class Auxiliares {
    public static  void pro_MenToClip(String men, Context ctx){
        ClipboardManager clipboard = (ClipboardManager)ctx.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = null;
        if (men.contains("Error") || men.contains("error")) {
            clip = ClipData.newPlainText("Error - Monkibot: ", men);
            System.out.println(men);
        }
        clipboard.setPrimaryClip(clip);
    }

    //region Esconder Teclado
    public static void pro_HideKey(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void pro_HideKey(Context ctx, View view) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void pro_HideKey(ViewGroup root, Context ctx) {
        View v;
        for (int i = 0; i<root.getChildCount(); i++){
            v = root.getChildAt(i);
            if(v.getClass().equals(AppCompatEditText.class) || v.getClass().equals(EditText.class)){
                pro_HideKey(ctx,v);
            }
        }
    }
    //endregion
}
