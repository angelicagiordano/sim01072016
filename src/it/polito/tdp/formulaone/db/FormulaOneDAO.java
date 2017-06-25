package it.polito.tdp.formulaone.db;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.polito.tdp.formulaone.model.Circuit;
import it.polito.tdp.formulaone.model.Constructor;
import it.polito.tdp.formulaone.model.Driver;
import it.polito.tdp.formulaone.model.Season;


public class FormulaOneDAO {

	public List<Integer> getAllYearsOfRace() {
		
		String sql = "SELECT year FROM races ORDER BY year" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Integer> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(rs.getInt("year"));
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public List<Season> getAllSeasons() {
		
		String sql = "SELECT year, url FROM seasons ORDER BY year" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			List<Season> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(new Season(Year.of(rs.getInt("year")), rs.getString("url"))) ;
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Circuit> getAllCircuits() {

		String sql = "SELECT circuitId, name FROM circuits ORDER BY name";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Circuit> list = new ArrayList<>();
			while (rs.next()) {
				list.add(new Circuit(rs.getInt("circuitId"), rs.getString("name")));
			}

			conn.close();
			return list;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	public List<Driver> getDriverForSeason( int year){
		String sql = "Select drivers.* from results,races,drivers where races.year=? and results.raceId=races.raceId and results.driverId=drivers.driverId and results.position is not null";
		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, year);
			ResultSet rs = st.executeQuery();

			List<Driver> d = new ArrayList<>();
			while (rs.next()) {
				Driver dr=new Driver(rs.getInt("driverId"), rs.getString("driverRef"), rs.getInt("number"), rs.getString("code"), rs.getString("forename"), rs.getString("surname"),
						rs.getDate("dob").toLocalDate(), rs.getString("nationality"), rs.getString("url"));
				
				
				d.add(dr);
				
			}

			conn.close();
			return d;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}}

	public List<Driver> getAllDriver(Map<Integer, Driver> driverMap){
		String sql = "SELECT * FROM drivers ";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Driver> d = new ArrayList<>();
			while (rs.next()) {
				
				Driver dr=new Driver(rs.getInt("driverId"), rs.getString("driverRef"), rs.getInt("number"), rs.getString("code"), rs.getString("forename"), rs.getString("surname"),
						rs.getDate("dob").toLocalDate(), rs.getString("nationality"), rs.getString("url"));
				d.add(dr);
				driverMap.put(dr.getDriverId(), dr);
			}

			conn.close();
			return d;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}

		
	}
	public List<Integer> getRacesForYear(int anno){
     String sql = "SELECT raceId FROM races where year=?" ;
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1,anno);
			ResultSet rs = st.executeQuery() ;
			
			List<Integer> list = new ArrayList<>() ;
			while(rs.next()) {
				list.add(rs.getInt("raceId"));
			}
			
			conn.close();
			return list ;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}
	
	public List<Constructor> getAllConstructors() {

		String sql = "SELECT constructorId, name FROM constructors ORDER BY name";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			ResultSet rs = st.executeQuery();

			List<Constructor> constructors = new ArrayList<>();
			while (rs.next()) {
				constructors.add(new Constructor(rs.getInt("constructorId"), rs.getString("name")));
			}

			conn.close();
			return constructors;
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
	}


	public void addResults(Driver d, int year) {
		// TODO Auto-generated method stub
		String sql = "Select races.year,races.raceId, results.driverId, position from results,races where  position is not null and races.raceId=results.raceId and driverId=? and races.year=?;";

		try {
			Connection conn = DBConnect.getConnection();

			PreparedStatement st = conn.prepareStatement(sql);

			st.setInt(1, d.getDriverId());
			st.setInt(2, year);
			ResultSet rs = st.executeQuery();

			
			while (rs.next()) {
				d.addResult(rs.getInt("raceId"), rs.getInt("position"));
				
			}

			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("SQL Query Error");
		}
		
	}
	

	public static void main(String[] args) {
		FormulaOneDAO dao = new FormulaOneDAO() ;
		
		List<Integer> years = dao.getAllYearsOfRace() ;
		System.out.println(years);
		
		List<Season> seasons = dao.getAllSeasons() ;
		System.out.println(seasons);

		
		List<Circuit> circuits = dao.getAllCircuits();
		System.out.println(circuits);

		List<Constructor> constructors = dao.getAllConstructors();
		System.out.println(constructors);
		
	}

}
