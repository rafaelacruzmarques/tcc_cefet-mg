-- phpMyAdmin SQL Dump
-- version 4.4.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: 07-Dez-2015 às 02:52
-- Versão do servidor: 5.6.26
-- PHP Version: 5.6.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `gastations`
--

-- --------------------------------------------------------

--
-- Estrutura da tabela `cadastroposto`
--

CREATE TABLE IF NOT EXISTS `cadastroposto` (
  `idPosto` int(11) NOT NULL,
  `nomePosto` varchar(100) NOT NULL,
  `coordLatid` float(10,6) NOT NULL,
  `coordLongit` float(10,6) NOT NULL,
  `endereco` varchar(100) DEFAULT NULL,
  `bandeira` varchar(100) DEFAULT NULL,
  `formaDePagamento` varchar(100) DEFAULT NULL,
  `precoGasolina` float DEFAULT NULL,
  `precoEtanol` float DEFAULT NULL,
  `precoDiesel` float DEFAULT NULL,
  `adicionais` varchar(100) DEFAULT NULL,
  `horarioDeFuncionamento` varchar(25) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `cadastroposto`
--

INSERT INTO `cadastroposto` (`idPosto`, `nomePosto`, `coordLatid`, `coordLongit`, `endereco`, `bandeira`, `formaDePagamento`, `precoGasolina`, `precoEtanol`, `precoDiesel`, `adicionais`, `horarioDeFuncionamento`) VALUES
(1, '', -19.952335, -44.025455, NULL, NULL, NULL, NULL, NULL, NULL, 'Posto1', NULL),
(2, '', -19.954041, -44.025787, NULL, NULL, NULL, NULL, NULL, NULL, 'Posto2', NULL),
(3, '', -19.900429, -44.009148, NULL, NULL, NULL, NULL, NULL, NULL, 'Posto3', NULL),
(4, '', -19.892570, -43.996365, NULL, NULL, NULL, NULL, NULL, NULL, 'Posto Alissons', NULL),
(5, '', -19.947969, -44.024578, NULL, NULL, NULL, NULL, NULL, NULL, 'Posto ITAU', NULL);

-- --------------------------------------------------------

--
-- Estrutura da tabela `usuario`
--

CREATE TABLE IF NOT EXISTS `usuario` (
  `idUsuario` int(10) unsigned NOT NULL,
  `nome` varchar(50) DEFAULT NULL,
  `sobrenome` varchar(50) DEFAULT NULL,
  `usuario` varchar(50) NOT NULL,
  `senha` varchar(50) NOT NULL,
  `dataNascimento` date DEFAULT NULL,
  `idPosto` int(11) DEFAULT NULL
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=latin1;

--
-- Extraindo dados da tabela `usuario`
--

INSERT INTO `usuario` (`idUsuario`, `nome`, `sobrenome`, `usuario`, `senha`, `dataNascimento`, `idPosto`) VALUES
(1, NULL, NULL, 'alissonrs', 'ars', NULL, NULL),
(3, NULL, NULL, 'laura', '123', NULL, NULL),
(25, 'Rafaela', 'Duarte', 'rafaela145', '1234', '2015-11-09', NULL),
(26, 'Rafaela', 'Duarte', 'rafaelay', '1234', '2015-11-09', NULL),
(27, 'ff', 'nunes', 'ahass', 'asxdd', '0000-00-00', NULL),
(28, 'Victor ', 'Hugo', 'victorhugoams@gmail.com', '12345', '0000-00-00', NULL),
(29, 'Matheus', 'Antunes', 'matheusantunes@gmail.com', '123456', '0000-00-00', NULL),
(30, 'Rafaela ', 'Marques', 'rafaela', '1234', '1997-03-25', NULL);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cadastroposto`
--
ALTER TABLE `cadastroposto`
  ADD PRIMARY KEY (`idPosto`);

--
-- Indexes for table `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`idUsuario`),
  ADD UNIQUE KEY `email` (`usuario`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `cadastroposto`
--
ALTER TABLE `cadastroposto`
  MODIFY `idPosto` int(11) NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=28;
--
-- AUTO_INCREMENT for table `usuario`
--
ALTER TABLE `usuario`
  MODIFY `idUsuario` int(10) unsigned NOT NULL AUTO_INCREMENT,AUTO_INCREMENT=31;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
