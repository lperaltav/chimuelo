package cl.zprojects.backend.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import cl.zprojects.backend.dao.IComponenteDAO;
import cl.zprojects.backend.pojo.Ambiente;
import cl.zprojects.backend.pojo.Atributo;
import cl.zprojects.backend.pojo.Componente;
import cl.zprojects.backend.pojo.Desarrollador;
import cl.zprojects.backend.pojo.Tipo;
import cl.zprojects.backend.util.Atributos;
import cl.zprojects.backend.util.TipoComponente;
import cl.zprojects.backend.util.TipoRelacion;

@Service
public class ICompenenteImpl implements IComponenteDAO {

	Logger log = LoggerFactory.getLogger(getClass());
	@Autowired
	NamedParameterJdbcTemplate template;

	@Override
	public List<Componente> getServidores(Integer id) {

		List<Componente> componentes = new ArrayList<>();

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT C.ID, C.NOMBRE, T.* FROM chimuelo.COMPONENTE C JOIN TIPO T ON C.ID_TIPO = T.ID");
		sb.append(" WHERE C.ID_TIPO = :ID_TIPO");

		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("ID_TIPO", id);

		try {
			componentes = template.query(sb.toString(), paramMap, new RowMapper<Componente>() {
				@Override
				public Componente mapRow(ResultSet rs, int rowNum) throws SQLException {
					Componente c = new Componente();
					c.setId(rs.getInt("C.ID"));
					c.setNombre(rs.getString("C.NOMBRE"));
					c.setAtributos(getAtributosComponente(c.getId()));
					c.setAmbientes(getAmbiente(c.getId()));
					c.setDesarrolladores(getDesarrollador(c.getId()));
					Tipo tipo = new Tipo();
					tipo.setId(rs.getInt("T.ID"));
					tipo.setNombre(rs.getString("T.NOMBRE"));
					c.setTipo(tipo);
					return c;
				}

			});
		} catch (Exception e) {
			log.error("No se ha podido obtener los datos en el metodo 'getAll'.", e);
			componentes = new ArrayList<Componente>();
		}

		return componentes;
	}

	@Override
	public List<Atributo> getAtributosComponente(Integer id) {

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT A.ID, A.NOMBRE, CA.VALOR FROM chimuelo.COMPONENTE C");
		sb.append(" INNER JOIN chimuelo.COMPONENTE_ATRIB CA ON C.ID = CA.ID_COMPONENTE");
		sb.append(" INNER JOIN chimuelo.ATRIBUTO A ON CA.ID_ATRIBUTO = A.ID");
		sb.append(" WHERE C.ID = :ID_COMPONENTE");

		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("ID_COMPONENTE", id);

		return template.query(sb.toString(), paramMap, new RowMapper<Atributo>() {

			@Override
			public Atributo mapRow(ResultSet rs, int rowNum) throws SQLException {
				Atributo a = new Atributo();
				a.setId(rs.getInt("A.ID"));
				a.setNombre(rs.getString("A.NOMBRE"));
				a.setValor(rs.getString("CA.VALOR"));
				return a;
			}
		});
	}

	@Override
	public Componente getById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean save(Componente componente) {
		StringBuilder sbC = new StringBuilder();
		MapSqlParameterSource paramMapC = new MapSqlParameterSource();
		Integer saveC = 0;
		Integer saveCR = 0;
		Integer idComponente = 0;
		paramMapC.addValue("ID_RELACION",
				componente.getTipo().getId() != TipoComponente.SERVIDOR.tipo() ? componente.getRelacion().getId() : 1);
		paramMapC.addValue("ID_TIPO", componente.getTipo().getId());
		paramMapC.addValue("NOMBRE", componente.getNombre());
		sbC.append(
				"INSERT INTO chimuelo.COMPONENTE (ID_RELACION, ID_TIPO, NOMBRE) VALUES (:ID_RELACION, :ID_TIPO, :NOMBRE)");

		KeyHolder kh = new GeneratedKeyHolder();
		saveC = template.update(sbC.toString(), paramMapC, kh);
		if (saveC != 0) {
			idComponente = kh.getKey().intValue();
			if (componente.getTipo().getId() != TipoComponente.SERVIDOR.tipo()) {
				saveCR = saveRelacionComponente(componente, idComponente);
				saveDesarrollador(idComponente, componente);
			}
			saveAmbiente(idComponente, componente);
			
			for (Atributo atrib : componente.getAtributos()) {
				if(Atributos.BL.tipo() != atrib.getId() && Atributos.CR.tipo() != atrib.getId()) {
					saveAtributos(atrib.getId(), idComponente, atrib.getValor());
				}else {
					if(componente.getTipo().getId() != TipoComponente.SERVIDOR.tipo()) {
						saveBlCr(atrib.getId(), idComponente, atrib.getValor());
					}
				}
			}

		}
		if (componente.getTipo().getId() != TipoComponente.SERVIDOR.tipo()) {
			return saveC != 0 && saveCR != 0;
		}
		return saveC != 0;

	}

	@Override
	public Boolean update(Componente componente) {
		// TODO Auto-generated method stub
		return null;
	}

	private Integer saveRelacionComponente(Componente componente, Integer idComponente) {
		Integer saveCR = 0;
		StringBuilder sbCR = new StringBuilder();
		sbCR.append(
				"INSERT INTO chimuelo.COMPONENTE_RELACION (ID_COMPONENTE, ID_RELACION, ID_COMPONENTE_RELACION) VALUES (:ID_COMPONENTE, :ID_RELACION, :ID_COMPONENTE_RELACION)");

		MapSqlParameterSource paramMapCR = new MapSqlParameterSource();

		paramMapCR.addValue("ID_COMPONENTE", idComponente);
		paramMapCR.addValue("ID_RELACION", componente.getRelacion().getId());
		paramMapCR.addValue("ID_COMPONENTE_RELACION", componente.getIdRelacion());

		saveCR = template.update(sbCR.toString(), paramMapCR);
		if (saveCR == 0) {
			log.error("No se pudo insertar la relacion con los siguientes valores " + paramMapCR.getValues());
		}
		return saveCR;
	}

	@Override
	public List<Componente> getComponentesHijos(Integer id) {
		List<Componente> componentes = new ArrayList<>();

		StringBuilder sb = new StringBuilder();
		sb.append("SELECT C.ID, C.NOMBRE, T.* FROM chimuelo.COMPONENTE C");
		sb.append(" INNER JOIN chimuelo.COMPONENTE_RELACION CR ON C.ID = CR.ID_COMPONENTE");
		sb.append(" JOIN TIPO T ON T.ID = C.ID_TIPO");
		sb.append(" WHERE CR.ID_COMPONENTE_RELACION = :ID_COMPONENTE_PADRE");
		sb.append(" AND CR.ID_RELACION = :ID_RELACION_HIJO");

		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		paramMap.addValue("ID_COMPONENTE_PADRE", id);
		paramMap.addValue("ID_RELACION_HIJO", TipoRelacion.HIJO.tipo());

		try {
			componentes = template.query(sb.toString(), paramMap, new RowMapper<Componente>() {
				@Override
				public Componente mapRow(ResultSet rs, int rowNum) throws SQLException {
					Componente c = new Componente();
					c.setId(rs.getInt("C.ID"));
					c.setNombre(rs.getString("C.NOMBRE"));
					c.setAtributos(getAtributosComponente(c.getId()));
					c.setAmbientes(getAmbiente(c.getId()));
					c.setDesarrolladores(getDesarrollador(c.getId()));
					Tipo tipo = new Tipo();
					tipo.setId(rs.getInt("T.ID"));
					tipo.setNombre(rs.getString("T.NOMBRE"));
					c.setTipo(tipo);
					return c;
				}

			});
		} catch (Exception e) {
			log.error("No se ha podido obtener los datos en el metodo 'getComponentesHijos'.", e);
			componentes = new ArrayList<Componente>();
		}

		return componentes;
	}

	@Override
	public List<Tipo> getTipos() {
		StringBuilder sb = new StringBuilder();

		sb.append("SELECT ID, NOMBRE FROM chimuelo.TIPO");
		return template.query(sb.toString(), new RowMapper<Tipo>() {

			@Override
			public Tipo mapRow(ResultSet rs, int rowNum) throws SQLException {
				Tipo t = new Tipo();
				t.setId(rs.getInt("ID"));
				t.setNombre(rs.getString("NOMBRE"));
				return t;
			}
		});
	}

	@Override
	public List<Ambiente> getAmbiente(Integer id) {
		StringBuilder sb = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		if (id == null) {
			sb.append("SELECT ID, NOMBRE FROM chimuelo.AMBIENTE");
		} else {
			sb.append(" SELECT A.ID, A.NOMBRE ");
			sb.append(" FROM chimuelo.AMBIENTE A");
			sb.append(" INNER JOIN COMPONENTE_AMBIENTE CA ON A.ID = CA.ID_AMBIENTE");
			sb.append(" WHERE CA.ID_COMPONENTE = :ID_COMPONENTE");
			paramMap.addValue("ID_COMPONENTE", id);
		}
		return template.query(sb.toString(), paramMap, new RowMapper<Ambiente>() {

			@Override
			public Ambiente mapRow(ResultSet rs, int rowNum) throws SQLException {
				Ambiente a = new Ambiente();
				a.setId(rs.getInt("ID"));
				a.setNombre(rs.getString("NOMBRE"));
				return a;
			}
		});
	}

	@Override
	public List<Desarrollador> getDesarrollador(Integer id) {
		StringBuilder sb = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		if (id == null) {
			sb.append("SELECT ID, NOMBRE, IMAGEN, CORREO FROM chimuelo.DESARROLLADOR");
		} else {
			sb.append("SELECT D.ID, D.NOMBRE, D.IMAGEN, D.CORREO");
			sb.append(" FROM DESARROLLADOR D");
			sb.append(" INNER JOIN COMPONENTE_DESARROLLADOR CD ON D.ID = CD.ID_DESARROLLADOR");
			sb.append(" WHERE CD.ID_COMPONENTE = :ID_COMPONENTE");
			paramMap.addValue("ID_COMPONENTE", id);
		}

		return template.query(sb.toString(), paramMap, new RowMapper<Desarrollador>() {

			@Override
			public Desarrollador mapRow(ResultSet rs, int rowNum) throws SQLException {
				Desarrollador desa = new Desarrollador();
				desa.setId(rs.getInt("ID"));
				desa.setNombre(rs.getString("NOMBRE"));
				desa.setImagen(rs.getString("IMAGEN"));
				desa.setCorreo(rs.getString("CORREO"));
				return desa;
			}
		});
	}

	private Integer saveDesarrollador(Integer idComponente, Componente componente) {
		Integer saveDesa = 0;
		StringBuilder sbDesa = new StringBuilder();
		sbDesa.append(
				"INSERT INTO chimuelo.COMPONENTE_DESARROLLADOR (ID_COMPONENTE, ID_DESARROLLADOR) VALUES (:ID_COMPONENTE, :ID_DESARROLLADOR)");

		MapSqlParameterSource paramMapCR = new MapSqlParameterSource();

		for (Desarrollador desa : componente.getDesarrolladores()) {
			paramMapCR.addValue("ID_COMPONENTE", idComponente);
			paramMapCR.addValue("ID_DESARROLLADOR", desa.getId());
			try {
				saveDesa = template.update(sbDesa.toString(), paramMapCR);
				if (saveDesa == 0) {
					log.error("No se pudo insertar Desarrollador, contiene los siguentes valores "
							+ paramMapCR.getValues());
				}
			} catch (Exception e) {
				log.error("No se pudo insertar el registro desarrollador, Valores: " + idComponente + ", Desarollador:"
						+ desa.getId(), e);
			}

		}

		return saveDesa;
	}

	private Integer saveAmbiente(Integer idComponente, Componente componente) {
		Integer saveAmbiente = 0;
		StringBuilder sbAmbiente = new StringBuilder();
		sbAmbiente.append(
				"INSERT INTO chimuelo.COMPONENTE_AMBIENTE (ID_COMPONENTE, ID_AMBIENTE) VALUES (:ID_COMPONENTE, :ID_AMBIENTE)");

		MapSqlParameterSource paramMapCR = new MapSqlParameterSource();

		for (Ambiente amb : componente.getAmbientes()) {
			paramMapCR.addValue("ID_COMPONENTE", idComponente);
			paramMapCR.addValue("ID_AMBIENTE", amb.getId());
			try {
				saveAmbiente = template.update(sbAmbiente.toString(), paramMapCR);
				if (saveAmbiente == 0) {
					log.error("No se pudo insertar Ambiente, contiene los siguentes valores " + paramMapCR.getValues());
				}
			} catch (Exception e) {
				log.error("No se pudo insertar el registro de ambiente, Valores: " + idComponente + ", Ambiente: "
						+ amb.getId(), e);
			}

		}
		return saveAmbiente;
	}

	private Integer saveAtributos(Integer id, Integer idComponente, String valor) {
		StringBuilder sbTA = new StringBuilder();
		MapSqlParameterSource paramMapTA = new MapSqlParameterSource();
		Integer saveTA = 0;
		sbTA.append(
				"INSERT INTO chimuelo.COMPONENTE_ATRIB (ID_ATRIBUTO, ID_COMPONENTE, VALOR) VALUES (:ID_ATRIBUTO, :ID_COMPONENTE, :VALOR)");
		paramMapTA.addValue("ID_ATRIBUTO", id);
		paramMapTA.addValue("ID_COMPONENTE", idComponente);
		paramMapTA.addValue("VALOR", valor);
		saveTA = template.update(sbTA.toString(), paramMapTA);
		if (saveTA == 0) {
			log.error("No se pudo insertar el atributo con los siguientes valores " + paramMapTA.getValues());
		}
		return 0;
	}
	
	private Integer saveBlCr(Integer id, Integer idComponente, String valor) {
		StringBuilder sb = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		Integer save = 0;
		sb.append(
				"INSERT INTO chimuelo.COMPONENTE_CRBL (TIPO, ID_COMPONENTE, VALOR) VALUES (:TIPO, :ID_COMPONENTE, :VALOR)");
		paramMap.addValue("TIPO", id);
		paramMap.addValue("ID_COMPONENTE", idComponente);
		paramMap.addValue("VALOR", valor);
		save = template.update(sb.toString(), paramMap);
		if (save == 0) {
			log.error("No se pudo insertar el atributo con los siguientes valores " + paramMap.getValues());
		}
		return 0;
	}

	@Override
	public Boolean saveDesarrollador(Desarrollador desarrollador) {
		StringBuilder sb = new StringBuilder();
		MapSqlParameterSource paramMap = new MapSqlParameterSource();
		Integer save = 0;
		sb.append("INSERT INTO chimuelo.DESARROLLADOR (NOMBRE, IMAGEN, CORREO) VALUES (:NOMBRE, :IMAGEN, :CORREO)");
		paramMap.addValue("NOMBRE", desarrollador.getNombre());
		paramMap.addValue("IMAGEN", desarrollador.getImagen());
		paramMap.addValue("CORREO", desarrollador.getCorreo());
		save = template.update(sb.toString(), paramMap);
		if(save == 0) {
			log.error("No se pudo insertar el desarrollador");
		}
		return save == 1;
	}
}
