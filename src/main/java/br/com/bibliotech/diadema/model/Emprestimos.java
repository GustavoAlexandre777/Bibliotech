package br.com.bibliotech.diadema.model;


import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "emprestimos")
public class Emprestimos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "data_saida")
    private LocalDateTime dataSaida;

    @Column(name = "data_devolucao_prevista")
    private LocalDateTime dataDevolucao;

    @OneToOne
    @JoinColumn(name = "usuario_cpf", referencedColumnName = "cpf")
    private Usuario cpfUsuario;

    @OneToOne
    @JoinColumn(name = "isbn", referencedColumnName = "isbn")
    private Livros isbn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDataSaida() {
        return dataSaida;
    }

    public void setDataSaida(LocalDateTime dataSaida) {
        this.dataSaida = dataSaida;
    }

    public LocalDateTime getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDateTime dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public Usuario getCpfUsuario() {
        return cpfUsuario;
    }

    public void setCpfUsuario(Usuario cpfUsuario) {
        this.cpfUsuario = cpfUsuario;
    }

    public Livros getIsbn() {
        return isbn;
    }

    public void setIsbn(Livros isbn) {
        this.isbn = isbn;
    }
}
