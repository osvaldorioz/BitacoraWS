package mx.org.ift.bitacora.ws.rest.connector;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import static mx.org.ift.bitacora.ws.rest.pool.PoolMessage.LEVENTOVO;

public class Connector {
	private final static Logger LOGGER = 
			Logger.getLogger("mx.org.ift.bitacora.ws.rest.connector.Connector");
	
	private Connection cn;
	
	public Connector(){
		LOGGER.log(Level.INFO, "Iniciando conexion...");
		try {
			Hashtable environment = new Hashtable();
			environment.put("java.naming.factory.initial",
			                    "org.wso2.carbon.tomcat.jndi.CarbonJavaURLContextFactory");
			
			Context initContext = new InitialContext(environment);
            DataSource ds = (DataSource) initContext.lookup("jdbc/bitacoraift");
            cn = ds.getConnection();
            LOGGER.log(Level.INFO, "conectado!!!"); 
        } catch (NamingException e) {
        	LOGGER.log(Level.SEVERE, e.getMessage());
        } catch (SQLException e) {
        	LOGGER.log(Level.SEVERE, e.getMessage());
        }
		
	}
	public Connection getConnection(){
		return cn;
	}
}
