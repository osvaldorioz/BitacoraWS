package mx.org.ift.bitacora.ws.rest.pool;

import static mx.org.ift.bitacora.ws.rest.pool.PoolMessage.path;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import mx.org.ift.bitacora.ws.rest.service.EventoService;
import mx.org.ift.bitacora.ws.rest.service.impl.EventoServiceImpl;
import mx.org.ift.bitacora.ws.rest.vo.EventoVO;


public class Processor implements Job{
	private final static Logger LOGGER = 
			Logger.getLogger("mx.org.ift.bitacora.ws.rest.pool.Processor");
	
	private final EventoService es = new EventoServiceImpl();
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		if(es.countEvents() > 0){
			EventoVO vo = es.popObject();
			if(vo != null && vo.getUuid() != null){
				String result = es.insertarEvento(vo);
				if (result != null &&
					result.contains("Evento")){
					File file = new File(path + vo.getUuid() + ".eve");
					file.setExecutable(true);
			        file.setReadable(true);
			        file.setWritable(true);
		            file.delete();
		            LOGGER.log(Level.INFO, "Evento procesado:" + result );
				} else {
					LOGGER.log(Level.SEVERE, "Evento con error");
				}
			} else {
				LOGGER.log(Level.SEVERE, "Evento con error");
			}
		}
	}
}
