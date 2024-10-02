package com.proyecto_petplate.petplate.Repositories;

import com.proyecto_petplate.petplate.Entities.User;  
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  // Anotación para marcar esta clase como un repositorio
public interface UserRepository extends JpaRepository<User, Integer> {
    // JpaRepository proporciona todas las operaciones CRUD básicas

    // Método para verificar si ya existe un usuario con un nombre específico
    boolean existsByUserName(String userName);

    // Método para verificar si ya existe un usuario con un email específico
    boolean existsByUserEmail(String userEmail);
}