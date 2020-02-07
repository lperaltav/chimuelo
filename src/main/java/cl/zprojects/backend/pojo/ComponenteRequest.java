package cl.zprojects.backend.pojo;

import java.util.List;

public class ComponenteRequest {
	private String nombre;
	private String version;
	private String urlPing;
	private List<Ambiente> ambientes;
	private Tipo tipo;
	private List<BlCr> cr;
	private List<BlCr> bl;
	private Integer relacionId;
	private List<Desarrollador> desarrolladores;
	private String payload;
	private Integer relacionTipo;
	private String urlGitLab;
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getUrlPing() {
		return urlPing;
	}
	public void setUrlPing(String urlPing) {
		this.urlPing = urlPing;
	}
	public List<Ambiente> getAmbientes() {
		return ambientes;
	}
	public void setAmbientes(List<Ambiente> ambientes) {
		this.ambientes = ambientes;
	}
	public Tipo getTipo() {
		return tipo;
	}
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
	public List<BlCr> getCr() {
		return cr;
	}
	public void setCr(List<BlCr> cr) {
		this.cr = cr;
	}
	public List<BlCr> getBl() {
		return bl;
	}
	public void setBl(List<BlCr> bl) {
		this.bl = bl;
	}
	public Integer getRelacionId() {
		return relacionId;
	}
	public void setRelacionId(Integer relacionId) {
		this.relacionId = relacionId;
	}
	public List<Desarrollador> getDesarrolladores() {
		return desarrolladores;
	}
	public void setDesarrolladores(List<Desarrollador> desarrolladores) {
		this.desarrolladores = desarrolladores;
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public Integer getRelacionTipo() {
		return relacionTipo;
	}
	public void setRelacionTipo(Integer relacionTipo) {
		this.relacionTipo = relacionTipo;
	}
	public String getUrlGitLab() {
		return urlGitLab;
	}
	public void setUrlGitLab(String urlGitLab) {
		this.urlGitLab = urlGitLab;
	}
	
	
	
} 