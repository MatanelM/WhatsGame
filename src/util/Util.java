package util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import beans.Forum;
import beans.Gamer;

public class Util {
	public static final String MainFont = "Verdana";
	public static final String DATABASE_NAME = "whats_game";
	public static final String USERNAME = "david";
	public static final String PASSWORD = "my-secret-pwd";

	public static final String CSS_BACKGROUND = "-fx-background-color: ";
	public static final String CSS_BACKGROUND_RADIUS = "-fx-background-radius: ";
	public static final String CSS_BORDER_RADIUS = "-fx-border-radius: ";
	public static final String CSS_BORDER_COLOR = "-fx-border-color: ";
	public static final String CSS_COLOR = "-fx-text-fill: ";
	public static final String CSS_MOUSE = "-fx-cursor: ";

	public static final String WHITE = "#f6f6f6";
	public static final String WHITE_HALF = "#f6f6f650";

	public static final String BLACK = "#0e0e0e";

	public static final String LIGHT_GREEN = "#77d7c8";
	public static final String GREEN = "#128c7e";
	public static final String DARK_GREEN = "#064e45";
	public static final String CSS_VISIBILITY = "visibility: ";

	public static String encrypt256(String password) {

		MessageDigest digest;

		try {
			digest = MessageDigest.getInstance("SHA-256");
			byte[] encryptedhash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
			return bytesToHex(encryptedhash);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";

	}

	private static String bytesToHex(byte[] hash) {
		StringBuilder hexString = new StringBuilder(2 * hash.length);
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1) {
				hexString.append('0');
			}
			hexString.append(hex);
		}
		return hexString.toString();
	}

	public static <T> Collection<T> intersection(Collection<T> c1, Collection<T> c2) {

		ArrayList<T> list = new ArrayList<T>();

		for (T t2 : c2) {
			if (contains(c1, t2))
				list.add(t2);
		}

		return (Collection<T>) list;

	}

	public static <T> Collection<T> subtract(Collection<T> c1, Collection<T> c2) {
		ArrayList<T> list = new ArrayList<T>();

		for (T t2 : c2) {
			if (!contains(c1, t2))
				list.add(t2);
		}
		return list;

	}

	public static <T> boolean contains(Collection<T> c1, T t) {
		for (T t1 : c1) {
			if (t1.equals(t))
				return true;
		}
		return false;
	}

	public static String getFriendsPretty(Forum forum) {
		if (forum == null)
			return "";
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < forum.getGamers().size(); i++) {
			sb.append(forum.getGamers().get(i));
			if (i != forum.getGamers().size() - 1) {
				sb.append(", ");
			}

		}
		return sb.toString();
	}
	
	public static Gamer filterGamerByChatID(List<Gamer> list, String id) {
		for (Gamer gamer : list) {
			if ( gamer.getGamerChat().getId().equals(id))
				return gamer;
		}
		return null;
	}

}
