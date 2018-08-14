package mx.org.ift.bitacora.ws.rest.dao.impl;

import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import mx.org.ift.bitacora.ws.rest.dao.EventoDAO;
import mx.org.ift.bitacora.ws.rest.pool.PoolMessage;
import mx.org.ift.bitacora.ws.rest.vo.EventoVO;
import mx.org.ift.bitacora.ws.rest.connector.Connector;
import static mx.org.ift.bitacora.ws.rest.constantes.Constantes.EVENT_GEN;
import static mx.org.ift.bitacora.ws.rest.constantes.Constantes.EVENT_SAL;
import static mx.org.ift.bitacora.ws.rest.constantes.Constantes.EVENT_ING;

public class EventoDAOImpl implements EventoDAO{
	private final static Logger LOGGER = 
			Logger.getLogger("mx.org.ift.bitacora.ws.rest.dao.impl.EventoDAOImpl");
	private Connector cn = new Connector();
	
	public String insertarEvento(EventoVO evento){
		String msg = null;
		Connection con = cn.getConnection();
		
		Long idBitacora = obtenerBitacora(evento.getIp(), evento.getUsername(), con);
		
		if(idBitacora==null){
			idBitacora = this.insertarBitacora(evento.getIp(), evento.getUsername(), con);
		}
		
		if(idBitacora != null){
			Long idEvento = insertar(idBitacora, evento.getIdAplicacion(), 
					evento.getIdTipoEvento(), evento.getUuid(), con);
			
			if(idEvento != null){
				
				if(evento.getIdTipoEvento().equals(INGRESO)){
					msg = EVENT_ING;
				} else if(evento.getIdTipoEvento().equals(SALIDA)){
					msg = EVENT_SAL;
				} else {
					String json = map2JSON(evento);
						
					try{
						Clob clob = con.createClob();
						clob.setString(1, json);
						
						PreparedStatement ps = con.prepareStatement(INSERTAR_REGISTRO_EVENTO);
						ps.setLong(1, idEvento);
						ps.setClob(2, clob);
						ps.executeUpdate();
						ps.close();
					    
						msg = EVENT_GEN;
					}catch(SQLException err){
						LOGGER.log(Level.SEVERE,  evento.getUuid() + ": " + 
								err.getMessage());
					}
				}
			}
		} else {
			msg = "El usuario no está registrado en el sistema";
		}
		
		try{
			con.close();
		}catch(SQLException err){
			LOGGER.log(Level.SEVERE, err.getMessage());
			
		}finally{
		
			con = null;
		}
		return msg;
	}
	
	public Long obtenerBitacora(String ip, String username, Connection con){
		Long idBitacora = null;
		
		System.out.println(OBTENER_BITACORA);
		try{
			PreparedStatement ps = con.prepareStatement(OBTENER_BITACORA);			
			ps.setString(1, ip);
			ps.setString(2, username);
			ResultSet rs = ps.executeQuery();
			
			if(rs.next()){
				idBitacora = rs.getLong("id_bit_usuarios");
			}
			rs.close();
			ps.close();
			
		}catch(SQLException err){
			LOGGER.log(Level.SEVERE, err.getMessage());
		}
		return idBitacora;
	}
	
	public Long insertarBitacora(String ip, String username, Connection con){
		String msg = "";
		Long idBitacora = null;
		
		//System.out.println(INSERTAR_BITACORA);
		try{
			String generatedColumns[] = { "id_bit_usuarios" };
			PreparedStatement ps = con.prepareStatement(INSERTAR_BITACORA,
					generatedColumns);
			ps.setString(1, ip);
			ps.setString(2, username);
			ps.executeUpdate();
			
			ResultSet rs = ps.getGeneratedKeys(); //obtengo las ultimas llaves generadas
			if(rs.next()){ 
				idBitacora = rs.getLong(1);				
			}
			
			ps.close();
			msg = "Datos guardados";
		}catch(SQLException err){
			LOGGER.log(Level.SEVERE, err.getMessage());
		}
		
		return idBitacora;
	}
	
	public Long insertar(Long idBitacora, Long idSistema, Long idTipoEvento, String uuid, Connection con){
		Long idEvento = null;
		
		//System.out.println(INSERTAR_EVENTO);
		try{
			String generatedColumns[] = { "id_bit_eventos" };
			PreparedStatement ps = con.prepareStatement(INSERTAR_EVENTO,
					generatedColumns);
			ps.setLong(1, idBitacora);
			ps.setLong(2, idSistema);
			ps.setLong(3, idTipoEvento);
			ps.setString(4, uuid);
			
			ps.executeUpdate();
			
			ResultSet rs= ps.getGeneratedKeys(); //obtengo las ultimas llaves generadas
			if(rs.next()){ 
				idEvento = rs.getLong(1);				;
			}
			
			ps.close();
		}catch(SQLException err){
			LOGGER.log(Level.SEVERE, uuid + ": " + err.getMessage());
		}
		return idEvento;
	}
	
	public String map2JSON(EventoVO evento){
	    
		Map<String, Object> inner = evento.getCampos();
		
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put( "servername", PoolMessage.servername );
	    map.put( "timeevent", evento.getTimeEvent() );
	    map.put( "tablename", evento.getTablename() );
	    map.put( "modulename", evento.getModulename() );
	    map.put( "rowaffected", evento.getRowAffected() );
	    map.put( "map", inner );
	    
	    JSONObject json = new JSONObject(map);
	    
	    //System.out.printf( " %s", json.toString() );
	    return json.toString();
	}
	
	public static void main(String ... a){
		EventoVO vo = new EventoVO();
		vo.setIdAplicacion(1l);
		vo.setIdTipoEvento(31l);
		vo.setIp("192.168.1.22");
		vo.setModulename("Modulo 1");
		vo.setRowAffected(2345l);
		vo.setTablename("Table 1");
		vo.setUsername("dgticexterno.140");
		
		HashMap<String, Object> m = new HashMap<String, Object>();
		m.put("campo 1", 2343);
		m.put("campo 2", "Alpha");
		m.put("campo 3", "07/07/2010");
		m.put("campo 4", "A");
		
		vo.setCampos(m);
		for(int i=0; i < 10; i++){
		Long milis = System.currentTimeMillis();
		String uuid = UUID.randomUUID().toString();
		String last = uuid.substring(23,uuid.length());
		Boolean rand = new Random().nextBoolean();
		if(rand){
			last = last + "a";
		} else {
			last = last + "f";
		}
		uuid = uuid.substring(0,9) + milis + last;
		
		System.out.println(uuid + " " +uuid.length());
		}
		//new EventoDAOImpl().map2JSON(vo);
	}
}
