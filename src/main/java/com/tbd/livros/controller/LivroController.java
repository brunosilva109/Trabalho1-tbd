package com.tbd.livros.controller;

import com.tbd.livros.service.LivroService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LivroController {
    private final LivroService livroService;

    public LivroController(LivroService livroService) {
        this.livroService = livroService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/jdbc/livros")
    public String listarViaJDBC(Model model) {
        model.addAttribute("livros", livroService.listarViaJDBC());
        model.addAttribute("fonte", "JDBC");
        return "livros";
    }

    @GetMapping("/jpa/livros")
    public String listarViaJPA(Model model) {
        model.addAttribute("livros", livroService.listarViaJPA());
        model.addAttribute("fonte", "JPA");
        return "livros";
    }

    @GetMapping("/jpa/livros/buscar")
    public String buscarPorAutor(@RequestParam(required = false) String autor, Model model) {
        if (autor != null && !autor.trim().isEmpty()) {
            model.addAttribute("livros", livroService.buscarPorAutor(autor));
            model.addAttribute("fonte", "JPA - Busca por Autor: " + autor);
        }
        return "livros-autor";
    }

    @GetMapping("/cache/livros")
    public String listarViaCache(Model model) {
        model.addAttribute("livros", livroService.listarViaCache());
        model.addAttribute("fonte", "Redis Cache");
        return "livros";
    }

    @GetMapping("/desempenho")
    public String desempenho(Model model) {
        model.addAttribute("relatorio", livroService.executarTesteDesempenho());
        return "desempenho";
    }
}
