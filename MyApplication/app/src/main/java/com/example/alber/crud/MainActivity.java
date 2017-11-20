package com.example.alber.crud;

import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.content.*;

import java.lang.reflect.Array;
import java.util.*;
import com.firebase.client.*;



public class MainActivity extends Activity {

    ListView listado;
    Button btnagregar;
    alumnoAdapter adapter;
    ArrayList<Alumno> alumnos;
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
        mFirebase = new Firebase("https://crud-fdf06.firebaseio.com/").child("alumno");
        setContentView(R.layout.activity_main);
        listado=(ListView) findViewById(R.id.listado);
        btnagregar=(Button) findViewById(R.id.btnagregar);
        alumnos=new ArrayList<Alumno>();
        adapter = new alumnoAdapter(this,alumnos);
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
                keyArray.remove(p1.getKey());
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
        View v = LayoutInflater.from(this).inflate(R.layout.formulario,null);
        final EditText nombre = (EditText)  v.findViewById(R.id.txtNombre);
        final EditText codigo = (EditText) v.findViewById(R.id.txtCodigo);
        final RadioGroup genero=(RadioGroup) v.findViewById(R.id.form_genero);
        if(inputalumno!=null){
            nombre.setText(inputalumno.getNombre());
            codigo.setText(inputalumno.getCodigo());
            genero.check(inputalumno.getGenero());
            initialTitle="Actualización";
        }else {
            initialTitle="Agregar";
        }
            AlertDialog.Builder dlg = new AlertDialog.Builder(this);
            dlg.setTitle(initialTitle+" Alumnos");
            dlg.setView(v);
            dlg.setPositiveButton(initialTitle, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface p1, int p2) {
                    if(nombre.getText().toString().length()<2||codigo.getText().toString().length()<2){
                        Toast.makeText(MainActivity.this, "Datos invalidos", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Alumno alumno=new Alumno(nombre.getText().toString(),genero.getCheckedRadioButtonId(),codigo.getText().toString());
                    if(inputalumno==null){
                        mFirebase.push().setValue(alumno);
                    }else{
                        mFirebase.child(keyArray.get(pos)).setValue(alumno);
                    }

                    Toast.makeText(MainActivity.this,"Usuario exitoso"+(initialTitle.toLowerCase()), Toast.LENGTH_SHORT).show();
                }
            });
            if (inputalumno!=null){
                dlg.setNeutralButton("Hapus", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mFirebase.child(keyArray.get(pos)).removeValue();
                    }
                });
            }
            dlg.setNegativeButton("Batal", null);
            dlg.show();
        }


    @Override
    protected void onDestroy() {
        mFirebase = null;
        super.onDestroy();
    }
}
