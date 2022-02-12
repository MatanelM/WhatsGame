package interfaces;

import java.util.ArrayList;

import beans.Genre;


public interface GenresActions {

	void addGenreToFavorites(Genre genre, String gamerID);
	ArrayList<Genre> getGenresFavorites(String gamerID);
	
}
