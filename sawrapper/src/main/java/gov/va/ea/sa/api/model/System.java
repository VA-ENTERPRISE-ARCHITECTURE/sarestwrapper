package gov.va.ea.sa.api.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "System")
@XmlAccessorType (XmlAccessType.FIELD)
public class System {

	@XmlElement(name = "prp_Element-spa-Id")
	private String elementId;
	
	@XmlElement(name = "prp_Class-spa-Id")
	private String classId;
	
	@XmlElement(name = "prp_Full-spa-Name")
	private String fullName;
	
	@XmlElement(name = "prp_System-spa-Acronym")
	private String acronym;
    
	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getAcronym() {
		return acronym;
	}

	public void setAcronym(String acronym) {
		this.acronym = acronym;
	}	
	
}
