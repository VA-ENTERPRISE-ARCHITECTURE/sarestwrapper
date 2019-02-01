package gov.va.ea.sa.api.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Definitions")
@XmlAccessorType (XmlAccessType.FIELD)
public class Definitions {
	
	@XmlElement(name = "type")
	private List<Type> types = null;
	
	@XmlElement(name = "System")
	private List<System> systems = null;

	public List<System> getSystems() {
		return systems;
	}

	public void setSystems(List<System> systems) {
		this.systems = systems;
	}

	public List<Type> getTypes() {
		return types;
	}

	public void setTypes(List<Type> types) {
		this.types = types;
	}
}
