package it.tirocinio.backend;


import org.springframework.data.jpa.repository.JpaRepository;


import it.tirocinio.entity.Utente;


public interface UtenteRepository extends JpaRepository<Utente,Long> {

	 Utente findByUsername(String nome);
	 Utente findByPassword(String password);

}
