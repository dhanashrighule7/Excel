package com.cyperts.ExcellML.UserAndRole;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetail implements UserDetailsService {
	@Autowired
	UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		User user = userRepo.findByUsernameOrEmail(username, username);
//		if (user == null) {
//			throw new UsernameNotFoundException("User not exists by Username");
//		}
//		Set<GrantedAuthority> authorities = user.getRoles().stream()
//				.map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
//		return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
//	}
}
