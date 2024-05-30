package com.FCAI.GPA;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

public class Settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.indigo));
        FloatingActionButton bs= findViewById(R.id.save);
        bs.setOnClickListener(v -> savee());
        Button backk= findViewById(R.id.back_button);
        backk.setOnClickListener(v -> finish());
    }
    @Override
    protected void onResume() {
        EditText Ap = findViewById(R.id.editTextNumberap);
        EditText A = findViewById(R.id.editTextNumbera);
        EditText Am = findViewById(R.id.editTextNumberam);
        EditText Bp = findViewById(R.id.editTextNumberbp);
        EditText B = findViewById(R.id.editTextNumberb);
        EditText Bm = findViewById(R.id.editTextNumberbm);
        EditText Cp = findViewById(R.id.editTextNumbercp);
        EditText C = findViewById(R.id.editTextNumberc);
        EditText Cm = findViewById(R.id.editTextNumbercm);
        EditText Dp = findViewById(R.id.editTextNumberdp);
        EditText D = findViewById(R.id.editTextNumberd);
        EditText Dm = findViewById(R.id.editTextNumberdm);
        //try {
        super.onResume();
        SharedPreferences sh = getSharedPreferences("pp", MODE_PRIVATE);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
        Ap.setText(Objects.equals(sh.getString("A+", ""), "") ? "4.0" : sh.getString("A+", ""));
        A.setText(Objects.equals(sh.getString("A", ""), "") ? "3.7" : sh.getString("A", ""));
        Am.setText(Objects.equals(sh.getString("A-", ""), "") ? "" : sh.getString("A-", ""));
        Bp.setText(Objects.equals(sh.getString("B+", ""), "") ? "3.3" : sh.getString("B+", ""));
        B.setText(Objects.equals(sh.getString("B", ""), "") ? "3.0" : sh.getString("B", ""));
        Bm.setText(Objects.equals(sh.getString("B-", ""), "") ? "" : sh.getString("B-", ""));
        Cp.setText(Objects.equals(sh.getString("C+", ""), "") ? "2.7" : sh.getString("C+", ""));
        C.setText(Objects.equals(sh.getString("C", ""), "") ? "2.4" : sh.getString("C", ""));
        Cm.setText(Objects.equals(sh.getString("C-", ""), "") ? "" : sh.getString("C-", ""));
        Dp.setText(Objects.equals(sh.getString("D+", ""), "") ? "2.2" : sh.getString("D+", ""));
        D.setText(Objects.equals(sh.getString("D", ""), "") ? "2.0" : sh.getString("D", ""));
        Dm.setText(Objects.equals(sh.getString("D-", ""), "") ? "" : sh.getString("D-", ""));
    //}catch(Exception ignored){}
    }
    protected void savee() {
        EditText Ap=findViewById(R.id.editTextNumberap);
        EditText A=findViewById(R.id.editTextNumbera);
        EditText Am=findViewById(R.id.editTextNumberam);
        EditText Bp=findViewById(R.id.editTextNumberbp);
        EditText B=findViewById(R.id.editTextNumberb);
        EditText Bm=findViewById(R.id.editTextNumberbm);
        EditText Cp=findViewById(R.id.editTextNumbercp);
        EditText C=findViewById(R.id.editTextNumberc);
        EditText Cm=findViewById(R.id.editTextNumbercm);
        EditText Dp=findViewById(R.id.editTextNumberdp);
        EditText D=findViewById(R.id.editTextNumberd);
        EditText Dm=findViewById(R.id.editTextNumberdm);
        //try {
            SharedPreferences sharedPreferences = getSharedPreferences("pp", MODE_PRIVATE);
            SharedPreferences.Editor myEdit = sharedPreferences.edit();
        if(!Ap.getText().toString().equals("")){myEdit.putString("A+",Ap.getText().toString());}
        else{myEdit.putString("A+","");}
        if(!A.getText().toString().equals("")){myEdit.putString("A",A.getText().toString());}
        else{myEdit.putString("A","");}
        if(!Am.getText().toString().equals("")){myEdit.putString("A-",Am.getText().toString());}
        else{myEdit.putString("A-","");}
        if(!Bp.getText().toString().equals("")){myEdit.putString("B+",Bp.getText().toString());}
        else{myEdit.putString("B+","");}
        if(!B.getText().toString().equals("")){myEdit.putString("B",B.getText().toString());}
        else{myEdit.putString("B","");}
        if(!Bm.getText().toString().equals("")){myEdit.putString("B-",Bm.getText().toString());}
        else{myEdit.putString("B-","");}
        if(!Cp.getText().toString().equals("")){myEdit.putString("C+",Cp.getText().toString());}
        else{myEdit.putString("C+","");}
        if(!C.getText().toString().equals("")){myEdit.putString("C",C.getText().toString());}
        else{myEdit.putString("C","");}
        if(!Cm.getText().toString().equals("")){myEdit.putString("C-",Cm.getText().toString());}
        else{myEdit.putString("C-","");}
        if(!Dp.getText().toString().equals("")){myEdit.putString("D+",Dp.getText().toString());}
        else{myEdit.putString("D+","");}
        if(!D.getText().toString().equals("")){myEdit.putString("D",D.getText().toString());}
        else{myEdit.putString("D","");}
        if(!Dm.getText().toString().equals("")){myEdit.putString("D-",Dm.getText().toString());}
        else{myEdit.putString("D-","");}
            myEdit.apply();
            Toast.makeText(getApplicationContext(),"GPA Points updated",Toast.LENGTH_SHORT).show();
        //}catch (Exception ignored){}
    }
}
