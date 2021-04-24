package br.edu.ifma.engenharia2.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Emprestimo {
    private Usuario usuarioEmprestimo;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao;
    private LocalDate dataPrevista;
    private Livro livroLocado;
    private BigDecimal valorPago;

    private BigDecimal multa = BigDecimal.valueOf(0.4);
    private BigDecimal valor = BigDecimal.valueOf(5);

    public Emprestimo(Usuario usuarioEmprestimo, LocalDate dataEmprestimo, LocalDate dataDevolucao, LocalDate dataPrevista, Livro livroLocado, BigDecimal valorPago) {
        this.usuarioEmprestimo = usuarioEmprestimo;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
        this.dataPrevista = dataPrevista;
        this.livroLocado = livroLocado;
        this.valorPago = valorPago;
    }

    public void setDataPrevista(LocalDate dataPrevista) {
        this.dataPrevista = dataPrevista;
    }

    public void setValorPago(BigDecimal valorPago) {
        this.valorPago = valorPago;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public LocalDate getDataPrevista() {
        return dataPrevista;
    }

    public Livro getLivroLocado() {
        return livroLocado;
    }

    public BigDecimal getValorPago() {
        return valorPago;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    @Override
    public String toString() {
        return "Emprestimo{" +
                "dataEmprestimo=" + dataEmprestimo +
                ", dataDevolucao=" + dataDevolucao +
                ", dataPrevista=" + dataPrevista +
                ", livroLocado=" + livroLocado +
                ", valorPago=" + valorPago +
                '}';
    }
}
