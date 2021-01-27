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
	private QuizService quizS;

	public UtenteService(UtenteRepository userRepository,PasswordEncoder pe,CorsoService c,QuizService q,CorsoRepository cr) {
		this.utenteRepository = userRepository;
		this.corsoR=cr;
		this.corsoS=c;
		this.quizS=q;
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
/* Rimuovere commenti per popolare se si rimuove liquibase
	@PostConstruct
	public void populateTestData() {
		if(utenteRepository.count()==0){
			Utente admin = new Utente();
			admin.setNome("admin");
			admin.setPassword("a");
			admin.setRuolo("ADMIN");
			admin.setQuizpassati(new ArrayList<Long>());
			admin.setValoretesteffetuati(new HashMap<Long,Double>());		
			Corso corso= new Corso();
			corso.setNomeCorso("TestCorso");
			corso.setDescrizioneCorso("un test di popolamento");
			corso.setDocente(admin);
			corso.setQuizDelcorso(new ArrayList<Quiz>());
			corso.setUtentirischiesta(new ArrayList<Long>());
			corso.setSelezione(false);
			
			List<Corso> corsinew = new ArrayList<>(); 
			corsinew.add(corso);
			admin.setCorsifrequentati(corsinew);
			List<Utente> utentinew = new ArrayList<>(); 
			utentinew.add(admin);
			corso.setUtentifreq(utentinew);
			
			Quiz quiz=new Quiz();
			quiz.setNomeQuiz("un test ");
			quiz.setCorsoAppartenenza(corso);
			quiz.setModalitaPercentuale(false);
			quiz.setSoglia(0);
			quiz.setSogliaPercentuale(0);
			quiz.setAttivato(false);
			quiz.setTempo(60.0);
			quiz.setValoreGiusta(1);
			quiz.setValoreSbagliata(0);
			quiz.setDomande(new HashSet<>());
			corso.getQuizDelcorso().add(quiz);
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
			this.corsoS.save(corso);
			this.quizS.save(quiz);
		}
	}
*/

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
			if(u.getID().equals(id)){
				return u;
			}
		}
		return null;
	}

	public void AddCorso(Corso c,Utente utente){	
		if(c==null){
			return;
		}
		else{

			for(Utente u:findAll()){
				if(u.equals(utente)){
					if(utente.getCorsifrequentati()==null){
						List<Corso> corsinew = new ArrayList<>(); 
						corsinew.add(c);
						utente.setCorsifrequentati(corsinew);
						this.corsoS.addStudente(utente,c);
						this.utenteRepository.save(utente);
					}
					else{		
						utente.getCorsifrequentati().add(c);
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
				
			}
		}
		Utente ut=value.getDocente();
		ut.getCorsifrequentati().remove(value);
		

	}


	public void DeleteCorsoperdoc(Utente doc,Corso value) {
		
		doc.getCorsitenuti().remove(value);
		this.save(doc);
		
		
		
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






