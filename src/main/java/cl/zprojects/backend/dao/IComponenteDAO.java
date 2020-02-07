package cl.zprojects.backend.dao;

import java.util.List;

import cl.zprojects.backend.pojo.Ambiente;
import cl.zprojects.backend.pojo.Atributo;
import cl.zprojects.backend.pojo.Componente;
import cl.zprojects.backend.pojo.Desarrollador;
import cl.zprojects.backend.pojo.Tipo;

public interface IComponenteDAO {

	List<Componente> getServidores(Integer id);
	List<Componente> getComponentesHijos(Integer id);
	Componente getById(Integer id);
	Boolean save(Componente componente);
	Boolean update(Componente componente);
	List<Atributo> getAtributosComponente(Integer id);
	
	
	
	/*
	 * Util. 
	 */
	List<Tipo> getTipos();
	List<Ambiente> getAmbiente(Integer id);
	List<Desarrollador> getDesarrollador(Integer id);
	Boolean saveDesarrollador(Desarrollador desarrollador);
}
