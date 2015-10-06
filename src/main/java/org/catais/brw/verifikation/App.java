package org.catais.brw.verifikation;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import ch.ehi.ili2db.base.Ili2dbException;

public class App {

	//--initdb  --import 2549 --delete 2601 --itf_nf /home/stefan/fubar.itf --itf_ig /home/stefan/fubar.itf
	public static void main(String[] args) {
		
		HashMap<String,String> params = new HashMap();
		
		Options options = new Options();
		options.addOption(null, "initdb", false, "Create empty database schemas.");		
		options.addOption(null, "import", true, "Import interlis files into database and set gem_bfs attribute.");		
		options.addOption(null, "itf_nf", true, "Interlis file from Nachführungsgeometer.");		
		options.addOption(null, "itf_ig", true, "Interlis file from Infogrips.");		
		options.addOption(null, "delete", true, "Delete data from database with specific gem_bfs attribute value.");		
		options.addOption(null, "dbhost", true, "The host name of the server. Defaults to localhost.");
		options.addOption(null, "dbport", true, "The port number the server is listening on. Defaults to 5432.");
		options.addOption(null, "dbdatabase", true, "The database name. Defaults to xanadu.");
		options.addOption(null, "dbusr", true, "User name to access database. Defaults to stefan.");
		options.addOption(null, "dbpwd", true, "Password of user used to access database. Defaults to ziegler12.");
		options.addOption(null, "defaultSrsAuth", true, "Default SRS authority EPSG.");
		options.addOption(null, "defaultSrsCode ", true, "Default SRS code 2056.");
		options.addOption(null, "model", true, "Name of ili-model to generate an db schema for. Defaults to DM01AVSO24LV95.");
		options.addOption(null, "dbschema", true, "The name of the (root) schema in the database. '_trans' and '_verikation' will be created too. Defaults to av_brw_lv95.");
		
		
		try {
			// Parse the arguments and throw exception w/ help 
			// if some of the mandatory options are missing.
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(options, args);

			boolean initdb = cmd.hasOption("initdb");
			 
			boolean importFile = cmd.hasOption("import");
			String itf_nf = null;
			String itf_ig = null;
			if (importFile) {
				if (cmd.getOptionValue("import") == null) {
					throw new MissingOptionException("import");
				} else {
					params.put("gem_bfs", cmd.getOptionValue("import"));
					
					itf_nf = cmd.getOptionValue("itf_nf");
					if (itf_nf == null) {
						throw new MissingOptionException("itf_nf");
					} else {
						params.put("itf_nf", itf_nf);
					}
					
					itf_ig = cmd.getOptionValue("itf_ig");
					if (itf_ig == null) {
						throw new MissingOptionException("itf_ig");
					} else {
						params.put("itf_ig", itf_ig);
					}
				}
			}
			
			boolean deleteData = cmd.hasOption("delete");
			if (deleteData) {
				if (cmd.getOptionValue("delete") == null) {
					throw new MissingOptionException("delete");
				} else {
					params.put("gem_bfs", cmd.getOptionValue("delete"));
				}	 
			}
			
			String dbhost = cmd.getOptionValue("dbhost", "localhost");
			params.put("dbhost", dbhost);
			
			String dbport = cmd.getOptionValue("dbport", "5432");
			params.put("dbport", dbport);

			String dbdatabase = cmd.getOptionValue("dbdatabase", "xanadu");
			params.put("dbdatabase", dbdatabase);

			String dbusr = cmd.getOptionValue("dbusr", "stefan");
			params.put("dbusr", dbusr);

			String dbpwd = cmd.getOptionValue("dbpwd", "ziegler12");
			params.put("dbpwd", dbpwd);
				
			String defaultSrsAuth = cmd.getOptionValue("defaultSrsAuth", "EPSG");
			params.put("defaultSrsAuth", defaultSrsAuth);
			
			String defaultSrsCode = cmd.getOptionValue("defaultSrsCode", "2056");
			params.put("defaultSrsCode", defaultSrsCode);
			
			String model = cmd.getOptionValue("model", "DM01AVSO24LV95");
			params.put("model", model);
			
			String dbschema = cmd.getOptionValue("dbschema", "av_brw_lv95");
			params.put("dbschema", dbschema);

			// We create a single jdbc connection since we want all the work 
			// within one transaction.
			String dburl = "jdbc:postgresql://"+dbhost+":"+dbport+"/"+dbdatabase;
			Class.forName("org.postgresql.Driver");
	        Connection conn = DriverManager.getConnection(dburl, dbusr, dbpwd);
            conn.setAutoCommit(false);

			// Initialize database (create empty schemas).
            if (initdb) {
    			Ili2pg ili2pg = new Ili2pg(params, conn);
//    			ili2pg.initItfSchema(dbschema);
//    			ili2pg.initItfSchema(dbschema + "_trans");
    			ili2pg.initDataSchema(dbschema + "_verifikation");
    			
    			
    			
            }
			
            
            
            // Beim Import wird zuerst immer gem_bfs gelöscht!!!!
            
			conn.commit();

			
			
		} catch (IOException e) {
			e.printStackTrace();			
//		} catch (Ili2dbException e) {
//			e.printStackTrace();			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (MissingOptionException e) {
			e.printStackTrace();
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("brw-verifikation.jar", options);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
	
		System.out.println("Hallo Stefan.");

	}

}
