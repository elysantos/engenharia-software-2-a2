package br.edu.ifma.engenharia2.service;

import br.edu.ifma.engenharia2.exceptions.*;
import br.edu.ifma.engenharia2.model.Emprestimo;
import br.edu.ifma.engenharia2.model.Livro;
import br.edu.ifma.engenharia2.model.Usuario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class EmprestimoService {

    private List<Livro> livrosDisponiveis;
    private List<Usuario> usuarios;
    private static final int LIMITE_LIVROS = 2;
    private static final BigDecimal VALOR_EMPRESTIMO = BigDecimal.valueOf(5);
    private static final BigDecimal VALOR_MULTA_DIA = BigDecimal.valueOf(0.4);


    public EmprestimoService() {
    }

    public EmprestimoService(List<Livro> livrosDisponiveis, List<Usuario> usuarios) {

        this.livrosDisponiveis = livrosDisponiveis;
        this.usuarios = usuarios;
    }

    public List<Livro> listLivros() {
        return livrosDisponiveis;
    }

    public List<Usuario> listUsuarios() {
        return usuarios;
    }


    public void emprestar(Livro livro, Usuario usuario) throws LivroJaEmprestadoException, LivroReservadoException, LimiteEmprestimosUsuarioExcedidoException {
        Usuario usuarioEmprestimo = usuario;
        if(livro.isEmprestado()){
            throw new LivroJaEmprestadoException();
        }
        if(livro.isReservado() && !livro.getUsuarioReserva().equals(usuario)){
            throw new LivroReservadoException();
        }
        Optional<Usuario> optionalUsuario = usuarios.stream().filter(usuarioEmprestimo::equals).findAny();
        if(optionalUsuario.isEmpty()){
            this.usuarios.add(usuario);
        }else{
            usuarioEmprestimo = optionalUsuario.get();
        }

        if(usuarioEmprestimo.getEmprestimos().size() >= LIMITE_LIVROS){
            throw new LimiteEmprestimosUsuarioExcedidoException();
        }

        livro.setEmprestado(true);
        livro.setUsuarioReserva(null);
        Emprestimo emprestimo = new Emprestimo(usuario, LocalDate.now(), null, LocalDate.now().plusDays(7), livro, BigDecimal.ZERO);
        usuarioEmprestimo.addEmprestimo(emprestimo);
    }

    public Emprestimo encontrarEmprestimo(Livro livro, Usuario usuario) throws UsuarioNaoEncontradoException, EmprestimoNaoEncontradoException {
        Optional<Usuario> optionalUsuario = usuarios.stream().filter(usuario::equals).findAny();
        if(optionalUsuario.isEmpty()){
            throw new UsuarioNaoEncontradoException();
        }
        Usuario usuarioEmprestimo = optionalUsuario.get();

        Optional<Emprestimo> optionalEmprestimo = usuarioEmprestimo.getEmprestimos()
                .stream()
                .filter(emprestimo -> emprestimo.getLivroLocado().equals(livro)).findAny();

        if(optionalEmprestimo.isEmpty()){
            throw new EmprestimoNaoEncontradoException();
        }
        return optionalEmprestimo.get();
    }

    public void devolver(Livro livro, Usuario usuario, BigDecimal valorPago, LocalDate dataDevolucao)
            throws UsuarioNaoEncontradoException, EmprestimoNaoEncontradoException, DataInvalidaException, ValorInvalidoExcepton {

        Emprestimo emprestimo = encontrarEmprestimo(livro, usuario);
        BigDecimal valorPagar = valorPagarPorEmprestimo(livro, usuario,dataDevolucao);

        if(valorPago.compareTo(valorPagar) != 0){
            throw new ValorInvalidoExcepton();
        }

        emprestimo.setValorPago(valorPago);
        emprestimo.setDataDevolucao(dataDevolucao);
        emprestimo.getLivroLocado().setEmprestado(false);
        emprestimo.getLivroLocado().setUsuarioReserva(null);

    }

    public BigDecimal valorPagarPorEmprestimo(Livro livro, Usuario usuario, LocalDate dataDevolucao) throws UsuarioNaoEncontradoException, EmprestimoNaoEncontradoException, DataInvalidaException {


        Emprestimo emprestimo = encontrarEmprestimo(livro, usuario);

        if(emprestimo.getDataEmprestimo().plusDays(7).isAfter(dataDevolucao)){
            return VALOR_EMPRESTIMO;
        }

        int diasTotalAtraso = dataDevolucao.compareTo(emprestimo.getDataPrevista());
        if(diasTotalAtraso <= 0){
            throw new DataInvalidaException();
        }
        BigDecimal valorMulta = VALOR_MULTA_DIA.multiply(BigDecimal.valueOf(diasTotalAtraso));
        BigDecimal limiteMulta = VALOR_EMPRESTIMO.multiply(BigDecimal.valueOf(0.6));

        if(valorMulta.compareTo(limiteMulta) > 0){
            valorMulta = limiteMulta;
        }
        return valorMulta.add(VALOR_EMPRESTIMO);
    }
}
