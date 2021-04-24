package br.edu.ifma.engenharia2.service;

import br.edu.ifma.engenharia2.exceptions.*;
import br.edu.ifma.engenharia2.model.Emprestimo;
import br.edu.ifma.engenharia2.model.Livro;
import br.edu.ifma.engenharia2.model.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@Slf4j
class EmprestimoServiceTest {

    private EmprestimoService emprestimoService;
    private List<Livro> livros;
    private List<Usuario> usuarios;

    /*
    Popular os atributos de emprestimo com exemplos
     */
    @BeforeEach
    void setUp() {
        usuarios = Arrays.asList(
                new Usuario("Aluno 1", "M1"),
                new Usuario("Aluno 2", "M2"),
                new Usuario("Aluno 3", "M3"),
                new Usuario("Aluno 4", "M4"));

        livros = Arrays.asList(new Livro("Autor 1", "Titulo 1", false),
                new Livro("Autor 2", "Titulo 2", false),
                new Livro("Autor 3", "Titulo 3", false),
                new Livro("Autor 4", "Titulo 4", false));
        emprestimoService = new EmprestimoService(livros, usuarios);
    }

    @Test
    void deveRealizarEmprestimoDeLivroNaoReservado() {
        Livro livroEmprestar = livros.get(0);
        Usuario usuarioEmprestimo = usuarios.get(0);
        boolean resultado;
        try {
            emprestimoService.emprestar(livroEmprestar, usuarioEmprestimo);
            resultado = true;
        } catch (LivroJaEmprestadoException | LivroReservadoException | LimiteEmprestimosUsuarioExcedidoException e) {
            log.error(e.getMessage());
            resultado = false;
        }
        Assertions.assertTrue(resultado);

    }

    @Test
    void deveFalharAoRealizarEmprestimoDeLivroReservado() {
        Livro livroEmprestar = livros.get(0);
        Usuario usuarioEmprestimo = usuarios.get(0);
        boolean resultado;
        try {
            emprestimoService.emprestar(livroEmprestar, usuarioEmprestimo);
            emprestimoService.emprestar(livroEmprestar, usuarioEmprestimo);
            resultado = false;
        } catch (LivroJaEmprestadoException e){
            log.info(e.getMessage());
            resultado = true;
        } catch (LivroReservadoException | LimiteEmprestimosUsuarioExcedidoException e) {
            log.error(e.getMessage());
            resultado = false;
        }
        Assertions.assertTrue(resultado);
    }

    @Test
    void deveVerificarSeDataPrevistaEstaCorreta() {
        Livro livroEmprestar = livros.get(0);
        Usuario usuarioEmprestimo = usuarios.get(0);
        boolean resultado;
        try {
            LocalDate dataEmprestimo = LocalDate.now();
            LocalDate dataPrevista = dataEmprestimo.plusDays(7);
            emprestimoService.emprestar(livroEmprestar, usuarioEmprestimo);

            Emprestimo emprestimo = emprestimoService.encontrarEmprestimo(livroEmprestar, usuarioEmprestimo);
            log.info("Data Emprestimo: {}", emprestimo.getDataEmprestimo().format(DateTimeFormatter.ISO_DATE));
            log.info("Data Prevista: {}", dataPrevista.format(DateTimeFormatter.ISO_DATE));
            log.info("Data Encontrada: {}", emprestimo.getDataPrevista().format(DateTimeFormatter.ISO_DATE));

            if(dataPrevista.compareTo(emprestimo.getDataPrevista()) == 0){
                resultado = true;
            }else{
                resultado = false;
            }
        } catch (LivroJaEmprestadoException | LivroReservadoException | LimiteEmprestimosUsuarioExcedidoException | UsuarioNaoEncontradoException | EmprestimoNaoEncontradoException e) {
            log.error(e.getMessage());
            resultado = false;
        }
        Assertions.assertTrue(resultado);
    }

    @Test
    void deveVerificarSeUsuarioNaoTemEmpresimo() {
        Livro livroEmprestar = livros.get(0);
        Usuario usuarioEmprestimo = usuarios.get(0);
        Usuario usuarioTeste = usuarios.get(1);
        boolean resultado = false;

        try {
            emprestimoService.emprestar(livroEmprestar, usuarioEmprestimo);
        } catch (LivroJaEmprestadoException | LivroReservadoException | LimiteEmprestimosUsuarioExcedidoException e) {
            log.error(e.getMessage());
        }

        if(usuarioTeste.getEmprestimos().size() == 0){
            resultado = true;
        }
        Assertions.assertTrue(resultado);
    }

    @Test
    void deveVerificarSeUsuarioTemUmEmprestimo() {
        Livro livroEmprestar = livros.get(0);
        Usuario usuarioEmprestimo = usuarios.get(0);
        boolean resultado = false;

        try {
            emprestimoService.emprestar(livroEmprestar, usuarioEmprestimo);
        } catch (LivroJaEmprestadoException | LivroReservadoException | LimiteEmprestimosUsuarioExcedidoException e) {
            log.error(e.getMessage());
        }

        if(usuarioEmprestimo.getEmprestimos().size() == 1){
            resultado = true;
        }
        Assertions.assertTrue(resultado);
    }

    @Test
    void deveVerificarSeUsuarioTemDoisEmprestimos() {
        Livro livroEmprestar1 = livros.get(0);
        Livro livroEmprestar2 = livros.get(1);
        Usuario usuarioEmprestimo = usuarios.get(0);
        boolean resultado = false;

        try {
            emprestimoService.emprestar(livroEmprestar1, usuarioEmprestimo);
            emprestimoService.emprestar(livroEmprestar2, usuarioEmprestimo);
        } catch (LivroJaEmprestadoException | LivroReservadoException | LimiteEmprestimosUsuarioExcedidoException e) {
            log.error(e.getMessage());
        }

        if(usuarioEmprestimo.getEmprestimos().size() == 2){
            resultado = true;
        }
        Assertions.assertTrue(resultado);
    }
    @Test
    void deveFalharEmFazerTerceiroEmprestimo() {
        Livro livroEmprestar1 = livros.get(0);
        Livro livroEmprestar2 = livros.get(1);
        Livro livroEmprestar3 = livros.get(2);
        Usuario usuarioEmprestimo = usuarios.get(0);
        boolean resultado = false;

        try {
            emprestimoService.emprestar(livroEmprestar1, usuarioEmprestimo);
            emprestimoService.emprestar(livroEmprestar2, usuarioEmprestimo);
            emprestimoService.emprestar(livroEmprestar3, usuarioEmprestimo);

        } catch (LivroJaEmprestadoException | LivroReservadoException e) {
            log.error(e.getMessage());
        } catch (LimiteEmprestimosUsuarioExcedidoException e) {
            log.info(e.getMessage());
            resultado = true;
        }

        Assertions.assertTrue(resultado);
    }

    @Test
    void deveTestarDevolucaoAntesDaData() {
        Livro livroEmprestar = livros.get(0);
        Usuario usuarioEmprestimo = usuarios.get(0);
        boolean resultado = false;
        try {
            emprestimoService.emprestar(livroEmprestar, usuarioEmprestimo);
            emprestimoService.devolver(livroEmprestar, usuarioEmprestimo, BigDecimal.valueOf(5), LocalDate.now().plusDays(6));
            Emprestimo emprestimo = emprestimoService.encontrarEmprestimo(livroEmprestar, usuarioEmprestimo);
            if(emprestimo.getValorPago() == BigDecimal.valueOf(5) && !livroEmprestar.isEmprestado()){
                resultado = true;
            }
        } catch (LivroJaEmprestadoException | LivroReservadoException | LimiteEmprestimosUsuarioExcedidoException | UsuarioNaoEncontradoException | EmprestimoNaoEncontradoException | DataInvalidaException | ValorInvalidoExcepton e) {
            log.error(e.getMessage());
        }

        Assertions.assertTrue(resultado);
    }

    @Test
    void deveTestarDevolucaoUmDiaAposAPrevista() {
        Livro livroEmprestar = livros.get(0);
        Usuario usuarioEmprestimo = usuarios.get(0);
        boolean resultado = false;
        try {
            emprestimoService.emprestar(livroEmprestar, usuarioEmprestimo);
            Emprestimo emprestimo = emprestimoService.encontrarEmprestimo(livroEmprestar, usuarioEmprestimo);
            emprestimoService.devolver(livroEmprestar, usuarioEmprestimo, BigDecimal.valueOf(5.4), LocalDate.now().plusDays(8));
            if(emprestimo.getValorPago().compareTo(BigDecimal.valueOf(5.4)) == 0 && !livroEmprestar.isEmprestado()){
                resultado = true;
            }
        } catch (LivroJaEmprestadoException | LivroReservadoException | LimiteEmprestimosUsuarioExcedidoException | UsuarioNaoEncontradoException | EmprestimoNaoEncontradoException | DataInvalidaException | ValorInvalidoExcepton e) {
            log.error(e.getMessage());
        }

        Assertions.assertTrue(resultado);
    }

    @Test
    void deveTestarDevolucaoTrintaDiasAposAPrevista() {
        Livro livroEmprestar = livros.get(0);
        Usuario usuarioEmprestimo = usuarios.get(0);
        boolean resultado = false;
        try {
            emprestimoService.emprestar(livroEmprestar, usuarioEmprestimo);
            Emprestimo emprestimo = emprestimoService.encontrarEmprestimo(livroEmprestar, usuarioEmprestimo);

            emprestimoService.devolver(livroEmprestar, usuarioEmprestimo, BigDecimal.valueOf(8), LocalDate.now().plusDays(37));
            if(emprestimo.getValorPago().compareTo(BigDecimal.valueOf(8)) == 0 && !livroEmprestar.isEmprestado()){
                resultado = true;
            }
        } catch (LivroJaEmprestadoException | LivroReservadoException | LimiteEmprestimosUsuarioExcedidoException | UsuarioNaoEncontradoException | EmprestimoNaoEncontradoException | DataInvalidaException | ValorInvalidoExcepton e) {
            log.error(e.getMessage());
        }

        Assertions.assertTrue(resultado);
    }
}