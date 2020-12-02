package it.tirocinio.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import it.tirocinio.backend.DomandaRepository;
import it.tirocinio.backend.QuizRepository;
import it.tirocinio.backend.RispostaRepository;
import it.tirocinio.entity.quiz.Domanda;
import it.tirocinio.entity.quiz.Quiz;
import it.tirocinio.entity.quiz.Risposta;

@Service
public class RispostaService {
	private DomandaRepository domandar;
	private RispostaRepository rispostaR;

	public RispostaService(DomandaRepository d,RispostaRepository r){
		this.domandar=d;
		this.rispostaR=r;
	}
	public List<Risposta> findAll(){
		return this.rispostaR.findAll();
	}
	
	public void save(Risposta c){
		if(c==null){
			return;
		}
		else
			this.rispostaR.save(c);
	}
	
	public List<Risposta> findByDomanda(Domanda q){
		List<Risposta> lista = new ArrayList<>();
		for(Risposta d : findAll()){
			if(d.getDomandaApparteneza().equals(q)){
				lista.add(d);
			}
		}
		return lista;		
	}
	public void modifica(Risposta risposta, Risposta value) {
		this.rispostaR.delete(value);
		this.save(risposta);
		
	}
	public void elimina(Risposta value) {
		this.rispostaR.delete(value);
		
	}
	public void eliminatutte(Set<Risposta> risposte) {
		for(Risposta r : risposte){
			elimina(r);
		}	
	}
}
