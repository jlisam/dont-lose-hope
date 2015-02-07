package models;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlElement;

@XmlRootElement(name="LostProperty")
public class LostProperty {

	private int responseCode;
	private long numberOfLostArticles;
	private long numberOfItemsClaimed;
	private List<Category> categories;

	public LostProperty(int responseCode, long numberOfLostArticles, long numberOfItemsClaimed,
			List<Category> categories) {
		this.responseCode = responseCode;
		this.numberOfLostArticles = numberOfLostArticles;
		this.numberOfItemsClaimed = numberOfItemsClaimed;
		this.categories = categories;
	}

	@XmlElement(name="responsecode")
	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	@XmlElement(name="NumberOfLostArticles")
	public long getNumberOfLostArticles() {
		return numberOfLostArticles;
	}

	public void setNumberOfLostArticles(long numberOfLostArticles) {
		this.numberOfLostArticles = numberOfLostArticles;
	}

	@XmlElement(name="NumberOfItemsclaimed")
	public long getNumberOfItemsClaimed() {
		return numberOfItemsClaimed;
	}

	public void setNumberOfItemsClaimed(long numberOfItemsClaimed) {
		this.numberOfItemsClaimed = numberOfItemsClaimed;
	}

	@XmlElement(name="Category")
	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public LostProperty() {}

	@Override
	public String toString() {
		return "LostProperty [responseCode=" + responseCode + ", numberOfLostArticles=" + numberOfLostArticles
				+ ", numberOfItemsClaimed=" + numberOfItemsClaimed + ", categories=" + categories + "]";
	}

	

}
