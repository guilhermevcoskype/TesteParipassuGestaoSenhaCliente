package com.teste.paripassu.gestaoDeSenhaCliente.Controller;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.teste.paripassu.gestaoDeSenhaCliente.model.Usuario;

@Controller
public class LoginController {
	
	@Autowired
	RestTemplate restTemplate;
	
	final static String URL_WEBSERVICE = "http://localhost:8080/login";
	
	@RequestMapping("/login")
	public ModelAndView login() {
		ModelAndView modelAndView = new ModelAndView("login");
		modelAndView.addObject("paginaLogin", true);
		return modelAndView;
	}

	@RequestMapping("/login-error")  
    public String loginError(Model model) {  
        model.addAttribute("loginError", true);  
        return "login";  
    }
	
	@RequestMapping(value="/login", method = RequestMethod.POST)  
	public Callable<ModelAndView> recuperarSenha(String email, String username, String password, RedirectAttributes model) {
		return () -> {
			try {
				Usuario usuario= new Usuario(email, username, password);
				Usuario response = restTemplate.postForObject(URL_WEBSERVICE, usuario, Usuario.class);
				model.addFlashAttribute("usuario", response);
				return new ModelAndView("redirect:/index");
			} catch (HttpClientErrorException e) {
				e.printStackTrace();
				model.addFlashAttribute("falha", "Ocorreu uma falha na transação, favor contatar os ervidor");
				return new ModelAndView("redirect:/error");
			}
		};
	}
	
}
