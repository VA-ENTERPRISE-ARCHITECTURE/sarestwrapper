package gov.va.ea.sa.api.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Type")
@XmlAccessorType(XmlAccessType.FIELD)
public class Type {

	@XmlAttribute
	private String name;

	@XmlAttribute
	private String href;

	// private String formattedName;

	public String getFormattedName() {
		return this.name.replaceAll("-spa-", " ").replaceAll("-fws-", "/").replaceAll("-col-", ":")
				.replaceAll("-opb-", "[").replaceAll("-clb-", "]").replaceAll("_x002C_", ",");
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
}
