package com.tbd.livros.service;

import com.tbd.livros.cache.LivroCache;
import com.tbd.livros.dto.RelatorioDesempenhoDTO;
import com.tbd.livros.jdbc.LivroJDBC;
import com.tbd.livros.model.Livro;
import com.tbd.livros.repository.LivroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LivroService {
    private static final Logger log = LoggerFactory.getLogger(LivroService.class);

    private final LivroJDBC livroJDBC;
    private final LivroRepository livroRepository;
    private final LivroCache livroCache;

    public LivroService(LivroJDBC livroJDBC, LivroRepository livroRepository, LivroCache livroCache) {
        this.livroJDBC = livroJDBC;
        this.livroRepository = livroRepository;
        this.livroCache = livroCache;
    }

    public List<Livro> listarViaJDBC() {
        return livroJDBC.listarLivros();
    }

    public List<Livro> listarViaJPA() {
        return livroRepository.listarTodos();
    }

    public List<Livro> buscarPorAutor(String autor) {
        return livroRepository.buscarPorAutor(autor);
    }

    public List<Livro> listarViaCache() {
        return livroCache.listarLivros();
    }

    public RelatorioDesempenhoDTO executarTesteDesempenho() {
        int iteracoes = 500;
        String filtro = "ficcao";

        // Warm-up: popula o cache com a busca filtrada
        livroCache.listarLivrosFiltrado(filtro);

        log.info("=== TESTE DE DESEMPENHO ({} iterações, filtro '{}') ===", iteracoes, filtro);

        long inicio = System.currentTimeMillis();
        for (int i = 0; i < iteracoes; i++) {
            livroJDBC.listarLivrosFiltrado(filtro);
        }
        long tempoSemCache = System.currentTimeMillis() - inicio;
        log.info("Sem cache: {} ms ({} ms por chamada)", tempoSemCache, tempoSemCache / (double) iteracoes);

        inicio = System.currentTimeMillis();
        for (int i = 0; i < iteracoes; i++) {
            livroCache.listarLivrosFiltrado(filtro);
        }
        long tempoComCache = System.currentTimeMillis() - inicio;
        log.info("Com cache: {} ms ({} ms por chamada)", tempoComCache, tempoComCache / (double) iteracoes);

        return new RelatorioDesempenhoDTO(tempoSemCache, tempoComCache);
    }
}
