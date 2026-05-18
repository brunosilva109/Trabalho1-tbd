package com.tbd.livros.jdbc;

import com.tbd.livros.model.Livro;
import org.springframework.stereotype.Repository;
import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class LivroJDBC {
    private final DataSource dataSource;

    public LivroJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Livro> listarLivros() {
        String sql = "SELECT id, titulo, autor, ano_publicacao, categoria FROM livro ORDER BY id";
        List<Livro> livros = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Livro livro = new Livro();
                livro.setId(rs.getLong("id"));
                livro.setTitulo(rs.getString("titulo"));
                livro.setAutor(rs.getString("autor"));
                livro.setAnoPublicacao(rs.getInt("ano_publicacao"));
                livro.setCategoria(rs.getString("categoria"));
                livros.add(livro);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Falha ao Listar Livros via JDBC", e);
        }
        return livros;
    }

    public List<Livro> listarLivrosFiltrado(String filtro) {
        String sql = "SELECT id, titulo, autor, ano_publicacao, categoria FROM livro "
                    + "WHERE lower(categoria) LIKE ? ORDER BY id";
        List<Livro> livros = new ArrayList<>();
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, "%" + filtro.toLowerCase() + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Livro livro = new Livro();
                    livro.setId(rs.getLong("id"));
                    livro.setTitulo(rs.getString("titulo"));
                    livro.setAutor(rs.getString("autor"));
                    livro.setAnoPublicacao(rs.getInt("ano_publicacao"));
                    livro.setCategoria(rs.getString("categoria"));
                    livros.add(livro);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro livros não encontrados pelos filtros via JDBC", e);
        }
        return livros;
    }
}
