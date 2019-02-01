package gov.va.ea.sa.api.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Workspace")
@XmlAccessorType (XmlAccessType.FIELD)
public class Workspace {

	@XmlAttribute(name = "Name")
	private String name;
	
	@XmlAttribute(name = "ID")
	private String id;
	
	@XmlAttribute
	private String baseline;
	
	@XmlAttribute
	private String description;

	@XmlAttribute
	private String path;

	@XmlAttribute
	private String href;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBaseline() {
		return baseline;
	}

	public void setBaseline(String baseline) {
		this.baseline = baseline;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}	
}
