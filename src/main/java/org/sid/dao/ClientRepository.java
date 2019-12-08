package org.sid.dao;

 
import org.sid.entities.Client;
 import org.springframework.data.jpa.repository.JpaRepository;
 
public interface ClientRepository extends JpaRepository<Client,Long> {  //interface JPA/repository (utilisation de spring data necessite la creation de l'interface jpaRepository)  ---- le Id de Client est de type long
	
	 
}
