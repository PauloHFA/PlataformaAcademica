package com.plataforma_academica.plataforma.repository;

import com.plataforma_academica.plataforma.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
}