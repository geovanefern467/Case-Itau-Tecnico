provider "aws" {
  region = "sa-east-1"
}

# VPC
resource "aws_vpc" "my_vpc" {
  cidr_block           = "10.0.0.0/16"
  enable_dns_support   = true
  enable_dns_hostnames = true
  tags = { Name = "MyVPC" }
}

# Internet Gateway
resource "aws_internet_gateway" "my_igw" {
  vpc_id = aws_vpc.my_vpc.id
  tags = { Name = "MyInternetGateway" }
}

# Route Table
resource "aws_route_table" "my_route_table" {
  vpc_id = aws_vpc.my_vpc.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.my_igw.id
  }
  tags = { Name = "MyRouteTable" }
}

# Subnet 1
resource "aws_subnet" "my_subnet_a" {
  vpc_id                  = aws_vpc.my_vpc.id
  cidr_block              = "10.0.10.0/24"
  availability_zone       = "sa-east-1a"
  map_public_ip_on_launch = true
  tags = { Name = "MySubnetA" }
}

# Subnet 2
resource "aws_subnet" "my_subnet_b" {
  vpc_id                  = aws_vpc.my_vpc.id
  cidr_block              = "10.0.20.0/24"
  availability_zone       = "sa-east-1b"
  map_public_ip_on_launch = true
  tags = { Name = "MySubnetB" }
}

# Associa as subnets à route table
resource "aws_route_table_association" "a" {
  subnet_id      = aws_subnet.my_subnet_a.id
  route_table_id = aws_route_table.my_route_table.id
}

resource "aws_route_table_association" "b" {
  subnet_id      = aws_subnet.my_subnet_b.id
  route_table_id = aws_route_table.my_route_table.id
}

# Security Group
resource "aws_security_group" "rds_sg" {
  name        = "rds_sg"
  description = "Allow MySQL access"
  vpc_id      = aws_vpc.my_vpc.id

  ingress {
    from_port   = 3306
    to_port     = 3306
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] # Acesso externo (pode restringir depois)
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

# Subnet Group para o RDS
resource "aws_db_subnet_group" "my_subnet_group_v1" {
  name       = "my-new-subnet-group"
  subnet_ids = [
    aws_subnet.my_subnet_a.id,
    aws_subnet.my_subnet_b.id
  ]
  tags = { Name = "MyNewSubnetGroup" }
}

# Instância RDS MySQL (Free Tier)
resource "aws_db_instance" "mysql_db" {
  identifier              = "mysql-case-itau"
  allocated_storage       = var.db_allocated_storage
  engine                  = "mysql"
  engine_version          = "8.0"
  instance_class          = var.db_instance_class
  db_name                 = var.db_name
  username                = var.db_username
  password                = var.db_password
  skip_final_snapshot     = true
  publicly_accessible     = false
  multi_az                = false
  vpc_security_group_ids  = [aws_security_group.rds_sg.id]
  db_subnet_group_name    = aws_db_subnet_group.my_subnet_group_v1.name
  deletion_protection     = false
  tags = { Name = "MyRDSFreeTier" }
}
