package beans;

import java.time.LocalDate;
import java.util.UUID;

import interfaces.Notificationable;

public class FriendRequest implements Notificationable {

	private String frienRequestID;
	private Gamer gamerSender;
	private Gamer gamerReceiver;

	private boolean isAccepted;
	private LocalDate date;

	public FriendRequest() {
		this(null, null);
	}

	public FriendRequest(Gamer gamerSender, Gamer gamerReceiver) {
		this(gamerSender, gamerReceiver, false);
	}

	public FriendRequest(Gamer gamerSender, Gamer gamerReceiver, boolean isAccepted) {
		this(UUID.randomUUID().toString(), gamerSender, gamerReceiver, isAccepted, LocalDate.now());
	}

	public FriendRequest(String frienRequestID, Gamer gamerSender, Gamer gamerReceiver, boolean isAccepted,
			LocalDate date) {
		super();
		this.frienRequestID = frienRequestID;
		this.gamerSender = gamerSender;
		this.gamerReceiver = gamerReceiver;
		this.isAccepted = isAccepted;
		this.date = date;
	}

	public String getFrienRequestID() {
		return frienRequestID;
	}

	public void setFrienRequestID(String frienRequestID) {
		this.frienRequestID = frienRequestID;
	}

	public Gamer getGamerSender() {
		return gamerSender;
	}

	public void setGamerSender(Gamer gamerSender) {
		this.gamerSender = gamerSender;
	}

	public Gamer getGamerReceiver() {
		return gamerReceiver;
	}

	public void setGamerReceiver(Gamer gamerReceiver) {
		this.gamerReceiver = gamerReceiver;
	}

	public boolean isAccepted() {
		return isAccepted;
	}

	public void setAccepted(boolean isAccepted) {
		this.isAccepted = isAccepted;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((frienRequestID == null) ? 0 : frienRequestID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FriendRequest other = (FriendRequest) obj;
		if (frienRequestID == null) {
			if (other.frienRequestID != null)
				return false;
		} else if (!frienRequestID.equals(other.frienRequestID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "FriendRequest [frienRequestID=" + frienRequestID + ", gamerSender=" + gamerSender + ", gamerReceiver="
				+ gamerReceiver + ", isAccepted=" + isAccepted + ", date=" + date + "]";
	}

}
