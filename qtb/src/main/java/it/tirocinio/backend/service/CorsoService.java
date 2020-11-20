package it.tirocinio.backend.service;

import java.util.ArrayList;
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
	
	public void addStudente(Utente u,String nomec){
		List<Utente> iscritti = new ArrayList<>(); 
		if(u==null){
			return;
		}
		else{
			List<Corso> co=findAll();
			for(Corso c:co){
				if(c.getNomeCorso().equals(nomec)){
					iscritti = c.getUtentifreq();
					iscritti.add(u);
					c.setUtentifreq(iscritti);
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
}
