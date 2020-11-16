package it.tirocinio.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import it.tirocinio.data.*;
import it.tirocinio.backend.UtenteRepository;

@Service
public class UtenteService {
	private UtenteRepository utenterep;
	
	public UtenteService(UtenteRepository utenterepository){
		this.utenterep=utenterepository;
	}
	public List<Utente> findAll(){
		return this.utenterep.findAll();
	}
	public void  save(Utente u){
		if(u==null){
			return;
		}
		utenterep.save(u);
	}
}
