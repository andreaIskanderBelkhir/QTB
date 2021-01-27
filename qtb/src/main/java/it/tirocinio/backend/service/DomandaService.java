package it.tirocinio.backend.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.vaadin.ui.Notification;

import it.tirocinio.backend.DomandaRepository;
import it.tirocinio.backend.QuizRepository;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;
import it.tirocinio.entity.quiz.Domanda;
import it.tirocinio.entity.quiz.Quiz;
import it.tirocinio.entity.quiz.Risposta;

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
		Boolean b=true;
		for(Domanda c:findAll()){
			if(c.getNomedomanda().equals(domanda.getNomedomanda()))
				if(!(c.getID().equals(domanda.getID())))
				b=false;
		}
		return b;
	}

	public void modificaDomanda(Domanda domanda, Domanda domandavecchia) {
		this.elimina(domandavecchia);
		this.save(domanda);
		
	}

	public void elimina(Domanda value) {
		this.domandaR.delete(value);
		
	}

	public void eliminaRisposta(Domanda domanda, Risposta risposta) {
		for(Domanda d:findAll()){
			if(d.equals(domanda))
				d.getRisposte().remove(risposta);
		}
		
	}

	public List<Risposta> findRisposte(Domanda value) {	
		List<Risposta>lista= new ArrayList<>();
		for(Domanda dom:findAll()){
			if(dom.equals(value))
				lista.addAll(dom.getRisposte());
		}

		return lista;	
	}

	public Domanda findById(Long id) {
		List<Domanda> ut=findAll();
		for(Domanda u: ut){
			if(u.getID().equals(id)){
				return u;
			}
		}
		return null;
	}
}
