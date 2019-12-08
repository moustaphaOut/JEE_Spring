package org.sid.metier;

import java.util.Date;
import java.util.Optional;

import javax.transaction.Transactional;

import org.sid.dao.CompteRepository;
import org.sid.dao.OperationRepository;
import org.sid.entities.Compte;
import org.sid.entities.CompteCourant;
import org.sid.entities.Operation;
import org.sid.entities.Retrait;
import org.sid.entities.Versement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class BanqueServicesImpl implements BanqueServices {
	@Autowired
	private CompteRepository compteRepository;
	@Autowired
	private OperationRepository operationRepository;
	
	@Override
	public Compte consulter(String codeCompte) {
		Optional<Compte> cp = compteRepository.findById(codeCompte);
		Compte cpp = cp.get();
		if(cpp == null) throw new RuntimeException("Compte Introuvable");
		return cpp;
	}

	@Override
	public void verser(String codeCompte, double montant) {
		Compte cp = consulter(codeCompte);
		Versement vr = new Versement(new Date(),montant,cp);
		operationRepository.save(vr);
		cp.setSolde(cp.getSolde()+montant);
		compteRepository.save(cp);
	}

	@Override
	public void retirer(String codeCompte, double montant) {
		Compte cp = consulter(codeCompte);
		double facilitesCaisse = 0;
		if(cp instanceof CompteCourant)
			facilitesCaisse = ((CompteCourant) cp).getDecouvert();
		if(cp.getSolde()+facilitesCaisse<montant)
			throw new RuntimeException("Solde insuffisant");
		Retrait r = new Retrait(new Date(),montant,cp);
		operationRepository.save(r);
		cp.setSolde(cp.getSolde()-montant);
		compteRepository.save(cp);	
	}

	@Override
	public void virement(String codeCompteRetrait, String codeCompteVersement, double montant) {
		if(codeCompteRetrait.equals(codeCompteVersement)) {
			throw new RuntimeException("Impossible virment sur le meme compte");
		}
		retirer(codeCompteRetrait,montant);
		verser(codeCompteVersement, montant);
	}

	@Override
	public Page<Operation> listOperation(String codeCompte, int page, int sizePage) {
		Pageable pageable = PageRequest.of(page, sizePage);
		return operationRepository.listOperation
				(codeCompte,pageable);
	}

	
}
