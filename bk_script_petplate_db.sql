CREATE DATABASE petplate_db;

USE petplate_db;

-- tabla de roles
DROP TABLE IF EXISTS roles;
CREATE TABLE roles (
  id_rol int AUTO_INCREMENT,
  nombre_rol ENUM('Administrador', 'Auditor', 'Usuario') NOT NULL,
  PRIMARY KEY (id_rol)
);

-- tabla de usuarios
DROP TABLE IF EXISTS usuarios;
CREATE TABLE usuarios (
  id_usuario int AUTO_INCREMENT,
  usuario varchar(20) UNIQUE NOT NULL,
  email varchar(254) UNIQUE NOT NULL,
  contrasena varchar(255) NOT NULL,
  img_perfil LONGBLOB,
  fk_rol int NOT NULL,
  FOREIGN KEY (fk_rol) REFERENCES roles (id_rol),
  PRIMARY KEY (id_usuario)
);

-- tabla de sesiones
DROP TABLE IF EXISTS sesiones;
CREATE TABLE sesiones (
  id_sesion int AUTO_INCREMENT,
  token_sesion text NOT NULL,
  fecha_inicio timestamp NOT NULL,
  fecha_fin timestamp NOT NULL,
  fk_usuario int NOT NULL,
  FOREIGN KEY (fk_usuario) REFERENCES usuarios (id_usuario),
  PRIMARY KEY (id_sesion)
);

-- tabla de ingredientes
DROP TABLE IF EXISTS ingredientes;
CREATE TABLE ingredientes (
  id_ingrediente int AUTO_INCREMENT,
  nombre_ingrediente varchar(30) NOT NULL,
  PRIMARY KEY (id_ingrediente)
);

-- tabla de categorias
DROP TABLE IF EXISTS categorias;
CREATE TABLE categorias (
  id_categoria int AUTO_INCREMENT,
  nombre_categoria varchar(15) NOT NULL,
  subcategoria varchar(20),
  PRIMARY KEY (id_categoria)
);

-- tabla de recetas
DROP TABLE IF EXISTS recetas;
CREATE TABLE recetas (
  id_receta int AUTO_INCREMENT,
  titulo varchar(254) NOT NULL,
  descripcion text NOT NULL,
  img_perfil LONGBLOB,
  fecha_publicacion timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  contador_de_recomendaciones int NOT NULL DEFAULT 0,
  fk_usuario int NOT NULL,
  fk_categoria int NOT NULL,
  FOREIGN KEY (fk_usuario) REFERENCES usuarios (id_usuario),
  FOREIGN KEY (fk_categoria) REFERENCES categorias (id_categoria),
  PRIMARY KEY (id_receta)
);

-- tabla que relaciona recetas e ingredientes
DROP TABLE IF EXISTS recetas_ingredientes;
CREATE TABLE recetas_ingredientes (
  cantidad double NOT NULL,
  fk_receta int NOT NULL,
  fk_ingrediente int NOT NULL,
  FOREIGN KEY (fk_receta) REFERENCES recetas (id_receta),
  FOREIGN KEY (fk_ingrediente) REFERENCES ingredientes (id_ingrediente)
);

-- tabla para registrar recomendaciones de usuarios en recetas
DROP TABLE IF EXISTS usuarios_recomendaciones;
CREATE TABLE usuarios_recomendaciones (
  fk_usuario int NOT NULL,
  fk_receta int NOT NULL,
  FOREIGN KEY (fk_usuario) REFERENCES usuarios (id_usuario),
  FOREIGN KEY (fk_receta) REFERENCES recetas (id_receta)
);

-- insertar los 3 roles 
INSERT INTO roles (nombre_rol) 
VALUES 
('Administrador'),
('Auditor'),
('Usuario');

INSERT INTO categorias (nombre_categoria, subcategoria)
VALUES
('Perro'),('Perro','Pequeño'),('Perro','Mediano'),('Perro','Grande'),
('Gato'),('Gato','Pequeño'),('Gato','Mediano'),('Gato','Grande'),
('Tortuga'),('Tortuga','Terrestre'),('Tortuga','Marina'),('Ave'),('Ave','Periquito'),('Ave','Canario'),('Ave','Agapornis'),('Ave','Cacatúa'),('Ave','Ninfa'),
('Ave','Loro Gris Africano'),('Ave','Diamante Mandarín'),('Ave','Cotorra'),('Ave','Pionus'),('Ave','Guacamayo');

-- asignar el rol 'Usuario' a los usuarios que se registran en la pagina automaticamente
DELIMITER $$

CREATE TRIGGER asignar_rol_predeterminado
BEFORE INSERT ON usuarios
FOR EACH ROW
BEGIN
  -- asignar el rol 'Usuario' por defecto si no se especifica
  IF NEW.fk_rol IS NULL THEN
    SET NEW.fk_rol = (SELECT id_rol FROM roles WHERE nombre_rol = 'Usuario');
  END IF;
END $$

DELIMITER ;

-- incrementar el contador de recomendaciones cuando se da like a una receta
DELIMITER $$

CREATE TRIGGER incrementar_contador_recomendaciones
AFTER INSERT ON usuarios_recomendaciones
FOR EACH ROW
BEGIN
  -- manejo de concurrencia con bloqueos para evitar inconsistencias
  UPDATE recetas
  SET contador_de_recomendaciones = contador_de_recomendaciones + 1
  WHERE id_receta = NEW.fk_receta;
END $$

DELIMITER ;

-- disminuir el contador de recomendaciones cuando se quita un like a una receta
DELIMITER $$

CREATE TRIGGER decrementar_contador_recomendaciones
AFTER DELETE ON usuarios_recomendaciones
FOR EACH ROW
BEGIN
  -- validar que el contador no baje de 0 para evitar inconsistencias
  UPDATE recetas
  SET contador_de_recomendaciones = GREATEST(contador_de_recomendaciones - 1, 0)
  WHERE id_receta = OLD.fk_receta;
END $$

DELIMITER ;