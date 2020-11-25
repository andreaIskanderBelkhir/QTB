package it.tirocinio.backend.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import it.tirocinio.backend.DomandaRepository;
import it.tirocinio.backend.QuizRepository;
import it.tirocinio.entity.quiz.Corso;
import it.tirocinio.entity.quiz.Domanda;
import it.tirocinio.entity.quiz.Quiz;

@Service
public class DomandaService {
	private QuizRepository quizrep;
	private DomandaRepository domandaR;
	public DomandaService(QuizRepository q,DomandaRepository d){
		this.quizrep=q;
		this.domandaR=d;
	}
	
	public List<Domanda> findAll(){
		return this.domandaR.findAll();
	}
	
	public void save(Domanda c){
		if(c==null){
			return;
		}
		else
			this.domandaR.save(c);
	}
	
	public List<Domanda> findByQuiz(Quiz q){
		List<Domanda> lista = new ArrayList<>();
		for(Domanda d : findAll()){
			if(d.getQuizapparteneza().equals(q)){
				lista.add(d);
			}
		}
		return lista;		
	}

	public boolean domandaNonEsiste(Domanda domanda) {
		for(Domanda c:findAll()){
			if(c.getDescrizione().equals(domanda.getDescrizione()))
				return false;
		}
		return true;
	}
}
