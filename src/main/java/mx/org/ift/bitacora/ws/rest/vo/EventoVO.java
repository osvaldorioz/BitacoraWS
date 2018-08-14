package mx.org.ift.bitacora.ws.rest.vo;

import java.io.Serializable;
import java.util.Map;

public class EventoVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5903107980732133995L;
	private String ip;
	private String username;
	private Long   idAplicacion;
	private Long   idTipoEvento;
	private Long   rowAffected;
	private String modulename;
	private String tablename;
	private String timeEvent;
	private Map    campos;
	private String uuid; 
	
	/**
	 * @return the rowaffected
	 */
	public Long getRowAffected() {
		return rowAffected;
	}
	/**
	 * @param rowaffected the rowaffected to set
	 */
	public void setRowAffected(Long rowAffected) {
		this.rowAffected = rowAffected;
	}
	/**
	 * @return the modulename
	 */
	public String getModulename() {
		return modulename;
	}
	/**
	 * @param modulename the modulename to set
	 */
	public void setModulename(String modulename) {
		this.modulename = modulename;
	}
	/**
	 * @return the tablename
	 */
	public String getTablename() {
		return tablename;
	}
	/**
	 * @param tablename the tablename to set
	 */
	public void setTablename(String tablename) {
		this.tablename = tablename;
	}
	
	
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	/**
	 * @return the idAplicacion
	 */
	public Long getIdAplicacion() {
		return idAplicacion;
	}
	/**
	 * @param idAplicacion the idAplicacion to set
	 */
	public void setIdAplicacion(Long idAplicacion) {
		this.idAplicacion = idAplicacion;
	}
	/**
	 * @return the idTipoEvento
	 */
	public Long getIdTipoEvento() {
		return idTipoEvento;
	}
	/**
	 * @param idTipoEvento the idTipoEvento to set
	 */
	public void setIdTipoEvento(Long idTipoEvento) {
		this.idTipoEvento = idTipoEvento;
	}
	/**
	 * @return the campos
	 */
	public Map getCampos() {
		return campos;
	}
	/**
	 * @param campos the campos to set
	 */
	public void setCampos(Map campos) {
		this.campos = campos;
	}
	/**
	 * @return the timeEvent
	 */
	public String getTimeEvent() {
		return timeEvent;
	}
	/**
	 * @param timeEvent the timeEvent to set
	 */
	public void setTimeEvent(String timeEvent) {
		this.timeEvent = timeEvent;
	}
	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}
	/**
	 * @param uuid the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
}
