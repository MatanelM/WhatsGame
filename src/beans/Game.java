package beans;


public class Game {

	private int id;
	private String name;
	private Genre genre;

	public Game() {
		this("", null);
	}

	public Game(String name, Genre genre) {
		this(0, name, genre);
	}

	public Game(int id, String name, Genre genre) {
		super();
		this.id = id;
		this.name = name;
		this.setGenre(genre);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Game [id=" + id + ", name=" + name + "]";
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

}
