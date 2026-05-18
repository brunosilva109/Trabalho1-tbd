package com.tbd.livros.cache;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tbd.livros.jdbc.LivroJDBC;
import com.tbd.livros.model.Livro;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class LivroCache {
    
    private static final String CHAVE_GERAL = "livros:todos";
    private static final long TEMPO_EXPIRACAO_SEGUNDOS = 60;
    private static final Logger registroLog = LoggerFactory.getLogger(LivroCache.class);

    private final StringRedisTemplate templateRedis;
    private final LivroJDBC conexaoJdbc;
    private final ObjectMapper conversorJson;

    public LivroCache(StringRedisTemplate templateRedis, LivroJDBC conexaoJdbc, ObjectMapper conversorJson) {
        this.templateRedis = templateRedis;
        this.conexaoJdbc = conexaoJdbc;
        this.conversorJson = conversorJson;
    }

    public List<Livro> listarLivros() {
        try {
            String dadosJson = templateRedis.opsForValue().get(CHAVE_GERAL);
            if (dadosJson != null) {
                List<Livro> colecaoLivros = conversorJson.readValue(dadosJson, new TypeReference<List<Livro>>() {});
                registroLog.info("Cache HIT - Encontrados {} livros", colecaoLivros.size());
                return colecaoLivros;
            }
        } catch (Exception erro) {
            registroLog.warn("Falha no GET do Redis, buscando no PostgreSQL", erro);
        }
        
        List<Livro> colecaoLivros = conexaoJdbc.listarLivros();
        
        try {
            String dadosJson = conversorJson.writeValueAsString(colecaoLivros);
            templateRedis.opsForValue().set(CHAVE_GERAL, dadosJson, TEMPO_EXPIRACAO_SEGUNDOS, TimeUnit.SECONDS);
            registroLog.info("Cache MISS - Guardando {} livros", colecaoLivros.size());
        } catch (Exception erro) {
            registroLog.warn("Falha no SET do Redis", erro);
        }
        
        return colecaoLivros;
    }

    public List<Livro> listarLivrosFiltrado(String termoBusca) {
        String chaveFiltro = "livros:busca:" + termoBusca.toLowerCase();
        try {
            String dadosJson = templateRedis.opsForValue().get(chaveFiltro);
            if (dadosJson != null) {
                List<Livro> colecaoLivros = conversorJson.readValue(dadosJson, new TypeReference<List<Livro>>() {});
                registroLog.info("Cache HIT - Busca por '{}': {} livros", termoBusca, colecaoLivros.size());
                return colecaoLivros;
            }
        } catch (Exception erro) {
            registroLog.warn("Falha no GET do Redis para a busca '{}', consultando banco de dados", termoBusca, erro);
        }
        
        List<Livro> colecaoLivros = conexaoJdbc.listarLivrosFiltrado(termoBusca);
        
        try {
            String dadosJson = conversorJson.writeValueAsString(colecaoLivros);
            templateRedis.opsForValue().set(chaveFiltro, dadosJson, TEMPO_EXPIRACAO_SEGUNDOS, TimeUnit.SECONDS);
            registroLog.info("Cache MISS - Busca por '{}': salvando {} livros", termoBusca, colecaoLivros.size());
        } catch (Exception erro) {
            registroLog.warn("Falha no SET do Redis para a busca '{}'", termoBusca, erro);
        }
        
        return colecaoLivros;
    }
}