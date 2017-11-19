package com.example.alber.crud;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ListView listado;
    Button btnagregar;
    userAdapter adapter;
    Array<Alumno> alumnos;
    String initialTitle;
    List<String> keyArray = new ArrayList<String>();

    Firebase mFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try{
            Firebase.getDefaultConfig().setPersistenceEnabled(true);
        }catch(Exception e){}
        Firebase.setAndroidContext(this);
        mFirebase = new Firebase("hhtps://nosql-e3ae7.firabaseio.com/").child(user);
        setContentView(R.layout.activity_main);
        list=(ListView) findViewById(R.id.listado);
        btnagregar=(Button) findViewById(R.id.btnagregar);
        alumnos=new ArrayList<Alumno>();
        adapter = new userAdapter(this,alumnos);
        listado.setAdapter(adapter);
        btnagregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View p1) {
                formulir(null, -1);
            }
        });
        listado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int itiempos, long p4) {
                formulir((Alumno) p1.getItemAtPosition(itiempos),itiempos);
            }
        });

        mFirebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Alumno alumno = dataSnapshot.getValue(Alumno.class);
                keyArray.add(dataSnapshot.getKey());
                alumnos.add(alumno);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot p1, String p2) {
                Alumno u=p1.getValue(Alumno.class);
                alumnos.set(keyArray.indexOf(p1.getKey()),u);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot p1) {
                alumnos.remove(keyArray.indexOf(p1.getKey()));
                keyArray.remove(p1.getKey()):
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
    private void formulir(final Alumno inputalumno, final int pos){
        View v = LayoutInflater.from(this).inflate(R.layout.form_dialog,null);
        final EditText nama = (EditText)  v.findViewById(R.id.form_nama);
        final EditText alamat = (EditText) v.findViewById(R.id.form_alamat);
        final RadioGroup gender=(RadioGroup) v.findViewById(R.id.form_gender);
        if(inputalumno!=null){
            nama.setText(inputalumno.getNama());
            alamat.setText(inputalumno.getAlamat());
            gender.check(inputalumno.getGender());
            initialTitle="Perbauri";
        }else{
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(initialTitle+" Pengguna");
            dlg.setView(v);
            dlg.setPositiveButton(initialTitle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface p1, int p2) {
                    if(nama.getText().toString().length()<2||alamat.getText().toString().length()<2){
                        Toast.makeText(MainActivity.this, "Data tidak valid", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Al
                }
            });
        }
    }
}
