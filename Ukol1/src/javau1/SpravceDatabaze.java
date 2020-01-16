package javau1;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.log4j.Logger;

public class SpravceDatabaze {
	
	private static final Logger LOGGER = Logger.getLogger(SpravceDatabaze.class);
	private static SpravceDatabaze instance = null;
	private SpravceDatabaze() {
		
		LOGGER.debug("Begin");
		
		try {
			Class.forName(Ukol1conf.getDriverClass());
		}
		catch (ClassNotFoundException ex) {
			LOGGER.error("Driver not found");
		}
		catch (PropertyFileException ex) {
			LOGGER.error("Failed to load property file");
		}	
	}
	
	public static SpravceDatabaze getInstance() {
		if (instance == null) {
			instance = new SpravceDatabaze();
		}
		return instance;
	}
	
	private void zapisHistorii (Connection connection, int kod_kmen, String jmeno, int mnozstvi_puvodni, int mnozstvi_nove) {
		try (Statement statement = connection.createStatement()){
			StringBuilder sb = new StringBuilder();
			sb.append("insert into public.historie (kod_kmen, jmeno, mnozstvi_puvodni, mnozstvi_nove, cas) values (");
			sb.append(kod_kmen);
			sb.append(",'");
			sb.append(jmeno);
			sb.append("',");
			sb.append(mnozstvi_puvodni);
			sb.append(",");
			sb.append(mnozstvi_nove);
			sb.append(",'");
			LocalDateTime cas = LocalDateTime.now();
			sb.append(cas.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
			sb.append("')");
			String updateString = sb.toString();
			LOGGER.debug(updateString);
			int updateCount = statement.executeUpdate(updateString);
			LOGGER.debug("upadteCount: " + updateCount);
		}
		catch (SQLException sqlException) {
			int errCode = sqlException.getErrorCode();
			String SQLState = sqlException.getSQLState();
			StringBuilder sb = new StringBuilder();
			sb.append("ErrorCode: ");
			sb.append(Integer.toString(errCode));
			sb.append("SQLState: ");
			sb.append(SQLState);
			LOGGER.error(sb.toString());
		}
	}
	
	private void editujZaznam(Connection connection, int kod_kmen, String jmeno, int zmena) throws SQLException {
		String queryStringKmen = "select * from public.kmen where kod=" + kod_kmen;
		LOGGER.debug(queryStringKmen);
		
		try (Statement statement2 = connection.createStatement();
				ResultSet resultSetKmen = statement2.executeQuery(queryStringKmen)){
			resultSetKmen.next();
			int mnozstvi = resultSetKmen.getInt("mnozstvi");
			LOGGER.debug("statement2");
		
			if ((mnozstvi + zmena) >= 0) {
				StringBuilder sb = new StringBuilder();
				sb.append("update public.kmen set mnozstvi=");
				sb.append(mnozstvi + zmena);
				sb.append("where kod=");
				sb.append(kod_kmen);
				String updateString = sb.toString();
				LOGGER.debug(updateString);
				int updateCount = statement2.executeUpdate(updateString);
				LOGGER.debug("upadteCount: " + updateCount);
				zapisHistorii(connection, kod_kmen, jmeno, mnozstvi, mnozstvi + zmena);
			}
			else {
				LOGGER.error("Unable to update mnozstvi, mnozstvi=" + mnozstvi + ", zmena=" + zmena);
			}
		}
	}
	
	private void nactiZmeny(Connection connection) throws SQLException {
		String queryStringZmeny = "select * from public.zmeny";
		LOGGER.debug(queryStringZmeny);
		
		try (Statement statement1 = connection.createStatement();
				ResultSet resultSetZmeny = statement1.executeQuery(queryStringZmeny)){ 
		
			while (resultSetZmeny.next()) {
				int kod_kmen = resultSetZmeny.getInt("kod_kmen");
				String jmeno = resultSetZmeny.getString("jmeno");
				int zmena = resultSetZmeny.getInt("mnozstvi");
				editujZaznam(connection, kod_kmen, jmeno, zmena);
			}
		}
	}
	
	public void provedZmeny() {
		try (Connection connection = DriverManager.getConnection(Ukol1conf.getUrl(), Ukol1conf.getUserName(), Ukol1conf.getUserPassword())) {
			nactiZmeny(connection);
		}
		catch (SQLException sqlException) {
			int errCode = sqlException.getErrorCode();
			String SQLState = sqlException.getSQLState();
			StringBuilder sb = new StringBuilder();
			sb.append("ErrorCode: ");
			sb.append(Integer.toString(errCode));
			sb.append("SQLState: ");
			sb.append(SQLState);
			LOGGER.error(sb.toString());
		}
		catch (PropertyFileException ex) {
			LOGGER.error("Failed to load property file");
		}

	}
}
