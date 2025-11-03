# Sistema de Avaliação de Colaboradores - Case Técnico Itaú

## 📋 Sumário
- [Visão Geral](#visão-geral)
- [Regra de Negócio](#regra-de-negócio)
- [Arquitetura](#arquitetura)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Infraestrutura](#infraestrutura)
- [API REST](#api-rest)
- [Testes](#testes)
- [Como Executar](#como-executar)

---

## 🎯 Visão Geral

Sistema desenvolvido para gerenciar avaliações de colaboradores, contemplando aspectos comportamentais e entregas de desafios, com cálculo automático de nota final normalizada.

O projeto foi desenvolvido utilizando **Java 22**, **Spring Boot** e **Arquitetura Hexagonal (Ports & Adapters)**, garantindo alta qualidade de código, desacoplamento e testabilidade.

---

## 📊 Regra de Negócio

### Colaboradores
Cada colaborador possui:
- **Matrícula** (identificador único)
- **Nome**
- **Cargo**
- **Time**

### Avaliação Comportamental
São **4 perguntas fixas** para cada colaborador, cada uma com nota de **1 a 5**:
1. Ambiente colaborativo
2. Aprende o tempo todo
3. Usa dados para decisões
4. Entrega resultados sustentáveis

**Média Comportamental** = soma das 4 notas / 4

### Avaliação de Entregas (Desafios)
- Cada colaborador deve ter entre **2 e 4 desafios** cadastrados
- Cada desafio possui uma **descrição** e uma **pontuação de 0 a 100**
- **Média de Desafios** = soma das pontuações / quantidade de desafios

### Cálculo da Nota Final
```
Nota Final = ((Média Comportamental / 5) + (Média Desafios / 100)) / 2
```
**Resultado normalizado:** sempre entre **0 e 1**

### Listagem
O sistema permite listar todos os colaboradores com suas notas finais calculadas automaticamente.

---

## 🏗️ Arquitetura

### Arquitetura Hexagonal (Ports & Adapters)

Optei pela **Arquitetura Hexagonal** pelos seguintes motivos:

#### ✅ **Desacoplamento Total**
- A lógica de negócio (domínio) não depende de frameworks, bancos de dados ou APIs externas
- Facilita manutenção e evolução do código

#### ✅ **Testabilidade**
- Use cases isolados podem ser testados sem necessidade de infraestrutura
- Mocks e testes unitários são mais simples e rápidos

#### ✅ **Flexibilidade de Infraestrutura**
- Posso trocar o banco de dados (MySQL → PostgreSQL, MongoDB) sem alterar regras de negócio
- Posso trocar REST por GraphQL, gRPC ou mensageria sem impactar o core

#### ✅ **Separação Clara de Responsabilidades**
```
Domain (Core) → Define regras de negócio puras
Application → Orquestra casos de uso
Infrastructure → Implementa detalhes técnicos (API, BD, etc)
```

---

## 🛠️ Tecnologias Utilizadas

### Backend
- **Java 22** - Última versão LTS com recursos modernos
- **Spring Boot 3.x** - Framework principal
- **Spring Data JPA** - ORM para persistência
- **Lombok** - Redução de boilerplate (getters, setters, construtores)
- **Maven** - Gerenciamento de dependências

### Banco de Dados
- **MySQL 8.0** (Workbench)
- Hospedado no **Amazon RDS** (AWS - região sa-east-1)
- Provisionado via **Terraform** (Infrastructure as Code)

### Testes
- **JUnit 5** - Framework de testes unitários
- **Mockito** - Mocks para testes isolados
- **Spring Test** - Testes de integração
- **Insomnia** - Testes manuais de API

### DevOps & Infraestrutura
- **Terraform** - Provisionamento de infraestrutura AWS
- **GitHub Actions** - CI/CD Pipeline
- **Draw.io** - Diagramação da arquitetura

---

## 📁 Estrutura do Projeto

```
src/
├── main/java/com/itau/case_tecnico/
│   ├── domain/                    # 🧠 Camada de Domínio (Core)
│   │   ├── model/                 # Entidades de negócio
│   │   │   ├── Colaborador.java
│   │   │   ├── AvaliacaoComportamental.java
│   │   │   └── Desafio.java
│   │   └── port/                  # Interfaces (Ports)
│   │       ├── ColaboradorRepositoryPort.java
│   │       ├── AvaliacaoRepositoryPort.java
│   │       └── DesafioRepositoryPort.java
│   │
│   ├── application/               # 🎯 Camada de Aplicação
│   │   └── usecase/               # Casos de uso (lógica orquestrada)
│   │       ├── CriarColaboradorUseCase.java
│   │       ├── AvaliarComportamentoUseCase.java
│   │       ├── CriarDesafioUseCase.java
│   │       ├── AvaliarDesafioUseCase.java
│   │       ├── CalcularNotaFinalUseCase.java
│   │       ├── ListarTodosUseCase.java
│   │       ├── BuscarPorIdUseCase.java
│   │       └── ValidacaoException.java
│   │
│   └── infrastructure/            # 🔌 Camada de Infraestrutura
│       └── adapter/
│           ├── controller/        # API REST Controllers
│           │   ├── ColaboradorController.java
│           │   ├── AvaliacaoComportamentalController.java
│           │   └── DesafioController.java
│           ├── dto/               # Request/Response DTOs
│           │   ├── ColaboradorRequest.java
│           │   ├── ColaboradorResponse.java
│           │   ├── AvaliacaoComportamentalRequest.java
│           │   ├── AvaliacaoComportamentalResponse.java
│           │   ├── DesafioRequest.java
│           │   ├── DesafioResponse.java
│           │   └── NotaFinalResponse.java
│           ├── entity/            # Entidades JPA (BD)
│           │   ├── ColaboradorEntity.java
│           │   ├── AvaliacaoComportamentalEntity.java
│           │   └── DesafioEntity.java
│           └── repository/        # Implementação de Ports
│               ├── SpringDataColaboradorRepository.java
│               ├── SpringDataAvaliacaoComportamentalRepository.java
│               └── SpringDataDesafioRepository.java
│
└── test/                          # 🧪 Testes
    ├── usecase/                   # Testes unitários (Use Cases)
    └── controller/                # Testes de integração (API)
```

### Fluxo de Dados
```
Controller (REST) → Use Case → Domain Model → Port (Interface) → Repository (Adapter) → Database
```

---

## ☁️ Infraestrutura

### AWS - Amazon RDS MySQL

A infraestrutura foi provisionada utilizando **Terraform** com os seguintes recursos:

#### Recursos Criados
- **VPC** customizada (10.0.0.0/16)
- **2 Subnets** em zonas de disponibilidade diferentes (sa-east-1a e sa-east-1b)
- **Internet Gateway** e **Route Table**
- **Security Group** permitindo acesso MySQL (porta 3306)
- **DB Subnet Group** para alta disponibilidade
- **RDS MySQL 8.0** (instância db.t3.micro - Free Tier)

#### Arquivos Terraform
```
terraform/
├── main.tf           # Recursos AWS (VPC, RDS, Security Groups)
├── variables.tf      # Definição de variáveis
├── terraform.tfvars  # Valores sensíveis (gitignored)
├── outputs.tf        # Endpoint do banco
└── terraform.tfvars.example  # Template para equipe
```

#### Segurança
- Dados sensíveis (senhas, credenciais) estão no `.gitignore`
- State files do Terraform não são versionados
- Apenas `terraform.tfvars.example` está no repositório

---

## 🌐 API REST

### Endpoints Implementados

#### **Colaboradores**
```http
POST   /api/colaboradores           # Criar colaborador
GET    /api/colaboradores           # Listar todos
GET    /api/colaboradores/{id}      # Buscar por ID
GET    /api/colaboradores/{id}/nota-final  # Nota final calculada
```

#### **Avaliação Comportamental**
```http
POST   /api/avaliacoes              # Registrar avaliação comportamental
GET    /api/avaliacoes/{colaboradorId}  # Buscar avaliação
```

#### **Desafios**
```http
POST   /api/desafios                # Criar desafio
GET    /api/desafios/{colaboradorId}  # Listar desafios do colaborador
```

### Testes com Insomnia
Todas as APIs foram testadas utilizando **Insomnia**, validando:
- ✅ Criação de colaboradores
- ✅ Registro de avaliações comportamentais (4 notas de 1 a 5)
- ✅ Criação de desafios (2 a 4 por colaborador)
- ✅ Cálculo correto da nota final normalizada
- ✅ Listagem com nota final calculada automaticamente
- ✅ Validações de regra de negócio

---

## 🧪 Testes

### Cobertura de Testes
O projeto possui **cobertura acima de 80%**, incluindo:

#### Testes Unitários (Use Cases)
- ✅ `CriarColaboradorUseCaseTest`
- ✅ `AvaliarComportamentoUseCaseTest`
- ✅ `CriarDesafioUseCaseTest`
- ✅ `CalcularNotaFinalUseCaseTest`
- ✅ `ListarTodosUseCaseTest`
- ✅ `BuscarPorIdUseCaseTest`

#### Testes de Integração (Controllers)
- ✅ `ColaboradorControllerTest`
- ✅ `AvaliacaoComportamentalControllerTest`
- ✅ `DesafioControllerTest`

### Executar Testes
```bash
mvn test
```

---

## 📊 Diagramas

### Arquitetura do Sistema
O diagrama completo da arquitetura foi criado no **Draw.io**, ilustrando:
- Camadas hexagonais (Domain, Application, Infrastructure)
- Fluxo de dados entre camadas
- Integração com AWS RDS
- Estrutura de APIs REST

---

## 👨‍💻 Autor

**Geovane**  
Case Técnico - Itaú Unibanco

---

## 📄 Licença

Este projeto foi desenvolvido para fins de avaliação técnica.