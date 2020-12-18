package it.tirocinio.backend.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.tirocinio.backend.CorsoRepository;
import it.tirocinio.backend.UtenteRepository;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;
import it.tirocinio.entity.quiz.Quiz;



@Service
public class UtenteService  {

	private final UtenteRepository utenteRepository;
	private PasswordEncoder passwordencoder;
	private CorsoRepository corsoR;
	private CorsoService corsoS;

	public UtenteService(UtenteRepository userRepository,PasswordEncoder pe,CorsoService c,CorsoRepository cr) {
		this.utenteRepository = userRepository;
		this.corsoR=cr;
		this.corsoS=c;
		this.passwordencoder=pe;
	}

	public List<Utente> findAll(){
		return utenteRepository.findAll();
	}
	public void  save(Utente u){
		if(u==null){
			return;
		}
		utenteRepository.save(u);
	}

	public long count(){
		return utenteRepository.count();
	}

	@PostConstruct
	public void populateTestData() {
		if(utenteRepository.count()==0){
			Utente admin = new Utente();
			admin.setNome("admin");
			admin.setPassword(passwordencoder.encode("a"));
			admin.setRuolo("ADMIN");
			admin.setQuizpassati(new ArrayList<Long>());
			admin.setValoretesteffetuati(new HashMap<Long,Double>());		
			Utente prof = new Utente();
			prof.setNome("prof");
			prof.setPassword(passwordencoder.encode("a"));
			prof.setRuolo("PROFESSORE");
			
			Utente user = new Utente();
			user.setNome("studente");
			user.setPassword(passwordencoder.encode("a"));
			user.setRuolo("USER");
			user.setQuizpassati(new ArrayList<Long>());
			Utente user2 = new Utente();
			user2.setNome("studente2");
			user2.setPassword(passwordencoder.encode("a"));
			user2.setRuolo("USER");
			user2.setQuizpassati(new ArrayList<Long>());
			List<Utente> utenti= Arrays.asList(admin,prof,user,user2);
			this.utenteRepository.saveAll(utenti);
		}
	}

	public Utente findByName(String nome) {
		// TODO Auto-generated method stub
		List<Utente> ut=findAll();
		for(Utente u: ut){
			if(u.getNome().equals(nome)){
				return u;
			}
		}
		return null;
	}
	public Utente findById(Long id) {
		// TODO Auto-generated method stub
		List<Utente> ut=findAll();
		for(Utente u: ut){
			if(u.getId().equals(id)){
				return u;
			}
		}
		return null;
	}

	public void AddCorso(Corso c,Utente utente){
		List<Corso> corsinew = new ArrayList<>(); 
		if(c==null){
			return;
		}
		else{

			for(Utente u:findAll()){
				if(u.equals(utente)){
					if(utente.getCorsifrequentati()==null){
						List<Corso> corsofreqnew=new ArrayList<>();
						corsofreqnew.add(c);
						utente.setCorsifrequentati(corsofreqnew);
						this.corsoS.addStudente(utente,c);
						this.utenteRepository.save(utente);
					}
					else{
						corsinew = utente.getCorsifrequentati();
						corsinew.add(c);
						utente.setCorsifrequentati(corsinew);
						this.corsoS.addStudente(utente,c);
						this.utenteRepository.save(utente);
					}
				}
			}
		}
	}

	public List<Corso> findByfreq(Utente studente) {
		Utente u = findByName(studente.getNome());
		return u.getCorsifrequentati();
	}

	public List<Utente> findByCorso(Corso c){
		List<Utente> corsi= new ArrayList<>();
		for(Utente u:findAll()){
			for(Corso co:u.getCorsifrequentati()){
				if(co.equals(c)){
					corsi.add(u);
				}
			}
		}
		return corsi;
	}
	
	public List<Utente> findByCorsoSelezione(Corso c) {
		List<Utente> corsi= new ArrayList<>();
		for(Utente u:findAll()){
			if(u.getCorsoSelezione()!=null){
				if(u.getCorsoSelezione().equals(c)){
					corsi.add(u);
				}
			}
			
		}
		return corsi;
	}

	public void DeleteCorso(Corso value) {
		List<Utente> utenti=findByCorso(value);
		for(Utente u:utenti){
			if(!(u.getCorsifrequentati()==null)){
				
				u.getCorsifrequentati().remove(value);
				this.utenteRepository.save(u);
			}
		}

	}


	public void DeleteCorsoperdoc(Utente doc,Corso value) {
		doc.getCorsifrequentati().remove(value);
		value.setDocente(null);
		this.corsoR.save(value);
		this.utenteRepository.save(doc);
		
	}

	private void DeleteQuizperdoc(Quiz value) {
		Utente doc=value.getCorsoAppartenenza().getDocente();
		for(Corso c:doc.getCorsifrequentati()){
			if(c.equals(value.getCorsoAppartenenza())){
				this.utenteRepository.delete(doc);
				c.getQuizDelcorso().remove(value);
				this.utenteRepository.save(doc);
			}
		}

	}
	public void eliminaQuiz(Quiz value) {
		this.corsoS.eliminaQuiz(value);
		this.DeleteQuizperdoc(value);

	}

	public void addQuizpassati(Utente studente, Long id) {		
		studente.getQuizpassati().add(id);	
		this.utenteRepository.save(studente);
		
	}
	public void addQuizvalore(Utente studente, Long id,Double per) {
		studente.getValoretesteffetuati().put(id.longValue(), per.doubleValue());
		this.utenteRepository.save(studente);
	}
	
	

	public List<Utente> findStudenti() {
		List<Utente> ut=new ArrayList<>();
		for(Utente u:this.findAll()){
			if(u.getRuolo().equals("USER")){
				ut.add(u);
			}
		}
		return ut;
	}

	public String encriptPassword(String string) {
		return passwordencoder.encode(string);
		
		
	}






}






