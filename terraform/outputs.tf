output "rds_endpoint" {
  description = "Endpoint do RDS"
  value       = aws_db_instance.mysql_db.endpoint
}

output "rds_username" {
  description = "Usuário do banco"
  value       = aws_db_instance.mysql_db.username
}
