package com.example.testapp;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.AutoTransition;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.testapp.api.ExcelFileController;

import com.example.testapp.entiteti.Evidencija;
import com.example.testapp.entiteti.Pesticid;
import com.example.testapp.entiteti.Polje;
import com.example.testapp.entiteti.UtilityClass;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EvidencijaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EvidencijaFragment extends Fragment implements SelectListener {

    RecyclerView recyclerView;

    MyAdapter myAdapter;
   FloatingActionButton mButton;

    public Button filterBtn;
    public Button filterBtn2;


    public LinearLayout filterLayout;

    public Spinner nazivSpinner;

    public Spinner sredstvoSpinner;

    private boolean isVisible;

    private List<Evidencija> filteredList;

    private long selectedPolje;

    private long selectedSredstvo;

    public EditText datum;

    Animation animation;
    View viewExplosion;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EvidencijaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EvidencijaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static EvidencijaFragment newInstance(String param1, String param2) {
        EvidencijaFragment fragment = new EvidencijaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);



        }
        selectedSredstvo = 0L;
        selectedPolje = 0L;
      //  getActivity().setActionBar(new Toolbar(getContext()));
      //  ((AppCompatActivity) getActivity()).getSupportActionBar().show();









    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View view =inflater.inflate(R.layout.fragment_evidencija, container, false);
      recyclerView = view.findViewById(R.id.evidencijaListId);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setHasFixedSize(true);
        isVisible = false;



        filterBtn = view.findViewById(R.id.filterBtn);
        filterBtn.setVisibility(View.VISIBLE);
        filterBtn2 = view.findViewById(R.id.filterBtn2);
        filterLayout = view.findViewById(R.id.filterLayout);
        nazivSpinner = view.findViewById(R.id.nazivSpinner);
        datum = view.findViewById(R.id.datumId2);



        PoljeAdapter adapterPolja = new PoljeAdapter(getContext(), new ArrayList<>(MainActivity2.poljeList));
        adapterPolja.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        nazivSpinner.setAdapter(adapterPolja);
        nazivSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(adapterPolja.getItem(position) != null)
                {
                    selectedPolje = adapterPolja.getItem(position).getId();

                }
                else
                    selectedPolje = 0L;


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sredstvoSpinner = view.findViewById(R.id.sredstvoSpinner);
        PesticidAdapter pesticidAdapter = new PesticidAdapter(getContext(), new ArrayList<>(MainActivity2.pesticidList));
        pesticidAdapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        sredstvoSpinner.setAdapter(pesticidAdapter);
        sredstvoSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(pesticidAdapter.getItem(position) != null)
                {
                    selectedSredstvo = pesticidAdapter.getItem(position).getId();


                }
                else selectedSredstvo = 0L;

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });







        myAdapter = new MyAdapter(view.getContext(), (ArrayList<Evidencija>) MainActivity2.evidencijaList, this);
      recyclerView.setAdapter(myAdapter);

    Button novoPoljeBtn = view.findViewById(R.id.dodajZemlju);
    novoPoljeBtn.setOnClickListener(new View.OnClickListener(){

      @Override
      public void onClick(View v)
      {
          Intent myIntent = new Intent(getActivity(), NovoPoljeActivity.class);


          startActivity(myIntent);
      }

    });

    Button posaljiBtn = view.findViewById(R.id.izvoz);
    posaljiBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ExcelFileController.writeExcelFileAndSendByEmail(view.getContext(), MainActivity2.korisnik, MainActivity2.evidencijaList);
            Toast.makeText(getContext(), "Formular poslan na email!", Toast.LENGTH_SHORT).show();
        }
    });

    Button novPesticidBtn = view.findViewById(R.id.button8);
    novPesticidBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v)
        {
            Intent myIntent = new Intent(getActivity(), NovPesticidActivity.class);

            startActivity(myIntent);
        }

    });

    filterBtn.setOnClickListener(new View.OnClickListener(){
        @Override
        public void onClick(View v)
        {
                TransitionManager.beginDelayedTransition(filterLayout, new AutoTransition());
            AlphaAnimation animation1 = new AlphaAnimation(1.0f, 0.0f);
            animation1.setDuration(300);
            //animation1.setFillAfter(true);
            AlphaAnimation animation2 = new AlphaAnimation(0.0f, 1.0f);
            animation2.setDuration(150);
            animation2.setFillAfter(true);
                filterBtn.startAnimation(animation1);
                filterBtn.setVisibility(View.GONE);
                filterLayout.setVisibility(View.VISIBLE);
                filterBtn2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (filtrirajListu() != null) {
                            myAdapter.list = (ArrayList<Evidencija>) filteredList;
                            recyclerView.setAdapter(myAdapter);

                        } else {
                            myAdapter.list = (ArrayList<Evidencija>) MainActivity2.evidencijaList;
                            recyclerView.setAdapter(myAdapter);
                        }
                        TransitionManager.beginDelayedTransition(filterLayout, new AutoTransition());
                        filterBtn.startAnimation(animation2);
                        filterLayout.startAnimation(animation1);
                        filterBtn.setVisibility(View.VISIBLE);
                        filterLayout.setVisibility(View.GONE);

                    }
                });
        }
    });

    datum.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pickDatum();
        }
    });

        animation = AnimationUtils.loadAnimation(view.getContext(), R.anim.animation_circle_explosion);
        viewExplosion=view.findViewById(R.id.circle);
        mButton=view.findViewById(R.id.addEvidencijaButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                animation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                        Runnable myThread = () ->
                        {
                            try {
                                TimeUnit.MILLISECONDS.sleep(300);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                            Intent myIntent = new Intent(view.getContext(), NovaEvidencijaActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(myIntent);


                        };
                        Thread run = new Thread(myThread);
                        run.start();

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {


                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });

                viewExplosion.startAnimation(animation);

            }
        });

                return view;


    }

    @Override
    public void onItemClicked(Evidencija evidencija) {
        Intent myIntent = new Intent(getActivity(), OdabranaEvidencijaActivity.class);

        myIntent.putExtra("evidencija", evidencija);
        startActivity(myIntent);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(isVisible())
        {
            recyclerView.setAdapter(myAdapter);
        }
    }


    public List<Evidencija> filtrirajListu()
    {
        if(selectedSredstvo != 0L)
        {
            filteredList=new ArrayList<>(MainActivity2.evidencijaList);
            filteredList=filteredList.stream().filter(e -> e.getPesticidId() == selectedSredstvo).collect(Collectors.toList());

            if(selectedPolje != 0L)
            {
                filteredList=filteredList.stream().filter(e -> e.getPoljeId() == selectedPolje).collect(Collectors.toList());
            }
            if(!datum.getText().toString().equals(""))
            {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MM. yyyy.");
                LocalDate localDateTime = LocalDate.parse(datum.getText().toString(), formatter);
                filteredList=filteredList.stream().filter(e->e.getVrijemeStart().toLocalDate().equals(localDateTime)).collect(Collectors.toList());

            }

            return filteredList;
        }
        else if(selectedPolje != 0L)
        {
            filteredList = new ArrayList<>(MainActivity2.evidencijaList);
            filteredList=filteredList.stream().filter(e -> e.getPoljeId() == selectedPolje).collect(Collectors.toList());
            if(!datum.getText().toString().equals(""))
            {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MM. yyyy.");
                LocalDate localDateTime = LocalDate.parse(datum.getText().toString(), formatter);
                filteredList=filteredList.stream().filter(e->e.getVrijemeStart().toLocalDate().equals(localDateTime)).collect(Collectors.toList());
            }
            return filteredList;

        }
        else if(!datum.getText().toString().equals(""))
        {
            filteredList = new ArrayList<>(MainActivity2.evidencijaList);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd. MM. yyyy.");
            LocalDate localDateTime = LocalDate.parse(datum.getText().toString(), formatter);
            filteredList=filteredList.stream().filter(e->e.getVrijemeStart().toLocalDate().equals(localDateTime)).collect(Collectors.toList());
            return filteredList;
        }

        return null;
    }

    public void pickDatum()
    {
        LocalDateTime result = null;
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH);
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mCalendar.set(year, month, dayOfMonth);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd. MM. yyyy.", Locale.ITALY);
                        datum.setText(sdf.format(mCalendar.getTime()));
                    }
                }, year, month, day);
        datePickerDialog.show();


    }



}