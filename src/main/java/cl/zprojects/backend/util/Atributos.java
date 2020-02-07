package cl.zprojects.backend.util;

public enum Atributos {
	NOMBRE(1),
	VERSION(2),
	BL(3),
	CR(4),
	GITLAB(5),
	URL(6),
	PAYLOAD(7),
	AMBIENTE(8),
	DESARROLLADOR(9);
	private Integer tipo;
	
	
	Atributos(Integer tipo){
		this.tipo = tipo;
	}
	
	public Integer tipo() {
		return tipo;
	}
}
/*
  	NOMBRE(1),
	VERSION(2),
	BL(3),
	CR(4),
	GITLAB(5),
	URL(6),
	PAYLOAD(7),
	AMBIENTE(8),
	DESARROLLADOR(9);
 
 */