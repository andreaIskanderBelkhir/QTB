package it.tirocinio.backend.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import com.vaadin.flow.component.textfield.TextField;

import it.tirocinio.backend.CorsoRepository;
import it.tirocinio.backend.UtenteRepository;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;
import it.tirocinio.entity.quiz.Quiz;

@Service
public class CorsoService {

	private final UtenteRepository utenteRepository;
	private CorsoRepository corsorep;


	public CorsoService(UtenteRepository u,CorsoRepository c){
		this.corsorep=c;

		this.utenteRepository=u;
	}



	public List<Corso> findAll(){
		return (List<Corso>) this.corsorep.findAll();
	}

	public List<Corso> findAllbyAdmin(){
		List<Corso> lista = new ArrayList<>();
		for(Utente u:this.utenteRepository.findAll()){
			if((u.getRuolo()=="ADMIN")||(u.getRuolo()=="PROFESSORE")){
				for(Corso c:u.getCorsifrequentati()){
					if(c.getDocente().equals(u)){
						lista.add(c);
					}
				}
			}
		}
		return lista;
	}

	public List<Corso> findAll(TextField filter) {
		if(filter.getValue()==null||filter.getValue().isEmpty()){
			return this.findAll();
		}
		else{
			return this.corsorep.search(filter.getValue());
		}
	}
	public List<Corso> findbyDocente(Utente nome, TextField filter) {
		if(filter.getValue()==null||filter.getValue().isEmpty()){
			return findbyDocente(nome);
		}
		else{
			List<Corso> co= findAll(filter);
			List<Corso> lista = new ArrayList<>();
			if(nome.getRuolo().equals("PROFESSORE")){
				for(Corso c:co){
					if(c.getDocente()!=null){
						if(c.getDocente().getNome().equals(nome.getNome())){
							if(c.getDocente().getCorsifrequentati().contains(c)){
								lista.add(c);
							}
						}
					}
				}
			}
			if(nome.getRuolo().equals("ADMIN")){
				for(Corso c:co){
					if(c.getDocente()!=null){		
						if(c.getDocente().getCorsifrequentati().contains(c)){
							lista.add(c);
						}
					}
				}
			}
			return lista;			
		}
	}
	public List<Corso> findbyDocente(Utente nome){
		List<Corso> co= nome.getCorsifrequentati();
		List<Corso> lista = new ArrayList<>();
		if(nome.getRuolo().equals("PROFESSORE")){
			for(Corso c:co){
				if(c.getDocente()!=null){
					if(c.getDocente().equals(nome)){
						lista.add(c);
					}
				}
			}
		}
		if(nome.getRuolo().equals("ADMIN")){
			for(Corso c:co){
				if(c.getDocente()!=null){			
					lista.add(c);
				}
			}
		}
		return lista;			
	}


	public void addCandidato(Utente u, Corso c) {
		List<Utente> iscritti = new ArrayList<>(); 
		if(u==null){
			return;
		}
		else{
			for(Corso cor:findAll()){
				if(cor.equals(c)){
					if(c.getCandidati()==null){
						List<Utente> iscrittinew =new ArrayList<>();
						iscrittinew.add(u);
						c.setCandidati(iscrittinew);
						this.corsorep.save(c);
					}
					else
					{
						iscritti = c.getCandidati();
						iscritti.add(u);
						c.setCandidati(iscritti);
						this.corsorep.save(c);
					}
				}
			}

		}


	}

	public void addStudente(Utente u,Corso c){

		if(u==null){
			return;
		}
		else{
			for(Corso cor:findAll()){
				if(cor.equals(c)){
					if(c.getUtentifreq()==null){
						List<Utente> iscritti = new ArrayList<>(); 
						iscritti.add(u);
						c.setUtentifreq(iscritti);
						this.corsorep.save(c);
					}
					else
					{

						c.getUtentifreq().add(u);
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
		this.elimina(corsovecchio);
		this.save(corso);

	}

	public void elimina(Corso value) {
		this.corsorep.delete(value);

	}



	public void eliminaQuiz(Quiz quiz) {
		Corso corso=quiz.getCorsoAppartenenza();
		this.corsorep.delete(corso);
		corso.getQuizDelcorso().remove(quiz);
		this.corsorep.save(corso);
	}

	public void addStudenteRichiesta(Utente u, Corso c) {
		List<Long> iscritti = new ArrayList<>(); 
		if(u==null){
			return;
		}
		else{
			for(Corso cor:findAll()){
				if(cor.equals(c)){
					if(c.getUtentirischiesta()==null){
						List<Long> iscrittinew =new ArrayList<>();
						iscrittinew.add(u.getID());
						c.setUtentirischiesta(iscrittinew);
						this.corsorep.save(c);
					}
					else
					{
						iscritti = c.getUtentirischiesta();
						iscritti.add(u.getID());
						c.setUtentirischiesta(iscritti);
						this.corsorep.save(c);
					}
				}
			}

		}

	}




	public List<Utente> getStudenteRichiesta(List<Long> utentirischiesta) {
		List<Utente> utenti=new ArrayList<>();
		for(Long l:utentirischiesta){
			if(findByIdUtente(l)!=null){
				utenti.add(findByIdUtente(l));
			}
		}
		return utenti;
	}

	public Utente findByIdUtente(Long id) {
		// TODO Auto-generated method stub
		List<Utente> ut=this.utenteRepository.findAll();
		for(Utente u: ut){
			if(u.getID().equals(id)){
				return u;
			}
		}
		return null;
	}

	public void removeStudenteApprovare(Corso corso, Utente u) {
		corso.getUtentirischiesta().remove(u.getID());
		this.corsorep.save(corso);

	}

	public Collection<Corso> findbySelezione() {
		List<Corso> corsi=new ArrayList<>();
		for(Corso c:findAll()){
			if(c.getSelezione())
				corsi.add(c);
		}
		return corsi;
	}



	public boolean nonRichiesto(Corso c, Utente studente) {
		List<Long> partecipanti=c.getUtentirischiesta();
		Boolean ris=true;
		for(Long u:partecipanti){
			if(u.equals(studente.getID())){
				return false;
			}
		}
		return ris;
	}


}





