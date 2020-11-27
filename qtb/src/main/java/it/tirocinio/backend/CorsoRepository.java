package it.tirocinio.backend;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;

public interface CorsoRepository extends JpaRepository<Corso,Long> {

	 @Query("select c from Corso c " +
		      "where lower(c.nomeCorso) like lower(concat('%', :searchTerm, '%')) ")
	List<Corso> search(@Param("searchTerm") String value);

   
}
