package it.tirocinio.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import it.tirocinio.entity.Utente;



public class pdfUserDetails implements UserDetails {
	private Utente utente;

	public pdfUserDetails(Utente user)
	{
		this.utente=user;
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> auths= new ArrayList<>();
		auths.add(new SimpleGrantedAuthority("ROLE_" + this.utente.getRuolo()));
		return auths;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return utente.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return utente.getNome();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	public Utente getUtenteDetails(){
		return utente;
	}
}
