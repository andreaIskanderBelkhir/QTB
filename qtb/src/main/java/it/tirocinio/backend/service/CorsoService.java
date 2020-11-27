package it.tirocinio.backend.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import it.tirocinio.backend.CorsoRepository;
import it.tirocinio.backend.UtenteRepository;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;

@Service
public class CorsoService {

	private final UtenteRepository utenteRepository;
	private CorsoRepository corsorep;
	
	public CorsoService(UtenteRepository u,CorsoRepository c){
		this.corsorep=c;
		this.utenteRepository=u;
	}
	
	public List<Corso> findAll(){
		return this.corsorep.findAll();
	}
	public List<Corso> findAll(String value) {
		if(value==null||value.isEmpty()){
		return this.corsorep.findAll();
		}
		else{
			return this.corsorep.search(value);
		}
	}
	
	public List<Corso> findbyDocente(String nome){
		List<Corso> co= findAll();
		List<Corso> lista = new ArrayList<>();
		for(Corso c:co){
			if(c.getDocente().getNome().equals(nome)){
				lista.add(c);
			}
		}
		return lista;			
	}
	
	public void addStudente(Utente u,Corso c){
		List<Utente> iscritti = new ArrayList<>(); 
		if(u==null){
			return;
		}
		else{
			for(Corso cor:findAll()){
				if(cor.equals(c)){
					if(c.getUtentifreq()==null){
						List<Utente> iscrittinew =new ArrayList<>();
						iscrittinew.add(u);
						c.setUtentifreq(iscrittinew);
						this.corsorep.save(c);
					}
					else
					{
						iscritti = c.getUtentifreq();
						iscritti.add(u);
						c.setUtentifreq(iscritti);
						this.corsorep.save(c);
					}
				}
			}

				}

	}
	
	
	
	public void save(Corso c){
		if(c==null){
			return;
		}
		else
			this.corsorep.save(c);
	}

	public boolean partecipa(Corso c, Utente studente) {
		List<Utente> partecipanti=c.getUtentifreq();
		Boolean ris=false;
		for(Utente u:partecipanti){
			if(u.equals(studente)){
				return true;
			}
		}
		return ris;
	}

	public boolean corsoNonEsistente(Corso corso) {
		for(Corso c:findAll()){
			if(c.getNomeCorso().equals(corso.getNomeCorso()))
				return false;
		}
		return true;
	}

	public void modificaCorso(Corso corso, Corso corsovecchio) {
		this.corsorep.delete(corsovecchio);
		this.save(corso);
		
	}

	public void elimina(Corso value) {
		this.corsorep.delete(value);
		
	}



}
