package mx.org.ift.bitacora.ws.rest.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;
import com.google.gson.Gson;

import mx.org.ift.bitacora.ws.rest.constantes.Constantes;
import mx.org.ift.bitacora.ws.rest.dao.impl.EventoDAOImpl;
import mx.org.ift.bitacora.ws.rest.service.EventoService;
import mx.org.ift.bitacora.ws.rest.vo.EventoVO;
import mx.org.ift.bitacora.ws.rest.vo.MessageVO;
import static mx.org.ift.bitacora.ws.rest.pool.PoolMessage.LEVENTOVO;
import static mx.org.ift.bitacora.ws.rest.pool.PoolMessage.path;
import static mx.org.ift.bitacora.ws.rest.pool.PoolMessage.bearer;

import java.util.logging.Level;
import java.util.logging.Logger;

public class EventoServiceImpl implements EventoService {
	private final static Logger LOGGER = 
			Logger.getLogger("mx.org.ift.bitacora.ws.rest.service.impl.EventoServiceImpl");
	public String insertarEvento(EventoVO evento){
		return new EventoDAOImpl().insertarEvento(evento);
	}
	
	
	public void pushObject(MessageVO evento){
		String name = path + evento.getUuid() + ".eve";
		
		try {
			File file = new File(name);
	         
	        file.setExecutable(true);
	        file.setReadable(true);
	        file.setWritable(true);
	        LOGGER.log(Level.SEVERE, "File permissions changed.");
	 
			OutputStream output = new FileOutputStream(file);
			ObjectOutputStream oos = new ObjectOutputStream(output);
            oos.writeObject(evento);
            oos.close();
            oos = null;
            output.close();
            output = null;
        } catch (IOException e) {
        	LOGGER.log(Level.SEVERE, e.getMessage());
        }
	}
	
	public EventoVO popObject(){
		EventoVO evento = null;
		try {
			if(LEVENTOVO.size()>0){
				String name = LEVENTOVO.pollFirst();
				File file = new File(path + name);
				ObjectInputStream ois = new ObjectInputStream( 
						new FileInputStream(file));
				MessageVO message = (MessageVO)ois.readObject();
				LOGGER.log(Level.INFO, name + ": " + message.getContent().length());
				
				/*int pos = json.indexOf("||");
				String js = json.substring(0, pos);
				String time = json.substring(pos+2, json.length());*/
				
	            evento = new Gson().fromJson(message.getContent(), EventoVO.class);
	            evento.setTimeEvent(message.getTime());
	    		evento.setUuid(message.getUuid());
	            ois.close();
			}
        } catch (IOException e) {
        	LOGGER.log(Level.SEVERE, e.getMessage());
        } catch (ClassNotFoundException e) {
        	LOGGER.log(Level.SEVERE, e.getMessage());
        } 
		
		return evento;
	}
	
	public Integer countEvents(){
		final File dir = new File(path + ".");
		File[] filesList = dir.listFiles();
		for (File file : filesList) {
			if (file.isFile() && file.getName().endsWith(".eve")) {
				LEVENTOVO.add(file.getName());
			}			
		}
		return LEVENTOVO.size();
	}
	
	public static void main(String ...strings ){
		/*EventoVO vo = new EventoVO();
		vo.setIdAplicacion(22l);
		new EventoServiceImpl().pushObject(vo);
		vo = new EventoVO();
		vo.setIdAplicacion(232l);
		new EventoServiceImpl().pushObject(vo);
		vo = new EventoVO();
		vo.setIdAplicacion(255l);
		new EventoServiceImpl().pushObject(vo);
		vo = new EventoVO();
		vo.setIdAplicacion(2345l);
		new EventoServiceImpl().pushObject(vo);
		int i = new EventoServiceImpl().countEvents();
		System.out.println(i);
		
		
		
		for(i=1; i<7; i++){
			EventoVO vv = new EventoServiceImpl().popObject();
			System.out.println(vv.getIdAplicacion());
	    }
		path = "C:/Temp/Eventos/";
		int i = new EventoServiceImpl().countEvents();
		
		
		
		System.out.println(i);	
		
		String content = "";
		for(String ss: LEVENTOVO){
			content += ss + " \n";
		}*/
		
		String content = UUID.randomUUID().toString() + ".eve";
		System.out.println(content);	
		int pos = content.indexOf(".");
		content = content.substring(0,pos);
		System.out.println(content);	
		
	}
	
	public String getNtpTime(){
		String time = null;
		try {
			URL url = new URL(Constantes.WS_TIME);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		    //Bearer 07bfda62-2aef-30b7-83b4-2440687a865e
			if (bearer == null || bearer.length() == 0){
				bearer = "07bfda62-2aef-30b7-83b4-2440687a865e";
			}
			conn.setRequestProperty("Authorization", "Bearer " + bearer);
			conn.setRequestMethod("GET");

            conn.setRequestProperty("Accept", "text/plain");
            conn.addRequestProperty("User-Agent", "Mozilla/4.0");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("charset", "UTF-8");
            
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));

			String output;
			while ((output = br.readLine()) != null) {
				time = output;
			}
			conn.disconnect();
		} catch (MalformedURLException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage());
		}
	
		return time;
	}
}
