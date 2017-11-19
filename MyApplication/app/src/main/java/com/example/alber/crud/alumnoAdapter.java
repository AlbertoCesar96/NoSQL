package com.example.alber.crud;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alber on 11/19/2017.
 */

public class alumnoAdapter extends ArrayAdapter<Alumno> {
    HashMap<Integer, Drawable> gender_icon;
    public alumnoAdapter(Context ctx, ArrayList<Alumno> alumnos){
        super(ctx, 0, alumnos);
        gender_icon=new HashMap<Integer, Drawable>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            gender_icon.put(R.id.form_genero_f, ctx.getDrawable(R.drawable.human_female));
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            gender_icon.put(R.id.form_genero_m, ctx.getDrawable(R.drawable.human_male));
        }
    }
    public static class ViewHolder{
        TextView nombre;
        TextView codigo;
        ImageView genero;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Alumno alumno=getItem(position);
        ViewHolder holder;
        if(convertView==null){
            holder=new ViewHolder();
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false);
            holder.nombre=(TextView) convertView.findViewById(R.id.nombreAlumno);
            holder.codigo=(TextView) convertView.findViewById(R.id.codigoAlumno);
            holder.genero=(ImageView) convertView.findViewById(R.id.generoAlumno);
            convertView.setTag(holder);
        }else{
            holder=(alumnoAdapter.ViewHolder) convertView.getTag();
        }
        holder.nombre.setText(alumno.getNombre());
        holder.codigo.setText(alumno.getCodigo());
        holder.genero.setImageDrawable(gender_icon.get(alumno.getGenero()));
        return convertView;
    }
}
