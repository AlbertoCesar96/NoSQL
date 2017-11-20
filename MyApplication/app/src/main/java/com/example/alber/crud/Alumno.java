package com.example.alber.crud;

/**
 * Created by alber on 11/19/2017.
 */

public class Alumno {
    private String nombre;
    private int genero;
    private String codigo;

    public Alumno (String nombre, int genero, String codigo)
    {
        this.nombre = nombre;
        this.genero = genero;
        this.codigo = codigo;
    }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getNombre() { return nombre; }

    public void setGenero(int genero) { this.genero = genero; }
    public int getGenero() { return genero; }

    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getCodigo() { return codigo; }
}

