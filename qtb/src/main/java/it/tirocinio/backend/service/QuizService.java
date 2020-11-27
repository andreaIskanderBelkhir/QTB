package it.tirocinio.backend.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import it.tirocinio.backend.CorsoRepository;
import it.tirocinio.backend.QuizRepository;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;
import it.tirocinio.entity.quiz.Quiz;

@Service
public class QuizService {
	private QuizRepository quizr;
	private CorsoRepository corsor;

	public QuizService(QuizRepository r,CorsoRepository c){
		this.quizr=r;
		this.corsor=c;
	}
	public List<Quiz> findAll(){
		return this.quizr.findAll();
	}

	public List<Quiz> findAllAttivati(Boolean b){
		List<Quiz> quizs=findAll();
		List<Quiz> quiz=new ArrayList<>();
		for(Quiz q : quizs){
			if(q.getAttivato()==b){
				quiz.add(q);
			}
		}
		return quiz;
	}

	public List<Quiz> findAllByCorso(Corso c){
		List<Quiz> quizs=findAll();
		List<Quiz> quiz=new ArrayList<>();
		for(Quiz q : quizs){
			if(q.getCorsoAppartenenza().equals(c)){
				quiz.add(q);
			}
		}
		return quiz;
	}

	public void save(Quiz z){
		if(z==null){
			return;
		}
		else 
			this.quizr.save(z);
	}
	public void changeValid(Quiz q) {

		if(q.getAttivato()==true){
			q.setAttivato(false);
		}
		else
			q.setAttivato(true);
		this.quizr.save(q);
	}
	public List<Quiz> findAllByCorsoandAttivati(Corso value) {
		List<Quiz> quizs=findAll();
		List<Quiz> quiz=new ArrayList<>();
		for(Quiz q : quizs){
			if((q.getCorsoAppartenenza().equals(value)) && (q.getAttivato()==true)){
				quiz.add(q);
			}
		}
		return quiz;
	}
	public boolean quizNonEsistente(Quiz quiz) {
		for(Quiz c:findAll()){
			if(c.getNomeQuiz().equals(quiz.getNomeQuiz()))
				return false;
		}
		return true;
	}
	public List<Quiz> findAllByDocente(Utente docente) {
		List<Quiz> possibili = new ArrayList<>();
		List<Corso> co= this.corsor.findAll();
		List<Corso> corsideldoc = new ArrayList<>();
		for(Corso c:co){
			if(c.getDocente().equals(docente)){
				corsideldoc.add(c);
			}
		}
		for(Corso cor:corsideldoc){
			for(Quiz q:findAll()){
				if(q.getCorsoAppartenenza().equals(cor))
					possibili.add(q);
			}
		}
      return possibili;
	}
}


