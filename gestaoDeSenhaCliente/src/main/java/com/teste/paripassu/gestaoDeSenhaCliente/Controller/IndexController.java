package com.teste.paripassu.gestaoDeSenhaCliente.Controller;

import java.util.concurrent.Callable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping({ "/", "/index" })

public class IndexController {

	@Autowired
	RestTemplate restTemplate;

	final static String URL_WEBSERVICE = "http://localhost:8080/receberRequisicao";

	@GetMapping
	public String index() {
		return "/index";
	}
	
	@RequestMapping(value = "/visualizarProximaSenha", method = RequestMethod.GET)
	public ModelAndView visualizarSenhas(RedirectAttributes model) {

		try {
			String response = restTemplate.getForObject(URL_WEBSERVICE, String.class);
			model.addFlashAttribute("proximaSenha", response);
			return new ModelAndView("redirect:/index");
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			model.addFlashAttribute("falha", "Ocorreu uma falha na transação, favor contatar os ervidor");
			return new ModelAndView("redirect:/error");
		}
	}

	@PostMapping
	public Callable<ModelAndView> gerarProximaSenha(String reqTipoSenha, RedirectAttributes model) {
		return () -> {
			try {
				String response = restTemplate.postForObject(URL_WEBSERVICE, reqTipoSenha, String.class);
				model.addFlashAttribute("senhaGerada", response);
				return new ModelAndView("redirect:/index");
			} catch (HttpClientErrorException e) {
				e.printStackTrace();
				model.addFlashAttribute("falha", "Ocorreu uma falha na transação, favor contatar os ervidor");
				return new ModelAndView("redirect:/error");
			}
		};
	}

	@RequestMapping(value = "/zerarsenhas", method = RequestMethod.POST)
	public ModelAndView zerarSenhas(RedirectAttributes model) {

		try {
			String response = restTemplate.postForObject(URL_WEBSERVICE, "zerarsenhas", String.class);
			model.addFlashAttribute("senhaGerada", response);
			return new ModelAndView("redirect:/index");
		} catch (HttpClientErrorException e) {
			e.printStackTrace();
			model.addFlashAttribute("falha", "Ocorreu uma falha na transação, favor contatar os ervidor");
			return new ModelAndView("redirect:/error");
		}
	}
}
