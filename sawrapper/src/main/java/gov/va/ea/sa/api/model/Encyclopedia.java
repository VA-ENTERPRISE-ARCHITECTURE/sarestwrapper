package gov.va.ea.sa.api.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="Encyclopedia")
@XmlAccessorType (XmlAccessType.FIELD)
public class Encyclopedia {
	
	@XmlAttribute
	private String Name;

	@XmlElement
	private Definitions Definitions;
	
	@XmlElement
	private Diagrams Diagrams;
	
	@XmlElement(name = "Workspace_List")
	private WorkspaceList workspaceList;

	
	public WorkspaceList getWorkspaceList() {
		return workspaceList;
	}

	public void setWorkspaceList(WorkspaceList workspaceList) {
		this.workspaceList = workspaceList;
	}

	public Diagrams getDiagrams() {
		return Diagrams;
	}

	public void setDiagrams(Diagrams diagrams) {
		Diagrams = diagrams;
	}

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public Definitions getDefinitions() {
		return Definitions;
	}

	public void setDefinitions(Definitions Definitions) {
		this.Definitions = Definitions;
	}

}
