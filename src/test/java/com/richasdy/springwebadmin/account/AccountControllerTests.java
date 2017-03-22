package com.richasdy.springwebadmin.account;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.transaction.Transactional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.util.NestedServletException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.richasdy.springwebadmin.AbstractControllerTests;
import com.richasdy.springwebadmin.account.Account;
import com.richasdy.springwebadmin.account.AccountService;

@Transactional
public class AccountControllerTests extends AbstractControllerTests {

	// connection using mockmvc
	// this is integration test

	@Autowired
	private AccountService service;

	private Account foo;

	@Before
	public void setUp() {
		super.setUp();

		foo = new Account();
		foo.setEmail("foo@email.com");
		foo.setPhone("000000000000");
		foo.setUsername("fooUsername");
		foo.setPassword("fooPassword");

		foo = service.save(foo);

	}

	@Test
	public void index() throws Exception {

		// prepare
		String uri = "/account";

		// action
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.ALL))
				// .andDo(print())
				.andReturn();

		Map<String, Object> model = result.getModelAndView().getModel();
		String view = result.getModelAndView().getViewName();
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();

		// check
		assertTrue("failure - expected model attribute listAccount", model.containsKey("listEntity"));
		assertTrue("failure - expected model attribute pageName", model.containsKey("pageName"));
		assertTrue("failure - expected model attribute pageNameDesc", model.containsKey("pageNameDesc"));
		assertEquals("failure - expected HTTP Status 200", HttpStatus.OK.value(), status);
		assertEquals("failure - expected view account/index", "account/index", view);

	}

	@Test
	public void create() throws Exception {

		// prepare
		String uri = "/account/create";

		// action
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.ALL)).andReturn();

		Map<String, Object> model = result.getModelAndView().getModel();
		String view = result.getModelAndView().getViewName();
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();

		// check
		assertTrue("failure - expected model attribute account", model.containsKey("account"));
		assertTrue("failure - expected model attribute account", model.get("account") != null);
		assertTrue("failure - expected model attribute pageName", model.containsKey("pageName"));
		assertTrue("failure - expected model attribute pageNameDesc", model.containsKey("pageNameDesc"));
		assertEquals("failure - expected HTTP Status 200", HttpStatus.OK.value(), status);
		assertEquals("failure - expected view account/index", "account/create", view);

	}

	@Test
	public void save() throws Exception {

		// prepare
		String uri = "/account";

		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.set("email", "bar@email.com");
		params.set("phone", "999999999999");
		params.set("username", "barUsername");
		params.set("password", "barPassword");

		// action
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post(uri).contentType(MediaType.ALL).accept(MediaType.ALL)
						.params(params))
				.andExpect(model().hasNoErrors()).andExpect(status().is3xxRedirection()).andReturn();

	}

	@Test
	public void saveValidationErrorEmptyField() throws Exception {

		// prepare
		String uri = "/account";

		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.set("email", "bar@email.com");
		params.set("phone", "999999999999");
		params.set("username", "barUsername");
		// params.set("password", "barPassword");
		// System.out.println(params);

		// action
		// check using mockmvc assert
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.post(uri).contentType(MediaType.ALL).accept(MediaType.ALL).params(params))
				.andExpect(model().hasErrors()).andReturn();

	}

	@Test
	public void show() throws Exception {

		// prepare
		String uri = "/account/{id}";
		long id = foo.getId();

		// action
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.ALL)).andReturn();

		Map<String, Object> model = result.getModelAndView().getModel();
		String view = result.getModelAndView().getViewName();
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();

		// check
		assertTrue("failure - expected model attribute entity", model.containsKey("entity"));
		assertTrue("failure - expected model attribute entity not null", model.get("entity") != null);
		assertTrue("failure - expected model attribute pageName", model.containsKey("pageName"));
		assertTrue("failure - expected model attribute pageNameDesc", model.containsKey("pageNameDesc"));
		assertEquals("failure - expected HTTP Status 200", HttpStatus.OK.value(), status);
		assertEquals("failure - expected view account/show", "account/show", view);

	}

	@Test
	public void showOtherAssertTechniqueShowcase() throws Exception {

		// prepare
		String uri = "/account/{id}";
		long id = foo.getId();

		// action
		mockMvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.ALL))
				.andExpect(model().attributeExists("pageName")).andExpect(model().attributeExists("pageNameDesc"))
				.andExpect(model().attributeExists("entity"))
				.andExpect(model().attribute("entity", hasProperty("email", is(foo.getEmail()))))
				.andExpect(model().attributeExists("entity")).andExpect(view().name("account/show"));

	}

	@Test(expected = NestedServletException.class)
	public void showNotFound() throws Exception {

		// prepare
		String uri = "/account/{id}";
		int id = Integer.MAX_VALUE;

		// action
		// proses mock tidak jalan, null pointer di theamleaf.
		// saat akses account.id padahal account = null
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.ALL))
				.andExpect(model().attributeExists("entity")).andExpect(model().attribute("entity", nullValue()))
				.andReturn();

	}

	@Test
	public void edit() throws Exception {

		// prepare
		String uri = "/account/{id}/edit";
		long id = foo.getId();

		// action
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.ALL)).andReturn();

		Map<String, Object> model = result.getModelAndView().getModel();
		String view = result.getModelAndView().getViewName();
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();

		// check
		assertTrue("failure - expected model attribute account", model.containsKey("account"));
		assertTrue("failure - expected model attribute account", model.get("account") != null);
		assertTrue("failure - expected model attribute pageName", model.containsKey("pageName"));
		assertTrue("failure - expected model attribute pageNameDesc", model.containsKey("pageNameDesc"));
		assertEquals("failure - expected HTTP Status 200", HttpStatus.OK.value(), status);
		assertEquals("failure - expected view account/edit", "account/edit", view);

	}

	@Test
	public void update() throws Exception {

		// prepare
		String uri = "/account/{id}/update";
		long id = foo.getId();

		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.set("email", "fooUpdate@email.com");
		params.set("phone", "999999999999");
		params.set("username", "fooUpdateUsername");
		params.set("password", "fooUpdatePassword");

		// action
		MvcResult result = mockMvc
				.perform(MockMvcRequestBuilders.post(uri, id).contentType(MediaType.ALL).accept(MediaType.ALL)
						.params(params))
				.andExpect(model().hasNoErrors()).andExpect(status().is3xxRedirection()).andReturn();

	}

	@Test
	public void updateValidationErrorEmptyField() throws Exception {

		// prepare
		String uri = "/account/{id}/update";
		long id = foo.getId();

		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.set("email", "fooUpdate@email.com");
		params.set("phone", "999999999999");
		params.set("username", "fooUpdateUsername");
		// params.set("password", "fooUpdatePassword");

		// action
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.post(uri, id).contentType(MediaType.ALL).accept(MediaType.ALL).params(params))
				.andExpect(model().hasErrors()).andReturn();
	}

	@Test
	public void updateHackingPosibility() throws Exception {

		// change other data
		// accont.id != pathVariable
		// SOLUSI : check controller
		// JIKA sudah disolusikan :
		// hapus model().hasNoErrors()) dibawah, ganti dengan view().name("account/edit")
		// ganti nama test menjadi updateValidationErrorNotConsistentId

		// prepare
		String uri = "/account/{id}/update";
		int id = 1;

		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.set("email", "fooUpdate@email.com");
		params.set("phone", "999999999999");
		params.set("username", "fooUpdateUsername");
		params.set("password", "fooUpdatePassword");

		// action
		MvcResult result = mockMvc.perform(
				MockMvcRequestBuilders.post(uri, id).contentType(MediaType.ALL).accept(MediaType.ALL).params(params))
				.andExpect(model().hasNoErrors()).andReturn();
		
		System.out.println(service.findOne(id).toString());

	}

	@Test
	public void delete() throws Exception {

		// prepare
		String uri = "/account/{id}/delete";
		long id = foo.getId();

		// action
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.ALL))
				.andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/account")).andReturn();

	}

	@Test(expected = NestedServletException.class)
	public void deleteNotFound() throws Exception {

		// prepare
		String uri = "/account/{id}/delete";
		int id = Integer.MAX_VALUE;

		// action
		// proses mock tidak jalan, null pointer di controller.
		// saat akses account.activated padahal account = null
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri, id).accept(MediaType.ALL)).andDo(print())
				.andReturn();

	}
	
	@Test
	public void search() throws Exception {

		// prepare
		String uri = "/account/search";
		MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
		params.set("q", "id:" + foo.getId());

		// action
		MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(uri).accept(MediaType.ALL).params(params)).andReturn();

		Map<String, Object> model = result.getModelAndView().getModel();
		String view = result.getModelAndView().getViewName();
		String content = result.getResponse().getContentAsString();
		int status = result.getResponse().getStatus();

		// check
		assertTrue("failure - expected model attribute listAccount", model.containsKey("listEntity"));
		assertTrue("failure - expected model attribute pageName", model.containsKey("pageName"));
		assertTrue("failure - expected model attribute pageNameDesc", model.containsKey("pageNameDesc"));
		assertEquals("failure - expected HTTP Status 200", HttpStatus.OK.value(), status);
		assertEquals("failure - expected view account/index", "account/index", view);

	}

}
