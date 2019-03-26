package com.projectSpring.td5.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.projectSpring.td5.entities.History;
import com.projectSpring.td5.entities.Script;

@Repository
public interface ScriptsRepository extends JpaRepository<Script, Integer>{
	public List<History> findByHistory_id(int id);
}
