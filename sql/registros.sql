/*Datos BANCOS*/

INSERT INTO BANCOS VALUES (1,'SEFED CREDIT BANK');
INSERT INTO BANCOS VALUES (2,'IBER BANK');
INSERT INTO BANCOS VALUES (3,'AERUS BANK');


/*Datos CLIENTES*/

INSERT INTO CLIENTES VALUES (1,'CSIC TECNICA, SAS','ES0000000000000000000001');
INSERT INTO CLIENTES VALUES (2,'CSIC MIRALL SAS','ES0000000000000000000002');
INSERT INTO CLIENTES VALUES (3,'CSIC BILIASA SLS','ES0000000000000000000003');
INSERT INTO CLIENTES VALUES (4,'CSIC EROSKI, SAS','ES0000000000000000000004');
INSERT INTO CLIENTES VALUES (5,'SALGAR MATERIAL DE OFICINA','ES0000000000000000000005');
INSERT INTO CLIENTES VALUES (6,'JARDINERIA SANT VICEN� SAS','ES0000000000000000000006');
INSERT INTO CLIENTES VALUES (7,'GLYCITACA','ES0000000000000000000007');
INSERT INTO CLIENTES VALUES (8,'TECHNOFFICE S.L.S','ES0000000000000000000008');
INSERT INTO CLIENTES VALUES (9,'DESK-TGN, S.L.S','ES0000000000000000000009');
INSERT INTO CLIENTES VALUES (10,'ILUMINACION ALMI','ES0000000000000000000010');
INSERT INTO CLIENTES VALUES (11,'OFIMOBLE','ES0000000000000000000011');
INSERT INTO CLIENTES VALUES (12,'OFIARAGON S.L.S','ES0000000000000000000012');
INSERT INTO CLIENTES VALUES (13,'IBERICODROCHES, SLS','ES0000000000000000000013');
INSERT INTO CLIENTES VALUES (14,'AGRO CABA�AS S.L.S','ES0000000000000000000014');
INSERT INTO CLIENTES VALUES (15,'TEKNOBURU','ES0000000000000000000015');
INSERT INTO CLIENTES VALUES (16,'GARBI S.L.S','ES0000000000000000000016');
INSERT INTO CLIENTES VALUES (17,'BINARI','ES0000000000000000000017');


/*Datos PROVEEDORES*/

INSERT INTO PROVEEDORES VALUES (1, 'CSIP SONDIKA, SLS');
INSERT INTO PROVEEDORES VALUES (2, 'CSIP PAMPLONESA, SAS');
INSERT INTO PROVEEDORES VALUES (3, 'CSIP LEKINAGA, SAS');
INSERT INTO PROVEEDORES VALUES (4, 'CSIP ALAVESA, SAS');
INSERT INTO PROVEEDORES VALUES (5, 'CSIP BONET SLS');
INSERT INTO PROVEEDORES VALUES (6, 'CSIP SOLAN, SAS');


/*Datos PAGOS*/
INSERT INTO PAGOS VALUES(1, 3, 2, 24.3, 100021, '10/02/2021', 'Transferencia', 1);
INSERT INTO PAGOS VALUES(2, 2, 2, 43.2, 110021, '10/09/2021', 'Domiciliado', 1);
INSERT INTO PAGOS VALUES(3, 2, 2, 337, 120021, '02/06/2021', 'Tarjeta de cr�dito', 0);
INSERT INTO PAGOS VALUES(4, 6, 2, 33.1, 130021, '30/03/2021', 'Transferencia', 0);
INSERT INTO PAGOS VALUES(5, 4, 3, 664, 140021, '30/03/2021', 'Transferencia', 1);
INSERT INTO PAGOS VALUES(6, 6, 3, 998.7, 150021, '14/02/2021', 'Domiciliado', 1);
INSERT INTO PAGOS VALUES(7, 6, 1, 23.3, 160021, '27/01/2021', 'Domiciliado', 0);
INSERT INTO PAGOS VALUES(8, 2, 2, 46, 170021, '15/04/2021', 'Transferencia', 0);
INSERT INTO PAGOS VALUES(9, 1, 3, 78.2, 180021, '16/01/2021', 'Transferencia', 1);
INSERT INTO PAGOS VALUES(10, 1, 3, 234.3, 190021, '22/08/2020', 'Tarjeta de cr�dito', 0);
INSERT INTO PAGOS VALUES(11, 3, 1, 43.3, 199021, '11/03/2021', 'Tarjeta de cr�dito', 1);

/*Datos COBROS*/
INSERT INTO COBROS VALUES(1, 10, 2, 74.55, 210002, '03/01/2021', 'Domiciliado', 1);
INSERT INTO COBROS VALUES(2, 12, 1, 12.44, 210005, '05/12/2020', 'Domiciliado', 1);
INSERT INTO COBROS VALUES(3, 9, 1, 362.95, 210007, '24/03/2021', 'Transferencia', 0);
INSERT INTO COBROS VALUES(4, 2, 3, 45.2, 210010, '07/11/2020', 'Transferencia', 1);
INSERT INTO COBROS VALUES(5, 3, 3, 111.24, 210009, '14/01/2021', 'Tranferencia', 0);
INSERT INTO COBROS VALUES(6, 4, 1, 345.12, 210002, '18/02/2021', 'Domiciliado', 1);
INSERT INTO COBROS VALUES(7, 6, 2, 22.12, 210042, '25/03/2021', 'Domiciliado', 0);
INSERT INTO COBROS VALUES(8, 8, 3, 33.19, 210045, '31/03/2021', 'Transferencia', 0);
INSERT INTO COBROS VALUES(9, 1, 2, 95.99, 210008, '09/10/2021', 'Transferencia', 0);
INSERT INTO COBROS VALUES(10, 8, 1, 109.95, 210012, '21/11/2020', 'Transferencia', 0);
INSERT INTO COBROS VALUES(11, 7, 2, 7.55, 210027, '07/08/2021', 'Domiciliado', 1);
INSERT INTO COBROS VALUES(12, 3, 2, 12.55, 210027, '04/08/2021', 'Domiciliado', 0);

