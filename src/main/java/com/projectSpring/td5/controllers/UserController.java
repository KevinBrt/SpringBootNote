package com.projectSpring.td5.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.projectSpring.td5.entities.User;
import com.projectSpring.td5.repositories.UsersRepository;


@Controller
@RequestMapping("/users/")
public class UserController {
	
	@Autowired
	private UsersRepository usersRepo;
	
	@RequestMapping("connexion")
	public String connect(Model model, HttpSession session) {
		
		if(isConnected(session) == false) {
			model.addAttribute("user", new User());
			return "user/connect";
		}else {
			return "user/index";
		}
		
	}
	
	public boolean isConnected(HttpSession session) {
		if( session.getAttribute("user") == null) {
			return false;
		}else {
			return true;
		}
	}
	
	
	
	@PostMapping("connexion")
	public RedirectView testConnexion(@ModelAttribute("user") User u, HttpSession session) {
		User user = usersRepo.findByLogin(u.getLogin());
		
		if(user != null) {
			System.out.println("ACCESS AUTORISE");
			session.setAttribute("user", user);
			return new RedirectView("/users/index");
		}else {
			System.out.println("ACCESS NON AUTORISE");
			return new RedirectView("/users/connexion");
		}
		
		
	}
	
	@RequestMapping("/create")
	@ResponseBody
	public String ind(ModelMap model) {
		
		User user = new User();
		user.setLogin("kevin");
		user.setEmail("test@test.com");
		user.setPassword("azerty1");
		user.setIdentity("kevin Brisset");
		
		usersRepo.save(user);
		
		return user + " ajoutée dans la bdd";
	}
	
	@RequestMapping("/index")
	public String index(ModelMap model, HttpSession session) {
		if(isConnected(session)) {
			model.addAttribute("user", session.getAttribute("user"));
			return "user/index";
		}else {
			model.addAttribute("user", new User());
			return "user/connect";
		}
		
	}
	
	@RequestMapping("/logout")
	public RedirectView logout(HttpSession session) {
		
		session.setAttribute("user", null);
		
		return new RedirectView("/users/connexion");
	}

}
