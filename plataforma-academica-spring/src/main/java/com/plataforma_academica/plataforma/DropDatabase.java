package com.plataforma_academica.plataforma;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DropDatabase {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "phfa1996";

        try (Connection conn = DriverManager.getConnection(url, user, password);
                Statement stmt = conn.createStatement()) {
            stmt.execute("DROP DATABASE IF EXISTS plataforma_academica");
            stmt.execute("CREATE DATABASE plataforma_academica");
            System.out.println("Database reset successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
