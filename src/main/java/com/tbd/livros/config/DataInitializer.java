package com.tbd.livros.config;

import com.tbd.livros.model.Livro;
import com.tbd.livros.repository.LivroRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer {
    
    private static final Logger logRegistro = LoggerFactory.getLogger(DataInitializer.class);
    private static final int MAX_LIVROS_GERADOS = 10000;
    private static final int TAMANHO_DO_LOTE = 1000;

    private final LivroRepository repositorioLivro;
    private final Random geradorAleatorio = new Random(42);

    private static final String[] ESCRITORES = {
        "J.R.R. Tolkien", "George Orwell", "Machado de Assis", "Isaac Asimov",
        "Gabriel García Márquez", "J.K. Rowling", "Douglas Adams", "Frank Herbert",
        "William Gibson", "Patrick Rothfuss", "Dan Brown", "Júlio Verne",
        "Jane Austen", "Fiódor Dostoiévski", "Ernest Hemingway", "Clarice Lispector",
        "Carlos Drummond de Andrade", "Jorge Amado", "Érico Veríssimo", "Cecília Meireles"
    };

    private static final String[] PARTE_INICIAL_TITULO = {
        "O Enigma", "A Jornada", "O Segredo", "O Mistério", "O Legado",
        "A Profecia", "O Universo", "O Horizonte", "O Abismo", "O Portal",
        "O Silêncio", "A Memória", "O Tempo", "O Destino", "O Caos",
        "A Luz", "A Sombra", "O Vento", "O Rio", "A Montanha"
    };

    private static final String[] PARTE_FINAL_TITULO = {
        "dos Livros", "do Tempo", "da Alma", "do Fogo", "do Mar",
        "das Estrelas", "do Vento", "da Terra", "do Sonho", "da Noite",
        "do Sol", "das Nuvens", "do Gelo", "da Chuva", "do Destino",
        "dos Deuses", "do Amor", "da Morte", "da Vida", "das Palavras"
    };

    private static final String[] GENEROS_LITERARIOS = {
        "Fantasia", "Ficção Científica", "Literatura Brasileira", "Aventura",
        "Suspense", "Drama", "Romance", "Terror", "Humor", "Ficção Política",
        "Realismo Mágico", "Clássico", "Infantojuvenil", "Ensaio", "Biografia"
    };

    public DataInitializer(LivroRepository repositorioLivro) {
        this.repositorioLivro = repositorioLivro;
    }

    @PostConstruct
    public void init() {
        if (repositorioLivro.count() > 0) {
            logRegistro.info("Base de dados já contém {} registros, ignorando a inicialização", repositorioLivro.count());
            return;
        }

        logRegistro.info("Iniciando a inserção de {} livros em lotes de {}...", MAX_LIVROS_GERADOS, TAMANHO_DO_LOTE);
        long marcaInicial = System.currentTimeMillis();

        for (int indiceLote = 0; indiceLote < MAX_LIVROS_GERADOS / TAMANHO_DO_LOTE; indiceLote++) {
            List<Livro> loteLivros = new ArrayList<>(TAMANHO_DO_LOTE);
            
            for (int i = 0; i < TAMANHO_DO_LOTE; i++) {
                String tituloGerado = PARTE_INICIAL_TITULO[geradorAleatorio.nextInt(PARTE_INICIAL_TITULO.length)] + " "
                    + PARTE_FINAL_TITULO[geradorAleatorio.nextInt(PARTE_FINAL_TITULO.length)];
                String autorSorteado = ESCRITORES[geradorAleatorio.nextInt(ESCRITORES.length)];
                int anoPublicacao = 1800 + geradorAleatorio.nextInt(225);
                String generoSorteado = GENEROS_LITERARIOS[geradorAleatorio.nextInt(GENEROS_LITERARIOS.length)];
                
                loteLivros.add(new Livro(tituloGerado, autorSorteado, anoPublicacao, generoSorteado));
            }
            repositorioLivro.saveAll(loteLivros);
        }

        long marcaFinal = System.currentTimeMillis();
        logRegistro.info("Carga concluída: {} livros inseridos em {} ms", MAX_LIVROS_GERADOS, marcaFinal - marcaInicial);
    }
}