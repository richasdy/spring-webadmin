package com.richasdy.springwebadmin.account;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;

import javax.sound.midi.Soundbank;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.AssertTrue;

import org.assertj.core.util.Lists;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.richasdy.springwebadmin.AbstractTests;
import com.richasdy.springwebadmin.account.Account;
import com.richasdy.springwebadmin.account.AccountRepository;
import com.richasdy.springwebadmin.account.AccountService;

@Transactional
public class AccountServiceTests extends AbstractTests {

	// this is integration test
	// service dont have validation, controller have
	// independent test with initialization db dml
	// no change db status, no left some garbage data in db

	@Autowired
	private AccountService service;
	private Account foo;

	@Before
	public void init() {
		foo = new Account();
		// validation using hibernate validator
		foo.setEmail("foo@email.com");
		foo.setPhone("000000000000");
		foo.setUsername("fooUsername");
		foo.setPassword("fooPassword");
		foo.setPermissions("fooPermissions");
		foo.setNote("fooNote");
		foo.setActivated(Boolean.TRUE);
		foo.setActivationCode("fooActivationCode");

		foo = service.save(foo);
	}

	@After
	public void destroy() {
		// accountService.delete(foo.getId());
	}

	@Test
	public void save() {

		// error jika di test semua, jika di test sendiri all pass

		// prepare
		Account bar = new Account();
		bar.setEmail("bar@email.com");
		bar.setPhone("999999999999");
		bar.setUsername("barUsername");
		bar.setPassword("barPassword");

		long countBefore = service.count();

		// action
		Account confirm = service.save(bar);

		long countAfter = service.count();

		// delete data
		// handle by transactional
		// accountService.delete(bar.getId());

		// check
		assertTrue("failure - expected not null", confirm != null);
		assertEquals("failure - expected right count", countAfter, countBefore + 1);
		assertEquals("failure - expected same value", bar.getEmail(), confirm.getEmail());

	}

	@Test(expected = ConstraintViolationException.class)
	public void saveValidationErrorEmptyField() {

		// prepare
		Account bar = new Account();
		bar.setEmail("bar@email.com");
		bar.setPhone("999999999999");
		bar.setUsername("barUsername");

		// action
		Account confirm = service.save(bar);

		// check

	}

	@Test(expected = DataIntegrityViolationException.class)
	public void saveValidationErrorDupicate() {

		// prepare
		Account bar = new Account();
		// validation using hibernate validator
		// email
		bar.setEmail("foo@email.com");
		bar.setPhone("000000000000");
		bar.setUsername("fooUsername");
		bar.setPassword("fooPassword");

		// action
		Account confirm = service.save(bar);

		// check

	}

	@Test
	public void update() {

		// prepare
		foo.setEmail("fooUpdate@email.com");

		// action
		Account confirm = service.update(foo);

		// check
		assertTrue("failure - expected not null", confirm != null);
		assertEquals("failure - expected same value", foo.getEmail(), confirm.getEmail());
		assertThat("failure - expected has email updated", confirm, hasProperty("email", is(foo.getEmail())));

	}

	@Test(expected = NullPointerException.class)
	public void updateNotFound() {

		// prepare
		Account notFound = service.findOne(Integer.MAX_VALUE);

		// action
		Account confirm = service.update(notFound);

		// check

	}

	@Test(expected = ConstraintViolationException.class)
	public void updateValidationErrorEmptyField() {

		// prepare
		Account bar = new Account();
		bar.setId(Integer.MAX_VALUE);
		bar.setEmail("bar@email.com");
		bar.setPhone("999999999999");
		// bar.setUsername("barUsername");
		bar.setPassword("barPassword");
		// createdAt tidak boleh null, diset di fungsi save
		bar.setCreatedAt(new Date());

		// action
		// kalau lolos menjadi save
		Account confirm = service.update(bar);

		// check

	}

	@Test
	public void findOne() {

		// prepare

		// action
		Account confirm = service.findOne(foo.getId());

		// check
		assertTrue("failure - expected not null", confirm != null);
		assertEquals("failure - expected same id", foo.getId(), confirm.getId());
	}

	@Test
	public void findOneNotFound() {

		// prepare
		int id = Integer.MAX_VALUE;

		// action
		Account confirm = service.findOne(id);

		// check
		assertTrue("failure - expected null", confirm == null);
	}

	@Test
	public void findAll() {

		// prepare

		// action

		// check

	}

	@Test
	public void count() {

		// prepare

		// action
		long count = service.count();

		// check
		assertTrue("failure - expected count > 0", count > 0);
	}

	@Test
	public void delete() {

		// prepare

		// action
		service.delete(foo.getId());

		Account confirm = service.findOne(foo.getId());

		// check
		assertTrue("failure - expected null", confirm == null);

	}

	@Test
	public void deleteSoft() {

		// prepare

		// action
		Account confirm = service.deleteSoft(foo.getId());

		// check
		assertTrue("failure - expected not null", confirm.getDeletedAt() != null);

	}

	@Test
	public void searchByEmptyString() {

		// prepare
		Pageable page = new PageRequest(0, 2);

		// action
		Page<Account> pageableConfirm = service.searchBy("", page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check
		assertTrue("failure - expected not null", pageableConfirm != null);
		assertTrue("failure - expected size > 0", listConfirm.size() > 0);

	}

	@Test
	public void searchById() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "id:" + foo.getId();

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check
		assertTrue("failure - expected not null", pageableConfirm != null);
		assertTrue("failure - expected size > 0", listConfirm.size() > 0);

	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void searchByIdEmpty() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "id:";

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check

	}

	@Test
	public void searchByIdNotFound() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "id:" + Long.MAX_VALUE;

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check
		assertTrue("failure - expected not null", pageableConfirm != null);
		assertTrue("failure - expected size > 0", listConfirm.size() == 0);

	}

	@Test(expected = NumberFormatException.class)
	public void searchByIdWrongFormat() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "id:a";

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check

	}

	@Test
	public void searchByEmail() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "email:" + foo.getEmail();

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check
		assertTrue("failure - expected not null", pageableConfirm != null);
		assertTrue("failure - expected size > 0", listConfirm.size() > 0);

	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void searchByEmailEmpty() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "email:";

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check

	}

	@Test
	public void searchByEmailNotFound() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "email:notFoundEmail";

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check
		assertTrue("failure - expected size > 0", listConfirm.size() == 0);

	}

	@Test
	public void searchByPhone() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "phone:" + foo.getPhone();

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check
		assertTrue("failure - expected not null", pageableConfirm != null);
		assertTrue("failure - expected size > 0", listConfirm.size() > 0);

	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void searchByPhoneEmpty() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "phone:";

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check

	}

	@Test
	public void searchByPhoneNotFound() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "phone:notFoundPhone";

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check
		assertTrue("failure - expected size > 0", listConfirm.size() == 0);

	}

	@Test
	public void searchByUsername() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "username:" + foo.getUsername();

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check
		assertTrue("failure - expected not null", pageableConfirm != null);
		assertTrue("failure - expected size > 0", listConfirm.size() > 0);

	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void searchByUsernameEmpty() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "username:";

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check

	}

	@Test
	public void searchByUsernameNotFound() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "username:notFoundUsername";

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check
		assertTrue("failure - expected size > 0", listConfirm.size() == 0);

	}

	@Test
	public void searchByPassword() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "password:" + foo.getPassword();

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check
		assertTrue("failure - expected not null", pageableConfirm != null);
		assertTrue("failure - expected size > 0", listConfirm.size() > 0);

	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void searchByPasswordEmpty() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "password:";

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check

	}

	@Test
	public void searchByPasswordNotFound() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "password:notFoundPassword";

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check
		assertTrue("failure - expected size > 0", listConfirm.size() == 0);

	}

	@Test
	public void searchByNote() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "note:" + foo.getNote();

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check
		assertTrue("failure - expected not null", pageableConfirm != null);
		assertTrue("failure - expected size > 0", listConfirm.size() > 0);

	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void searchByNoteEmpty() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "note:";

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check

	}

	@Test
	public void searchByNoteNotFound() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "note:notFoundNote";

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check
		assertTrue("failure - expected size > 0", listConfirm.size() == 0);

	}

	@Test
	public void searchByPermissions() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "permissions:" + foo.getPermissions();

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check
		assertTrue("failure - expected not null", pageableConfirm != null);
		assertTrue("failure - expected size > 0", listConfirm.size() > 0);

	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void searchByPermissionsEmpty() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "permissions:";

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check

	}

	@Test
	public void searchByPermissionsNotFound() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "permissions:notFoundPermissions";

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check
		assertTrue("failure - expected size > 0", listConfirm.size() == 0);

	}

	@Test
	public void searchByActivated() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "activated:" + foo.getActivated();

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check
		assertTrue("failure - expected not null", pageableConfirm != null);
		assertTrue("failure - expected size > 0", listConfirm.size() > 0);

	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void searchByActivatedEmpty() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "activated:";

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check

	}

	// tidak bsia diakukan karena sample data lengkap
	// @Test
	public void searchByActivatedNotFound() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "activated:" + !foo.getActivated();

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check
		assertTrue("failure - expected not null", pageableConfirm != null);
		assertTrue("failure - expected size > 0", listConfirm.size() == 0);

	}

	public void searchByActivatedWrongFormat() {
		// tidak ada, karena selain "true" diconvert menjadi false oleh kelas
		// boolean
	}

	@Test
	public void searchByActivationCode() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "activationcode:" + foo.getActivationCode();

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check
		assertTrue("failure - expected not null", pageableConfirm != null);
		assertTrue("failure - expected size > 0", listConfirm.size() > 0);

	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void searchByActivationCodeEmpty() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "activationcode:";

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check

	}

	@Test
	public void searchByActivationCodeNotFound() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		String searchTerm = "activationcode:notFoundActivationCode";

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check
		assertTrue("failure - expected size > 0", listConfirm.size() == 0);

	}

	@Test
	public void searchByCreatedAt() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		Date date = new Date();

		// calculation of year and date, by documentation
		String searchTerm = "createdat:" + (date.getYear() + 1900) + "-" + String.format("%02d", date.getMonth() + 1)
				+ "-" + date.getDate();

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check
		assertTrue("failure - expected not null", pageableConfirm != null);
		assertTrue("failure - expected size > 0", listConfirm.size() > 0);

	}

	@Test(expected = ArrayIndexOutOfBoundsException.class)
	public void searchByCreatedAtEmpty() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		Date date = new Date();

		// calculation of year and date, by documentation
		String searchTerm = "createdat:";

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check

	}

	@Test
	public void searchByCreatedAtNotFound() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		Date date = new Date();

		// calculation of year and date, by documentation
		String searchTerm = "createdat:1990-10-06";

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check
		assertTrue("failure - expected size > 0", listConfirm.size() == 0);

	}

	@Test(expected = DateTimeParseException.class)
	public void searchByCreatedAtWrongFormat() {

		// prepare
		Pageable page = new PageRequest(0, 2);
		Date date = new Date();

		// calculation of year and date, by documentation
		String searchTerm = "createdat:0000";
		// String searchTerm = "createdat:0000-00-00";

		// action
		Page<Account> pageableConfirm = service.searchBy(searchTerm, page);
		List listConfirm = Lists.newArrayList(pageableConfirm);

		// check

	}
}
