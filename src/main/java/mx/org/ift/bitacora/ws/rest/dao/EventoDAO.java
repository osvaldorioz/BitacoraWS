package mx.org.ift.bitacora.ws.rest.dao;

import mx.org.ift.bitacora.ws.rest.vo.EventoVO;

public interface EventoDAO {
	Long INGRESO = 31l;
	Long SALIDA  = 32l;
	
	String INSERTAR_BITACORA = new String(
	"insert into bit_usuarios " + 
	"values(bit_usuarios_seq.nextval,?," + 
	       " (select id_usuario from usuarios where usuario = ?)" + 
	       ")"		
	);

	String OBTENER_BITACORA = new String(
	"select id_bit_usuarios" +
	"  from bit_usuarios bu" + 
    " inner join usuarios u on u.id_usuario = bu.id_usuario" +
    " where bu.ip_address = ?" +
    "   and u.usuario = ?");
	
	public static final String INSERTAR_EVENTO = new String(
	"insert into bit_eventos(id_bit_eventos,id_bit_usuarios,id_sistemas_ift,id_cat_eventos, uuid)" +
	" values(bit_eventos_seq.nextval,?,?,?,?)"		
	);
	
	public static final String INSERTAR_REGISTRO_EVENTO = new String(
	"insert into bit_registro" +
	" values(bit_registro_seq.nextval,?,?)"		
	);
	
	String insertarEvento(EventoVO evento);

}
