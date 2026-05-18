package com.tbd.livros.repository;

import com.tbd.livros.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {

    @Query("select l from Livro l")
    List<Livro> listarTodos();

    @Query("select l from Livro l where lower(l.autor) like lower(concat('%', :autor, '%'))")
    List<Livro> buscarPorAutor(@Param("autor") String autor);
}
