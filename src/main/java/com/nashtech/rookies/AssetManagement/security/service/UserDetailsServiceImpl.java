package com.nashtech.rookies.AssetManagement.security.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nashtech.rookies.AssetManagement.model.entities.Accounts;
import com.nashtech.rookies.AssetManagement.repository.AccountsRepository;



@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	private final AccountsRepository accountsRepository;

	public UserDetailsServiceImpl(AccountsRepository accountsRepository) {
		this.accountsRepository = accountsRepository;
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Accounts acc = accountsRepository.findByuserName(userName).orElseThrow(
				() -> new UsernameNotFoundException("User Not Found with -> username: " + userName));
		return UserDetailsImpl.build(acc);
	}

}
