package cl.zprojects.backend.util;

public enum TipoRelacion {

	PADRE(1),
	HIJO(2);
	
	private Integer tipo;
	
	
	TipoRelacion(Integer tipo){
		this.tipo = tipo;
	}
	
	public Integer tipo() {
		return tipo;
	}
}
