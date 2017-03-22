package com.richasdy.springwebadmin.account;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;

import javax.sound.midi.Soundbank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

@Service
public class AccountServiceImpl implements AccountService {

	AccountRepository repository;

	@Autowired
	public AccountServiceImpl(AccountRepository repository) {
		this.repository = repository;
	}

	@Override
	public Account save(Account entity) {
		entity.setCreatedAt(new Date());
		return repository.save(entity);
	}

	@Override
	public Account update(Account entity) {
		entity.setUpdatedAt(new Date());
		return repository.save(entity);
	}

	@Override
	public Account findOne(long id) {
		return repository.findOne(id);
	}

	@Override
	public long count() {
		return repository.count();
	}

	@Override
	public void delete(long id) {
		repository.delete(id);
	}

	@Override
	public Account deleteSoft(long id) {
		Account entity = repository.findOne(id);
		entity.setDeletedAt(new Date());
		return repository.save(entity);
	}

	@Override
	public Page<Account> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	@Override
	public Page<Account> searchBy(String searchTerm, Pageable pageable) {
		
		Page<Account> retVal = null;

		if (searchTerm == null || searchTerm.isEmpty()) {

			retVal = repository.findAll(pageable);

		} else {
			// if search not null or empty

			// explode string
			searchTerm = searchTerm.toLowerCase();
			String[] searchSplit = searchTerm.split(":");

			// java 7 above
			switch (searchSplit[0]) {
			case "id":

				retVal = repository.findDistinctAccountById(Long.parseLong(searchSplit[1]), pageable);
				break;

			case "email":

				retVal = repository.findDistinctAccountByEmailContaining(searchSplit[1], pageable);
				break;

			case "phone":

				retVal = repository.findDistinctAccountByPhoneContaining(searchSplit[1], pageable);
				break;

			case "username":
				
				retVal = repository.findDistinctAccountByUsernameContaining(searchSplit[1], pageable);
				break;
				
			case "password":
				
				retVal = repository.findDistinctAccountByPasswordContaining(searchSplit[1], pageable);
				break;

			case "note":

				retVal = repository.findDistinctAccountByNoteContaining(searchSplit[1], pageable);
				break;

			case "permissions":

				retVal = repository.findDistinctAccountByPermissionsContaining(searchSplit[1], pageable);
				break;

			case "activated":
				retVal = repository.findDistinctAccountByActivated(Boolean.parseBoolean(searchSplit[1]), pageable);
				break;
				
			case "activationcode":

				retVal = repository.findDistinctAccountByActivationCodeContaining(searchSplit[1], pageable);
				break;

			case "activatedat":

				if (Utility.isValidDate(searchSplit[1])) {
					Date start = Utility.stringToDate(searchSplit[1]);
					Date end = Utility.stringToDate(searchSplit[1]);
					start.setHours(00);
					start.setMinutes(00);
					start.setSeconds(00);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);

					retVal = repository.findDistinctAccountByActivatedAtBetween(start, end, pageable);
				}  else {
					throw new DateTimeParseException("error date parsing", searchTerm, 0);
				}
				break;

			case "lastlogin":
				if (Utility.isValidDate(searchSplit[1])) {
					Date start = Utility.stringToDate(searchSplit[1]);
					Date end = Utility.stringToDate(searchSplit[1]);
					start.setHours(00);
					start.setMinutes(00);
					start.setSeconds(00);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);

					retVal = repository.findDistinctAccountByLastLoginBetween(start, end, pageable);
				} else {
					throw new DateTimeParseException("error date parsing", searchTerm, 0);
				}
				break;

			case "createdat":
				if (Utility.isValidDate(searchSplit[1])) {
					Date start = Utility.stringToDate(searchSplit[1]);
					Date end = Utility.stringToDate(searchSplit[1]);
					start.setHours(00);
					start.setMinutes(00);
					start.setSeconds(00);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);

					retVal = repository.findDistinctAccountByCreatedAtBetween(start, end, pageable);
				} else {
					throw new DateTimeParseException("error date parsing", searchTerm, 0);
				}
				break;

			case "updatedat":
				if (Utility.isValidDate(searchSplit[1])) {
					Date start = Utility.stringToDate(searchSplit[1]);
					Date end = Utility.stringToDate(searchSplit[1]);
					start.setHours(00);
					start.setMinutes(00);
					start.setSeconds(00);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);
					retVal = repository.findDistinctAccountByUpdatedAtBetween(start, end, pageable);
				} else {
					throw new DateTimeParseException("error date parsing", searchTerm, 0);
				}
				break;

			case "deleteddat":
				if (Utility.isValidDate(searchSplit[1])) {
					Date start = Utility.stringToDate(searchSplit[1]);
					Date end = Utility.stringToDate(searchSplit[1]);
					start.setHours(00);
					start.setMinutes(00);
					start.setSeconds(00);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);
					retVal = repository.findDistinctAccountByDeletedAtBetween(start, end, pageable);
				} else {
					throw new DateTimeParseException("error date parsing", searchTerm, 0);
				}
				break;

			}

		}

		return retVal;
	}

	

}