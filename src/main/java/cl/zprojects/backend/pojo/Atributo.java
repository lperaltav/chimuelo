package cl.zprojects.backend.pojo;

public class Atributo {

	private Integer id;
	private String nombre;
	private String valor;

	public Atributo() {
		super();
	}
	
	public Atributo(Integer id, String valor) {
		super();
		this.id = id;
		this.valor = valor;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

}