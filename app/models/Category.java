package models;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlAccessType;


@XmlRootElement(name="Category")
@XmlAccessorType(XmlAccessType.FIELD)
public class Category {

	@XmlAttribute(name="Category")
	private String name;
	
	@XmlElement(name="SubCategory")
	private List<SubCategory> subCategories;


	public String getCategoryName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SubCategory> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<SubCategory> subCategories) {
		this.subCategories = subCategories;
	}

	private Category() {
	}

	@Override
	public String toString() {
		return "Category [name=" + name + ", subCategories=" + subCategories + "]";
	}

}
