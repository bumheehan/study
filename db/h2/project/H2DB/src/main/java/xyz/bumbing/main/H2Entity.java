package xyz.bumbing.main;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class H2Entity {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long no;
	private String subject;
	private String content;
	@Column(nullable = false, updatable = false)
	private Date date;

	public H2Entity(String subject, String content) {
		this.subject = subject;
		this.content = content;
		this.date = new Date();
	}

	@Override
	public String toString() {
		return "H2Entity [no=" + no + ", subject=" + subject + ", content=" + content + ", date=" + date + "]";
	}

	public H2Entity() {
	}

	public long getNo() {
		return no;
	}

	public void setNo(long no) {
		this.no = no;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
