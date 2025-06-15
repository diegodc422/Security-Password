Password Security API
API desenvolvida em Java com Spring Boot para cadastro de usuários com validação de senhas seguras e armazenamento com criptografia.
Funcionalidades
- Cadastro de usuário com validação de senha forte
- Criptografia de senha com PasswordEncoder
- Listagem de usuários cadastrados (visando ambiente de desenvolvimento)
- Verificação de login único (não duplicado)
Regras de Validação da Senha
- Mínimo de 8 caracteres
- Pelo menos 3 letras maiúsculas
- Pelo menos 2 letras minúsculas
- Pelo menos 1 número
- Pelo menos 1 caractere especial (ex: !@#$%&*)
Fluxo da Aplicação
1. Usuário faz requisição POST /registrar
2. Controller delega para o PasswordService
3. Service valida login e senha
4. Senha é criptografada
5. Dados são persistidos no banco
6. Resposta é retornada com sucesso ou lista de erros
Como Executar Localmente
Pré-requisitos:
- Java 17+
- Maven
- Docker
- Postman ou similar

Rodando com banco MySQL
git clone https://github.com/diegodc422/Security-Password.git
cd password-security-api
mvn spring-boot:run
Endpoints
POST   /registrar     → Cadastra um novo usuário
GET    /usuarios      → Lista todos os usuários
Exemplo de Requisição - POST /registrar
{
  "login": "usuario01",
  "password": "SEnhaSegura#123"
}
Segurança
- As senhas não são armazenadas em texto plano.
- Foi utilizado PasswordEncoder (implementação padrão do Spring Security).
Autor
Desenvolvido por Diego Carvalho
LinkedIn: https://www.linkedin.com/in/diego-carvalho-a76b9b10b/
