INSERT INTO VAGA(mensalista,ocupada,placa) values (FALSE,FALSE,NULL);
INSERT INTO vaga(mensalista,ocupada,placa) values (FALSE,FALSE,NULL);
INSERT INTO vaga(mensalista,ocupada,placa) values (FALSE,FALSE,NULL);
INSERT INTO vaga(mensalista,ocupada,placa) values (FALSE,FALSE,NULL);
INSERT INTO vaga(mensalista,ocupada,placa) values (FALSE,FALSE,NULL);
INSERT INTO vaga(mensalista,ocupada,placa) values (FALSE,FALSE,NULL);
INSERT INTO vaga(mensalista,ocupada,placa) values (FALSE,FALSE,NULL);
INSERT INTO vaga(mensalista,ocupada,placa) values (TRUE,FALSE,NULL);
INSERT INTO vaga(mensalista,ocupada,placa) values (TRUE,FALSE,NULL);
INSERT INTO vaga(mensalista,ocupada,placa) values (TRUE,FALSE,NULL);

INSERT INTO tabela_preco (preco, mensalista,apartir_de_quantas_horas, data_validade) VALUES (20.00, TRUE,NULL, '2023-11-21');
INSERT INTO tabela_preco(preco,mensalista,apartir_de_quantas_horas,data_validade) VALUES (5.00,FALSE,1,'2023-11-20')

INSERT INTO clientes_mensalistas(nome,cpf,tel) VALUES ('Bruno Gomes','08664984514','75992042744');
INSERT INTO placas_mensalistas(placa,cliente_mensalista_id) VALUES ('PKW-4545',1);
INSERT INTO placas_mensalistas(placa,cliente_mensalista_id) VALUES ('BBB-1010',1);