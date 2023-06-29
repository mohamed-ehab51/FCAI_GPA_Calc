package com.HANZO.gpa_fcai;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
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
import android.view.MotionEvent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Collections;
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
    private final ArrayList<Vie> vies=new ArrayList<>();
    private ArrayList<Vie> delvies=new ArrayList<>();

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
            no.setText("No. Courses : "+lay.getChildCount());
            more = findViewById(R.id.butt);
            add = findViewById(R.id.add);
            back = findViewById(R.id.back_button);
            back.setOnClickListener(v-> finish());
            d=new Dialog(this);
            info = findViewById(R.id.infodel);
            info.setOnClickListener(v-> {
                d.setContentView(R.layout.delete_tutorial);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
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
                    no.setText("  No. Courses : "+lay.getChildCount());
                    Ho.setText("Total  Hours : "+sum_hours());
                    CALC();
                }
            }catch (Exception ignore){}
        }catch (Exception ignored){}
    }
    private void enableSelectionMode(View m) {try{
        isSelectionModeEnabled = true;
        int alpha = 50;
        int color = Color.argb(alpha, 255, 255, 255);
        Button z= m.findViewById(R.id.button2);
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
        for (int i = 0; i < lay.getChildCount(); i++) {
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
    }catch (Exception ignored){}}

    private void disableSelectionMode() {
        try {
        isSelectionModeEnabled = false;
            vies.clear();
            delvies.clear();
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
        for (int i = 0; i < lay.getChildCount(); i++) {
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
    }catch (Exception ignored){}}
    private void addrow() {try{
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
                        enableSelectionMode(v);

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
    }catch (Exception ignored){}}
    private int sum_hours(){try {
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
    }catch (Exception ignored){}
        return 0;
    }
    private void retrieve_row(String n,int h,int g) {
        try {
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
                            enableSelectionMode(v);

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
        }catch (Exception ignored ){}
    }
    private void removeRowWithAnimation(@NonNull final View view, final boolean swipeRight) {try{
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
                lay.removeView(view);
                for(int i=0;i<lay.getChildCount();i++)
                {
                    lay.getChildAt(i).setTag(i);
                }
                view.setBackgroundColor(defaultBackgroundColor);
                if(!isSelectionModeEnabled||(isSelectionModeEnabled&&delvies.size()==1))
                {
                    showUndoOption(view,originalIndex);
                }
                Ho.setText("Total  Hours : "+sum_hours());
                no.setText("No. Courses : "+lay.getChildCount());
                CALC();
                if(isSelectionModeEnabled)
                {
                    if(lay.getChildCount()==0)
                    {
                        disableSelectionMode();
                    }
                }
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });


        view.startAnimation(animation);


    }catch (Exception ignored){}}
    private double parseArabicNumber(String numberString) {
        try {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("ar"));
            DecimalFormat decimalFormat = new DecimalFormat("#0.00", symbols);
            Number number = decimalFormat.parse(numberString);
            assert number != null;
            return number.doubleValue();
        } catch (Exception e) {
            return 0.0;
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
                if(subname.equals("")){Snackbar snackbar = Snackbar.make(lay, "you deleted a subject", Snackbar.LENGTH_SHORT)
                        .setAction("Undo", v -> undoRowDeletion(deletedView,originalIndex));
                    snackbar.show();
                }
                else{Snackbar snackbar = Snackbar.make(lay, "you deleted "+subname, Snackbar.LENGTH_SHORT)
                        .setAction("Undo", v -> undoRowDeletion(deletedView,originalIndex));
                    snackbar.show();}
            }
        }catch (Exception ignored){}
    }
    private void undoRowDeletion(View deletedView, final int originalIndex) {
        try {
            if ((!isSelectionModeEnabled&&delvies.size()==0)||(isSelectionModeEnabled&&delvies.size()==1)) {
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
                    lay.removeView(de);
                    final ImageView redOverlay = de.findViewById(R.id.imageView);
                    redOverlay.setVisibility(View.GONE);
                    final ImageView redOverlay2 = de.findViewById(R.id.sora);
                    redOverlay2.setVisibility(View.GONE);
                    lay.addView(de, j);
                    vies.add(new Vie(de,j));
                    for(int k=0;k<lay.getChildCount();k++)
                    {
                        lay.getChildAt(k).setTag(k);
                    }
                }
                Ho.setText("Total  Hours : " + sum_hours());
                no.setText("No. Courses : " + lay.getChildCount());
                if(!isSelectionModeEnabled) {
                    try {
                        isSelectionModeEnabled = true;
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
                    }catch (Exception ignored){}
                }
                CALC();
            }
        }catch(Exception ignored){}
    }
    public static void sortVieList(ArrayList<Vie> vieList) {
        try{
            Collections.sort(vieList, (vie1, vie2) -> Integer.compare(vie1.getPlace(), vie2.getPlace()));}catch (Exception ignored){}
    }
    private double CALC() {
        try{ data.clear();
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
        } catch (Exception ignored){}
        return 0.0;
    }
}
