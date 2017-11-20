package com.example.alber.crud;
import android.app.*;
import android.os.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import java.util.*;
import com.firebase.client.*;

public class MainActivity extends Activity {

    ListView list;
    Button addbtn;
    alumnoAdapter adapter;
    ArrayList<Alumno> users;
    String initialTitle;
    List<String> keyArray = new ArrayList<String>();
    Firebase mFirebase;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //CONFIGURACION DE FIREBASE
        super.onCreate(savedInstanceState);
        try{
            Firebase.getDefaultConfig().setPersistenceEnabled(true);
        }catch(Exception e){}
        Firebase.setAndroidContext(this);
        mFirebase = new Firebase("https://crud-fdf06.firebaseio.com/").child("user"); //URL DE LA BASE DE DATOS EN FIREBASE
        setContentView(R.layout.activity_main);
        list=(ListView) findViewById(R.id.listado);
        addbtn=(Button) findViewById(R.id.btnagregar);
        users=new ArrayList<Alumno>();
        adapter=new alumnoAdapter(this, users);
        list.setAdapter(adapter);
        addbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View p1) {
                formulario(null, -1);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> p1, View p2, int itempos, long p4) {
                formulario((Alumno) p1.getItemAtPosition(itempos), itempos);
            }
        });
        // LISTENER DE FIREBASE
        mFirebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Alumno user = dataSnapshot.getValue(Alumno.class);
                keyArray.add(dataSnapshot.getKey());
                users.add(user);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot p1, String p2) {
                Alumno u=p1.getValue(Alumno.class);
                users.set(keyArray.indexOf(p1.getKey()), u);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot p1) {
                users.remove(keyArray.indexOf(p1.getKey()));
                keyArray.remove(p1.getKey());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot p1, String p2) {
                // TODO: Implement this method
            }

            @Override
            public void onCancelled(FirebaseError p1) {
                // TODO: Implement this method
            }
        });
    }
    //FORMULARIO FLOTANTE
    private void formulario(final Alumno inputuser, final int pos){
        View v = LayoutInflater.from(this).inflate(R.layout.formulario, null);
        final EditText nama = (EditText) v.findViewById(R.id.txtNombre);
        final EditText alamat = (EditText) v.findViewById(R.id.txtCodigo);
        final RadioGroup gender=(RadioGroup) v.findViewById(R.id.form_genero);
        if(inputuser!=null){
            nama.setText(inputuser.getNombre());
            alamat.setText(inputuser.getCodigo());
            gender.check(inputuser.getGenero());
            initialTitle="Actualización";
        }else{
            initialTitle="Agregar";
        }
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setTitle(initialTitle+" Usuarios");
        dlg.setView(v);
        dlg.setPositiveButton(initialTitle, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface p1, int p2) {
                if(nama.getText().toString().length()<2||alamat.getText().toString().length()<2){
                    Toast.makeText(MainActivity.this, "Datos invalidos", Toast.LENGTH_LONG).show();
                    return;
                }

                Alumno user=new Alumno(nama.getText().toString(), gender.getCheckedRadioButtonId(), alamat.getText().toString());
                if(inputuser==null){
                    mFirebase.push().setValue(user);
                }else{
                    mFirebase.child(keyArray.get(pos)).setValue(user);
                }

                Toast.makeText(MainActivity.this, "El alumno se agrego correctamente", Toast.LENGTH_SHORT).show();
            }
        });
        if(inputuser!=null){
            dlg.setNeutralButton("Eliminar", new DialogInterface.OnClickListener(){
                @Override
                public void onClick(DialogInterface p1, int p2) {
                    mFirebase.child(keyArray.get(pos)).removeValue();
                }
            });
        }
        dlg.setNegativeButton("Cancelar", null);
        dlg.show();
    }

    @Override
    protected void onDestroy() {
        mFirebase=null;
        super.onDestroy();
    }
}
