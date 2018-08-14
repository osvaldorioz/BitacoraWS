package mx.org.ift.bitacora.ws.rest.service;

import mx.org.ift.bitacora.ws.rest.vo.EventoVO;
import mx.org.ift.bitacora.ws.rest.vo.MessageVO;


public interface EventoService {
	
	String insertarEvento(EventoVO evento);
	String getNtpTime();
	void pushObject(MessageVO evento);
	EventoVO popObject();
	Integer countEvents();
}
