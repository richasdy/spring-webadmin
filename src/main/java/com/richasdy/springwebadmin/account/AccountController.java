package com.richasdy.springwebadmin.account;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("account")
public class AccountController {

	AccountService service;

	@Autowired
	public AccountController(AccountService service) {
		this.service = service;
	}

	@GetMapping()
	public String index(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Pageable pagination = new PageRequest(page, size);
		Page<Account> pageEntity = service.findAll(pagination);

		model.addAttribute("listEntity", pageEntity);
		model.addAttribute("pageName", "Account Table");
		model.addAttribute("pageNameDesc", "Account Detail List");

		return "account/index";
	}
	

	@GetMapping("/create")
	public String create(Model model) {

		model.addAttribute("account", new Account());

		model.addAttribute("pageName", "Account New");
		model.addAttribute("pageNameDesc", "Account Detail");

		return "account/create";
	}

	@PostMapping()
	public String save(@Valid Account entity, BindingResult result) {

		if (result.hasErrors()) {

			return "account/create";

		} else {

			// check activated untuk assign activatedAt
			// entity.getActivated() == true/false --> saat real
			// entity.getActivated() == null --> saat testing,
			// karena nilainya null, bukan hanya true/false
			if (entity.getActivated() == null || entity.getActivated()) {
				entity.setActivatedAt(new Date());
			}

			Account confirm = service.save(entity);

			return "redirect:/account/" + confirm.getId();
		}

	}

	@GetMapping("/{id}")
	public String show(Model model, @PathVariable int id) {

		Account entity = service.findOne(id);

		model.addAttribute("entity", entity);
		model.addAttribute("pageName", "Account Detail");
		model.addAttribute("pageNameDesc", "Account Detail");

		return "account/show";
	}

	@GetMapping("/{id}/edit")
	public String edit(Model model, @PathVariable int id) {

		Account entity = service.findOne(id);

		model.addAttribute("account", entity);
		model.addAttribute("pageName", "Account Edit");
		model.addAttribute("pageNameDesc", "Account Detail");

		return "account/edit";
	}

	@PostMapping("/{id}/update")
	public String update(@PathVariable int id, @Valid Account updatedEntity, BindingResult result) {

		if (result.hasErrors() || id != updatedEntity.getId()) {

			// VULNURABLE
			// ada kemungkinan dihack
			// merubah data account dengan id = id
			// tapi updatedAccount.getId() nya berbeda
			// pakai postman
			// SOLUSI
			// masukkan id sebagai hidden input
			// check PathVariable dan hidden input id harus sama, baru proses
			// update
			// DONE

			// VULNURABLE
			// manual edit id target post dan manual edit id hidden input id
			// SOLUSI
			// id ambil dari session
			// NOT YET

			return "account/edit";

		} else {

			Account currentEntity = service.findOne(id);

			// check perubahan
			// check activated untuk assign activatedAt
			// account.getActivated() == true/false --> saat real
			// account.getActivated() == null --> saat testing,
			// karena nilainya null, bukan hanya true/false
			if (updatedEntity.getActivated() == null || updatedEntity.getActivated() == false) {
				
				updatedEntity.setActivatedAt(null);
				currentEntity.setActivatedAt(null);
				
			} else {
				
				updatedEntity.setActivatedAt(new Date());

				// memastikan saat diaktifkan, deletedAt di kembalikan null
				updatedEntity.setDeletedAt(null);
				currentEntity.setDeletedAt(null);
				
			}

			// copy changed field into object
			// get null value field
			final BeanWrapper src = new BeanWrapperImpl(updatedEntity);
			java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();
			Set<String> emptyNames = new HashSet<String>();
			for (java.beans.PropertyDescriptor pd : pds) {
				Object srcValue = src.getPropertyValue(pd.getName());
				if (srcValue == null || srcValue.equals(0))
					emptyNames.add(pd.getName());
			}
			String[] resultNullField = new String[emptyNames.size()];

			// BeanUtils.copyProperties with ignore field
			BeanUtils.copyProperties(updatedEntity, currentEntity, emptyNames.toArray(resultNullField));

			service.update(currentEntity);

			return "redirect:/account/" + currentEntity.getId();

		}

	}

	@GetMapping("/{id}/delete")
	public String delete(@PathVariable int id) {

		Account currentEntity = service.findOne(id);
		service.delete(currentEntity.getId());
		return "redirect:/account";

	}

	@GetMapping("/search")
	public String search(@RequestParam String q, Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size) {

		Pageable pagination = new PageRequest(page, size);
		Page<Account> pageEntity = service.searchBy(q, pagination);

		model.addAttribute("q", q);
		model.addAttribute("listEntity", pageEntity);
		model.addAttribute("pageName", "Account Table");
		model.addAttribute("pageNameDesc", "Account Detail List, Search for " + q);

		return "account/index";
	}

}
