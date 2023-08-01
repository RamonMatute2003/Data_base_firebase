package com.example.data_base_firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Activity_list extends AppCompatActivity {

    private Button btn_delete, btn_update;
    private ListView list_view;
    List<String> list = new ArrayList<>();
    FirebaseFirestore db;
    CollectionReference collectionRef;
    ArrayAdapter<String> adapter;

    protected void onResume() {
        super.onResume();
        load();
        list=new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        btn_delete=findViewById(R.id.btn_delete);
        btn_update=findViewById(R.id.btn_update);
        list_view=findViewById(R.id.list_view);

        db=FirebaseFirestore.getInstance();
        collectionRef=db.collection("datos");

        btn_delete.setOnClickListener(act->{
            if(list.toArray().length>0){
                show_message();
            }else{
                Toast.makeText(this, "Debe seleccionar un usuario para eliminar", Toast.LENGTH_LONG).show();
            }
        });

        btn_update.setOnClickListener(act->{
            if(list.toArray().length>0){
                if(list.toArray().length==1){
                    Intent intent=new Intent(getApplicationContext(), Activity_update.class);
                    intent.putExtra("id",list.get(0));
                    startActivity(intent);
                }else{
                    Toast.makeText(this, "No se puede editar varios usuarios", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this, "Debe seleccionar un usuario para editar", Toast.LENGTH_LONG).show();
            }
        });

        load();

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected_item = (String) parent.getItemAtPosition(position);
                String id_member = selected_item.substring(0, (selected_item.indexOf("-")-1));

                if(!list.contains(id_member)){
                    list.add(id_member);
                }else{
                    Iterator<String> iterator=list.iterator();
                    while(iterator.hasNext()){
                        String memberId = iterator.next();
                        if(memberId.equals(id_member)){
                            iterator.remove();
                            break;
                        }
                    }
                }
                Log.e("e", list.toString());
            }
        });
    }

    private void show_message(){
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_list.this);
        builder.setTitle("¿Seguro que lo deseas eliminar de tus amigos?");
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete();
                load();
                adapter.clear();
                adapter.notifyDataSetChanged();
                list=new ArrayList<>();
            }
        });
        builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void load(){
        List<String> userList=new ArrayList<>();

        collectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task){
                if (task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    if(querySnapshot != null && !querySnapshot.isEmpty()){

                        userList.clear();

                        for(DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()){
                            Data data = documentSnapshot.toObject(Data.class);
                            userList.add(documentSnapshot.getId()+" - "+data.getNames()+" - "+data.getSurnames()+" - "+data.getGender()+" - "+data.getForum()+" - "+data.getBirthdate());
                            Log.e("s", documentSnapshot.getId()+" - "+data.getNames()+" - "+data.getSurnames()+" - "+data.getGender()+" - "+data.getForum() + " - " + data.getBirthdate());
                        }

                        adapter = new ArrayAdapter<>(getApplicationContext(), R.layout.list_check, userList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        list_view.setAdapter(adapter);
                    }else{

                    }
                }else{

                }
            }
        });
    }

    private void delete(){
        for(int j=0; j<list.toArray().length; j++){
            String documentId=list.get(j);

            collectionRef.document(documentId).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
        }
    }
}