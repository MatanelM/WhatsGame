package beans;

import java.time.LocalDate;
import java.util.UUID;

import interfaces.Notificationable;

public class Message implements Notificationable {

	public void setId(String id) {
		this.id = id;
	}

	private String id;
	private String content;
	private Chat from;
	private Chat to;
	private LocalDate date;

	public Message(String content, Chat from, Chat to, LocalDate date) {
		super();
		this.id = UUID.randomUUID().toString();
		this.content = content;
		this.from = from;
		this.to = to;
		this.date = date;
	}

	public Message(String id, String content, Chat from, Chat to, LocalDate date) {
		super();
		this.id = id;
		this.content = content;
		this.from = from;
		this.to = to;
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Chat getFrom() {
		return from;
	}

	public void setFrom(Chat from) {
		this.from = from;
	}

	public Chat getTo() {
		return to;
	}

	public void setTo(Chat to) {
		this.to = to;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Message [id=" + id + ", content=" + content + ", from=" + from + ", to=" + to + ", date=" + date + "]";
	}

}
