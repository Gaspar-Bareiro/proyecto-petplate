package com.proyecto_petplate.petplate.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto_petplate.petplate.Entities.EnumRolName;
import com.proyecto_petplate.petplate.Entities.Rol;

@Repository
public interface RolRepository extends JpaRepository <Rol, Integer> {
    
    // Método para obtener el id del rol basado en su nombre
    public Rol findByRolName(EnumRolName rolName);  // Retorna la entidad completa segun el nombre del rol
}
