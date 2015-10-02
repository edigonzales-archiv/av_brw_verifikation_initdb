package org.catais.brw.verifikation;

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
import ch.ehi.ili2db.gui.Config;
import ch.ehi.ili2pg.converter.PostgisGeometryConverter;
import ch.ehi.sqlgen.generator_impl.jdbc.GeneratorPostgresql;

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
//	boolean addAdditionalAttributes = true;
//	String grantRole = null;
	boolean addAdditionalAttributes = false;
		
	public Ili2pg(HashMap<String,String> params, java.sql.Connection conn) {
		dbhost = params.get("dbhost");		
		dbport = params.get("dbport");		
		dbdatabase = params.get("dbdatabase");		
		dbusr = params.get("dbusr");		
		dbpwd = params.get("dbpwd");		
		model = params.get("model");		
		defaultSrsAuth = params.get("defaultSrsAuth");
		defaultSrsCode = params.get("defaultSrsCode");
		
		dburl = "jdbc:postgresql://"+dbhost+":"+dbport+"/"+dbdatabase+"?user="+dbusr+"&password="+dbpwd;
		
		this.conn = conn;
	}
	
	public void initSchema(String dbschema) {
		this.dbschema = dbschema;
		
		try {
			Config config = ili2dbConfig();
			Ili2db.runSchemaImport(config, "");
			
			if (addAdditionalAttributes) {
				alterTablesWithAdditionalColumns();
			}
			
			if (grantRole != null) {
				grantTablesToPublicRole();
			}
					
		} catch (Ili2dbException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void grantTablesToPublicRole() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = null;

        try {
            con = DriverManager.getConnection(dburl);
            con.setAutoCommit(false);

			sql = "GRANT USAGE ON SCHEMA " + dbschema + " TO " + grantRole + "; \n" +
				"GRANT SELECT ON ALL TABLES IN SCHEMA " + dbschema + " TO " + grantRole + ";";    			

        	st = con.createStatement();
        	st.execute(sql);
        	
        	st.close();
    		con.commit();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            	e.printStackTrace();
            }
        }

	}
	
	private void alterTablesWithAdditionalColumns() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");

        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = null;
        
        try {
            con = DriverManager.getConnection(dburl);
            con.setAutoCommit(false);

    		ArrayList<String> tableNames = getDataTables();
    		for (String tableName: tableNames) {
    			sql = "ALTER TABLE " + dbschema + "." + tableName + " ADD COLUMN gem_bfs integer; \n" +
    				"ALTER TABLE " + dbschema + "." + tableName + " ADD COLUMN lieferdatum date;";    			

            	st = con.createStatement();
            	st.execute(sql);
            	
            	st.close();
    		}
    		con.commit();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
            	e.printStackTrace();
            }
        }
	}
	
	private ArrayList<String> getDataTables() throws ClassNotFoundException, SQLException {
		Class.forName("org.postgresql.Driver");

        ArrayList<String> tableNames = new ArrayList<String>();
		
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        String sql = null;
               
        try {
        	sql = "SELECT * FROM "+dbschema+".t_ili2db_classname;";
        	
            con = DriverManager.getConnection(dburl);
            st = con.createStatement();
            rs = st.executeQuery(sql);

            ArrayList<String> classNames = new ArrayList<String>();
            while (rs.next()) {
                classNames.add(rs.getString("sqlname"));
            }
            
            rs.close();
            st.close();
            
            for (String className: classNames) {
            	sql = "SELECT EXISTS (SELECT * FROM information_schema.tables WHERE table_schema = '"+dbschema+"' AND table_name = '"+className.toLowerCase()+"');";
            	
            	st = con.createStatement();
            	rs = st.executeQuery(sql);
            	
            	if (rs.next()) {
            		if (rs.getBoolean(1)) {
            			tableNames.add(className.toLowerCase());
            		}
            	}
            	
            	rs.close();
            	st.close();
            }           
        }  finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
                if (con != null) {
                    con.close();
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
						
		config.setSqlNull("enable"); // be less restrictive
		config.setValue("ch.ehi.sqlgen.createGeomIndex", "True");
		config.setCreateEnumCols("addTxtCol");
				
		config.setDefaultSrsAuthority(defaultSrsAuth);
		config.setDefaultSrsCode(defaultSrsCode);
		
//		EhiLogger.getInstance().setTraceFilter(false);
		config.setLogfile("init-db.log");
		
		config.setJdbcConnection2(conn);
	
		return config;
	}
}
