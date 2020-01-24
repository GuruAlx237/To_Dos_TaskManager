package com.example.to_dos_taskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.amitshekhar.DebugDB;
import com.example.to_dos_taskmanager.DataBase_TD.DB_To_Dos;
import com.getbase.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton _fab_add;
    LinearLayout _root;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Toast.makeText(this, DebugDB.getAddressLog(), Toast.LENGTH_SHORT).show();

        _root = findViewById(R.id.lay_root);
        pro_ListTask();

        _fab_add = findViewById(R.id.fab_add_tsk);
        _fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dia_Tsk_AEE dia = new Dia_Tsk_AEE();
                dia.tarea = null;
                dia.ilis = new Dia_Tsk_AEE.Int_Dia_Tsk_AEE() {
                    @Override
                    public void pro_ok(DB_To_Dos.TAB_Tareas tsk) {
                        pro_ListTask();
                    }

                    @Override
                    public void pro_del(String id) {
                        pro_ListTask();
                    }
                };
                dia.show(getSupportFragmentManager(),"ADD_TASK");
            }
        });

        //DB_To_Dos.TAB_Tareas tar = new DB_To_Dos.TAB_Tareas("Alx","12/07/94","Desputi");
        //Insertar
        //DB_To_Dos.bol_Insertar(tar,true,getApplicationContext());

        //DB_To_Dos.TAB_Tareas tar = new DB_To_Dos.TAB_Tareas("Alx","12/07/94","Desputi");
        //Upate
        /*DB_To_Dos.int_Update(tar,
                String.format("%s = '%s'", DB_To_Dos.TAB_Tareas.Campos.ID.getNom(),tar.getId_nombre()),
                true,getApplicationContext());*/

        //Delete
        /*final DB_To_Dos.TAB_Tareas tar = new DB_To_Dos.TAB_Tareas("Alx","12/07/94","Desputi");
        DB_To_Dos.int_Delete(DB_To_Dos.TAB_Tareas.getNombre(),
                String.format("%s = '%s'", DB_To_Dos.TAB_Tareas.Campos.ID.getNom(),"Alx")
                ,true,getApplicationContext());*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mnu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case  R.id.mnu_edt_usr:
                Toast.makeText(this, "Edit User", Toast.LENGTH_SHORT).show();
                return  true;
            case  R.id.mnu_clo_ses:
                Intent myIntent = new Intent(getApplicationContext(), Act_log.class);
                startActivity(myIntent);
                finish();
                return  true;
                default:
                    return super.onOptionsItemSelected(item);
        }

    }

    private void pro_ListTask() {
        _root.removeAllViews();

        Cursor cur =  DB_To_Dos.cur_Select("*",
                DB_To_Dos.TAB_Tareas.Nombre,
                "",
                false,getApplicationContext());

        if(cur.moveToFirst()){
            do{
                final Tarea tr = new Tarea(getApplicationContext());
                tr.ilis = new Tarea.Int_Tarea() {
                    @Override
                    public void pro_Edt(DB_To_Dos.TAB_Tareas tsk) {
                        Dia_Tsk_AEE dt = new Dia_Tsk_AEE();
                        dt.tarea = tsk;
                        dt.ilis = new Dia_Tsk_AEE.Int_Dia_Tsk_AEE() {
                            @Override
                            public void pro_ok(final DB_To_Dos.TAB_Tareas tsk) {
                                pro_ListTask();
                            }


                            @Override
                            public void pro_del(String id) {
                                pro_ListTask();
                            }
                        };

                        dt.show(getSupportFragmentManager(),"TASK_AEE");
                    }

                    @Override
                    public void pro_Del(String id) {
                        DB_To_Dos.int_Delete(DB_To_Dos.TAB_Tareas.getNombre(),
                                String.format("%s = '%s'", DB_To_Dos.TAB_Tareas.Campos.ID.getNom(),tr.get_nam())
                                ,true,getApplicationContext());
                        pro_ListTask();
                    }
                };
                tr.set_nam(cur.getString(0));
                tr.set_dat(cur.getString(1));
                tr.set_des(cur.getString(2));

                _root.addView(tr);

            }while (cur.moveToNext());
        }


    }
}
