package it.tirocinio.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import it.tirocinio.entity.quiz.Risposta;

public interface RispostaRepository extends JpaRepository<Risposta,Long>{

}
