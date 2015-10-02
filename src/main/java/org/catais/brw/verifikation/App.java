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

public class App {

	//--dbhost localhost --dbport 5432 --dbdatabase xanadu --dbusr stefan --dbpwd ziegler12 --defaultSrsAuth EPSG --defaultSrsCode 2056 --model DM01AVSO24LV95 --dbschema av_brw_lv95_trans
	public static void main(String[] args) {
		
		HashMap<String,String> params = new HashMap();
		
		Options options = new Options();
		options.addOption(null, "dbhost", true, "The host name of the server. Defaults to localhost.");
		options.addOption(null, "dbport", true, "The port number the server is listening on. Defaults to 5432.");
		options.addOption(null, "dbdatabase", true, "The database name.");
		options.addOption(null, "dbusr", true, "User name to access database.");
		options.addOption(null, "dbpwd", true, "Password of user used to access database.");
		options.addOption(null, "defaultSrsAuth", true, "Default SRS authority EPSG.");
		options.addOption(null, "defaultSrsCode ", true, "Default SRS code 2056.");
		options.addOption(null, "model", true, "Name of ili-model to generate an db schema for.");
		options.addOption(null, "dbschema", true, "The name of the (root) schema in the database. '_trans' and '_verikation' will be created too. Defaults to not set.");
		
		try {
			// Parse the arguments and throw exception w/ help 
			// if some of the mandatory options are missing.
			CommandLineParser parser = new DefaultParser();
			CommandLine cmd = parser.parse(options, args);
		
			String dbhost = cmd.getOptionValue("dbhost", "localhost");
			params.put("dbhost", dbhost);
			
			String dbport = cmd.getOptionValue("dbport", "5432");
			params.put("dbport", dbport);

			String dbdatabase = cmd.getOptionValue("dbdatabase");
			if (dbdatabase == null) {
				throw new MissingOptionException("dbdatabase");
			}
			params.put("dbdatabase", dbdatabase);

			String dbusr = cmd.getOptionValue("dbusr");
			if (dbusr == null) {
				throw new MissingOptionException("dbusr");
			}
			params.put("dbusr", dbusr);

			String dbpwd = cmd.getOptionValue("dbpwd");
			if (dbpwd == null) {
				throw new MissingOptionException("dbpwd");
			}
			params.put("dbpwd", dbpwd);
				
			String defaultSrsAuth = cmd.getOptionValue("defaultSrsAuth", "EPSG");
			params.put("defaultSrsAuth", defaultSrsAuth);
			
			String defaultSrsCode = cmd.getOptionValue("defaultSrsCode", "2056");
			params.put("defaultSrsCode", defaultSrsCode);
			
			String model = cmd.getOptionValue("model");
			if (model == null) {
				throw new MissingOptionException("model");
			}
			params.put("model", model);
			
			String dbschema = cmd.getOptionValue("dbschema");
			if (dbschema == null) {
				throw new MissingOptionException("dbschema");
			}
			
			// We create a single jdbc connection since we want all the work 
			// within one transaction.
			String dburl = "jdbc:postgresql://"+dbhost+":"+dbport+"/"+dbdatabase;
			Class.forName("org.postgresql.Driver");
	        Connection conn = DriverManager.getConnection(dburl, dbusr, dbpwd);
            conn.setAutoCommit(false);

			// Create the two interlis schemas.
			Ili2pg ili2pg = new Ili2pg(params, conn);
			ili2pg.initSchema(dbschema);
//			ili2pg.initSchema(dbschema + "_trans");
			
			conn.commit();

			// Initialize the schema for the verification tables.
//			PostgresqlDatabase PgObj = new PostgresqlDatabase(params);
//			PgObj.initSchema(dbschema + "_verikation");
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (MissingOptionException e) {
			e.printStackTrace();
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("brw-initdb.jar", options);
		} catch (ParseException e) {
			e.printStackTrace();
		}
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		System.out.println("Hallo Stefan.");

	}

}
