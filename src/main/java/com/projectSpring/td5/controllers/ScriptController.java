package com.projectSpring.td5.controllers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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

import com.projectSpring.td5.Recherche;
import com.projectSpring.td5.entities.Categorie;
import com.projectSpring.td5.entities.History;
import com.projectSpring.td5.entities.Language;
import com.projectSpring.td5.entities.Script;
import com.projectSpring.td5.entities.User;
import com.projectSpring.td5.repositories.CategoriesRepository;
import com.projectSpring.td5.repositories.HistoriesRepository;
import com.projectSpring.td5.repositories.LanguagesRepository;
import com.projectSpring.td5.repositories.ScriptsRepository;


import io.github.jeemv.springboot.vuejs.VueJS;
import io.github.jeemv.springboot.vuejs.utilities.Http;

@Controller
@RequestMapping("/users/script/")
public class ScriptController {
	
	@Autowired
	private ScriptsRepository scriptsRepo;
	
	@Autowired
	private LanguagesRepository languagesRepo;
	
	@Autowired
	private CategoriesRepository categoriesRepo;
	
	@Autowired
	private HistoriesRepository historiesRepo;
	
	@Autowired
	private VueJS vue;
	
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
	
	@GetMapping("create")
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
		
		History h = new History();
		h.setComment("Creation of the script");
		h.setContent(script.getContent());
		h.setDate(dateFormat.format(date));
		
		historiesRepo.save(h);
		
		script.addHistory(h);
		
		scriptsRepo.save(script);
		
		h.setScript(script);
		historiesRepo.save(h);
			
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
	public RedirectView saveChanges(@ModelAttribute("script") Script script, @RequestParam("comment") String comment) {
		
		
		
		System.out.println("DANS LE POST SAVE : " + script.getId());
		
		
		Optional<Script> s = scriptsRepo.findById(script.getId());
		
		if(s.isPresent()) {
			System.out.println("est present");
			Script script2 = s.get();
			
			script2.setContent(script.getContent());
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			
			History h = new History();
			h.setComment(comment);
			h.setContent(script2.getContent());
			h.setDate(dateFormat.format(date));
			
			historiesRepo.save(h);
			
			scriptsRepo.save(script2);
			
			h.setScript(script2);
			
			historiesRepo.save(h);
			
			
			
		}
			
		return new RedirectView("/users/index");
		
		
		
	}
	
	@RequestMapping("histories/{id}")
	public String showHistories(Model model,@PathVariable("id") String id) {
		
		
		List<History> h = historiesRepo.findByScript_id(Integer.parseInt(id));
		
		System.out.println(h);
		
		model.addAttribute("histories",h);
		
		
		return "script/histories";
		
		
		
	}
	  

	@RequestMapping("history/open/{id}")
	public String showHistory(Model model,@PathVariable("id") String id) {
		
		
		History h = historiesRepo.findById(Integer.parseInt(id));
		
		
		model.addAttribute("h",h);
		
		
		return "script/history";
		
		
		
	}
	 
	@GetMapping("search")
	public String search(Model model) {
		
		
		model.addAttribute("vue", vue);
		Recherche r = new Recherche();
		
		
		vue.addData("search", "");
		vue.addDataRaw("result", "[]");
		
		vue.addMethod("test", "var self=this;"+Http.post(
				"/rest/scripts/search",
				(Object)"{recherche:self.search}",
				" self.result = []; response.data.forEach(function(element) {self.result.push({title: element.title})}); "
				)
		);
		
		
		  
			
		
		return "vueJS/search";
		
		
	}
	
	
}
