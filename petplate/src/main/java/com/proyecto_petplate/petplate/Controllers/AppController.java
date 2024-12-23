package com.proyecto_petplate.petplate.Controllers;



import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.proyecto_petplate.petplate.services.ModelAndViewService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.ModelAndView;






@Controller
@RequiredArgsConstructor
public class AppController {

    @Autowired
    private ModelAndViewService modelAndViewService;

    @GetMapping("/recipe/{recipeId}")
    private String paginaReceta(@PathVariable int recipeId,Model model){
        return modelAndViewService.getPaginaReceta(recipeId, model);
    }

    @GetMapping("/profile/{profileId}")
    public String paginaPerfil(@PathVariable int profileId,Model model) {
        return modelAndViewService.getPaginaPerfil(profileId, model);
    }
    
    @GetMapping("/")
    public String paginaPrinsipal(Model model) {
        return modelAndViewService.getPaginaPrinsipal(model);
    }
    
    @GetMapping("/recipe/create")
    public String paginaCrearReceta(Model model) {
        
        return modelAndViewService.getCrearReceta(model);
    }

    @GetMapping("/recipe/modify/{recipeId}")
    public String modificarReceta(@PathVariable int recipeId,Model model) {
        return modelAndViewService.getModificarReceta(recipeId, model);
    }
    
    @GetMapping("/busqueda/resultados")
    public String resultadosBusqueda(Model model) {
        return modelAndViewService.getResultadosBusqueda(model);
    }
    
    @GetMapping("/backOffice")
    public ModelAndView backOffice(@CookieValue(value = "token", defaultValue = "") String token,Model model) {
        return modelAndViewService.getModelBackOfice(token,model);
    }
    
}
