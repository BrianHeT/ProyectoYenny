-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 26-06-2025 a las 09:28:40
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `libreria`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `administrador`
--

CREATE TABLE `administrador` (
  `id_administrador` int(11) NOT NULL,
  `apellido` varchar(45) NOT NULL,
  `fk_usuario` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Volcado de datos para la tabla `administrador`
--

INSERT INTO `administrador` (`id_administrador`, `apellido`, `fk_usuario`) VALUES
(1, 'george', 1);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `autor`
--

CREATE TABLE `autor` (
  `id_autor` int(11) NOT NULL,
  `independiente` tinyint(4) NOT NULL,
  `editorial` varchar(45) DEFAULT 'Independiente',
  `fk_usuario` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Volcado de datos para la tabla `autor`
--

INSERT INTO `autor` (`id_autor`, `independiente`, `editorial`, `fk_usuario`) VALUES
(1, 1, 'Independiente', 6),
(2, 0, 'dame el 10', 7),
(3, 0, 'Secker & Warburg', 14),
(4, 0, 'Éditions Gallimard', 15),
(5, 0, 'Editorial Sudamericana', 16),
(6, 0, 'Bibliotheca Teubneriana', 17),
(7, 0, 'Salerno Editrice', 18),
(8, 0, 'The Russian Messenger', 19),
(9, 0, 'Editorial Sudamericana', 20),
(10, 0, 'Gosselin', 21),
(11, 0, 'Ballantine Books', 22),
(12, 0, 'Allen & Unwin', 23),
(13, 0, 'Bloomsbury', 24),
(14, 0, 'T. Egerton', 25),
(15, 0, 'Constable & Co.', 26),
(16, 0, 'Doubleday', 27),
(17, 0, 'HarperCollins Brasil', 28),
(18, 0, 'HarperCollins', 29),
(19, 0, 'Ward, Lock & Co.', 30),
(20, 0, 'Imprenta Real', 31),
(21, 1, 'Independiente', 34);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `carrito`
--

CREATE TABLE `carrito` (
  `id_carrito` int(11) NOT NULL,
  `fk_cliente` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `cliente`
--

CREATE TABLE `cliente` (
  `id_cliente` int(11) NOT NULL,
  `direccion` varchar(45) NOT NULL,
  `fk_usuario` int(11) NOT NULL,
  `saldo` double DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Volcado de datos para la tabla `cliente`
--

INSERT INTO `cliente` (`id_cliente`, `direccion`, `fk_usuario`, `saldo`) VALUES
(1, 'guatemala 5539', 2, 9662),
(2, 'avcorrientes', 9, 0),
(3, 'avcorrientes', 10, 0),
(4, 'asd', 11, 0),
(5, 'avcorrientes', 12, 0),
(7, 'asd', 32, 0),
(8, 'ddd', 33, 0);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `itemcarrito`
--

CREATE TABLE `itemcarrito` (
  `id_itemCarrito` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `fk_libro` int(11) NOT NULL,
  `fk_transaccion` int(11) NOT NULL,
  `fk_carrito` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `libro`
--

CREATE TABLE `libro` (
  `id_libro` int(11) NOT NULL,
  `titulo` varchar(45) NOT NULL,
  `sipnosis` varchar(45) NOT NULL,
  `precio` int(11) NOT NULL,
  `fk_autor` int(11) DEFAULT NULL,
  `stock` int(11) NOT NULL,
  `estado` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Volcado de datos para la tabla `libro`
--

INSERT INTO `libro` (`id_libro`, `titulo`, `sipnosis`, `precio`, `fk_autor`, `stock`, `estado`) VALUES
(5, '1985', 'Distopía de Orwell', 1200, 14, 50, 'APROBADO'),
(6, 'El principito', 'Fábula filosófica', 1000, 15, 5, 'APROBADO'),
(7, 'Cien años de soledad', 'Obra de García Márquez', 1800, 16, 20, 'APROBADO'),
(8, 'La Odisea', 'Épica de Homero', 1400, 17, 18, 'APROBADO'),
(9, 'La Divina Comedia', 'Obra de Dante', 1600, 18, 22, 'APROBADO'),
(10, 'Crimen y Castigo', 'Novela de Dostoievski', 1300, 19, 18, 'APROBADO'),
(11, 'Rayuela', 'Novela de Cortázar', 1550, 20, 15, 'APROBADO'),
(12, 'Los Miserables', 'Historia de Victor Hugo', 1750, 21, 12, 'APROBADO'),
(13, 'Fahrenheit 451', 'Distopía de Bradbury', 1100, 22, 11, 'APROBADO'),
(14, 'El Señor de los Anillos', 'Fantasía épica de Tolkien', 2000, 23, 30, 'APROBADO'),
(15, 'Harry Potter y la Piedra Filosofal', 'Inicio de la saga mágica', 1800, 24, 23, 'APROBADO'),
(16, 'El Hobbit', 'Aventura de Bilbo Bolsón', 1700, 23, 22, 'APROBADO'),
(17, 'Orgullo y Prejuicio', 'Romance de Jane Austen', 1400, 25, 16, 'APROBADO'),
(18, 'Dráculaa', 'Historia de vampiros de Bram Stokerk', 2000, 26, 30, 'APROBADO'),
(19, 'Don Juan Tenorio', 'Clásico español', 1250, 31, 19, 'APROBADO'),
(20, 'El Alquimista', 'Obra de Paulo Coelho', 1450, 28, 23, 'APROBADO'),
(21, 'El Código Da Vinci', 'Novela de misterio de Dan Brown', 1600, 27, 20, 'APROBADO'),
(22, 'Las Crónicas de Narnia', 'Mundo fantástico de C.S. Lewis', 1550, 29, 26, 'APROBADO'),
(23, 'Sherlock Holmes', 'Investigaciones de Arthur Conan Doyle', 1500, 30, 24, 'APROBADO'),
(26, 'aaa', 'sss', 500, 1, 50, 'APROBADO'),
(27, 'venezuela is mayorr', 'asd', 50, 34, 50, 'APROBADO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `transaccion`
--

CREATE TABLE `transaccion` (
  `id_transaccion` int(11) NOT NULL,
  `total` int(11) NOT NULL,
  `fk_cliente` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Volcado de datos para la tabla `transaccion`
--

INSERT INTO `transaccion` (`id_transaccion`, `total`, `fk_cliente`) VALUES
(1, 16800, 12345679),
(2, 10000, 12345679),
(3, 2400, 12345679),
(4, 8400, 12345679),
(5, 15000, 12345679),
(6, 8520, 12345679),
(7, 6000, 1),
(8, 1200, 1),
(9, 10000, 1),
(10, 1200, 2),
(11, 1200, 2),
(12, 11350, 2),
(13, 6600, 2),
(14, 8400, 1),
(15, 9000, 2),
(16, 5400, 2);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `transaccion_libro`
--

CREATE TABLE `transaccion_libro` (
  `id` int(11) NOT NULL,
  `fk_transaccion` int(11) NOT NULL,
  `fk_libro` int(11) NOT NULL,
  `cantidad` int(11) NOT NULL,
  `precio_unitario` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `transaccion_libro`
--

INSERT INTO `transaccion_libro` (`id`, `fk_transaccion`, `fk_libro`, `cantidad`, `precio_unitario`) VALUES
(1, 8, 5, 1, 1200.00),
(2, 9, 6, 10, 1000.00),
(3, 11, 5, 1, 1200.00),
(4, 12, 7, 2, 1800.00),
(5, 12, 11, 5, 1550.00),
(6, 13, 13, 6, 1100.00),
(7, 15, 15, 5, 1800.00),
(8, 16, 7, 3, 1800.00);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id_usuario` int(11) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `mail` varchar(45) NOT NULL,
  `dni` int(11) NOT NULL,
  `password` char(64) NOT NULL,
  `tipo_usuario` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id_usuario`, `nombre`, `mail`, `dni`, `password`, `tipo_usuario`) VALUES
(1, 'hector', 'hector@hector', 12345678, '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'Administrador'),
(2, 'stefania', 'stefania@medina', 12345679, '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'Cliente'),
(6, 'fannyy', 'fanny@luzz', 12345677, '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'Autor'),
(7, 'ghamaa', 'ghama@sueltael10a', 12345699, '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'Autor'),
(9, 'Brian', 'brian@libreria.com', 43241445, '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'Cliente'),
(10, 'brian', 'brian1@gmail.com', 43241445, '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'Cliente'),
(11, 'asd', 'asd@gmail.com', 12345678, '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'Cliente'),
(12, 'brian', 'brian@yenny.com', 43241445, '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'Cliente'),
(14, 'George Orwell', 'orwell@libreria.com', 11111111, '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'Autor'),
(15, 'Antoine de Saint-Exupéry', 'saint@libreria.com', 22222222, '9b8769a4a742959a2d0298c36fb70623f2dfacda84362', 'Autor'),
(16, 'Gabriel García Márquez', 'marquez@libreria.com', 33333333, '9b8769a4a742959a2d0298c36fb70623f2dfacda84362', 'Autor'),
(17, 'Homero', 'homero@libreria.com', 44444444, '9b8769a4a742959a2d0298c36fb70623f2dfacda84362', 'Autor'),
(18, 'Dante Alighieri', 'dante@libreria.com', 55555555, '9b8769a4a742959a2d0298c36fb70623f2dfacda84362', 'Autor'),
(19, 'Fiódor Dostoyevski', 'dostoyevski@libreria.com', 66666666, '9b8769a4a742959a2d0298c36fb70623f2dfacda84362', 'Autor'),
(20, 'Julio Cortázar', 'cortazar@libreria.com', 77777777, '9b8769a4a742959a2d0298c36fb70623f2dfacda84362', 'Autor'),
(21, 'Victor Hugo', 'hugo@libreria.com', 88888888, '9b8769a4a742959a2d0298c36fb70623f2dfacda84362', 'Autor'),
(22, 'Ray Bradbury', 'bradbury@libreria.com', 99999999, '9b8769a4a742959a2d0298c36fb70623f2dfacda84362', 'Autor'),
(23, 'J.R.R. Tolkien', 'tolkien@libreria.com', 10101010, '9b8769a4a742959a2d0298c36fb70623f2dfacda84362', 'Autor'),
(24, 'J.K. Rowling', 'rowling@libreria.com', 12121212, '9b8769a4a742959a2d0298c36fb70623f2dfacda84362', 'Autor'),
(25, 'Jane Austen', 'austen@libreria.com', 13131313, '9b8769a4a742959a2d0298c36fb70623f2dfacda84362', 'Autor'),
(26, 'Bram Stoker', 'stoker@libreria.com', 14141414, '9b8769a4a742959a2d0298c36fb70623f2dfacda84362', 'Autor'),
(27, 'Dan Brown', 'brown@libreria.com', 20202020, '9b8769a4a742959a2d0298c36fb70623f2dfacda84362', 'Autor'),
(28, 'Paulo Coelho', 'coelho@libreria.com', 23232323, '9b8769a4a742959a2d0298c36fb70623f2dfacda84362', 'Autor'),
(29, 'C.S. Lewis', 'lewis@libreria.com', 24242424, '9b8769a4a742959a2d0298c36fb70623f2dfacda84362', 'Autor'),
(30, 'Arthur Conan Doyle', 'doyle@libreria.com', 25252525, '9b8769a4a742959a2d0298c36fb70623f2dfacda84362', 'Autor'),
(31, 'José Zorrilla', 'zorrilla@libreria.com', 26262626, '9b8769a4a742959a2d0298c36fb70623f2dfacda84362', 'Autor'),
(32, 'pela', 'as@asd', 58585858, 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'Cliente'),
(33, 'aaa', 'asddd@asd', 25412369, 'a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3', 'Cliente'),
(34, 'osmel', 'osmel@osmel', 95084719, '9b8769a4a742959a2d0298c36fb70623f2dfacda8436237df08d8dfd5b37374c', 'Autor');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `administrador`
--
ALTER TABLE `administrador`
  ADD PRIMARY KEY (`id_administrador`);

--
-- Indices de la tabla `autor`
--
ALTER TABLE `autor`
  ADD PRIMARY KEY (`id_autor`);

--
-- Indices de la tabla `carrito`
--
ALTER TABLE `carrito`
  ADD PRIMARY KEY (`id_carrito`);

--
-- Indices de la tabla `cliente`
--
ALTER TABLE `cliente`
  ADD PRIMARY KEY (`id_cliente`);

--
-- Indices de la tabla `itemcarrito`
--
ALTER TABLE `itemcarrito`
  ADD PRIMARY KEY (`id_itemCarrito`);

--
-- Indices de la tabla `libro`
--
ALTER TABLE `libro`
  ADD PRIMARY KEY (`id_libro`);

--
-- Indices de la tabla `transaccion`
--
ALTER TABLE `transaccion`
  ADD PRIMARY KEY (`id_transaccion`);

--
-- Indices de la tabla `transaccion_libro`
--
ALTER TABLE `transaccion_libro`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_transaccion` (`fk_transaccion`),
  ADD KEY `fk_libro` (`fk_libro`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `mail_UNIQUE` (`mail`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `administrador`
--
ALTER TABLE `administrador`
  MODIFY `id_administrador` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `autor`
--
ALTER TABLE `autor`
  MODIFY `id_autor` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=22;

--
-- AUTO_INCREMENT de la tabla `carrito`
--
ALTER TABLE `carrito`
  MODIFY `id_carrito` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `cliente`
--
ALTER TABLE `cliente`
  MODIFY `id_cliente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `itemcarrito`
--
ALTER TABLE `itemcarrito`
  MODIFY `id_itemCarrito` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `libro`
--
ALTER TABLE `libro`
  MODIFY `id_libro` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=28;

--
-- AUTO_INCREMENT de la tabla `transaccion`
--
ALTER TABLE `transaccion`
  MODIFY `id_transaccion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT de la tabla `transaccion_libro`
--
ALTER TABLE `transaccion_libro`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `transaccion_libro`
--
ALTER TABLE `transaccion_libro`
  ADD CONSTRAINT `transaccion_libro_ibfk_1` FOREIGN KEY (`fk_transaccion`) REFERENCES `transaccion` (`id_transaccion`),
  ADD CONSTRAINT `transaccion_libro_ibfk_2` FOREIGN KEY (`fk_libro`) REFERENCES `libro` (`id_libro`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
