package com.projectSpring.td5.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectSpring.td5.entities.Categorie;

@Repository
public interface CategoriesRepository extends JpaRepository<Categorie, Integer>{
	public Categorie findById(int id);
}
