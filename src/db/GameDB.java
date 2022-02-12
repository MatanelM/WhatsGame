package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import beans.Game;
import beans.Genre;
import core.ConnectionPool;
import interfaces.GameActions;

public class GameDB implements GameActions {

	@Override
	public void addGame(Genre genre, String name) {

		String statement = "INSERT INTO game (Game, GenreID, Genre) VALUES (?,?,?)";

		Connection conn = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = conn.prepareStatement(statement)) {

			pst.setString(1, name);
			pst.setInt(2, genre.getGenreID());
			pst.setString(3, genre.getGenre());

			pst.execute();
			System.out.println(String.format("Added game %s", name));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(conn);
		}

	}

	@Override
	public ArrayList<Game> getLikeGamesByID(String gamerID) {

		String statement = "select * from gamer_game natural join game where GamerID=?;";

		ArrayList<Game> games = new ArrayList<>();
		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = con.prepareStatement(statement)) {
			pst.setString(1, gamerID);
			
			try (ResultSet rs = pst.executeQuery()){
				while (rs.next()) {
					Game game = new Game(rs.getInt("GameID"), rs.getString("Game"), Genre.values()[rs.getInt("GenreID")-1]);
					games.add(game);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return games;
	}

	@Override
	public ArrayList<Game> getGameOfGenre(Genre genre) {
		String statement = "select * from game natural join genre where GenreID=?;";
		ArrayList<Game> games = new ArrayList<>();
		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = con.prepareStatement(statement)) {
			pst.setInt(1, genre.getGenreID());
			
			try (ResultSet rs = pst.executeQuery()){
				while (rs.next()) {
					Game game = new Game(rs.getInt("GameID"), rs.getString("Game"), Genre.values()[rs.getInt("GenreID")-1]);
					games.add(game);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return games;
	}

	public void addGameToFavorites(Game game, String gamerID) {
		String statement = "INSERT INTO gamer_game (GameID, GamerID) VALUES (?,?)";

		Connection conn = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = conn.prepareStatement(statement)) {

			pst.setInt(1, game.getId());
			pst.setString(2, gamerID);

			pst.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(conn);
		}
	}

	@Override
	public ArrayList<Game> getAllGames() {
		String statement = "select * from game ;";
		ArrayList<Game> games = new ArrayList<>();
		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = con.prepareStatement(statement)) {
			
			try (ResultSet rs = pst.executeQuery()){
				while (rs.next()) {
					Game game = new Game(rs.getInt("GameID"), rs.getString("Game"), Genre.values()[rs.getInt("GenreID")-1]);
					games.add(game);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return games;
	}

	@Override
	public Game getGameByID(int id) {
		Game game = null;
		String statement = "select * from game where GameID=?;";
		Connection con = ConnectionPool.getInstance().getConnection();

		try (PreparedStatement pst = con.prepareStatement(statement)) {
			
			try (ResultSet rs = pst.executeQuery()){
				if (rs.next()) {
					game = new Game(rs.getInt("GameID"), rs.getString("Game"), Genre.values()[rs.getInt("GenreID")-1]);
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnectionPool.getInstance().returnConnection(con);
		}
		return game;
	}

}
