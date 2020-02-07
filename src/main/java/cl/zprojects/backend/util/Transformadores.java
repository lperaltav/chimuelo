package cl.zprojects.backend.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cl.zprojects.backend.pojo.Atributo;
import cl.zprojects.backend.pojo.BlCr;
import cl.zprojects.backend.pojo.Componente;
import cl.zprojects.backend.pojo.ComponenteRequest;
import cl.zprojects.backend.pojo.Relacion;

public class Transformadores {

	Map<String, String> atributos = new HashMap<String, String>();

	public Transformadores() {
		atributos.put("nombre", "1");
		atributos.put("version", "2");
		atributos.put("bl", "3");
		atributos.put("cr", "4");
		atributos.put("urlGitLab", "5");
		atributos.put("urlPing", "6");
		atributos.put("payload", "7");
		atributos.put("ambiente", "8");
		atributos.put("desarrollador", "9");
	}

	public static Timestamp generateTimestamp(String format) {
		Timestamp timestamp = null;

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);

			Date date = dateFormat.parse(generateDate(format));

			timestamp = new Timestamp(date.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return timestamp;
	}

	public static String generateDate(String format) {
		Date date = Calendar.getInstance().getTime();

		DateFormat dateFormat = new SimpleDateFormat(format);

		return (dateFormat.format(date));
	}

	public Componente getRequest(ComponenteRequest request) {
		Componente c = new Componente();
		c.setTipo(request.getTipo());
		c.setAmbientes(request.getAmbientes());
		c.setDesarrolladores(request.getDesarrolladores());
		c.setIdRelacion(request.getRelacionId());
		c.setNombre(request.getNombre());
		c.setVersion(request.getVersion());
		Relacion r = new Relacion();
		r.setId(request.getRelacionTipo());
		c.setRelacion(r);
		c.setAtributos(obtenerAtributos(request));
		return c;
	}

	private List<Atributo> obtenerAtributos(ComponenteRequest request){
		List<Atributo> lista = new ArrayList<Atributo>();
		if(request.getNombre() != null && !request.getNombre().isEmpty()) {
			lista.add(new Atributo(Atributos.NOMBRE.tipo(), request.getNombre()));
		}
		if(request.getVersion() != null && !request.getVersion().isEmpty()) {
			lista.add(new Atributo(Atributos.VERSION.tipo(), request.getVersion()));
		}
		if(request.getNombre() != null && !request.getNombre().isEmpty()) {
			lista.add(new Atributo(Atributos.GITLAB.tipo(),request.getUrlGitLab()));
		}
		if(request.getNombre() != null && !request.getNombre().isEmpty()) {
			lista.add(new Atributo(Atributos.URL.tipo(), request.getUrlPing()));
		}
		if(request.getNombre() != null && !request.getNombre().isEmpty()) {
			lista.add(new Atributo(Atributos.PAYLOAD.tipo(), request.getPayload()));
		}
		if(request.getBl() != null && !request.getBl().isEmpty()) {
			for(BlCr bl : request.getBl()) {
				lista.add(new Atributo(Atributos.BL.tipo(), bl.getNombre()));
			}
		}
		if(request.getCr() != null && !request.getCr().isEmpty()) {
			for(BlCr cr : request.getBl()) {
				lista.add(new Atributo(Atributos.CR.tipo(), cr.getNombre()));
			}
		}
		return lista;
	}
	
}
