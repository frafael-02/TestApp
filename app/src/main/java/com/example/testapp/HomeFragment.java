package com.example.testapp;

import android.os.Bundle;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testapp.api.OpenMeteoApiClient;

import com.example.testapp.api.VirtualAgent;
import com.example.testapp.database.DatabaseQueries;
import com.example.testapp.entiteti.AccountDialog;
import com.example.testapp.entiteti.Current;
;
import com.example.testapp.entiteti.Korisnik;
import com.example.testapp.entiteti.WeatherData;
import com.example.testapp.entiteti.WeatherDataCallback;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static String odgovorStatic;

    private OpenMeteoApiClient apiClient;
    public TextView temperatureTextView;

    public EditText pitanje;

    public TextView odgovor;


    public static String string;

    public long timer;

    public Button pitajBtn;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        timer = 500L;
        apiClient = new OpenMeteoApiClient();
        double latitude = 46.32;
        double longitude = 16.80;

        apiClient.getWeatherData(latitude, longitude, new WeatherDataCallback() {
            @Override
            public void onSuccess(WeatherData weatherData) {
                // Use the weather data here
                //  List<Daily> dailyList = weatherData.getDailyList();
                //List<Hourly> hourlyList = weatherData.getHourlyList();
                Current current = weatherData.getCurrent();
                // Example: set the current temperature on a TextView

                string = current.getTemperature() + "°C";


            }

            @Override
            public void onFailure(Exception e) {
                // Handle the error here
                e.printStackTrace();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        temperatureTextView = view.findViewById(R.id.tempratureTextView);
        pitanje = view.findViewById(R.id.pitanjeTextView);
        odgovor=view.findViewById(R.id.odgovorText);
        pitajBtn = view.findViewById(R.id.button3);


        new Handler().postDelayed(new Runnable() {

            public void run () {
                if(string != null)
                temperatureTextView.setText("Trenutna temperatura: " + string);
            else
                timer=timer+100L;


            }
        }, timer);

        AppCompatImageButton profilBtn = view.findViewById(R.id.profilBtn);
        profilBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfile(MainActivity2.korisnik);
            }
        });

        pitajBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer=500L;

                pitaj(pitanje.getText().toString());
                new Handler().postDelayed(new Runnable() {
                    public void run () {
                        if(odgovorStatic != null)
                            odgovor.setText(odgovorStatic);
                        else
                            timer=timer+100L;


                    }
                }, timer);

            }
        });





        return view;
    }

    public void openProfile(Korisnik korisnik)
    {
        AccountDialog accountDialog = new AccountDialog();
        accountDialog.show(getParentFragmentManager(), "Vaš korisnički profil");
    }

    public void pitaj(String pitanje)
    {
        List<String> result = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
               odgovorStatic = (VirtualAgent.chatGPT(pitanje));

            }
        }).start();



    }



}