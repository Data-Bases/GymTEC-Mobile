-- Insert Branches
INSERT INTO Sucursal(Nombre) VALUES ('GymASETEC');

-- Insert Servicios 
INSERT INTO ServiciosClases(Nombre, Descripcion)
VALUES ('Indoor Cycling', 'Clase de bicicleta estacionaria'),
       ('Pilates', 'Clase de pilates relajante'),
       ('Yoga', 'Clase de yoga para la flexibilidad'),
       ('Zumba', 'Clase de Zumba con musica latina'),
       ('Natacion', 'Clase de natacion ideal para principiantes');

SELECT * FROM Cliente;
SELECT * FROM Sucursal;
