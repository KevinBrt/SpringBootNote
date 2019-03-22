package com.projectSpring.td5.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

import com.projectSpring.td5.entities.Categorie;
import com.projectSpring.td5.entities.Language;
import com.projectSpring.td5.entities.Script;
import com.projectSpring.td5.entities.User;
import com.projectSpring.td5.repositories.CategoriesRepository;
import com.projectSpring.td5.repositories.LanguagesRepository;
import com.projectSpring.td5.repositories.ScriptsRepository;
import com.projectSpring.td5.repositories.UsersRepository;

@Controller
@RequestMapping("/users/script/")
public class ScriptController {
	
	@Autowired
	private ScriptsRepository scriptsRepo;
	
	@Autowired
	private LanguagesRepository languagesRepo;
	
	@Autowired
	private CategoriesRepository categoriesRepo;
	
	@RequestMapping("createe")
	@ResponseBody
	public String ind(ModelMap model, HttpSession session) {
		
		Script script = new Script();
		
		script.setTitle("Premier script");
		script.setDescription("Mon premier script");
		
		Language lan = new Language();
		lan.setName("JAVA");
		languagesRepo.save(lan);
		
		Categorie cat = new Categorie();
		cat.setName("bash script");
		categoriesRepo.save(cat);
		
		script.setLanguage(lan);
		script.setCategorie(cat);
		
		User u = (User) session.getAttribute("user");
		
		script.setUser(u);
		
		script.setCreationData("22/03/2019");
		script.setContent("EFYQSDKHFHDB GHSDFHGDSBGKSDKJGHSJKDHGKSDKJKSGDNGFOZIEJNGOZEHGOZRHGZERNGZE?NG");
		
		scriptsRepo.save(script);
		
		return script + " ajoutée dans la bdd";
	}
	
	@RequestMapping("create")
	public String create(ModelMap model, HttpSession session) {
		
		model.addAttribute("languages", languagesRepo.findAll());
		
		model.addAttribute("categories", categoriesRepo.findAll());
		
		model.addAttribute("script", new Script());
		
		return "script/create";
	}
	
	@PostMapping("create")
	public RedirectView testConnexion(@ModelAttribute("script") Script script, @RequestParam int categorie_id, @RequestParam int language_id, HttpSession session) {
		
		script.setCategorie(categoriesRepo.findById(categorie_id));
		script.setLanguage(languagesRepo.findById(language_id));
		
		User u = (User) session.getAttribute("user");
		
		script.setUser(u);
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		
		script.setCreationData(dateFormat.format(date));
		
		scriptsRepo.save(script);
			
		return new RedirectView("/users/index");
		
		
		
	}
	
	@GetMapping("delete/{id}")
	public RedirectView delete(@PathVariable("id") String id) {
		
		Optional<Script> s = scriptsRepo.findById(Integer.parseInt(id));
		
		if(s.isPresent()) {
			Script script = s.get();
			
			scriptsRepo.delete(script);
		}
			
		return new RedirectView("/users/index");
		
		
		
	}
	
	@RequestMapping("open/{id}")
	public String open(Model model,@PathVariable("id") String id) {
		
		
		Optional<Script> s = scriptsRepo.findById(Integer.parseInt(id));
		
		if(s.isPresent()) {
			Script script = s.get();
			
			model.addAttribute("script", script);
			
			
			
		}
			
		return "script/info";
		
		
		
	}
	
	@PostMapping("open/saveChanges")
	public RedirectView saveChanges(@ModelAttribute("script") Script script) {
		
		
		
		System.out.println("DANS LE POST SAVE : " + script.getContent());
		
		
		Optional<Script> s = scriptsRepo.findById(script.getId());
		
		if(s.isPresent()) {
			Script script2 = s.get();
			
			script2.setContent(script.getContent());
			
			scriptsRepo.save(script2);
			
			
			
		}
			
		return new RedirectView("/users/index");
		
		
		
	}
	
	
}
