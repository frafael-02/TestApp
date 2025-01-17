package com.example.testapp.database;

import androidx.annotation.NonNull;


import com.example.testapp.DataLoadListener;
import com.example.testapp.EvidencijaLoadListener;
import com.example.testapp.MainActivity2;
import com.example.testapp.PesticidLoad;
import com.example.testapp.entiteti.Biljka;
import com.example.testapp.entiteti.Bolest;
import com.example.testapp.entiteti.Evidencija;
import com.example.testapp.entiteti.Koordinate;
import com.example.testapp.entiteti.Korisnik;
import com.example.testapp.entiteti.Pesticid;
import com.example.testapp.entiteti.Polje;
import com.example.testapp.entiteti.Proizvodjac;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseQueries {
private static DataLoadListener dataLoadListener;

private static EvidencijaLoadListener evidencijaLoadListener;

private static PesticidLoad pesticiLoad;

public static void registerDataLoadedListener(DataLoadListener listener)
{
    dataLoadListener = listener;
}

public static void registerDataLoadedPesticidListener(PesticidLoad listener)
{
    pesticiLoad=listener;
}

    public static void registerEvidencijaLoadedListener(EvidencijaLoadListener listener)
    {
        evidencijaLoadListener = listener;
    }
    public static List<Pesticid> getPesticidi()
    {   List<Pesticid> pesticidiList = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://testapp-dc63d-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = firebaseDatabase.getReference("pesticidi");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pesticidiList.clear();


                for (DataSnapshot pestSnapshot : dataSnapshot.getChildren()) {
                    Long id = Long.valueOf(pestSnapshot.getKey());
                    MainActivity2.maxIdPesticid=id;
                    String attribute1 = pestSnapshot.child("naziv").getValue(String.class);
                    Integer attribute2 = pestSnapshot.child("dozaMax").getValue(Integer.class);
                    Double cijena = pestSnapshot.child("cijena").getValue(Double.class);
                    String opis = pestSnapshot.child("opis").getValue(String.class);
                    Long proizvodjacId = pestSnapshot.child("proizvodjac").getValue(Long.class);
                    Boolean bio = pestSnapshot.child("bio").getValue(Boolean.class);
                    Integer vrsta = pestSnapshot.child("vrsta").getValue(Integer.class);
                    String nacinDjelovanja = pestSnapshot.child("nacinDjelovanja").getValue(String.class);
                    String formulacija = pestSnapshot.child("formulacija").getValue(String.class);
                    String primjena = pestSnapshot.child("primjena").getValue(String.class);
                    String mjereSigunorsti = pestSnapshot.child("mjereSigurnosti").getValue(String.class);
                    Pesticid pesticid = new Pesticid(id, attribute1, attribute2, cijena, opis, proizvodjacId, bio, vrsta, mjereSigunorsti, nacinDjelovanja, primjena, formulacija);
                    pesticidiList.add(pesticid);
                }
                if(pesticiLoad != null)
                {
                    pesticiLoad.onDataLoad2(pesticidiList);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    return pesticidiList;
    }

    public static List<Proizvodjac> getProizvodjaci()
    {
        List<Proizvodjac> proizvodjacList=new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://testapp-dc63d-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = firebaseDatabase.getReference("proizvodjac");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                proizvodjacList.clear();


                for (DataSnapshot pestSnapshot : dataSnapshot.getChildren()) {
                    Long id = Long.valueOf(pestSnapshot.getKey());

                    String attribute1 = pestSnapshot.child("naziv").getValue(String.class);
                    String email = pestSnapshot.child("email").getValue(String.class);
                    String telefon = pestSnapshot.child("telefon").getValue(String.class);

                    proizvodjacList.add(new Proizvodjac(id, attribute1, email, telefon));
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return proizvodjacList;
    }


    public static List<Bolest> getBolesti()
    {
        List<Bolest> bolestList=new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://testapp-dc63d-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = firebaseDatabase.getReference("bolest");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bolestList.clear();


                for (DataSnapshot pestSnapshot : dataSnapshot.getChildren()) {
                    Long id = Long.valueOf(pestSnapshot.getKey());

                    String attribute1 = pestSnapshot.child("naziv").getValue(String.class);
                    String code = pestSnapshot.child("code").getValue(String.class);
                    Long pesticidId = pestSnapshot.child("pesticidId").getValue(Long.class);

                    bolestList.add(new Bolest(id, attribute1, code, pesticidId));
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return bolestList;
    }



    public static List<Biljka> getBiljke()
    {   List<Biljka> biljkaList = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://testapp-dc63d-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = firebaseDatabase.getReference("biljka");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot pestSnapshot : dataSnapshot.getChildren()) {
                    Long id = Long.valueOf(pestSnapshot.getKey());
                    String attribute1 = pestSnapshot.child("naziv").getValue(String.class);


                    Biljka biljka= new Biljka(id, attribute1);
                    biljkaList.add(biljka);
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return biljkaList;
    }

    public static List<Polje> getPolja()
    {   List<Polje> poljeList = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://testapp-dc63d-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = firebaseDatabase.getReference("polje");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                poljeList.clear();

                for (DataSnapshot pestSnapshot : dataSnapshot.getChildren()) {
                    Long id = Long.valueOf(pestSnapshot.getKey());
                    MainActivity2.maxIdPolje=id;
                    String korisnikEmail = pestSnapshot.child("korisnikEmail").getValue(String.class);



                    if(korisnikEmail.equals(getCurrentUser()))
                    {String attribute1 = pestSnapshot.child("arkodId").getValue(String.class);
                        String attribute2 = pestSnapshot.child("naziv").getValue(String.class);
                        Long biljkaId = pestSnapshot.child("biljkaId").getValue(Long.class);
                        int povrsina = pestSnapshot.child("povrsina").getValue(Integer.class);
                        Koordinate koordinate = new Koordinate(pestSnapshot.child("x").getValue(Double.class), pestSnapshot.child("y").getValue(Double.class));
                        if(koordinate.getX() != 0.0 && koordinate.getY() != 0.0)
                        {
                            Polje polje = new Polje(id, attribute1, attribute2, biljkaId, korisnikEmail, povrsina, koordinate);
                            poljeList.add(polje);



                        }
                        else{
                            Polje polje = new Polje(id, attribute1, attribute2, biljkaId, korisnikEmail, povrsina);
                            poljeList.add(polje);


                        }




                    }

                }
        dataLoadListener.onDataLoaded2(poljeList);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return poljeList;
    }



    public static List<Evidencija> getEvidencija()
    {   List<Evidencija> evidencijaList = new ArrayList<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://testapp-dc63d-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = firebaseDatabase.getReference("evidencija");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                evidencijaList.clear();



                for (DataSnapshot pestSnapshot : dataSnapshot.getChildren()) {
                    Long id = Long.valueOf(pestSnapshot.getKey());
                    MainActivity2.maxId = id;
                    String korisnikEmail = pestSnapshot.child("korisnikEmail").getValue(String.class);
                    if(korisnikEmail.equals(getCurrentUser()))
                    {   Long biljkaId = pestSnapshot.child("biljkaId").getValue(Long.class);
                        Integer koristenaDoza = pestSnapshot.child("koristenaDoza").getValue(Integer.class);
                        Long pesticidId = pestSnapshot.child("pesticidId").getValue(Long.class);
                        Long poljeId = pestSnapshot.child("poljeId").getValue(Long.class);
                        Integer tretiranaPovrsina = pestSnapshot.child("tretiranaPovrsina").getValue(Integer.class);
                        String vrijemeStart = pestSnapshot.child("vrijemeStart").getValue(String.class);
                        String vrijemeKraj = pestSnapshot.child("vrijemeKraj").getValue(String.class);
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        LocalDateTime start = LocalDateTime.parse(vrijemeStart, formatter);
                        LocalDateTime kraj = LocalDateTime.parse(vrijemeKraj, formatter);
                        Evidencija evidencija = new Evidencija(id, pesticidId, koristenaDoza, poljeId,start, kraj, tretiranaPovrsina, biljkaId, korisnikEmail );
                        evidencijaList.add(evidencija);

                    }





                }
                if(evidencijaLoadListener != null)
                {
                    evidencijaLoadListener.onDataLoaded(evidencijaList);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                System.out.println("Error kod ucitavanja evidencije");
            }
        });


        return evidencijaList;
    }

    public static String getCurrentUser()
    {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null)
            return user.getEmail();
        else
            return "No user logged in";
    }

    public static void sendEvidencija(Evidencija evidencija)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://testapp-dc63d-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = firebaseDatabase.getReference("evidencija");
        DatabaseReference newRecordId = myRef.child(String.valueOf(evidencija.getId()));
        HashMap<String, Object> newRecordMap = new HashMap<>();
        newRecordMap.put("biljkaId", evidencija.getBiljkaId());
        newRecordMap.put("korisnikEmail", evidencija.getKorisnikEmail());
        newRecordMap.put("koristenaDoza", evidencija.getKoristenaDoza());
        newRecordMap.put("pesticidId", evidencija.getPesticidId());
        newRecordMap.put("poljeId", evidencija.getPoljeId());
        newRecordMap.put("tretiranaPovrsina", evidencija.getTretiranaPovrsina());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String vrijemeKraj = evidencija.getVrijemeKraj().format(formatter);
        String vrijemeStart = evidencija.getVrijemeStart().format(formatter);

        newRecordMap.put("vrijemeKraj", vrijemeKraj);
        newRecordMap.put("vrijemeStart", vrijemeStart);

        newRecordId.setValue(newRecordMap);


    }

    public static void editEvidencija(Evidencija evidencija)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://testapp-dc63d-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = firebaseDatabase.getReference("evidencija");
        DatabaseReference newRecordId = myRef.child(String.valueOf(evidencija.getId()));
        HashMap<String, Object> newRecordMap = new HashMap<>();
        newRecordMap.put("biljkaId", evidencija.getBiljkaId());
        newRecordMap.put("korisnikEmail", evidencija.getKorisnikEmail());
        newRecordMap.put("koristenaDoza", evidencija.getKoristenaDoza());
        newRecordMap.put("pesticidId", evidencija.getPesticidId());
        newRecordMap.put("poljeId", evidencija.getPoljeId());
        newRecordMap.put("tretiranaPovrsina", evidencija.getTretiranaPovrsina());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String vrijemeKraj = evidencija.getVrijemeKraj().format(formatter);
        String vrijemeStart = evidencija.getVrijemeStart().format(formatter);

        newRecordMap.put("vrijemeKraj", vrijemeKraj);
        newRecordMap.put("vrijemeStart", vrijemeStart);
        newRecordId.setValue(newRecordMap);

    }

    public static void sendPolje(Polje polje)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://testapp-dc63d-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = firebaseDatabase.getReference("polje");
        DatabaseReference newRecordId = myRef.child(String.valueOf(polje.getId()));
        HashMap<String, Object> newRecordMap = new HashMap<>();
        newRecordMap.put("arkodId", polje.getArkodId());
        newRecordMap.put("naziv", polje.getNaziv());
        newRecordMap.put("korisnikEmail", polje.getKorisnikEmail());
        newRecordMap.put("povrsina", polje.getPovrsina());
        newRecordMap.put("biljkaId", polje.getBiljkaId());
        if(polje.getKoordinate() != null)
        {
            newRecordMap.put("x", polje.getKoordinate().getX());
            newRecordMap.put("y", polje.getKoordinate().getY());
        }
        else{
            newRecordMap.put("x", 0.0);
            newRecordMap.put("y", 0.0);
        }
        newRecordId.setValue(newRecordMap);
    }

    public static void sendPesticid(Pesticid pesticid)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://testapp-dc63d-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = firebaseDatabase.getReference("pesticidi");
        DatabaseReference newRecordId = myRef.child(String.valueOf(pesticid.getId()));
        HashMap<String, Object> newRecordMap = new HashMap<>();
        newRecordMap.put("naziv", pesticid.getNaziv());
        newRecordMap.put("dozaMax", pesticid.getDozaMax());
        newRecordMap.put("cijena", pesticid.getCijena());
        newRecordMap.put("bio", pesticid.getVrsta());
        newRecordMap.put("formulacija", pesticid.getFormulacija());
        newRecordMap.put("mjereSigurnosti", pesticid.getMjereSigurnosti());
        newRecordMap.put("opis", pesticid.getOpis());
        newRecordMap.put("primjena", pesticid.getPrimjena());
        newRecordMap.put("proizvodjac", pesticid.getProizvodjacId());
        newRecordMap.put("nacinDjelovanja", pesticid.getNacinDjelovanja());
        newRecordMap.put("vrsta", 1);
        newRecordId.setValue(newRecordMap);
    }

    public static void addKorisnik(FirebaseUser user)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://testapp-dc63d-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = firebaseDatabase.getReference("korisnici");
        String newKey = user.getUid();
        Map<String, Object> korisnikData = new HashMap<>();
        korisnikData.put("email", user.getEmail());
        korisnikData.put("MIBPG", 0);
        korisnikData.put("ime", "Nema");
        myRef.child(newKey).setValue(korisnikData);

    }

    public static void urediKorisnik(String uid, String ime, int mibpg)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://testapp-dc63d-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = firebaseDatabase.getReference("korisnici");

        Map<String, Object> korisnikData = new HashMap<>();
        korisnikData.put("email", DatabaseQueries.getCurrentUser());
        korisnikData.put("MIBPG", mibpg);
        korisnikData.put("ime", ime);
        myRef.child(uid).setValue(korisnikData);
    }

    public static void dohvatiKorisnik(String uid)
    {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance("https://testapp-dc63d-default-rtdb.europe-west1.firebasedatabase.app");
        DatabaseReference myRef = firebaseDatabase.getReference("korisnici");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    if(dataSnapshot.getKey().equals(uid))
                    {
                       MainActivity2.korisnik = new Korisnik(dataSnapshot.child("email").getValue(String.class), dataSnapshot.child("MIBPG").getValue(Integer.class), dataSnapshot.child("ime").getValue(String.class));

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }






}
