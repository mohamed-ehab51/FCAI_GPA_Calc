package com.HANZO.gpa_fcai;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.MotionEvent;
import android.widget.Toast;

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

public class Sessions extends AppCompatActivity {

    Dictionary<String, Double> dict = new Hashtable<>();
    TextView no ;
    ArrayList<Subject> data=new ArrayList<>();
    TextView G ;
    TextView Ho ;
    private ArrayList<Vie> vies=new ArrayList<>();
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
        no.setText("  No. Courses : "+lay.getChildCount());
        more = findViewById(R.id.butt);
        add = findViewById(R.id.add);
        back = findViewById(R.id.back_button);
        back.setOnClickListener(v->{
            finish();
        });
        d=new Dialog(this);
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
    }
    private void enableSelectionMode() {
        isSelectionModeEnabled = true;
        more.setImageDrawable(getDrawable(R.drawable.baseline_delete_sweep_24));
        add.setImageDrawable(getDrawable(R.drawable.baseline_exit_to_app_24));
        add.show();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableSelectionMode();
            }
        });
        info.hide();
        closed=true;
        more.setOnClickListener(v -> {
            for(int i=0;i<vies.size();i++)
            {
                removeRowWithAnimation(vies.get(i).vi,false);
            }
        });
        // Show the checkboxes for all rows
        for (int i = 0; i < lay.getChildCount(); i++) {
            View row = lay.getChildAt(i);
            CheckBox checkBox = row.findViewById(R.id.checkBox);
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                int k=(int)row.getTag();
                Vie Q= new Vie(row,k);
                if (isChecked) {
                    vies.add(Q);
                } else {
                    for (Iterator<Vie> l = vies.iterator(); l.hasNext();) {
                        Vie b = l.next();
                        if ((k==b.getPlace())&& row.equals(b.getVi()))
                        {
                            l.remove();
                        }
                    }
                }
            });
            checkBox.setChecked(false);
            TextInputLayout t= row.findViewById(R.id.editTextText1);
            TextInputLayout t1= row.findViewById(R.id.editTextText);
            EditText t3= row.findViewById(R.id.editTextText3);
            t3.setEnabled(false);
            t.setEnabled(false);
            t1.setEnabled(false);
        }
    }

    private void disableSelectionMode() {
        isSelectionModeEnabled = false;
        more.setImageDrawable(getDrawable(R.drawable.baseline_more_horiz_24));
        add.setImageDrawable(getDrawable(R.drawable.baseline_add_24));
        add.setOnClickListener(v -> addrow());
        add.hide();
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
        // Hide the checkboxes for all rows
        for (int i = 0; i < lay.getChildCount(); i++) {
            View row = lay.getChildAt(i);
            CheckBox checkBox = row.findViewById(R.id.checkBox);
            checkBox.setVisibility(View.GONE);
            checkBox.setChecked(false);
            TextInputLayout t= row.findViewById(R.id.editTextText1);
            TextInputLayout t1= row.findViewById(R.id.editTextText);
            EditText t3= row.findViewById(R.id.editTextText3);
            t3.setEnabled(true);
            t.setEnabled(true);
            t1.setEnabled(true);
        }
        // Clear the selected rows list
        vies.clear();
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
        });H.setAdapter(adapter);
        H.setOnItemClickListener((parent, view, position, id) -> {
            Ho.setText("Total  Hours : "+sum_hours());
            CALC();
        });
        lay.addView(v, lay.getChildCount());
        v.setTag(lay.getChildCount() - 1);
        LinearLayout rowLayout = v.findViewById(R.id.rowe);
        GestureDetector gestureDetector = new GestureDetector(Sessions.this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                removeRowWithAnimation(v, e2.getX() > e1.getX());
                return true;
            }
        });

        rowLayout.setOnTouchListener(new View.OnTouchListener() {
            private GestureDetector gestureDetector = new GestureDetector(Sessions.this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    if(!isSelectionModeEnabled)
                    {
                        removeRowWithAnimation(v, e2.getX() > e1.getX());
                    }
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    if(!isSelectionModeEnabled){
                        enableSelectionMode();

                    }else{
                        disableSelectionMode();
                    }
                }
            });

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return true;
            }
        });
        no.setText("  No. Courses : "+lay.getChildCount());
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
    private double parseArabicNumber(String numberString) {
        try {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("ar"));
            DecimalFormat decimalFormat = new DecimalFormat("#0.00", symbols);
            // Convert Arabic number string to a numeric value
            Number number = decimalFormat.parse(numberString);
            return number.doubleValue();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle the parsing error gracefully
            return 0.0;
        }
    }
    private void undoRowDeletion(View deletedView, final int originalIndex) {
        if(!isSelectionModeEnabled)
        {
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
        }
        else{

            sortVieList(vies);
            for(int i=0;i<vies.size();i++)
            {
                int j=vies.get(i).place;
                View de=vies.get(i).vi;
                lay.removeView(de);
                final ImageView redOverlay = de.findViewById(R.id.imageView);
                redOverlay.setVisibility(View.GONE);
                final ImageView redOverlay2 = de.findViewById(R.id.sora);
                redOverlay2.setVisibility(View.GONE);
                lay.addView(de, j);
            }
            Ho.setText("Total  Hours : "+sum_hours());
            no.setText("  No. Courses : "+lay.getChildCount());
            CALC();
        }

    }
    public static void sortVieList(ArrayList<Vie> vieList) {
        // Use a custom Comparator to compare the 'place' property
        Collections.sort(vieList, new Comparator<Vie>() {
            @Override
            public int compare(Vie vie1, Vie vie2) {
                return Integer.compare(vie1.getPlace(), vie2.getPlace());
            }
        });
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
