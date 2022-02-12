package interfaces;

import java.util.ArrayList;

import beans.Game;
import beans.Genre;

public interface GameActions {

	void addGame(Genre genre, String name);
	ArrayList<Game> getLikeGamesByID(String gamerID);
	ArrayList<Game> getGameOfGenre(Genre genre);
	public void addGameToFavorites(Game game, String gamerID);
	ArrayList<Game> getAllGames();
	Game getGameByID(int id);
	
}
