package it.tirocinio.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import it.tirocinio.backend.CorsoRepository;
import it.tirocinio.backend.QuizRepository;
import it.tirocinio.entity.quiz.Corso;
import it.tirocinio.entity.quiz.Quiz;

@Service
public class QuizService {
	private QuizRepository quizr;
	
	public QuizService(QuizRepository r){
		this.quizr=r;
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
			if(q.getCorsoAppertenenza().equals(c)){
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
		quizr.delete(q);
		q.setAttivato(true);		
		quizr.save(q);
	}
	
}
