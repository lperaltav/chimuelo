package cl.zprojects.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.zprojects.backend.dao.IComponenteDAO;
import cl.zprojects.backend.pojo.Ambiente;
import cl.zprojects.backend.pojo.Componente;
import cl.zprojects.backend.pojo.ComponenteRequest;
import cl.zprojects.backend.pojo.Desarrollador;
import cl.zprojects.backend.pojo.Tipo;
import cl.zprojects.backend.util.TipoComponente;
import cl.zprojects.backend.util.Transformadores;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping(value = "/componente")
public class ComponenteController {

	Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IComponenteDAO componenteDAO;

	@GetMapping(path = "/getBase")
	public ResponseEntity<?> getAll() {
		List<Componente> proyectos = componenteDAO.getServidores(TipoComponente.SERVIDOR.tipo());
		if (proyectos != null && !proyectos.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(
					setResponse("200", proyectos, "Respuesta generada correctamente"), HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String, Object>>(
					setResponse("500", null, "No se ha podido obtener los datos."), HttpStatus.OK);
		}
	}

	@GetMapping(path = "/getChildrenById/{id}")
	public ResponseEntity<?> getById(@PathVariable String id) {
		if (id != null && id.matches("[0-9]+")) {
			List<Componente> proyectos = componenteDAO.getComponentesHijos(Integer.parseInt(id));
			if (proyectos != null && !proyectos.isEmpty()) {
				return new ResponseEntity<Map<String, Object>>(
						setResponse("200", proyectos, "Respuesta generada correctamente"), HttpStatus.OK);
			} else {
				return new ResponseEntity<Map<String, Object>>(
						setResponse("500", null, "No se ha podido obtener los datos."), HttpStatus.OK);
			}

		} else {
			return new ResponseEntity<Map<String, Object>>(setResponse("500", null, "Parametro ingresado incorrecto."),
					HttpStatus.OK);
		}

	}

	@PostMapping(path = "/save")
	public ResponseEntity<?> save(@RequestBody ComponenteRequest request) {
		Transformadores t = new Transformadores();
		Boolean respuesta = componenteDAO.save(t.getRequest(request));

		if (respuesta) {
			return new ResponseEntity<Map<String, Object>>(
					setResponse("200", respuesta, "Respuesta generada correctamente"), HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String, Object>>(
					setResponse("500", null, "No se ha podido obtener los datos."), HttpStatus.OK);
		}
	}
	
	@PostMapping(path = "/saveDesarrollador")
	public ResponseEntity<?> saveDesarrollador(@RequestBody Desarrollador desarrollador){
		Boolean respuesta = componenteDAO.saveDesarrollador(desarrollador);
		
		if(respuesta) {
			return new ResponseEntity<Map<String, Object>>(
					setResponse("200", respuesta, "Respuesta generada correctamente"), HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String, Object>>(
					setResponse("500", null, "No se ha podido obtener los datos."), HttpStatus.OK);
		}
	}

	@GetMapping(path = "/getTipo")
	public ResponseEntity<?> getTipo() {
		List<Tipo> tipos = componenteDAO.getTipos();
		if (tipos != null && !tipos.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(
					setResponse("200", tipos, "Respuesta generada correctamente"), HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String, Object>>(
					setResponse("500", null, "No se ha podido obtener los datos."), HttpStatus.OK);
		}
	}

	@GetMapping(path = "/getAmbiente")
	public ResponseEntity<?> getAmbiente() {
		List<Ambiente> ambientes = componenteDAO.getAmbiente(null);
		if (ambientes != null && !ambientes.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(
					setResponse("200", ambientes, "Respuesta generada correctamente"), HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String, Object>>(
					setResponse("500", null, "No se ha podido obtener los datos."), HttpStatus.OK);
		}
	}

	@GetMapping(path = "/getDesarrollador")
	public ResponseEntity<?> getDesarrollador() {
		List<Desarrollador> desarrolladores = componenteDAO.getDesarrollador(null);
		if (desarrolladores != null && !desarrolladores.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(
					setResponse("200", desarrolladores, "Respuesta generada correctamente"), HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String, Object>>(
					setResponse("500", null, "No se ha podido obtener los datos."), HttpStatus.OK);
		}
	}

	@GetMapping(path = "/getComponentesByTipo/{id}")
	public ResponseEntity<?> getComponentesByTipo(@PathVariable Integer id) {
		List<Componente> proyectos = componenteDAO.getServidores(id);
		if (proyectos != null && !proyectos.isEmpty()) {
			return new ResponseEntity<Map<String, Object>>(
					setResponse("200", proyectos, "Respuesta generada correctamente"), HttpStatus.OK);
		} else {
			return new ResponseEntity<Map<String, Object>>(
					setResponse("500", null, "No se ha podido obtener los datos."), HttpStatus.OK);
		}
	}
	private Map<String, Object> setResponse(String code, Object data, String message) {
		Map<String, Object> response = new HashMap<>();
		response.put("code", code);
		response.put("data", data);
		response.put("message", message);
		return response;
	}
}
