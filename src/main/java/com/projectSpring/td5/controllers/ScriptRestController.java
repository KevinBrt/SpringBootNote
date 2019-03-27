package com.projectSpring.td5.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.projectSpring.td5.Recherche;
import com.projectSpring.td5.entities.Script;
import com.projectSpring.td5.repositories.ScriptsRepository;

@RestController
@RequestMapping("/rest/scripts/")
public class ScriptRestController {
	@Autowired
	private ScriptsRepository repo;
	
	@ResponseBody
	@PostMapping("search")
	public List<Script> get(@RequestBody Recherche search){
		System.out.println("search : " + search.getRecherche());

		return repo.findAll();
		
	}
	
	
}
