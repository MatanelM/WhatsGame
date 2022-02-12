package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import beans.Genre;
import core.ConnectionPool;
import interfaces.GenresActions;

public class GenresDB implements GenresActions{

	@Override
	public void addGenreToFavorites(Genre genre, String gamerID) {
		
		String statement = "insert into gamer_genre (GenreID, GamerID) VALUES(?,?);";
		Connection conn = ConnectionPool.getInstance().getConnection();
		try( PreparedStatement pst = conn.prepareStatement(statement )){
			
			pst.setInt(1, genre.getGenreID());
			pst.setString(2, gamerID);
			
			pst.execute();
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			ConnectionPool.getInstance().returnConnection(conn);
		}
		
		
	}

	@Override
	public ArrayList<Genre> getGenresFavorites(String gamerID) {
		String statement = "select GenreID from genre natural join gamer_genre where GamerID=?;";
		
		ArrayList<Genre> genres = new ArrayList<>();
		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = con.prepareStatement(statement)) {
			pst.setString(1, gamerID);
			
			try (ResultSet rs = pst.executeQuery()){
				while (rs.next()) {
					Genre genre = Genre.values()[rs.getInt("GenreID")-1];
					genres.add(genre);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		
		return genres;
	}


}
