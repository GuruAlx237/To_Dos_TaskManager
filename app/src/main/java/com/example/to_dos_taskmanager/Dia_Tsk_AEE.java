package com.example.to_dos_taskmanager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.to_dos_taskmanager.DataBase_TD.DB_To_Dos;
import com.example.to_dos_taskmanager.DataBase_TD.Librerias.Auxiliares;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Dia_Tsk_AEE extends DialogFragment {

    private static final String TAG = "Dia_Tsk_AEE";
    public DB_To_Dos.TAB_Tareas tarea;

    ViewGroup vg_root;
    EditText _edt_nam,_edt_des;
    CalendarView _clv_dat;

    TextView _txv_ok, _txv_can,_txv_del;

    public Int_Dia_Tsk_AEE ilis;

    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder bui = new AlertDialog.Builder(getActivity());
        LayoutInflater inf = getActivity().getLayoutInflater();
        //Generar la interfaz
        final View v = inf.inflate(R.layout.dia_tsk_aee, null);
        bui.setView(v)
                .setTitle(R.string.task)
                .setCancelable(true)
                .setIcon(R.drawable.chromatic_logo);

        vg_root =v.findViewById(R.id.lay_root);

        _edt_nam = v.findViewById(R.id.edt_nam);


        _clv_dat = v.findViewById(R.id.clv_dat);
        _clv_dat.setDate(System.currentTimeMillis(),true,true);
        _clv_dat.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Log.d(TAG, "onSelectedDayChange: " + String.format("%d/%d/%d" , dayOfMonth,month,year));
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                long milliTime = calendar.getTimeInMillis();
                _clv_dat.setDate (milliTime, true, true);

                Log.d(TAG, "onSelectedDayChange: " + _clv_dat.getDate());

            }
        });

        _edt_des = v.findViewById(R.id.edt_des);


        _txv_ok = v.findViewById(R.id.txv_dia_col_ok);
        _txv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Auxiliares.pro_HideKey(vg_root,getContext());
                if(pro_Validar()){

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

                    final DB_To_Dos.TAB_Tareas tar = new DB_To_Dos.TAB_Tareas(_edt_nam.getText().toString(),
                            sdf.format(new Date(_clv_dat.getDate())),
                            _edt_des.getText().toString());

                    Cursor c = DB_To_Dos.cur_Select("*", DB_To_Dos.TAB_Tareas.Nombre,
                            String.format("%s = '%s'", DB_To_Dos.TAB_Tareas.Campos.ID.getNom(),(tarea!=null?tarea.getId_nombre():"")),
                            false,getContext());

                    if(c.getCount()==0){
                        DB_To_Dos.bol_Insertar(tar,true,getContext());
                        dismiss();
                        ilis.pro_ok(tar);
                    }
                    else if(tar!=null){
                        Log.d(TAG, "onClick: nom" + tar.getId_nombre());
                        Log.d(TAG, "onClick: fec" + tar.getFecha());
                        Log.d(TAG, "onClick: des" + tar.getDescripcion());
                        DB_To_Dos.int_Update(tar,
                                String.format("%s = '%s'", DB_To_Dos.TAB_Tareas.Campos.ID.getNom(),tarea.getId_nombre()),
                                true,getContext());
                    }


                    ilis.pro_ok(tarea);
                    dismiss();
                }
                else {
                    Toast.makeText(v.getContext(), R.string.val_tsk, Toast.LENGTH_SHORT).show();
                }
            }
        });

        _txv_can = v.findViewById(R.id.txv_dia_col_can);
        _txv_can.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        _txv_del= v.findViewById(R.id.txv_dia_col_eli);
        _txv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DB_To_Dos.int_Delete(DB_To_Dos.TAB_Tareas.Nombre,
                        String.format("%s = '%s'", DB_To_Dos.TAB_Tareas.Campos.ID.getNom(),tarea.getId_nombre()),
                        true,getContext());
                dismiss();
                ilis.pro_del(tarea.getId_nombre());
            }
        });



        if(tarea!=null){
            _txv_del.setVisibility(View.VISIBLE);
            _txv_ok.setText("Actualizar");
            _edt_nam.setText(tarea.getId_nombre());
            _edt_des.setText(tarea.getDescripcion());

            try {
                String parts[] = tarea.getFecha().split("/");

                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1])-1;
                int year = Integer.parseInt(parts[2]);

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                long milliTime = calendar.getTimeInMillis();
                _clv_dat.setDate (milliTime, true, true);
            }
            catch (NumberFormatException e) {
                _clv_dat.setDate(System.currentTimeMillis(),false,true);
                Toast.makeText(getContext(), R.string.invalid_date, Toast.LENGTH_SHORT).show();
            }
        }

        return bui.create();
    }


    private boolean pro_Validar() {
        boolean ok = true;

        if(_edt_nam.getText().toString().isEmpty()){
            return  false;
        }

        if(_edt_des.getText().toString().isEmpty()){
            _edt_des.setText("-");
        }



        return  ok;
    }

    public  interface Int_Dia_Tsk_AEE{
        void pro_ok(DB_To_Dos.TAB_Tareas tsk);

        void pro_del(String id);
    }
}
