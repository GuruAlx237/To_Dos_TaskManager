package com.example.to_dos_taskmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Act_log extends AppCompatActivity {
    private static final String TAG = "Act_log";

    EditText _edt_usr,_edt_pass;
    TextView _txv_log;
    ImageView _img_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_log);

        _img_logo = findViewById(R.id.img_logo);
        _img_logo.setImageResource(R.drawable.chromatic_logo);

        _edt_usr = findViewById(R.id.edt_usr);

        _edt_pass = findViewById(R.id.edt_pass);

        _txv_log = findViewById(R.id.txv_log);
        _txv_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String usr = _edt_usr.getText().toString(),
                pass = _edt_pass.getText().toString();


                if(!(usr.isEmpty() && pass.isEmpty())){
                    if(usr.equals("alx") && pass.equals("23")){
                        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
                        Bundle b = new Bundle();
                        b.putString("_par_usr", usr);
                        myIntent.putExtras(b);
                        startActivity(myIntent);
                        finish();
                    }
                    else {
                        Toast.makeText(Act_log.this, R.string.check_usr_pass, Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(Act_log.this, R.string.check_data, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
