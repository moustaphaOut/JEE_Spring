package org.sid.metier;


import org.sid.entities.Compte; 
import org.sid.entities.Operation;
import org.springframework.data.domain.Page;

public interface BanqueServices {

	 public Compte consulter(String codeCompte);
	 public void verser(String codeCompte, double montant );
	 public void retirer(String codeCompte, double montant );
	 public void virement(String codeCompteRetrait,String codeCompteVersement,double montant);
	 public Page<Operation> listOperation(String codeCompte,int page,int sizePage);

}
