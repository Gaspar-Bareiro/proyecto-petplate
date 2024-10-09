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
  usuario varchar(30) UNIQUE NOT NULL,
  email varchar(254) UNIQUE NOT NULL,
  contrasena varchar(255) NOT NULL,
  img_perfil VARCHAR(255),
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
  titulo varchar(200) NOT NULL,
  descripcion text NOT NULL,
  img_receta VARCHAR(255),
  fecha_publicacion timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
  contador_de_recomendaciones int NOT NULL DEFAULT 0,
  fk_usuario int NOT NULL,
  fk_categoria int NOT NULL,
  FOREIGN KEY (fk_usuario) REFERENCES usuarios (id_usuario),
  FOREIGN KEY (fk_categoria) REFERENCES categorias (id_categoria),
  PRIMARY KEY (id_receta)
);

-- tabla de recetas borradas
DROP TABLE IF EXISTS recetas_borradas;
CREATE TABLE recetas_borradas (
  id_receta int AUTO_INCREMENT,
  titulo varchar(200) NOT NULL,
  descripcion text NOT NULL,
  img_receta VARCHAR(255),
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
  id_receta_ingrediente int AUTO_INCREMENT,
  cantidad double NOT NULL,
  unidad_medida varchar(15) NOT NULL,
  fk_receta int NOT NULL,
  fk_ingrediente int NOT NULL,
  FOREIGN KEY (fk_receta) REFERENCES recetas (id_receta),
  FOREIGN KEY (fk_ingrediente) REFERENCES ingredientes (id_ingrediente),
  PRIMARY KEY (id_receta_ingrediente)
);


-- tabla que relaciona recetas borradas e ingredborradas
DROP TABLE IF EXISTS recetas_borradas_ingredientes;
CREATE TABLE recetas_borradas_ingredientes (
  id_receta_ingrediente int AUTO_INCREMENT,
  cantidad double NOT NULL,
  unidad_medida varchar(15) NOT NULL,
  fk_receta int NOT NULL,
  fk_ingrediente int NOT NULL,
  FOREIGN KEY (fk_receta) REFERENCES recetas (id_receta),
  FOREIGN KEY (fk_ingrediente) REFERENCES ingredientes (id_ingrediente),
  PRIMARY KEY (id_receta_ingrediente)
);

-- tabla para registrar recomendaciones de usuarios en recetas
DROP TABLE IF EXISTS usuarios_recomendaciones;
CREATE TABLE usuarios_recomendaciones (
  id_usuario_like int AUTO_INCREMENT,
  fk_usuario int NOT NULL,
  fk_receta int NOT NULL,
  FOREIGN KEY (fk_usuario) REFERENCES usuarios (id_usuario),
  FOREIGN KEY (fk_receta) REFERENCES recetas (id_receta),
  PRIMARY KEY (id_usuario_like)
);

-- insertar los 3 roles 
INSERT INTO roles (nombre_rol) 
VALUES 
('Administrador'),
('Auditor'),
('Usuario');

-- insertar las categorias y subcategoria (animal y tipo)
INSERT INTO categorias (nombre_categoria, subcategoria)
VALUES
('Perro', null),('Perro','Pequeño'),('Perro','Mediano'),('Perro','Grande'),
('Gato', null),('Gato','Pequeño'),('Gato','Mediano'),('Gato','Grande'),
('Conejo', null),('Tortuga', null),('Tortuga','Terrestre'),('Tortuga','Marina'),('Ave', null),('Ave','Periquito'),('Ave','Canario'),('Ave','Agapornis'),('Ave','Cacatúa'),('Ave','Ninfa'),
('Ave','Loro Gris Africano'),('Ave','Diamante Mandarín'),('Ave','Cotorra'),('Ave','Pionus'),('Ave','Guacamayo');

INSERT INTO ingredientes (nombre_ingrediente) VALUES
('Zanahoria'),
('Manzana'),
('Pera'),
('Pepino'),
('Calabaza'),
('Camote'),
('Brócoli'),
('Calabacín'),
('Espinaca'),
('Lechuga'),
('Col rizada'),
('Coles de Bruselas'),
('Judías verdes'),
('Apio'),
('Batata'),
('Berenjena'),
('Guisantes'),
('Pimiento rojo'),
('Pimiento verde'),
('Albahaca'),
('Menta'),
('Romero'),
('Tomillo'),
('Perejil'),
('Moras'),
('Frambuesas'),
('Arándanos'),
('Fresas'),
('Melocotón'),
('Plátano'),
('Melón'),
('Sandía'),
('Piña'),
('Coco'),
('Avena'),
('Quinoa'),
('Arroz integral'),
('Pasta integral'),
('Pan integral'),
('Lentejas'),
('Garbanzos'),
('Frijoles negros'),
('Frijoles pintos'),
('Tofu'),
('Huevo cocido'),
('Pechuga de pollo'),
('Muslo de pollo'),
('Carne de pavo'),
('Pescado blanco'),
('Atún'),
('Salmón'),
('Sardinas'),
('Carne de res magra'),
('Cordero magro'),
('Yogur natural'),
('Queso cottage'),
('Aceite de oliva'),
('Aceite de coco'),
('Aceite de linaza'),
('Huevos de codorniz'),
('Almendras'),
('Anacardos'),
('Cacahuetes (sin sal)'),
('Nueces'),
('Semillas de chía'),
('Semillas de lino'),
('Semillas de calabaza'),
('Semillas de girasol'),
('Mantequilla de maní (sin sal y sin azúcar)'),
('Hígado de pollo'),
('Hígado de res'),
('Músculo de pollo'),
('Pechuga de pato'),
('Jamón cocido (sin sal)'),
('Pulpa de remolacha'),
('Pollo molido'),
('Carne molida de res magra'),
('Pavo molido'),
('Espárragos'),
('Col rizada morada'),
('Higos'),
('Papaya'),
('Chía'),
('Zucchini'),
('Tomate cherry'),
('Nabos'),
('Apio nabo'),
('Calabacita'),
('Moras silvestres'),
('Chayote'),
('Guayaba'),
('Cerezas'),
('Ciruelas'),
('Pomelo'),
('Dátil'),
('Mango'),
('Pepitas de calabaza'),
('Endibia'),
('Repollo'),
('Escarola'),
('Acelga'),
('Cilantro'),
('Rúcula'),
('Jícama'),
('Kiwi'),
('Níspero'),
('Mijo'),
('Habas'),
('Miel'),
('Crema de cacahuate (sin azúcar)'),
('Caldo de hueso de pollo'),
('Caldo de hueso de res'),
('Carne de pato'),
('Carne de conejo'),
('Carne de canguro'),
('Carne de ciervo'),
('Carne de cabra'),
('Músculo de res'),
('Tocino sin sal'),
('Aceite de pescado'),
('Hígado de pato'),
('Molleja de pollo'),
('Bacalao'),
('Palmitos'),
('Mandarina'),
('Toronja'),
('Uvas (sin semilla)'),
('Dátil sin hueso'),
('Jengibre'),
('Sésamo'),
('Amaranto'),
('Maíz cocido'),
('Papas (cocidas y peladas)'),
('Polenta'),
('Trigo sarraceno'),
('Yuca cocida'),
('Achiote'),
('Ajo (en pequeñas cantidades)'),
('Tomillo fresco'),
('Cilantro fresco'),
('Orégano fresco'),
('Sábila (en pequeñas cantidades)'),
('Hinojo'),
('Diente de león'),
('Col morada'),
('Cilantro molido'),
('Canela (en pequeñas cantidades)'),
('Arroz blanco (cocido)'),
('Tapioca'),
('Trucha'),
('Carne de cabra'),
('Chucrut (sin sal)'),
('Frutos secos sin sal'),
('Pimientos dulces'),
('Berros'),
('Alubias'),
('Panceta sin sal'),
('Pavo horneado'),
('Calabaza en puré'),
('Alga nori'),
('Caldo de pescado'),
('Queso bajo en lactosa'),
('Pollo asado'),
('Carne magra de cerdo'),
('Piñones'),
('Pipas de girasol sin sal'),
('Frutos rojos'),
('Lecitina de soja'),
('Manteca de coco'),
('Té de manzanilla (sin azúcar)'),
('Yogur sin lactosa'),
('Queso sin sal'),
('Chirimoya'),
('Mangostán'),
('Aceitunas (sin sal)'),
('Espinaca cocida'),
('Brócoli cocido'),
('Calabaza asada'),
('Choclo cocido'),
('Mandioca hervida'),
('Cuscús integral'),
('Espárragos cocidos'),
('Chícharos'),
('Chalotes'),
('Palmitos frescos'),
('Castañas de agua'),
('Setas'),
('Champiñones'),
('Espinacas frescas'),
('Brotes de soja'),
('Alfalfa'),
('Brotes de trigo'),
('Borraja'),
('Col fermentada (sin sal)'),
('Eneldo fresco'),
('Tomate deshidratado'),
('Levadura nutricional'),
('Coliflor asada'),
('Pepino sin piel'),
('Rábano'),
('Brócoli al vapor'),
('Chirivía'),
('Espelta cocida'),
('Granos de centeno'),
('Avena integral'),
('Maca'),
('Harina de garbanzo'),
('Harina de arroz integral'),
('Harina de coco'),
('Puré de manzana sin azúcar'),
('Sésamo tostado'),
('Chayote cocido'),
('Frambuesas frescas'),
('Níspero cocido'),
('Pan sin gluten'),
('Tortillas de maíz'),
('Salmón ahumado'),
('Caviar de salmón'),
('Aceite de canola'),
('Gelatina sin azúcar'),
('Almidón de maíz'),
('Guisantes congelados'),
('Tocino sin sal cocido'),
('Piña fresca'),
('Col rizada al vapor'),
('Pimientos rojos asados'),
('Mantequilla de almendra'),
('Jícama cruda'),
('Raíz de apio'),
('Tofu orgánico'),
('Fruta del dragón'),
('Sorgo cocido'),
('Raíz de taro'),
('Cáscara de huevo molida'),
('Sésamo sin tostar'),
('Cúrcuma'),
('Ghee'),
('Yogur de cabra sin lactosa'),
('Huevo de pato cocido'),
('Filete de trucha'),
('Filete de lenguado'),
('Tilapia'),
('Pargo rojo'),
('Caballa'),
('Pescado gato'),
('Algas kelp'),
('Algas espirulina'),
('Manteca de cerdo'),
('Harina de avena cocida'),
('Trigo integral cocido'),
('Cuscús de garbanzo'),
('Pasta de quinoa'),
('Avena sin gluten'),
('Leche de coco sin azúcar');

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