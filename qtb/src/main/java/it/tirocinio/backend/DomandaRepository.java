package it.tirocinio.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import it.tirocinio.entity.quiz.Domanda;

public interface DomandaRepository extends JpaRepository<Domanda,Long>{

}
