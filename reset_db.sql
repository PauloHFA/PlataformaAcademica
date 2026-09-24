SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE datname = 'plataforma_academica' AND pid <> pg_backend_pid();
DROP DATABASE IF EXISTS plataforma_academica;
CREATE DATABASE plataforma_academica;
