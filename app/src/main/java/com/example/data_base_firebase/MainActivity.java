package com.example.data_base_firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ImageButton btn_calendar;
    private EditText txt_forum, txt_names, txt_surnames, txt_birthdate;
    private Button btn_insert, btn_list;
    private Spinner sp_gender;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        btn_calendar=findViewById(R.id.btn_calendar1);
        txt_forum=findViewById(R.id.txt_forum1);
        txt_names=findViewById(R.id.txt_names1);
        txt_surnames=findViewById(R.id.txt_surnames1);
        txt_birthdate=findViewById(R.id.txt_birthdate1);
        btn_insert=findViewById(R.id.btn_update1);
        btn_list=findViewById(R.id.btn_list);
        sp_gender=findViewById(R.id.sp_gender1);

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

        btn_insert.setOnClickListener(act->{
            Log.e("rt",""+txt_birthdate.getText().toString().length());
            if(txt_forum.getText().toString().isEmpty() || txt_birthdate.getText().toString().isEmpty() || txt_names.getText().toString().isEmpty() || txt_surnames.getText().toString().isEmpty()){
                Toast.makeText(this,"No dejar campos vacios", Toast.LENGTH_LONG).show();
            }else{
                insert_data();
            }
        });

        btn_list.setOnClickListener(act->{
            Intent intent=new Intent(this, Activity_list.class);
            startActivity(intent);
        });
    }

    private void insert_data(){
        FirebaseFirestore db=FirebaseFirestore.getInstance();

        String nombre = txt_names.getText().toString();
        String apellidos = txt_surnames.getText().toString();
        String foro = txt_forum.getText().toString();
        String fechaNacimiento = txt_birthdate.getText().toString();
        String genero = sp_gender.getItemAtPosition(i).toString();

        Map<String, Object> data = new HashMap<>();
        data.put("names", nombre);
        data.put("surnames", apellidos);
        data.put("forum", foro);
        data.put("birthdate", fechaNacimiento);
        data.put("gender", genero);

        db.collection("datos")
                .add(data)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(this,"insertado correctamente", Toast.LENGTH_LONG).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this,"Error al insertar datos: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e("TAG", "Error al insertar datos: " + e.getMessage());
                });

        txt_names.setText("");
        txt_surnames.setText("");
        txt_forum.setText("");
        txt_birthdate.setText("");
        sp_gender.getItemAtPosition(0);
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