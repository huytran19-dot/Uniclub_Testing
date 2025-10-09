package com.uniclub.repository;

import com.uniclub.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer> {

    boolean existsByNameIgnoreCase(String name);
}
