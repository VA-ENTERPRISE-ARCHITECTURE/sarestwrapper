package gov.va.ea.sa.api.web.model;

import java.io.Serializable;
import java.util.List;

import gov.va.ea.sa.api.model.Definitions;
import gov.va.ea.sa.api.model.Diagrams;

public class SearchOption implements Serializable{
	
	private static final long serialVersionUID = 8555065591186451313L;
	private String encOption;
	private List<PickList> pickList;
	private Definitions definitions;
	private Diagrams diagrams;
	
	public String getEncOption() {
		return encOption;
	}
	public void setEncOption(String encOption) {
		this.encOption = encOption;
	}
	public List<PickList> getPickList() {
		return pickList;
	}
	public void setPickList(List<PickList> pickList) {
		this.pickList = pickList;
	}
	public Definitions getDefinitions() {
		return definitions;
	}
	public void setDefinitions(Definitions definitions) {
		this.definitions = definitions;
	}
	public Diagrams getDiagrams() {
		return diagrams;
	}
	public void setDiagrams(Diagrams diagrams) {
		this.diagrams = diagrams;
	}	
}
