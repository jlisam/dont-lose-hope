package models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="SubCategory")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubCategory {
	@XmlAttribute(name="SubCategory")
	private String name;
	@XmlAttribute(name="count")
	private String count;

	
	public String getSubCategoryName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	
	public String getSubCategoryCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public SubCategory(String name, String count) {
		this.name = name;
		this.count = count;
	}

	// Jaxb requires a no args constructor
	public SubCategory(){
		
	}
	@Override
	public String toString() {
		return "SubCategory [name=" + name + ", count=" + count + "]";
	}

}
