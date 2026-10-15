# Sistema de Controle de EPIs - Backend

Requisitos: JDK 17+, Maven, MySQL 8.

1. Edite `src/main/resources/application.properties` (usuario/senha do MySQL).
2. Rode: `mvn spring-boot:run`
3. Login inicial: admin / admin123 (HTTP Basic). Troque a senha.
4. Acesso na rede: http://IP-DO-SERVIDOR:8080 (libere a porta 8080 no firewall).

Endpoints (todos exigem login; /api/usuarios e DELETE exigem ADMIN):
- GET/POST /api/colaboradores | GET/PUT/DELETE /api/colaboradores/{id}
- GET/POST /api/epis | GET/PUT/DELETE /api/epis/{id}
- GET/POST /api/categorias | PUT/DELETE /api/categorias/{id}
- GET/POST /api/usuarios | PUT/DELETE /api/usuarios/{id}
- POST /api/emprestimos | GET /api/emprestimos | GET /api/emprestimos/{id}
- GET /api/itens/pendentes | PUT /api/itens/{id}/devolucao
- GET /api/colaboradores/{id}/epis-em-uso

Exemplo de nova entrega (POST /api/emprestimos):
{ "idColaborador": 1, "observacao": "Obra A",
  "itens": [ { "idEpi": 1, "quantidade": 1, "dataPrevistaDevolucao": "2026-11-01" } ] }
