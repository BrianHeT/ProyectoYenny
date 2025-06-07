-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 07-06-2025 a las 04:47:58
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
(2, 0, 'dame el 10', 7);

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
(1, 'guatemala 5539', 2, 0),
(2, 'avcorrientes', 9, 0),
(3, 'avcorrientes', 10, 0),
(4, 'asd', 11, 0),
(5, 'avcorrientes', 12, 0);

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
  `stock` int(11) NOT NULL,
  `estado` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Volcado de datos para la tabla `libro`
--

INSERT INTO `libro` (`id_libro`, `titulo`, `sipnosis`, `precio`, `stock`, `estado`) VALUES
(5, '1984', 'Distopía de Orwell', 1200, 10, 'Disponible'),
(6, 'El principito', 'Fábula filosófica', 1000, 15, 'Disponible'),
(7, 'Cien años de soledad', 'Obra de García Márquez', 1800, 25, 'Disponible'),
(8, 'La Odisea', 'Épica de Homero', 1400, 18, 'Disponible'),
(9, 'La Divina Comedia', 'Obra de Dante', 1600, 22, 'Disponible'),
(10, 'Crimen y Castigo', 'Novela de Dostoievski', 1300, 18, 'Disponible'),
(11, 'Rayuela', 'Novela de Cortázar', 1550, 20, 'Disponible'),
(12, 'Los Miserables', 'Historia de Victor Hugo', 1750, 12, 'Disponible'),
(13, 'Fahrenheit 451', 'Distopía de Bradbury', 1100, 17, 'Disponible'),
(14, 'El Señor de los Anillos', 'Fantasía épica de Tolkien', 2000, 30, 'Disponible'),
(15, 'Harry Potter y la Piedra Filosofal', 'Inicio de la saga mágica', 1800, 28, 'Disponible'),
(16, 'El Hobbit', 'Aventura de Bilbo Bolsón', 1700, 22, 'Disponible'),
(17, 'Orgullo y Prejuicio', 'Romance de Jane Austen', 1400, 16, 'Disponible'),
(18, 'Dráculaa', 'Historia de vampiros de Bram Stokerk', 2000, 30, 'Disponible'),
(19, 'Don Juan Tenorio', 'Clásico español', 1250, 19, 'Disponible'),
(20, 'El Alquimista', 'Obra de Paulo Coelho', 1450, 23, 'Disponible'),
(21, 'El Código Da Vinci', 'Novela de misterio de Dan Brown', 1600, 20, 'Disponible'),
(22, 'Las Crónicas de Narnia', 'Mundo fantástico de C.S. Lewis', 1550, 26, 'Disponible'),
(23, 'Sherlock Holmes', 'Investigaciones de Arthur Conan Doyle', 1500, 24, 'Disponible'),
(24, 'asd', 'asd', 213, 10, 'disponible');

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
(6, 8520, 12345679);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id_usuario` int(11) NOT NULL,
  `nombre` varchar(45) NOT NULL,
  `mail` varchar(45) NOT NULL,
  `dni` int(11) NOT NULL,
  `password` varchar(45) NOT NULL,
  `tipo_usuario` varchar(45) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`id_usuario`, `nombre`, `mail`, `dni`, `password`, `tipo_usuario`) VALUES
(1, 'hector', 'hector@hector', 12345678, '123', 'Administrador'),
(2, 'stefaniaa', 'stefania@medinaa', 12345679, '123', 'Cliente'),
(6, 'fanny', 'fanny@luz', 12345677, '123', 'Autor'),
(7, 'ghama', 'ghama@sueltael10', 12345699, '123', 'Autor'),
(9, 'Brian', 'brian@libreria.com', 43241445, 'yenny', 'Cliente'),
(10, 'brian', 'brian1@gmail.com', 43241445, 'yenny', 'Cliente'),
(11, 'asd', 'asd@gmail.com', 12345678, 'dvg', 'Cliente'),
(12, 'brian', 'brian@yenny.com', 43241445, 'bhqqb', 'Cliente');

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
  MODIFY `id_autor` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `carrito`
--
ALTER TABLE `carrito`
  MODIFY `id_carrito` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `cliente`
--
ALTER TABLE `cliente`
  MODIFY `id_cliente` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `itemcarrito`
--
ALTER TABLE `itemcarrito`
  MODIFY `id_itemCarrito` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `libro`
--
ALTER TABLE `libro`
  MODIFY `id_libro` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=25;

--
-- AUTO_INCREMENT de la tabla `transaccion`
--
ALTER TABLE `transaccion`
  MODIFY `id_transaccion` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id_usuario` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
