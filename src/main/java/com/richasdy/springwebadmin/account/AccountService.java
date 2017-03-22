package com.richasdy.springwebadmin.account;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface AccountService {

	Account save(Account entity);

	Account update(Account entity);

	Account findOne(long id);

	Page<Account> findAll(Pageable pageable);

	long count();

	void delete(long id);

	Account deleteSoft(long id);

	Page<Account> searchBy(String searchTerm, Pageable pageable);


}
