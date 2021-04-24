package br.edu.ifma.engenharia2.model;

import java.util.ArrayList;
import java.util.List;

public class Livro {
    private String autor;
    private String titulo;
    private boolean isEmprestado;
    private Usuario usuarioReserva = null;

    public Livro() {
    }

    public Livro(String autor, String titulo, boolean isEmprestado) {
        this.autor = autor;
        this.titulo = titulo;
        this.isEmprestado = isEmprestado;
    }

    public Livro(String autor, String titulo, boolean isEmprestado, Usuario usuarioReserva) {
        this.autor = autor;
        this.titulo = titulo;
        this.isEmprestado = isEmprestado;
        this.usuarioReserva = usuarioReserva;
    }

    public boolean isEmprestado() {
        return isEmprestado;
    }

    public boolean isReservado() {
        return (usuarioReserva != null);
    }

    public void setEmprestado(boolean emprestado) {
        isEmprestado = emprestado;
    }

    public void setUsuarioReserva(Usuario usuarioReserva) {
        this.usuarioReserva = usuarioReserva;
    }

    public Usuario getUsuarioReserva() {
        return usuarioReserva;
    }

    @Override
    public String toString() {
        return "Livro{" +
                "autor='" + autor + '\'' +
                ", titulo='" + titulo + '\'' +
                '}';
    }
}
