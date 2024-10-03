package com.proyecto_petplate.petplate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto_petplate.petplate.Entities.Session;

@Repository
public interface SessionRepository extends JpaRepository <Session, Integer>{

    // Método para verificar si existe una sesión con el token dado
    boolean existsBySesionToken(String sesionToken);

}
