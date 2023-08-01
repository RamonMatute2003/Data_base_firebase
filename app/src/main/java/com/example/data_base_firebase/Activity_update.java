package com.example.data_base_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Activity_update extends AppCompatActivity {

    private ImageButton btn_calendar;
    private EditText txt_forum, txt_names, txt_surnames, txt_birthdate;
    private Button btn_update;
    private Spinner sp_gender;
    FirebaseFirestore db;
    CollectionReference collectionRef;
    String id;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        db=FirebaseFirestore.getInstance();
        collectionRef=db.collection("datos");

        btn_calendar=findViewById(R.id.btn_calendar1);
        txt_forum=findViewById(R.id.txt_forum1);
        txt_names=findViewById(R.id.txt_names1);
        txt_surnames=findViewById(R.id.txt_surnames1);
        txt_birthdate=findViewById(R.id.txt_birthdate1);
        btn_update=findViewById(R.id.btn_update1);
        sp_gender=findViewById(R.id.sp_gender1);

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
            load();
        }

        sp_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                i=position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_calendar.setOnClickListener(act->{
            generate_calendar();
        });

        btn_update.setOnClickListener(act->{
            if(txt_forum.getText().toString().isEmpty() || txt_birthdate.getText().toString().isEmpty() || txt_names.getText().toString().isEmpty() || txt_surnames.getText().toString().isEmpty()){
                Toast.makeText(this,"No dejar campos vacios", Toast.LENGTH_LONG).show();
            }else{
                update_data();
            }
        });
    }

    private void load(){
        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task){
                if (task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    if(querySnapshot != null && !querySnapshot.isEmpty()){
                        for(DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()){
                            Data data = documentSnapshot.toObject(Data.class);

                            if(documentSnapshot.getId().equals(id)){
                                txt_names.setText(data.getNames());
                                txt_surnames.setText(data.getSurnames());
                                txt_forum.setText(data.getForum());
                                txt_birthdate.setText(data.getBirthdate());

                                if(data.getGender().equals("Masculino")){
                                    sp_gender.setSelection(0);
                                }else{
                                    sp_gender.setSelection(1);
                                }

                                break;
                            }

                        }
                    }
                }
            }
        });
    }

    private void update_data(){
        Map<String, Object> data = new HashMap<>();
        data.put("names", txt_names.getText().toString());
        data.put("surnames", txt_surnames.getText().toString());
        data.put("forum", txt_forum.getText().toString());
        data.put("birthdate", txt_birthdate.getText().toString());
        data.put("gender", sp_gender.getItemAtPosition(i).toString());
        collectionRef.document(id).set(data, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Actualizado correctamente", Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(getApplicationContext(),"Actualizacion erronea", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    private void generate_calendar(){//generate_calendar=generar calendario
        Calendar calendar=Calendar.getInstance();

        int current_year=calendar.get(Calendar.YEAR);
        int current_month=calendar.get(Calendar.MONTH)+1;
        int current_day=calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {//dialog=dialogo
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String day_format, month_format;//day_format=formato de dia, month_format=formato de mes

                if(dayOfMonth<10){
                    day_format="0"+String.valueOf(dayOfMonth);
                }else{
                    day_format=String.valueOf(dayOfMonth);
                }

                if(month<10){
                    month_format="0"+String.valueOf((month+1));
                }else{
                    month_format=String.valueOf((month+1));
                }

                txt_birthdate.setText(year+"-"+month_format+"-"+day_format);
            }
        }, current_year,current_month,current_day);
        dialog.show();
    }
}