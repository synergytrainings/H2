package com.synisys.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


public class Main {

	/**
	 * @param args
	 * @throws SQLException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws Exception {
		try(Connection connection = DriverManager.getConnection("jdbc:h2:tcp://localhost/mem:db1;MODE=MSSQLServer", "sa", "sa")){
//			FileReader fileReader = new FileReader("d:/tmp/h2database-samples/import.txt");
			
			String queries = new String(Files.readAllBytes(Paths.get("d:/tmp/h2database-samples/import.txt")));
			try(Statement statement = connection.createStatement()){
				for(String query : queries.split("\r\nGO\r\n")){
					statement.executeUpdate(query);
				}	
			}
			
			//Files.readAllLines(Paths.get("d:/tmp/h2database-samples/import.txt"), Charset.forName("ANSII"));
		}
	}

}
