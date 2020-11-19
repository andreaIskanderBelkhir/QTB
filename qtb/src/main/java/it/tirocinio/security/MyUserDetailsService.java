package it.tirocinio.security;

import java.util.Collections;



import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.vaadin.flow.component.notification.Notification;

import it.tirocinio.backend.UtenteRepository;
import it.tirocinio.entity.Utente;

@Service
public class MyUserDetailsService implements UserDetailsService {
	

	private UtenteRepository utenterepository;

	
	
	public MyUserDetailsService(UtenteRepository ut) {
		this.utenterepository= ut;
		// TODO Auto-generated constructor stub
	}



	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Utente utente = utenterepository.findByUsername(username);
		if(utente== null){
			throw new UsernameNotFoundException(username);
		}
		return new pdfUserDetails(utente);
	}

}
