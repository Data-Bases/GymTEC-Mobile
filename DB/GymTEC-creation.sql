CREATE TABLE ServiciosSucursal
(
	Id INTEGER NOT NULL,
	IdServicioClase INTEGER NOT NULL,
	NombreSucursal VARCHAR(100) NOT NULL,

	PRIMARY KEY (Id),
	
	FOREIGN KEY (IdServicioClase) REFERENCES ServiciosClases(Id),
	FOREIGN KEY (NombreSucursal) REFERENCES Sucursal(Nombre)
);

CREATE TABLE Clase
(
	Id INTEGER NOT NULL,
	NombreClase VARCHAR(100) NOT NULL,
	HoraInicio TIME(0) NOT NULL,
	HoraFinalizacion TIME(0) NOT NULL,
	Capacidad INTEGER NOT NULL,
	EsGrupal BIT NOT NULL,
	NombreEmpleado VARCHAR(300) NOT NULL,

	PRIMARY KEY (Id)
);

CREATE TABLE ClaseFecha
(
	Id INTEGER NOT NULL,
	IdClase INTEGER NOT NULL,
	Fecha DATE NOT NULL,
	
	PRIMARY KEY (Id),
	
	FOREIGN KEY (IdClase) REFERENCES Clase(Id)
);

CREATE TABLE ServiciosClases
(
	Id INTEGER NOT NULL,
	Nombre VARCHAR(100) NOT NULL,
	Descripcion VARCHAR(100) NOT NULL UNIQUE,

	PRIMARY KEY (Id)
);

CREATE TABLE ClienteClase
(
	Id INTEGER NOT NULL,
	IdClaseFecha INTEGER NOT NULL,
	CedulaCliente INTEGER NOT NULL,

	PRIMARY KEY (Id),
	
	FOREIGN KEY (IdClaseFecha) REFERENCES ClaseFecha(Id),
	FOREIGN KEY (CedulaCliente) REFERENCES Cliente(Cedula)
);

CREATE TABLE Cliente
(
	Cedula INTEGER NOT NULL CHECK(length(Cedula) >= 9),
	Nombre VARCHAR(100) NOT NULL,
	Apellido1 VARCHAR(100) NOT NULL,
	Apellido2 VARCHAR(100),
	Provincia VARCHAR(100) NOT NULL,
	Canton VARCHAR(100) NOT NULL,
	Distrito VARCHAR(100) NOT NULL,
	Email VARCHAR(100) NOT NULL,
	Contrasena VARCHAR(100) NOT NULL,
	FechaNacimiento DATE NOT NULL,
	Peso FLOAT(2) DEFAULT 0,
	IMC FLOAT(2) DEFAULT 0,

	PRIMARY KEY (Cedula)
);

CREATE TABLE Sucursal
(
	Nombre VARCHAR(100) NOT NULL,

	PRIMARY KEY (Nombre)
);