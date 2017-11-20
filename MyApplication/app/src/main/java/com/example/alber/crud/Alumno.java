package com.example.alber.crud;

/**
 * Created by alber on 11/19/2017.
 */

public class Alumno {
    private String nombre;
    private int genero;
    private String codigo;

    public Alumno()
    {

    }
    public Alumno (String n, int g, String c)
    {
        this.nombre = n;
        this.genero = g;
        this.codigo = c;
    }
    public void setNombre(String n) { this.nombre = n; }
    public String getNombre() { return nombre; }

    public void setGenero(int g) { this.genero = g; }
    public int getGenero() { return genero; }

    public void setCodigo(String c) { this.codigo = c; }
    public String getCodigo() { return codigo; }
}

