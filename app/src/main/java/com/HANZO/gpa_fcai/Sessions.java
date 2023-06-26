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
import android.text.TextWatcher;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Locale;

public class Sessions extends AppCompatActivity {

    Dictionary<String, Double> dict = new Hashtable<>();
    TextView no ;
    ArrayList<Subject> data=new ArrayList<>();
    TextView G ;
    TextView Ho ;
    String[] grades = {"A+", "A", "B+", "B", "C+", "C", "D+", "D", "F"};
    String[] Hours = {"0","2","3","6"};
    AutoCompleteTextView aut;
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
        GestureDetector gestureDetector = new GestureDetector(Sessions.this, new GestureDetector.SimpleOnGestureListener() {
            private static final int SWIPE_THRESHOLD = 100;
            private static final int SWIPE_VELOCITY_THRESHOLD = 100;

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                boolean result = false;
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(velocityY) && Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    removeRowWithAnimation(v, diffX > 0);
                    result = true;
                }
                return result;
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
    private void removeRowWithAnimation(@NonNull final View view, final boolean swipeRight) {
        final int defaultBackgroundColor = view.getSolidColor();
        final ImageView redOverlay = view.findViewById(R.id.imageView);
        final int swipeBackgroundColor = getResources().getColor(R.color.REDD); // Change color as needed
        final int originalIndex = (int) view.getTag();
        Animation animation = new TranslateAnimation(0, swipeRight ? view.getWidth() : -view.getWidth(), 0, 0);
        animation.setDuration(300);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // Animation start
                view.setBackgroundColor(swipeBackgroundColor);
                redOverlay.setVisibility(View.VISIBLE);
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
        lay.removeView(deletedView);

        // Add the undeleted view back to its original position
        final ImageView redOverlay = deletedView.findViewById(R.id.imageView);
        redOverlay.setVisibility(View.GONE);
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
