package cl.zprojects.backend.util;

public enum TipoComponente {

	SERVIDOR(1),
	APLICACION(2),
	SERVICIO(3);
	
	private Integer tipo;
	
	
	TipoComponente(Integer tipo){
		this.tipo = tipo;
	}
	
	public Integer tipo() {
		return tipo;
	}
}
