package it.tirocinio.backend.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.tirocinio.backend.UtenteRepository;
import it.tirocinio.entity.Utente;
import it.tirocinio.entity.quiz.Corso;



@Service
public class UtenteService  {

	private final UtenteRepository utenteRepository;
	private PasswordEncoder passwordencoder;

	public UtenteService(UtenteRepository userRepository,PasswordEncoder pe) {
		this.utenteRepository = userRepository;
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
			admin.setPassword(passwordencoder.encode("admin"));
			admin.setRuolo("ADMIN");
			admin.setCorsifrequentati(new ArrayList<Corso>());
			Utente prof = new Utente();
			prof.setNome("prof");
			prof.setPassword(passwordencoder.encode("sonoilprof"));
			prof.setRuolo("PROFESSORE");
			Utente user = new Utente();
			user.setNome("studente");
			user.setPassword(passwordencoder.encode("stud"));
			user.setRuolo("USER");
			List<Utente> utenti= Arrays.asList(admin,prof,user);
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

	public void AddCorso(Corso c,String utente){
		List<Corso> corsinew = new ArrayList<>(); 
		if(c==null){
			return;
		}
		else{
			List<Utente> co=findAll();
			for(Utente u:co){
				if(u.getNome().equals(utente)){
					if(u.getCorsifrequentati()==null){
						List<Corso> corsofreqnew=new ArrayList<Corso>();
						corsofreqnew.add(c);
						u.setCorsifrequentati(corsofreqnew);
					}
					else{
					corsinew = u.getCorsifrequentati();
					corsinew.add(c);
					u.setCorsifrequentati(corsinew);
					}
				}
			}
		}
	}

	public List<Corso> findByfreq(Utente studente) {
		Utente u = findByName(studente.getNome());
		return u.getCorsifrequentati();
	}





}
