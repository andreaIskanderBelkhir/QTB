package it.tirocinio.backend;

import org.springframework.data.jpa.repository.JpaRepository;

import it.tirocinio.entity.quiz.Quiz;

public interface QuizRepository extends JpaRepository<Quiz,Long> {

}
