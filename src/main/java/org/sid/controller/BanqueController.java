package org.sid.controller;

import org.sid.entities.Compte;
import org.sid.entities.Operation;
import org.sid.metier.BanqueServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BanqueController {
	@Autowired
	private BanqueServices banque;
	
	@RequestMapping("/operations")
	public String index() {
		return "comptes";
	}
	
	@RequestMapping("/consulterCompte")
	public String consulter(Model model, String codeCompte,
			@RequestParam(name="page",defaultValue = "0") int page, 
			@RequestParam(name="size",defaultValue = "5")int size) {
		model.addAttribute("codeCompte", codeCompte);
		try {
			Compte cp = banque.consulter(codeCompte);
			Page<Operation> pageOperations = 
					banque.listOperation(codeCompte, page, size);
			model.addAttribute("listOperations", pageOperations.getContent());
			int[] pages = new int[pageOperations.getTotalPages()];
			model.addAttribute("pages",pages);
			model.addAttribute("compte", cp);
			
			
		} catch (Exception e) {
			model.addAttribute("exception",e);
		}
		return "comptes";
		
	}
	
	@RequestMapping(value="/saveOperation",method = RequestMethod.POST)
	public String saveOperation(Model model,String typeOperation, String codeCompte, double montant,String codeCompte2) {
		try {
			if(typeOperation.equals("VERS")) {
				banque.verser(codeCompte, montant);
			}else if(typeOperation.equals("RETR")) {
				banque.retirer(codeCompte, montant);
			}else{
				banque.virement(codeCompte, codeCompte2, montant);
			}
		} catch (Exception e) {
			model.addAttribute("error" , e);
			return "redirect:/consulterCompte?codeCompte="+codeCompte+"&error="+e.getMessage();
		}
		return "redirect:/consulterCompte?codeCompte="+codeCompte;
	}
}
