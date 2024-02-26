package com.HANZO.gpa_fcai;

import android.annotation.SuppressLint;
import android.app.Dialog;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Objects;

public class Sessions extends AppCompatActivity {

    Dictionary<String, Double> dict = new Hashtable<>();
    TextView no ;
    ArrayList<Subject> data=new ArrayList<>();
    TextView G ;
    TextView Ho ;
    private final ArrayList<Vie> vies=new ArrayList<>();
    private final ArrayList<Vie> delvies=new ArrayList<>();

    String[] grades = {"A+", "A", "B+", "B", "C+", "C", "D+", "D", "F"};
    String[] Hours = {"0","2","3","6"};
    AutoCompleteTextView aut;
    private boolean isSelectionModeEnabled;
    LinearLayout lay;
    Dialog d;
    AutoCompleteTextView H;
    EditText na;
    ArrayAdapter<String> adapt;
    ArrayAdapter<String> adapter;

    FloatingActionButton more,add,info;
    Button back;
    boolean closed = true;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.sessions);
            no=findViewById(R.id.textView2);
            Window window = this.getWindow();
            isSelectionModeEnabled=false;
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.indigo));
            G=findViewById(R.id.textView3);
            Ho=findViewById(R.id.textView4);
            lay=findViewById(R.id.linearLayout1);
            no.setText("No. Courses : "+(lay.getChildCount()-1));
            EditText Cgpa= findViewById(R.id.CGPA);
            EditText CHours= findViewById(R.id.CHours);
            Cgpa.setText(getIntent().getStringExtra("cgpa"));
            CHours.setText(getIntent().getStringExtra("chours"));
            TextWatcher textWatcher = new TextWatcher() {

                public void afterTextChanged(Editable s) {
                    if(s.toString().equals(".")) {
                        if (Cgpa.getText().toString().equals(".")) {
                            Cgpa.setError("please enter a number");
                        }
                        if (CHours.getText().toString().equals(".")){
                            CHours.setError("please enter a number");
                        }
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
            CHours.addTextChangedListener(textWatcher);
            more = findViewById(R.id.butt);
            add = findViewById(R.id.add);
            back = findViewById(R.id.back_button);
            back.setOnClickListener(v-> finish());
            d=new Dialog(this);
            info = findViewById(R.id.infodel);
            info.setOnClickListener(v-> {
                d.setContentView(R.layout.delete_tutorial);
                Objects.requireNonNull(d.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button b= d.findViewById(R.id.button);
                b.setOnClickListener(v1 -> d.dismiss());
                d.show();
            });
            more.setOnClickListener(v -> {
                if (closed)
                {
                    add.show();
                    info.show();
                    closed=false;
                }
                else {
                    add.hide();
                    info.hide();
                    closed=true;
                }
            });
            add.setOnClickListener(v -> addrow());
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
            try {
                ArrayList<Subject> coming = getIntent().getParcelableArrayListExtra("data");
                if(coming.size()!=0)
                {
                    for (int i = 0; i< coming.size(); i++)
                    {
                        Double fd = coming.get(i).grade;
                        int intValue = (int) Math.round(fd);
                        retrieve_row(coming.get(i).name, coming.get(i).hours,intValue);
                    }
                    no.setText("  No. Courses : "+(lay.getChildCount()-1));
                    Ho.setText("Total  Hours : "+sum_hours());
                    CALC();
                }
            }catch (Exception ignore){}
            CALC();
        }catch (Exception ignored){}
    }
    @SuppressLint("UseCompatLoadingForDrawables")
    private void enableSelectionMode(View m) {try{
        isSelectionModeEnabled = true;
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
        add.setImageDrawable(getDrawable(R.drawable.baseline_exit_to_app_24));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            add.setTooltipText("exit multiple selection mode");
        }
            add.show();
        add.setOnClickListener(v -> disableSelectionMode());
        info.hide();
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
                                if(vies.size()==0)
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
            add.setOnClickListener(v -> addrow());
        add.hide();
            more.setOnClickListener(v -> {
                if (closed)
                {
                    add.setImageDrawable(getDrawable(R.drawable.baseline_add_24));
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        add.setTooltipText("Add a new subject");
                    }
                    add.show();
                    info.show();
                    closed=false;
                }
                else {
                    add.hide();
                    info.hide();
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
        aut.setOnItemClickListener((parent, view, position, id) -> CALC());H.setAdapter(adapter);
        H.setOnItemClickListener((parent, view, position, id) -> {
            Ho.setText("Total  Hours : "+sum_hours());
            CALC();
        });
        lay.addView(v, lay.getChildCount());
        v.setTag(lay.getChildCount() - 1);
        LinearLayout rowLayout = v.findViewById(R.id.rowe);
        rowLayout.setOnTouchListener(new View.OnTouchListener() {
            private final GestureDetector gestureDetector = new GestureDetector(Sessions.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling( MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
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
        }catch (Exception ignored){}
    }
    private double sum_hours() {
        try {
        EditText CHours= findViewById(R.id.CHours);
        double sum = 0;
        for (int i=1;i<lay.getChildCount();i++)
        {
            View v = lay.getChildAt(i);
            AutoCompleteTextView Hours = v.findViewById(R.id.autoCompleteTextView20);
            if(!Hours.getText().toString().equals(""))
            {
                String s=Hours.getText().toString();
                if(!s.equals(".")){
                try{sum+=Double.parseDouble(Hours.getText().toString());}catch (Exception e)
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
        return sum;
        }catch (Exception ignored){
        return 0;
        }
    }
    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
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


        if(h!=-1){H.setText(adapter.getItem(h));}
        if(g!=-1){aut.setText(adapt.getItem(g));}
        aut.setAdapter(adapt);
        aut.setOnItemClickListener((parent, view, position, id) -> CALC());H.setAdapter(adapter);
        H.setOnItemClickListener((parent, view, position, id) -> {
            Ho.setText("Total  Hours : "+sum_hours());
            CALC();
        });
        na.setText(n);
        lay.addView(v, lay.getChildCount());
        v.setTag(lay.getChildCount() - 1);
        LinearLayout rowLayout = v.findViewById(R.id.rowe);
        rowLayout.setOnTouchListener(new View.OnTouchListener() {
            private final GestureDetector gestureDetector = new GestureDetector(Sessions.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling( MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
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

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
        }catch (Exception ignored ){}
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
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

        });


        view.startAnimation(animation);


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
            if(subname.equals("")){
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
                Ho.setText("Total  Hours : " + sum_hours());
                no.setText("No. Courses : " + lay.getChildCount());
                CALC();
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

        }catch(Exception ignored){}
    }
    public static void sortVieList(ArrayList<Vie> vieList) {
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Collections.sort(vieList, Comparator.comparingInt(Vie::getPlace));
            }
        }catch (Exception ignored){}
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
} catch (Exception ignored){}
    }
}
