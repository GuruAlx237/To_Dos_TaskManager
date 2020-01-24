package com.example.to_dos_taskmanager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.to_dos_taskmanager.DataBase_TD.DB_To_Dos;


public class Tarea  extends LinearLayout {

    Int_Tarea ilis;

    private String _nam, _dat, _des;

    //region Get&Set

    public String get_nam() {
        return _nam;
    }

    public void set_nam(String _nam) {
        _txv_nam.setText(_nam);
        this._nam = _nam;
    }

    public String get_dat() {
        return _dat;
    }

    public void set_dat(String _dat) {
        _txv_dat.setText(_dat);
        this._dat = _dat;
    }

    public String get_des() {
        return _des;
    }

    public void set_des(String _des) {
        this._des = _des;
    }
    //endregion

    private TextView _txv_nam, _txv_dat;
    private ImageView _img_edt, _img_del;

    //region Constructores
    public Tarea(Context ctx) {
        super(ctx);
        iniViews(ctx,null);
    }

    public Tarea(Context ctx, AttributeSet attrs) {
        super(ctx, attrs);
        iniViews(ctx,attrs);
    }

    public Tarea(Context ctx, AttributeSet attrs, int defStyleAttr) {
        super(ctx, attrs, defStyleAttr);
        iniViews(ctx,attrs);
    }
    //endregion

    private void iniViews(final Context ctx, AttributeSet attrs) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.com_vie_tarea, this);

        _txv_nam = findViewById(R.id.txv_nam);

        _txv_dat = findViewById(R.id.txv_dat);

        _img_edt = findViewById(R.id.img_edt);
        _img_edt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ilis!=null) {
                    ilis.pro_Edt(new DB_To_Dos.TAB_Tareas(_nam,_dat,_des));
                }
                else {
                    Toast.makeText(ctx, "No se añadió una interfáz de interacción", Toast.LENGTH_SHORT).show();
                }
            }
        });

        _img_del = findViewById(R.id.img_del);
        _img_del.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ilis!=null) {
                    ilis.pro_Del(_nam);
                }
                else {
                    Toast.makeText(ctx, "No se añadió una interfáz de interacción", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public interface Int_Tarea{
        void pro_Edt(DB_To_Dos.TAB_Tareas tsk);
        void pro_Del(String id);
    }
}
