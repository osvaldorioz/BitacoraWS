package mx.org.ift.bitacora.ws.rest.activity;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import com.google.gson.Gson;

import mx.org.ift.bitacora.ws.rest.pool.PoolMessage;
import mx.org.ift.bitacora.ws.rest.pool.Processor;
import mx.org.ift.bitacora.ws.rest.service.impl.EventoServiceImpl;
import mx.org.ift.bitacora.ws.rest.vo.EventoVO;
import mx.org.ift.bitacora.ws.rest.vo.MessageVO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import static mx.org.ift.bitacora.ws.rest.pool.PoolMessage.path;
import static mx.org.ift.bitacora.ws.rest.pool.PoolMessage.LEVENTOVO;
import static mx.org.ift.bitacora.ws.rest.pool.PoolMessage.bearer;

@Path("/registro")
public class RegistroEvento {
	private final static Logger LOGGER = 
			Logger.getLogger("mx.org.ift.bitacora.ws.rest.activity.RegistroEvento");
	private Scheduler scheduler;
	
	@POST
	@Path("/evento")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.TEXT_PLAIN)
	public String crearEvento(String eve){
		MessageVO mm = null;
		List<String> list = new ArrayList<String>();
		StringTokenizer st2 = new StringTokenizer(eve, "||");
		while (st2.hasMoreElements()) {
			list.add((String)st2.nextElement());
		}
		
		for(String ss: list){
			int pos1 = ss.indexOf("#$");
			int pos2 = ss.indexOf("$#");
			String content = ss.substring(0, pos1);
			String fx = ss.substring(pos1 + 2, pos2);
			String uuid = ss.substring(pos2 + 2, ss.length());
			mm = new MessageVO(uuid, content, fx);
			new EventoServiceImpl().pushObject(mm);
		}
		
		return list.size() + " eventos agregados...";
	}
	
	@POST
	@Path("/espacio")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.TEXT_PLAIN)
	public String crearEspacio(String _path){
		path = _path;
		return "Nuevo Path: " + path;
	}
	
	@POST
	@Path("/bearer")
	@Produces(MediaType.TEXT_HTML)
	@Consumes(MediaType.TEXT_PLAIN)
	public String putBearer(String _bearer){
		bearer = _bearer;
		return "Bearer " + bearer;
	}
	
	@GET
	@Path("/processor/{StartStop}/{Interval}/{Server}")
	@Produces(MediaType.TEXT_HTML)
	public String startstop(@PathParam("StartStop") String ss,
			@PathParam("Interval") Integer interval,
			@PathParam("Server") String server){
		
		PoolMessage.servername = server;
		String msg = "";
		
		if(interval==null || interval < 1){
			interval = 2000;
		}
		JobDetail job = JobBuilder.newJob(Processor.class)
				.withIdentity("Processor", "group1").build();

		Trigger trigger = TriggerBuilder
				.newTrigger()
				.withIdentity("Processor", "group1")
				.withSchedule(
			SimpleScheduleBuilder.simpleSchedule().
				withIntervalInMilliseconds(interval).repeatForever())
				.build();

		try{
			if(ss.equals("start")){
				scheduler = new StdSchedulerFactory().
						getScheduler();
				scheduler.start();
				scheduler.scheduleJob(job, trigger);
				msg = "Procesador iniciado";
			} else if(ss.equals("stop")){
				if(scheduler != null && !scheduler.isShutdown()){
					scheduler.shutdown();
					msg = "Procesador detenido";
				} else {
					msg = "Procesador no está en ejecución";
				}
			}
		}catch(SchedulerException err){
			
		}
	
		return msg;
	}
	
	@GET
	@Path("/noprocesados")
	@Produces(MediaType.TEXT_HTML)
	public String howMany(){
		return "Eventos por procesar: " +  new EventoServiceImpl().countEvents();
	}
	
	@GET
	@Path("/list")
	@Produces(MediaType.TEXT_HTML)
	public String listContent(){
		String content = "";
		for(String ss: LEVENTOVO){
			content += ss + " \n";
		}
		return content;
	}
	
	public static void main(String...s){
		String eve = "{Evento: {\"rowaffected\":2345,\"modulename\":\"Modulo 1\",\"tablename\":\"Table 1\",\"map\":{\"campo 3\":\"07/07/2000\",\"campo 2\":\"Alpha\",\"campo 1\":234}}}";
		eve = "{\"idAplicacion\":1,\"idTipoEvento\":31,\"ip\":\"192.168.1.22\",\"modulename\":\"Modulo 1\",\"campos\":{\"campo 4\":\"A\",\"campo 3\":\"07/07/2010\",\"campo 2\":\"Alpha\",\"campo 1\":2343},\"tablename\":\"Table 1\",\"rowAffected\":2345,\"username\":\"dgticexterno.140\"}";
		
		System.out.println(eve);
		
		EventoVO evento = new Gson().fromJson(eve, EventoVO.class);
		System.out.println(evento.getIdAplicacion());
		System.out.println(evento.getUsername());
		System.out.println(evento.getCampos().size());
		Map<String, Object> cc = evento.getCampos();
		
		for(Map.Entry<String, Object> entry : cc.entrySet() ){
			System.out.println( entry.getKey() + " * " + entry.getValue());
		}
	}

}