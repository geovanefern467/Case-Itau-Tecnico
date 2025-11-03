variable "db_allocated_storage" {
  description = "Espaço em disco do banco"
  type        = number
}

variable "db_instance_class" {
  description = "Tipo de instância do RDS"
  type        = string
}

variable "db_name" {
  description = "Nome do banco de dados"
  type        = string
}

variable "db_username" {
  description = "Usuário administrador"
  type        = string
}

variable "db_password" {
  description = "Senha do administrador"
  type        = string
  sensitive   = true
}
