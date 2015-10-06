package org.catais.brw.verifikation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.ili2db.base.Ili2db;
import ch.ehi.ili2db.base.Ili2dbException;
import ch.ehi.ili2db.extended.Ili2dbTransactional;
import ch.ehi.ili2db.gui.Config;
import ch.ehi.ili2pg.converter.PostgisGeometryConverter;
import ch.ehi.sqlgen.generator_impl.jdbc.GeneratorPostgresql;
import ch.ehi.sqlgen.generator.SqlConfiguration;

public class Ili2pg {

	String dbhost = null;
	String dbport = null;
	String dbdatabase = null;
	String dbusr = null;
	String dbpwd = null;
	String dbschema = null;
	String dburl = null; 
	
	String model = null;	
	String modeldir = "http://www.catais.org/models";
	
	String defaultSrsAuth = null;
	String defaultSrsCode = null;
	
	java.sql.Connection conn = null;
	
	String grantRole = "mspublic";
	boolean addAdditionalAttributes = true;
//	String grantRole = null;
//	boolean addAdditionalAttributes = false;
		
	public Ili2pg(HashMap<String,String> params, java.sql.Connection conn) {
		dbhost = params.get("dbhost");		
		dbport = params.get("dbport");		
		dbdatabase = params.get("dbdatabase");		
		dbusr = params.get("dbusr");		
		dbpwd = params.get("dbpwd");		
		model = params.get("model");		
		defaultSrsAuth = params.get("defaultSrsAuth");
		defaultSrsCode = params.get("defaultSrsCode");
		
		dburl = "jdbc:postgresql://"+dbhost+":"+dbport+"/"+dbdatabase;
		
		this.conn = conn;
	}
	
	public void initItfSchema(String dbschema) throws Ili2dbException, SQLException {
		this.dbschema = dbschema;
		
		Config config = ili2dbConfig();
		Ili2dbTransactional.runSchemaImport(config, "", conn);

		if (addAdditionalAttributes) {
			alterTablesWithAdditionalColumns();
		}

		if (grantRole != null) {
			grantTablesToPublicRole();
		}
	}
	
	public void initDataSchema(String dbschema) throws IOException {
		InputStream is = null;
		BufferedReader reader = null;
		String line = null;
		StringBuilder sql = new StringBuilder();
		
		// Create schema.
		sql.append("CREATE SCHEMA " + dbschema + "; \n");
		
		// Set search_path.
		sql.append("SET search_path TO " + dbschema + "; \n");
		
		// Get create table statements from the sql text file.
		is =  Ili2pg.class.getResourceAsStream("tables.sql");
        reader = new BufferedReader(new InputStreamReader(is));
        while ((line = reader.readLine()) != null) {
            sql.append(line);
        }
        reader.close();
        
        
        System.out.println(sql.toString());    
        
        // grant permissions
	}
	
	private void grantTablesToPublicRole() throws SQLException {
        Statement st = null;
        String sql = null;
        
        sql = "GRANT USAGE ON SCHEMA " + dbschema + " TO " + grantRole + "; \n" +
        		"GRANT SELECT ON ALL TABLES IN SCHEMA " + dbschema + " TO " + grantRole + ";";    			
        try {
        st = conn.createStatement();
    	st.execute(sql);
        	
    	st.close();
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
            	e.printStackTrace();
            }
        }
	}
	
	private void alterTablesWithAdditionalColumns() throws SQLException {

		ArrayList<String> tableNames = getDataTables();

		Statement st = null;
		ResultSet rs = null;
		String sql = null;

		try {
			for (String tableName: tableNames) {
				sql = "ALTER TABLE " + dbschema + "." + tableName + " ADD COLUMN gem_bfs integer; \n" +
						"ALTER TABLE " + dbschema + "." + tableName + " ADD COLUMN lieferdatum date;";    			

				st = conn.createStatement();
				st.execute(sql);
			}
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
			} catch (SQLException e) {
            	e.printStackTrace();
            }
		}
   	}
	
	private ArrayList<String> getDataTables() throws SQLException {
        ArrayList<String> tableNames = new ArrayList<String>();
        ArrayList<String> classNames = new ArrayList<String>();

        Statement st = null;
        ResultSet rs = null;
        String sql = null;
        
        sql = "SELECT * FROM "+dbschema+".t_ili2db_classname;";
       			
        try {
        	st = conn.createStatement();
            rs = st.executeQuery(sql);
        	
            while (rs.next()) {
                classNames.add(rs.getString("sqlname"));
            }

        } finally {
            try {
            	if (rs != null) {
            		rs.close();
            	}
            	if (st != null) {
            		st.close();
            	}
            } catch (SQLException e) {
            	e.printStackTrace();
            }
        }
        
        try {
        	for (String className: classNames) {
            	sql = "SELECT EXISTS (SELECT * FROM information_schema.tables WHERE table_schema = '"+dbschema+"' AND table_name = '"+className.toLowerCase()+"');";
            	
            	st = conn.createStatement();
            	rs = st.executeQuery(sql);
            	
            	if (rs.next()) {
            		if (rs.getBoolean(1)) {
            			tableNames.add(className.toLowerCase());
            		}
            	}
        	}
        } finally {
            try {
            	if (rs != null) {
            		rs.close();
            	}
            	if (st != null) {
            		st.close();
            	}
            } catch (SQLException e) {
            	e.printStackTrace();
            }
        }
        return tableNames;
	}
	
	private Config ili2dbConfig() {
		Config config = new Config();
		config.setDbhost(dbhost);
		config.setDbdatabase(dbdatabase);
		config.setDbport(dbport);
		config.setDbusr(dbusr);
		config.setDbpwd(dbpwd);
		config.setDbschema(dbschema);
		config.setDburl(dburl);
		
		config.setModels(model);
		config.setModeldir(modeldir);
		
		config.setGeometryConverter(PostgisGeometryConverter.class.getName());
		config.setDdlGenerator(GeneratorPostgresql.class.getName());
		config.setJdbcDriver("org.postgresql.Driver");

		config.setNameOptimization("topic");
		config.setMaxSqlNameLength("60");
		config.setStrokeArcs("enable");
						
		config.setSqlNull("enable"); 
//		config.setValue("ch.ehi.sqlgen.createGeomIndex", "True");
		config.setValue(SqlConfiguration.CREATE_GEOM_INDEX, "True"); // does not work?!
		config.setCreateFkIdx("yes");
				
		config.setDefaultSrsAuthority(defaultSrsAuth);
		config.setDefaultSrsCode(defaultSrsCode);
		
//		EhiLogger.getInstance().setTraceFilter(false);
//		config.setLogfile("brw-verifikation.log");
			
		return config;
	}
}
