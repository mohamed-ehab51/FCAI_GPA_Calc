package com.FCAI.GPA;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    Dictionary<String, Double> dict = new Hashtable<>();
    TextView no ;
    ArrayList<Subject> data=new ArrayList<>();
    ArrayList<Subject> throwdata=new ArrayList<>();
    private boolean isSelectionModeEnabled;
    private final ArrayList<Vie> vies=new ArrayList<>();
    private final ArrayList<Vie> delvies=new ArrayList<>();

    TextView G ;
    Dialog d;
    TextView Ho ;
    ArrayList<String> grades= new ArrayList<>();
    String[] Hours = {"0","2","3","6"};
    AutoCompleteTextView aut;
    LinearLayout lay;
    AutoCompleteTextView H;
    EditText na;
    ArrayAdapter<String> adapt;
    private boolean isDataLoading = true;
    ArrayAdapter<String> adapter;
    FloatingActionButton more,add,session,info;
    boolean closed = true;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
       try {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = this.getWindow();
        isSelectionModeEnabled=false;
        throwdata.clear();
        dict = new Hashtable<>();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.indigo));
        d=new Dialog(this);
        no=findViewById(R.id.textView2);
        G=findViewById(R.id.textView3);
        Ho=findViewById(R.id.textView4);
        lay=findViewById(R.id.linearLayout1);
        //settings=findViewById(R.id.setings);
        no.setText("No. Courses : "+(lay.getChildCount()-1));
        more = findViewById(R.id.butt);
        add = findViewById(R.id.add);
        info = findViewById(R.id.infodel);
        info.setOnClickListener(v-> {
            d.setContentView(R.layout.delete_tutorial);
            Objects.requireNonNull(d.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            @SuppressLint("CutPasteId") Button b= d.findViewById(R.id.button);
            b.setOnClickListener(v1 -> d.dismiss());
            d.show();
        });
            EditText Cgpa= findViewById(R.id.CGPA);
            EditText Chour= findViewById(R.id.CHours);
            TextWatcher textWatcher = new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    if(s.toString().equals(".")) {
                        if (Cgpa.getText().toString().equals(".")) {
                            Cgpa.setError("please enter a number");
                        }
                        if (Chour.getText().toString().equals(".")){
                            Chour.setError("please enter a number");
                        }
                    }
                    if (!isDataLoading) {
                        onPause();
                    }
                    CALC();
                    Ho.setText("Total  Hours : "+sum_hours());
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                }
            };

            Cgpa.addTextChangedListener(textWatcher);
            Chour.addTextChangedListener(textWatcher);
        session = findViewById(R.id.session);
        more.setOnClickListener(v -> {
            if (closed)
            {
                add.show();
                info.show();
                session.show();
                //settings.show();
                closed=false;
            }
            else {
                add.hide();
                info.hide();
                session.hide();
                //settings.hide();
                closed=true;
            }
        });
//        settings.setOnClickListener(v -> {
//            Intent intent = new Intent( getApplicationContext(),Settings.class);
//            startActivity(intent);
//        });
        add.setOnClickListener(v -> addrow());
        session.setOnClickListener(v -> {
            d.setContentView(R.layout.draftdialog);
            Objects.requireNonNull(d.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button yes= d.findViewById(R.id.button3);
            @SuppressLint("CutPasteId") Button no= d.findViewById(R.id.button);
            yes.setOnClickListener(v12 -> {
                collectall();
                d.dismiss();
                Intent intent = new Intent( getApplicationContext(),Sessions.class);
                intent.putParcelableArrayListExtra("data",throwdata);
                intent.putExtra("cgpa",Cgpa.getText().toString());
                intent.putExtra("chours",Chour.getText().toString());
                startActivity(intent);
            });
            no.setOnClickListener(v13 -> {
                d.dismiss();
                Intent intent = new Intent( getApplicationContext(),Sessions.class);
                intent.putExtra("cgpa",Cgpa.getText().toString());
                intent.putExtra("chours",Chour.getText().toString());
                startActivity(intent);
            });
            d.show();

        });
        Ho.setText("Total  Hours : "+sum_hours());
        G.setText("GPA : "+0.0);
        }catch (Exception ignored){}
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private void enableSelectionMode(View m) {
        try {
        isSelectionModeEnabled = true;
        //settings.hide();
        int alpha = 50;
        int color = Color.argb(alpha, 255, 255, 255);
        Button z= m.findViewById(R.id.button2);
        z.setBackgroundColor(color);
        z.setVisibility(View.VISIBLE);
        z.setTextColor(Color.WHITE);
        z.setText("✓");
        int f=(int)m.getTag();
        Vie a= new Vie(m,f);
        vies.add(a);
        more.setImageDrawable(getDrawable(R.drawable.baseline_delete_sweep_24));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                more.setTooltipText("Delete");
            }
            info.setImageDrawable(getDrawable(R.drawable.baseline_exit_to_app_24));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                info.setTooltipText("exit multiple selection mode");
            }
        info.show();

        info.setOnClickListener(v -> disableSelectionMode());
        add.setImageDrawable(getDrawable(R.drawable.baseline_content_copy_24));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                add.setTooltipText("copy subjects to one time draft");
            }
        add.show();
        add.setOnClickListener(v -> {
            collect();
            EditText Cgpa= findViewById(R.id.CGPA);
            EditText Chour= findViewById(R.id.CHours);
            Intent intent = new Intent( getApplicationContext(),Sessions.class);
            intent.putParcelableArrayListExtra("data",throwdata);
            intent.putExtra("cgpa",Cgpa.getText().toString());
            intent.putExtra("chours",Chour.getText().toString());
            startActivity(intent);
        });
        session.hide();
        closed=true;
        more.setOnClickListener(v -> {
            delvies.clear();
            delvies.addAll(vies);
            vies.clear();
            int i=0;
            for(;i<delvies.size();i++)
            {
                removeRowWithAnimation(delvies.get(i).vi,false);
            }
            if(delvies.size()>1)
            {
                showUndoOption(delvies.get(i-1).vi, (Integer) delvies.get(i-1).vi.getTag());
            }
        });
        for (int i = 1; i < lay.getChildCount(); i++) {
            View row = lay.getChildAt(i);
            Button b= row.findViewById(R.id.button2);
            b.setBackgroundColor(Color.TRANSPARENT);
            b.setVisibility(View.VISIBLE);
            b.setOnClickListener(v -> {
                int k=(int)row.getTag();
                Vie Q= new Vie(row,k);
                if(b.getText().equals(""))
                {
                    b.setBackgroundColor(color);
                    b.setTextColor(Color.WHITE);
                    b.setText("✓");
                    vies.add(Q);
                }
                else
                {
                    b.setBackgroundColor(Color.TRANSPARENT);
                    b.setText("");
                    for (Iterator<Vie> l = vies.iterator(); l.hasNext();) {
                        Vie b1 = l.next();
                        if ((k== b1.getPlace())&& row.equals(b1.getVi()))
                        {
                            l.remove();
                            if(vies.isEmpty())
                            {
                                disableSelectionMode();
                            }
                        }
                    }
                }
            });

        }
        z.setBackgroundColor(color);
        }catch (Exception ignored){}
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private void disableSelectionMode() {
        try {
        isSelectionModeEnabled = false;
        vies.clear();
        more.setImageDrawable(getDrawable(R.drawable.baseline_more_horiz_24));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                more.setTooltipText("Show options");
            }
        add.hide();
        info.hide();
        info.setOnClickListener(v-> {
            d.setContentView(R.layout.delete_tutorial);
            Objects.requireNonNull(d.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button b= d.findViewById(R.id.button);
            b.setOnClickListener(v1 -> d.dismiss());
            d.show();
        });
        add.setOnClickListener(v -> addrow());
        more.setOnClickListener(v -> {
            if (closed)
            {
                info.setImageDrawable(getDrawable(R.drawable.baseline_delete_sweep_24));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    info.setTooltipText("Show how to delete");
                }
                add.setImageDrawable(getDrawable(R.drawable.baseline_add_24));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    add.setTooltipText("Add a new subject");
                }
                add.show();
                info.show();
                session.show();
                //settings.show();
                closed=false;
            }
            else {
                add.hide();
                info.hide();
                session.hide();
                //settings.hide();
                closed=true;
            }
        });
        for (int i = 1; i < lay.getChildCount(); i++) {
            View row = lay.getChildAt(i);
            Button m=row.findViewById(R.id.button2);
            m.setVisibility(View.GONE);
            m.setBackgroundColor(Color.TRANSPARENT);
            m.setText("");
            TextInputLayout t= row.findViewById(R.id.editTextText1);
            TextInputLayout t1= row.findViewById(R.id.editTextText);
            EditText t3= row.findViewById(R.id.editTextText3);
            t3.setEnabled(true);
            t.setEnabled(true);
            t1.setEnabled(true);
        }
        vies.clear();
        session.hide();
        }catch (Exception ignored){}
    }
    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    private void addrow() {
        try {
        @SuppressLint("InflateParams") View v=getLayoutInflater().inflate(R.layout.row,null,false);
        aut = v.findViewById(R.id.autoCompleteTextView2);
        na=v.findViewById(R.id.editTextText3);
        H = v.findViewById(R.id.autoCompleteTextView20);
        H.setKeyListener(null);
        aut.setKeyListener(null);
        adapt = new ArrayAdapter<String>(this, R.layout.drop, grades) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(grades.get(position));
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(grades.get(position));
                return view;
            }
        };
        TextWatcher textWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                System.out.println("on pause 347");

                if (!isDataLoading) {
                onPause();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        };
        na.addTextChangedListener(textWatcher);
        adapter = new ArrayAdapter<String>(this, R.layout.drop, Hours) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(Hours[position]);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(Hours[position]);
                return view;
            }
        };
        aut.setAdapter(adapt);
        aut.setOnItemClickListener((parent, view, position, id) -> {
            CALC();
            System.out.println("on pause 380");
            if (!isDataLoading) {
            onPause();
            }
        });H.setAdapter(adapter);
        H.setOnItemClickListener((parent, view, position, id) -> {
            Ho.setText("Total  Hours : "+sum_hours());
            CALC();
            System.out.println("on pause 386");
            if (!isDataLoading) {
            onPause();
            }
        });
        lay.addView(v, lay.getChildCount());
        v.setTag(lay.getChildCount() - 1);
        LinearLayout rowLayout = v.findViewById(R.id.rowe);
        rowLayout.setOnTouchListener(new View.OnTouchListener() {
            private final GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
                    if(!isSelectionModeEnabled)
                    {
                        removeRowWithAnimation(v, e2.getX() > e1.getX());
                    }
                    return true;
                }

                @Override
                public void onLongPress(@NonNull MotionEvent e) {
                        enableSelectionMode(v);
                }
            });

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }

        });
        no.setText("No. Courses : "+(lay.getChildCount()-1));
        System.out.println("on pause 417");
        if (!isDataLoading) {
        onPause();
        }
        }catch (Exception ignored){}
    }
    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    private void retrieve_row(String n, int h, int g) {
        try {
        @SuppressLint("InflateParams") View v=getLayoutInflater().inflate(R.layout.row,null,false);
        na=v.findViewById(R.id.editTextText3);
        aut = v.findViewById(R.id.autoCompleteTextView2);
        H = v.findViewById(R.id.autoCompleteTextView20);
        H.setKeyListener(null);
        aut.setKeyListener(null);
        adapt = new ArrayAdapter<String>(this, R.layout.drop, grades) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(grades.get(position));
                return view;
            }
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(grades.get(position));
                return view;
            }
        };
        adapter = new ArrayAdapter<String>(this, R.layout.drop, Hours) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(Hours[position]);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(Hours[position]);
                return view;
            }
        };


        TextWatcher textWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                if (!isDataLoading) {
                onPause();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        };
        na.addTextChangedListener(textWatcher);
        if(h!=-1){H.setText(adapter.getItem(h));}
        if(g!=-1){aut.setText(adapt.getItem(g));}
        aut.setAdapter(adapt);
        aut.setOnItemClickListener((parent, view, position, id) -> {
            CALC();
            System.out.println("on pause 487");
            if (!isDataLoading) {
            onPause();
            }
        });H.setAdapter(adapter);
        H.setOnItemClickListener((parent, view, position, id) -> {
            Ho.setText("Total  Hours : "+sum_hours());
            CALC();
            System.out.println("on pause 493");
            if (!isDataLoading) {
            onPause();
            }
        });
        na.setText(n);
        lay.addView(v, lay.getChildCount());
        v.setTag(lay.getChildCount() - 1);
        LinearLayout rowLayout = v.findViewById(R.id.rowe);
        rowLayout.setOnTouchListener(new View.OnTouchListener() {
            private final GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
                    if(!isSelectionModeEnabled)
                    {
                        removeRowWithAnimation(v, e2.getX() > e1.getX());
                    }
                    return true;
                }

                @Override
                public void onLongPress(@NonNull MotionEvent e) {
                        enableSelectionMode(v);
                }
            });

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
        }catch (Exception ignored ){}
    }
    @SuppressLint("SetTextI18n")
    @Override
    protected void onResume() {
        try {
        super.onResume();
        SharedPreferences sh = getSharedPreferences("GG", MODE_PRIVATE);
            //SharedPreferences shp = getSharedPreferences("pp", MODE_PRIVATE);
            dict = new Hashtable<>();
            grades.clear();
//            String ap=shp.getString("A+","");
//            String a=shp.getString("A","");
//            String am=shp.getString("A-","");
//            String bp=shp.getString("B+","");
//            String bb=shp.getString("B","");
//            String bm=shp.getString("B-","");
//            String cp=shp.getString("C+","");
//            String c=shp.getString("C","");
//            String cm=shp.getString("C-","");
//            String dp=shp.getString("D+","");
//            String dd=shp.getString("D","");
//            String dm=shp.getString("D-","");
            //try{
                //String [] lista={"A+","A","A-","B+","B","B-","C+","C","C-","D+","D","D-","F"};
//                dict.put("A+", Double.valueOf(ap));
//                dict.put("A", Double.valueOf(a));
//                dict.put("A-", Double.valueOf(am));
//                dict.put("B+", Double.valueOf(bp));
//                dict.put("B", Double.valueOf(bb));
//                dict.put("B-", Double.valueOf(bm));
//                dict.put("C+", Double.valueOf(cp));
//                dict.put("C", Double.valueOf(c));
//                dict.put("C-", Double.valueOf(cm));
//                dict.put("D+", Double.valueOf(dp));
//                dict.put("D", Double.valueOf(dd));
//                dict.put("D-", Double.valueOf(dm));
//                dict.put("F",0.0);
//                for (int i=0;i<dict.size();i++)
//                {
//                    if(dict.get(lista[i])!=-1.0)
//                    {
//                        grades.add(lista[i]);
//                    }
//                }

            //}catch (Exception in){
                String [] lista={"A+","A","B+","B","C+","C","D+","D","F"};
                dict.put("A+", 4.0);
                dict.put("A", 3.7);
                dict.put("B+", 3.3);
                dict.put("B", 3.0);
                dict.put("C+", 2.7);
                dict.put("C", 2.4);
                dict.put("D+", 2.2);
                dict.put("D", 2.0);
                dict.put("F", 0.0);
        grades.addAll(Arrays.asList(lista).subList(0, dict.size()));
            //}
        int Count=sh.getInt("Count",0);
            throwdata.clear();
        EditText Cgpa= findViewById(R.id.CGPA);
        EditText CHours= findViewById(R.id.CHours);
        if(!sh.getString("Current GPA", "").isEmpty()){
            Cgpa.setText(sh.getString("Current GPA", "").equals("0") ?"":sh.getString("Current GPA",""));}
        String curhours= sh.getString("Current H", "");
        if (curhours.equals("blank")){
            CHours.setText("");
        }
        else{
            CHours.setText(curhours);
        }





        if(lay.getChildCount()==1)
        {

            for (int i=0;i<Count;i++)
            {
                String n=sh.getString("Name"+(i),"");
                int h =sh.getInt("Hours"+(i), 0);
                int g=sh.getInt("Grade"+(i),0);
                if(n.equals("no name provided"))
                {
                    n="";
                }
                retrieve_row(n,h,g);
            }

            no.setText("No. Courses : "+(lay.getChildCount()-1));
            Ho.setText("Total  Hours : "+sum_hours());
        }
            try{CALC();}
            catch (Exception ignored){
                String def=sh.getString("GPA","");
                if(!def.isEmpty())
                {

                    try{double gp=Double.parseDouble(def);
                    G.setText("GPA : "+gp);}
                    catch (Exception ignored2)
                    {
                        double gp=Double.parseDouble(def.substring(0,def.length()-1));
                        G.setText("GPA : "+gp);
                    }

                }
                else{G.setText("GPA : "+0.0);}}
        }catch (Exception ignored){}
        isDataLoading = false;
    }
    @Override
    protected void onPause() {
        try {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("GG", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("Count", lay.getChildCount()-1);
        String f=G.getText().toString();
        f=f.substring(6);
        myEdit.putString("GPA", f);
        EditText Cgpa= findViewById(R.id.CGPA);
        EditText Chour= findViewById(R.id.CHours);
        String cgpa;
        if(!Cgpa.getText().toString().isEmpty()){cgpa=(Cgpa.getText().toString());}
        else{
            cgpa="0";}
        if(Chour.getText().toString().isEmpty()||Chour.getText().toString().equals(".")){
            myEdit.putString("Current H", "blank");
        }
        else{myEdit.putString("Current H", Chour.getText().toString());}
        System.out.println(cgpa);
        myEdit.putString("Current GPA", cgpa);
        for (int i=1;i<lay.getChildCount();i++) {
            View v = lay.getChildAt(i);
            EditText name = v.findViewById(R.id.editTextText3);
            AutoCompleteTextView grade = v.findViewById(R.id.autoCompleteTextView2);
            AutoCompleteTextView Hours = v.findViewById(R.id.autoCompleteTextView20);
            if(name.getText().toString().isEmpty()){myEdit.putString("Name"+(i-1), "no name provided");}else{myEdit.putString("Name"+(i-1), name.getText().toString());}
            if(Hours.getText().toString().isEmpty()){myEdit.putInt("Hours"+(i-1),-1);}else{myEdit.putInt("Hours"+(i-1),adapter.getPosition(Hours.getText().toString()));}
            if(grade.getText().toString().isEmpty()){myEdit.putInt("Grade"+(i-1), -1);}else{myEdit.putInt("Grade"+(i-1),adapt.getPosition(grade.getText().toString()));}
            System.out.println("i saved :"+(i-1));
        }


        myEdit.apply();
        }catch (Exception ignored){}
    }
    void collect(){
        try {
        for (int i = 0; i < vies.size(); i++) {
            View v = vies.get(i).vi;
            Subject sub = new Subject();
            EditText name = v.findViewById(R.id.editTextText3);
            AutoCompleteTextView grade = v.findViewById(R.id.autoCompleteTextView2);
            AutoCompleteTextView Hours = v.findViewById(R.id.autoCompleteTextView20);
            if (name.getText().toString().isEmpty()) {
                sub.name = "";
            } else {
                sub.name = name.getText().toString();
            }
            if (grade.getText().toString().isEmpty()) {
                sub.grade = -1.0;
            } else {
                sub.grade = (double) adapt.getPosition(grade.getText().toString());
            }
            if (Hours.getText().toString().isEmpty()) {
                sub.hours = -1;
            } else {
                sub.hours = adapter.getPosition(Hours.getText().toString());
            }
            throwdata.add(sub);
        }
        }catch (Exception ignored){}
    }
    void collectall(){
        try {
        for (int i = 1; i < lay.getChildCount(); i++) {
            View v = lay.getChildAt(i);
            Subject sub = new Subject();
            EditText name = v.findViewById(R.id.editTextText3);
            AutoCompleteTextView grade = v.findViewById(R.id.autoCompleteTextView2);
            AutoCompleteTextView Hours = v.findViewById(R.id.autoCompleteTextView20);
            if (name.getText().toString().isEmpty()) {
                sub.name = "";
            } else {
                sub.name = name.getText().toString();
            }
            if (grade.getText().toString().isEmpty()) {
                sub.grade = -1.0;
            } else {
                sub.grade = (double) adapt.getPosition(grade.getText().toString());
            }
            if (Hours.getText().toString().isEmpty()) {
                sub.hours = -1;
            } else {
                sub.hours = adapter.getPosition(Hours.getText().toString());
            }
            throwdata.add(sub);
        }
        }catch (Exception ignored){}
    }
    private double parseArabicNumber(String numberString) {
        try {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("ar"));
            DecimalFormat decimalFormat = new DecimalFormat("#0.00", symbols);
            Number number = decimalFormat.parse(numberString);
            assert number != null;
            return number.doubleValue();
        } catch (Exception e) {
            try{
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("ar"));
            DecimalFormat decimalFormat = new DecimalFormat("#0.00", symbols);
            Number number = decimalFormat.parse(numberString.substring(0,numberString.length()-1));
            assert number != null;
            return number.doubleValue();}
            catch (Exception e1)
            {
                return 0.0;
            }
        }
    }
    private int sum_hours() {
        try {
        EditText CHours= findViewById(R.id.CHours);
        double sum = 0;
        for (int i=1;i<lay.getChildCount();i++)
        {
            View v = lay.getChildAt(i);
            AutoCompleteTextView Hours = v.findViewById(R.id.autoCompleteTextView20);
            if(!Hours.getText().toString().isEmpty())
            {
                String s=Hours.getText().toString();
                if(!s.equals(".")){
                try{sum+=Double.parseDouble(s);}catch (Exception e)
                {
                    sum+=Double.parseDouble(Hours.getText().toString().substring(0,Hours.getText().toString().length()-1));
                }}
            }
        }
        if((!CHours.getText().toString().isEmpty())&&(!CHours.getText().toString().equals("."))){
            String s=CHours.getText().toString();
            if(!s.equals(".")){
            try{sum+=Double.parseDouble(CHours.getText().toString());}catch (Exception e)
            {
                sum+=Double.parseDouble(CHours.getText().toString().substring(0,CHours.getText().toString().length()-1));
            }}
        }
        return (int)sum;
        }catch (Exception ignored){
        return 0;
        }
    }
    private void removeRowWithAnimation(@NonNull final View view, final boolean swipeRight) {
        try {

        final int defaultBackgroundColor = view.getSolidColor();
        ImageView redOverlay = view.findViewById(R.id.imageView);
        ImageView redOverlay2 = view.findViewById(R.id.sora);
        final int swipeBackgroundColor = getResources().getColor(R.color.REDD);
        final int originalIndex = (int) view.getTag();
        Animation animation = new TranslateAnimation(0, swipeRight ? view.getWidth() : -view.getWidth(), 0, 0);
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setBackgroundColor(swipeBackgroundColor);
                if(swipeRight)
                {
                    redOverlay2.setVisibility(View.VISIBLE);}
                else{
                    redOverlay.setVisibility(View.VISIBLE);

                }
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onAnimationEnd(Animation animation) {
                lay.removeView(view);
                for(int i=0;i<lay.getChildCount();i++)
                {
                    lay.getChildAt(i).setTag(i);
                }
                view.setBackgroundColor(defaultBackgroundColor);
                if(delvies.size()<=1)
                {
                    showUndoOption(view,originalIndex);
                }
                Ho.setText("Total  Hours : "+sum_hours());
                no.setText("No. Courses : "+(lay.getChildCount()-1));
                CALC();
                disableSelectionMode();
                System.out.println("on pause 800");
                if (!isDataLoading) {
                onPause();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });


        view.startAnimation(animation);


        }catch (Exception ignored){}
    }
    private void showUndoOption(@NonNull final View deletedView, final int originalIndex) {
        try{
        if(delvies.size()>1){
            Snackbar snackbar = Snackbar.make(lay, "you deleted multiple subjects", Snackbar.LENGTH_SHORT)
                    .setAction("Undo", v -> undoRowDeletion(deletedView,originalIndex));
            snackbar.show();
        }
        else{
            EditText ed= deletedView.findViewById(R.id.editTextText3);
            String subname= ed.getText().toString();
            Snackbar snackbar;
            if(subname.isEmpty()){
                snackbar = Snackbar.make(lay, "you deleted a subject", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", v -> undoRowDeletion(deletedView, originalIndex));
            }
            else{
                snackbar = Snackbar.make(lay, "you deleted " + subname, Snackbar.LENGTH_SHORT)
                        .setAction("Undo", v -> undoRowDeletion(deletedView, originalIndex));
            }
            snackbar.show();
        }
        }catch (Exception ignored){}
    }
    @SuppressLint("SetTextI18n")
    private void undoRowDeletion(View deletedView, final int originalIndex) {
        try {
        if (!got_it(new Vie(deletedView,originalIndex))) {
            final ImageView redOverlay = deletedView.findViewById(R.id.imageView);
            redOverlay.setVisibility(View.GONE);
            final ImageView redOverlay2 = deletedView.findViewById(R.id.sora);
            redOverlay2.setVisibility(View.GONE);
            for(int i=originalIndex;i< lay.getChildCount();i++)
            {
                lay.getChildAt(i).setTag(i+1);
            }
            lay.addView(deletedView, originalIndex);
            if((isSelectionModeEnabled&&delvies.size()<=1))
            {
                vies.add(new Vie(deletedView,originalIndex));
            }
        } else {
            sortVieList(delvies);
            for (int i = 0; i < delvies.size(); i++) {
                int j = delvies.get(i).place;
                View de = delvies.get(i).vi;
                final ImageView redOverlay = de.findViewById(R.id.imageView);
                final Button m = de.findViewById(R.id.button2);
                m.setVisibility(View.GONE);
                m.setBackgroundColor(Color.TRANSPARENT);
                m.setText("");
                redOverlay.setVisibility(View.GONE);
                final ImageView redOverlay2 = de.findViewById(R.id.sora);
                redOverlay2.setVisibility(View.GONE);
                lay.addView(de, j);
                for(int k=0;k<lay.getChildCount();k++)
                {
                    lay.getChildAt(k).setTag(k);
                }
            }
            delvies.clear();
        }
        Ho.setText("Total  Hours : " + sum_hours());
        no.setText("No. Courses : " + (lay.getChildCount()-1));
        CALC();
        System.out.println("on pause 881");
        if (!isDataLoading) {
            onPause();
        }

        }catch(Exception ignored){}
    }
    public static void sortVieList(ArrayList<Vie> vieList) {
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Collections.sort(vieList, Comparator.comparingInt(Vie::getPlace));
            }
        }catch (Exception ignored){}
    }
    public boolean got_it(Vie obj) {
        for (int i=0;i<delvies.size();i++)
        {
            if((delvies.get(i).getVi()==obj.getVi()&&delvies.get(i).getPlace()==obj.getPlace()))
            {
                return true;
            }
        }
        return false;
    }
    @SuppressLint("SetTextI18n")
    private void CALC() {
        try{
            data.clear();
        if(lay.getChildCount()!=1)
        {
            EditText Cgpa= findViewById(R.id.CGPA);
            double cgpa = 0;
            EditText Chours= findViewById(R.id.CHours);
            double chours = 0;
            if(Cgpa.getText().toString().isEmpty()||Cgpa.getText().toString().equals(".")){cgpa=0;} else{

                String s=Cgpa.getText().toString();
                if(!s.equals(".")){
                try{cgpa=Double.parseDouble(Cgpa.getText().toString());}catch (Exception e)
                {
                    cgpa=Double.parseDouble(Cgpa.getText().toString().substring(0,Cgpa.getText().toString().length()-1));
                }}



            }
            if(Chours.getText().toString().isEmpty()||Chours.getText().toString().equals(".")){chours=0;} else{

                String s=Chours.getText().toString();
                if(!s.equals(".")){
                try{chours=Double.parseDouble(Chours.getText().toString());}catch (Exception e)
                {
                    chours=Double.parseDouble(Chours.getText().toString().substring(0,Chours.getText().toString().length()-1));
                }}



            }
            double pastgpa= chours*cgpa;
            for (int i=1;i<lay.getChildCount();i++)
            {
                Subject sub=new Subject();
                View v=lay.getChildAt(i);
                EditText name=v.findViewById(R.id.editTextText3);
                AutoCompleteTextView grade = v.findViewById(R.id.autoCompleteTextView2);
                AutoCompleteTextView Hours = v.findViewById(R.id.autoCompleteTextView20);
                if(!(grade.getText().toString().isEmpty()) && !(Hours.getText().toString().isEmpty()))
                {
                    if(dict.get(grade.getText().toString())!=-1){
                        if(name.getText().toString().isEmpty()){sub.setName("no name provided");}
                        else {sub.setName(name.getText().toString());}
                        sub.setHours(Integer.parseInt(Hours.getText().toString()));
                        sub.setGrade(dict.get(grade.getText().toString()));
                        data.add(sub);
                    }
                    else{
                        grade.setText("");
                    }
                }
            }
            double points =0.0;
            double hou=0;
            for(int i=0;i<data.size();i++)
            {
                points+=data.get(i).hours*data.get(i).grade;
                hou+=data.get(i).hours;
            }
            points+=pastgpa;
            if (cgpa!=0){hou+=chours;}
            double res=points/hou;
            DecimalFormat dec = new DecimalFormat("#0.00");
            Locale currentLocale = getResources().getConfiguration().locale;
            boolean isArabicLocale = currentLocale.getLanguage().equals("ar");
            double gpa = 0;
            if (isArabicLocale) {
                gpa= parseArabicNumber(dec.format(res));
            } else {
                String s=dec.format(res);
                if(!s.equals(".")){
                try{gpa= Double.parseDouble(dec.format(res));}catch (Exception e)
                {
                    gpa=Double.parseDouble(dec.format(res).substring(0,dec.format(res).length()-1));
                }}

            }

            if (Double.isNaN(gpa)) {
                gpa = 0.0;
            }
            G.setText("GPA : "+gpa);
        }
        else {
            EditText Cgpa= findViewById(R.id.CGPA);
            EditText Chou= findViewById(R.id.CHours);
            if(Cgpa.getText().toString().isEmpty()||Cgpa.getText().toString().equals(".")){G.setText("GPA : 0.0");}
            else {
                if(Chou.getText().toString().isEmpty()||Chou.getText().toString().equals(".")){
                    G.setText("GPA : 0.0");
                }
                else{
                    double cgpa = 0;
                    String s=Cgpa.getText().toString();
                    if(!s.equals(".")){
                    try{cgpa=Double.parseDouble(Cgpa.getText().toString());}catch (Exception e)
                    {
                        cgpa=Double.parseDouble(Cgpa.getText().toString().substring(0,Cgpa.getText().toString().length()-1));
                    }}
                    G.setText("GPA : "+cgpa);
                }
            }
        }
    }catch (Exception ignored){}
    }
}