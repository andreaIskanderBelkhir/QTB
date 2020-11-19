package it.tirocinio.backend.service;

import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;
import it.tirocinio.entity.Utente;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import it.tirocinio.backend.UtenteRepository;



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






}
