package it.tirocinio.backend;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;

public interface CorsoRepository extends JpaRepository<Corso,Long> {

   
}
