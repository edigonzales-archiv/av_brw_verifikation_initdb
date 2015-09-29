package org.catais.brw.verifikation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class PostgresqlDatabase {
	
	String dbhost = null;
	String dbport = null;
	String dbdatabase = null;
	String dbusr = null;
	String dbpwd = null;
	String dbschema = null;
	String dburl = null; 
	
	String defaultSrsAuth = null;
	String defaultSrsCode = null;

	public PostgresqlDatabase(HashMap<String,String> params) {
		dbhost = params.get("dbhost");		
		dbport = params.get("dbport");		
		dbdatabase = params.get("dbdatabase");		
		dbusr = params.get("dbusr");		
		dbpwd = params.get("dbpwd");		
		defaultSrsAuth = params.get("defaultSrsAuth");
		defaultSrsCode = params.get("defaultSrsCode");
		
		dburl = "jdbc:postgresql://"+dbhost+":"+dbport+"/"+dbdatabase+"?user="+dbusr+"&password="+dbpwd;
	}
	
	public void initSchema(String dbschema) throws IOException {
		// The StringBuilder that holds all the sql stuff.
		StringBuilder sql = new StringBuilder();
		
		// Get create table statements from the sql text file.
		InputStream is =  PostgresqlDatabase.class.getResourceAsStream("tables.sql");
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null) {
            sql.append(line);
        }
        System.out.println(sql.toString());    
        reader.close();
		
	}

}
