package gov.va.ea.sa.api.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Workspace_List")
@XmlAccessorType (XmlAccessType.FIELD)
public class WorkspaceList {
	
	@XmlElement(name = "Workspace")
	private List<Workspace> workspaces = null;

	public List<Workspace> getWorkspaces() {
		return workspaces;
	}

	public void setWorkspacess(List<Workspace> workspaces) {
		this.workspaces = workspaces;
	}	
}
