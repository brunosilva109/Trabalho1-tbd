package com.tbd.livros.dto;

public class RelatorioDesempenhoDTO {
    private long tempoSemCache;
    private long tempoComCache;

    public RelatorioDesempenhoDTO() {}

    public RelatorioDesempenhoDTO(long tempoSemCache, long tempoComCache) {
        this.tempoSemCache = tempoSemCache;
        this.tempoComCache = tempoComCache;
    }

    public long getTempoSemCache() { return tempoSemCache; }
    public void setTempoSemCache(long tempoSemCache) { this.tempoSemCache = tempoSemCache; }
    public long getTempoComCache() { return tempoComCache; }
    public void setTempoComCache(long tempoComCache) { this.tempoComCache = tempoComCache; }
}
