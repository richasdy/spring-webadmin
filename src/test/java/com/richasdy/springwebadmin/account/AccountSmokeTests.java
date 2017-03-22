package com.richasdy.springwebadmin.account;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.richasdy.springwebadmin.account.Account;
import com.richasdy.springwebadmin.account.AccountController;
import com.richasdy.springwebadmin.account.AccountRepository;
import com.richasdy.springwebadmin.account.AccountService;
import com.richasdy.springwebadmin.account.AccountServiceImpl;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountSmokeTests {

	// @Autowired
	// private Account account;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private AccountController accountController;

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private AccountServiceImpl accountServiceImpl;

	// @Test
	// public void contexLoadsAccount() throws Exception {
	// assertThat(account).isNotNull();
	// }

	@Test
	public void contexLoadsAccountRepository() throws Exception {
		assertThat(accountRepository).isNotNull();
	}


	@Test
	public void contexLoadsAccountController() throws Exception {
		assertThat(accountController).isNotNull();
	}

	@Test
	public void contexLoadsAccountService() throws Exception {
		assertThat(accountService).isNotNull();
	}
	
	@Test
	public void contexLoadsAccountServiceImpl() throws Exception {
		assertThat(accountServiceImpl).isNotNull();
	}

}
