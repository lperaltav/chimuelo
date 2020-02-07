package cl.zprojects.backend.pojo;

import java.util.List;

public class Componente {

	private Tipo tipo;
	private Relacion relacion;
	private Integer id;
	private Integer idRelacion;
	private String nombre;
	private List<Atributo> atributos;
	private List<Componente> hijos;
	private List<Ambiente> ambientes;
	private List<Desarrollador> desarrolladores;
	private String version;
	
	public Tipo getTipo() {
		return tipo;
	}

	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}

	public Relacion getRelacion() {
		return relacion;
	}

	public void setRelacion(Relacion relacion) {
		this.relacion = relacion;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdRelacion() {
		return idRelacion;
	}

	public void setIdRelacion(Integer idRelacion) {
		this.idRelacion = idRelacion;
	}
	public String getNombre() {
		return this.nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Atributo> getAtributos() {
		return atributos;
	}

	public void setAtributos(List<Atributo> atributos) {
		this.atributos = atributos;
	}

	public List<Componente> getHijos() {
		return hijos;
	}

	public void setHijos(List<Componente> hijos) {
		this.hijos = hijos;
	}

	public List<Ambiente> getAmbientes() {
		return ambientes;
	}

	public void setAmbientes(List<Ambiente> ambientes) {
		this.ambientes = ambientes;
	}

	public List<Desarrollador> getDesarrolladores() {
		return desarrolladores;
	}

	public void setDesarrolladores(List<Desarrollador> desarrolladores) {
		this.desarrolladores = desarrolladores;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
	

}
