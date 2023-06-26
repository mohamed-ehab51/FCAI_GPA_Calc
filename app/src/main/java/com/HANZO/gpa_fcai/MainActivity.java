package com.HANZO.gpa_fcai;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Locale;

import android.view.Window;


public class MainActivity extends AppCompatActivity {

    Dictionary<String, Double> dict = new Hashtable<>();
    TextView no ;
    ArrayList<Subject> data=new ArrayList<>();
    TextView G ;
    Dialog d;
    TextView Ho ;
    String[] grades = {"A+", "A", "B+", "B", "C+", "C", "D+", "D", "F"};
    String[] Hours = {"0","2","3","6"};
    AutoCompleteTextView aut;
    LinearLayout lay;
    AutoCompleteTextView H;
    EditText na;
    ArrayAdapter<String> adapt;
    ArrayAdapter<String> adapter;
    FloatingActionButton more,add,session,info;
    boolean closed = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.indigo));
        d=new Dialog(this);
        no=findViewById(R.id.textView2);
        G=findViewById(R.id.textView3);
        Ho=findViewById(R.id.textView4);
        lay=findViewById(R.id.linearLayout1);
        no.setText("  No. Courses : "+lay.getChildCount());
        more = findViewById(R.id.butt);
        add = findViewById(R.id.add);
        info = findViewById(R.id.infodel);
        info.setOnClickListener(v-> {
            d.setContentView(R.layout.delete_tutorial);
            d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Button b= d.findViewById(R.id.button);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.dismiss();
                }
            });
            d.show();
        });
        session = findViewById(R.id.session);
        more.setOnClickListener(v -> {
            if (closed)
            {
                add.show();
                info.show();
                session.show();
                closed=false;
            }
            else {
                add.hide();
                info.hide();
                session.hide();
                closed=true;
            }
        });
        add.setOnClickListener(v -> addrow());
        session.setOnClickListener(v -> {
            Intent intent = new Intent( getApplicationContext(),Sessions.class);
            startActivity(intent);
        });


        dict.put("A+", 4.0);
        dict.put("A", 3.7);
        dict.put("B+", 3.3);
        dict.put("B", 3.0);
        dict.put("C+", 2.7);
        dict.put("C", 2.4);
        dict.put("D+", 2.2);
        dict.put("D", 2.0);
        dict.put("F", 0.0);
        Ho.setText("Total  Hours : "+sum_hours());
        G.setText("GPA : "+0.0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Fetching the stored data from the SharedPreference
        SharedPreferences sh = getSharedPreferences("GG", MODE_PRIVATE);
        int Count=sh.getInt("Count",0);
        String def=sh.getString("GPA","");
        if(!def.equals(""))
        {double gp=Double.parseDouble(def);
            G.setText("GPA : "+gp);}
        else{G.setText("GPA : "+0.0);}
        if(lay.getChildCount()==0)
        {
            for (int i=0;i<Count;i++)
            {
                String n=sh.getString("Name"+i,"");
                int h =sh.getInt("Hours"+i, 0);
                int g=sh.getInt("Grade"+i,0);
                if(n.equals("no name provided"))
                {
                    n="";
                }
                retrieve_row(n,h,g);
            }
            no.setText("  No. Courses : "+lay.getChildCount());
            Ho.setText("Total  Hours : "+sum_hours());
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Creating a shared pref object with a file name "MySharedPref" in private mode
        SharedPreferences sharedPreferences = getSharedPreferences("GG", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putInt("Count", lay.getChildCount());
        String f=G.getText().toString();
        f=f.substring(6);
        myEdit.putString("GPA", f);
        for (int i=0;i<lay.getChildCount();i++) {
            View v = lay.getChildAt(i);
            EditText name = v.findViewById(R.id.editTextText3);
            AutoCompleteTextView grade = v.findViewById(R.id.autoCompleteTextView2);
            AutoCompleteTextView Hours = v.findViewById(R.id.autoCompleteTextView20);
            if(name.getText().toString().equals("")){myEdit.putString("Name"+i, "no name provided");}else{myEdit.putString("Name"+i, name.getText().toString());}
            if(Hours.getText().toString().equals("")){myEdit.putInt("Hours"+i,-1);}else{myEdit.putInt("Hours"+i,adapter.getPosition(Hours.getText().toString()));}
            if(grade.getText().toString().equals("")){myEdit.putInt("Grade"+i, -1);}else{myEdit.putInt("Grade"+i,adapt.getPosition(grade.getText().toString()));}
            myEdit.apply();
        }
    }
    private double parseArabicNumber(String numberString) {
        try {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("ar"));
            DecimalFormat decimalFormat = new DecimalFormat("#0.00", symbols);
            // Convert Arabic number string to a numeric value
            Number number = decimalFormat.parse(numberString);
            return number.doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0;
        }
    }

    private void addrow() {
        View v=getLayoutInflater().inflate(R.layout.row,null,false);
        aut = v.findViewById(R.id.autoCompleteTextView2);
        na=v.findViewById(R.id.editTextText3);
        H = v.findViewById(R.id.autoCompleteTextView20);
        H.setKeyListener(null);
        aut.setKeyListener(null);
        adapt = new ArrayAdapter<String>(this, R.layout.drop, grades) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(grades[position]);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(grades[position]);
                return view;
            }
        };
        TextWatcher textWatcher = new TextWatcher() {

            public void afterTextChanged(Editable s) {
                onPause();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        };
        na.addTextChangedListener(textWatcher);
        adapter = new ArrayAdapter<String>(this, R.layout.drop, Hours) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
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
            onPause();
        });H.setAdapter(adapter);
        H.setOnItemClickListener((parent, view, position, id) -> {
            Ho.setText("Total  Hours : "+sum_hours());
            CALC();
            onPause();
        });
        lay.addView(v, lay.getChildCount());
        v.setTag(lay.getChildCount() - 1);
        LinearLayout rowLayout = v.findViewById(R.id.rowe);
        GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                removeRowWithAnimation(v, e2.getX() > e1.getX());
                return true;
            }
        });

        rowLayout.setOnTouchListener((v1, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });
        no.setText("  No. Courses : "+lay.getChildCount());
        onPause();
    }
    private int sum_hours() {
        int sum = 0;
        for (int i=0;i<lay.getChildCount();i++)
        {
            View v = lay.getChildAt(i);
            AutoCompleteTextView Hours = v.findViewById(R.id.autoCompleteTextView20);
            if(!Hours.getText().toString().equals(""))
            {
                sum+=Integer.parseInt(Hours.getText().toString());
            }
        }
        return sum;
    }
    private void retrieve_row(String n,int h,int g) {
        View v=getLayoutInflater().inflate(R.layout.row,null,false);
        na=v.findViewById(R.id.editTextText3);
        aut = v.findViewById(R.id.autoCompleteTextView2);
        H = v.findViewById(R.id.autoCompleteTextView20);
        H.setKeyListener(null);
        aut.setKeyListener(null);
        adapt = new ArrayAdapter<String>(this, R.layout.drop, grades) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(grades[position]);
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setText(grades[position]);
                return view;
            }
        };
        adapter = new ArrayAdapter<String>(this, R.layout.drop, Hours) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
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
                onPause();
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
            onPause();
        });H.setAdapter(adapter);
        H.setOnItemClickListener((parent, view, position, id) -> {
            Ho.setText("Total  Hours : "+sum_hours());
            CALC();
            onPause();
        });
        na.setText(n);
        lay.addView(v, lay.getChildCount());
        v.setTag(lay.getChildCount() - 1);
        LinearLayout rowLayout = v.findViewById(R.id.rowe);
            GestureDetector gestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    removeRowWithAnimation(v, e2.getX() > e1.getX());
                    return true;
                }
            });

        rowLayout.setOnTouchListener((v1, event) -> {
            gestureDetector.onTouchEvent(event);
            return true;
        });
    }

    private void removeRowWithAnimation(@NonNull final View view, final boolean swipeRight) {
        final int defaultBackgroundColor = view.getSolidColor();
        ImageView redOverlay = view.findViewById(R.id.imageView);
        ImageView redOverlay2 = view.findViewById(R.id.sora);
        final int swipeBackgroundColor = getResources().getColor(R.color.REDD); // Change color as needed
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
                // Animation start
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Animation end
                lay.removeView(view);
                view.setBackgroundColor(defaultBackgroundColor);
                showUndoOption(view,originalIndex);
                Ho.setText("Total  Hours : "+sum_hours());
                no.setText("  No. Courses : "+lay.getChildCount());
                CALC();
                onPause();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });


        view.startAnimation(animation);


    }
    private void showUndoOption(@NonNull final View deletedView, final int originalIndex) {
               EditText ed= deletedView.findViewById(R.id.editTextText3);
        String subname= ed.getText().toString();
        if(subname.equals("")){Snackbar snackbar = Snackbar.make(lay, "you deleted a subject", Snackbar.LENGTH_LONG)
                .setAction("Undo", v -> undoRowDeletion(deletedView,originalIndex));
            snackbar.show();
        }
        else{Snackbar snackbar = Snackbar.make(lay, "you deleted "+subname, Snackbar.LENGTH_LONG)
                .setAction("Undo", v -> undoRowDeletion(deletedView,originalIndex));
            snackbar.show();}
    }
    private void undoRowDeletion(View deletedView, final int originalIndex) {
        lay.removeView(deletedView);

        // Add the undeleted view back to its original position
        final ImageView redOverlay = deletedView.findViewById(R.id.imageView);
        redOverlay.setVisibility(View.GONE);
        final ImageView redOverlay2 = deletedView.findViewById(R.id.sora);
        redOverlay2.setVisibility(View.GONE);
        lay.addView(deletedView, originalIndex);
        Ho.setText("Total  Hours : "+sum_hours());
        no.setText("  No. Courses : "+lay.getChildCount());
        CALC();
        onPause();

    }
    private double CALC() {
        data.clear();
        if(lay.getChildCount()!=0)
        {
            for (int i=0;i<lay.getChildCount();i++)
            {
                Subject sub=new Subject();
                View v=lay.getChildAt(i);
                EditText name=v.findViewById(R.id.editTextText3);
                AutoCompleteTextView grade = v.findViewById(R.id.autoCompleteTextView2);
                AutoCompleteTextView Hours = v.findViewById(R.id.autoCompleteTextView20);
                if(!(grade.getText().toString().equals("")) && !(Hours.getText().toString().equals("")))
                {
                    if(name.getText().toString().equals("")){sub.setName("no name provided");}
                    else {sub.setName(name.getText().toString());}
                    sub.setHours(Integer.parseInt(Hours.getText().toString()));
                    sub.setGrade(dict.get(grade.getText().toString()));
                    data.add(sub);
                }
            }
            double points =0.0;
            int hou=0;
            for(int i=0;i<data.size();i++)
            {
                points+=data.get(i).hours*data.get(i).grade;
                hou+=data.get(i).hours;
            }
            double res=points/hou;
            DecimalFormat dec = new DecimalFormat("#0.00");
            Locale currentLocale = getResources().getConfiguration().locale;
            boolean isArabicLocale = currentLocale.getLanguage().equals("ar");
            double gpa=0.0;
            if (isArabicLocale) {
                gpa= parseArabicNumber(dec.format(res));
            } else {
                gpa= Double.parseDouble(dec.format(res));
            }

            if (Double.isNaN(gpa)) {
                gpa = 0.0;
            }
            G.setText("GPA : "+gpa);
            return gpa;
        }
          G.setText("GPA : "+0.0);
          return 0.0;
    }
}