package gov.va.ea.sa.api.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gov.va.ea.sa.api.data.SADatabaseService;

@RestController
@RequestMapping("/rest/api")
public class SAWrapperRestController {

    @Autowired
    SADatabaseService sADatabaseService;

    @RequestMapping(method = RequestMethod.GET, value = "/diagram-types")
    public List<Map<String, Object>> getDiagramTypes(@RequestParam String encOption) {

	return sADatabaseService.getDiagramTypes(encOption);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/definition-types")
    public List<Map<String, Object>> getDefinitionTypes(@RequestParam String encOption) {

	return sADatabaseService.getDefinitionTypes(encOption);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/definitions")
    public List<Map<String, Object>> getDefinitions(@RequestParam String encOption,
	    @RequestParam String definitionType) {

	return sADatabaseService.getDifinitionsVearLink(encOption, definitionType);

	// Map<String, String> result = new HashMap<>();
	// result.put(encOption + definitionType + "def1", encOption + definitionType +
	// "Fist Definition");
	//
	// result.put(encOption + definitionType + "def2", encOption + definitionType +
	// "Second Definition");
	// return result;

    }

    @RequestMapping(method = RequestMethod.GET, value = "/diagrams")
    public List<Map<String, Object>> getDiagrams(@RequestParam String encOption, @RequestParam String diagramType) {

	return sADatabaseService.getDiagrams(encOption, diagramType);

    }

    // @RequestMapping(method = RequestMethod.GET, value = "/diagram")
    // public Map<String, Object> getDiagrams(@RequestParam String encOption,
    // @RequestParam String diagramType,
    // @RequestParam String diagramId) {
    // String encyclopedia = encOption.substring(0, encOption.indexOf("/"));
    // String workspaceId = encOption.substring(encOption.indexOf("/") + 1);
    //
    // Map<String, Object> result = new HashMap<>();
    // String imageURL =
    // "http://vaausdarapp83.aac.dva.va.gov:27000/SARest/SQL/vaausdarsql80/" +
    // encyclopedia + "/"
    // + workspaceId + "/Diagrams/aaaaa/" + diagramId + "/Image/SVG";
    // result.put("svgImageUrl", imageURL);
    //
    // try {
    // String encResp = this.restTemplate.getForObject(imageURL, String.class);
    //
    // result.put("svgImageData", encResp);
    // result.put("Status", "Success");
    // } catch (RestClientException e) {
    // e.printStackTrace();
    // result.put("Status", "Image Not Found");
    // }
    // return result;
    // // return sADatabaseService.getDiagram(encOption, diagramType, diagramId);
    //
    // }

    @RequestMapping(method = RequestMethod.GET, value = "/diagram")
    public Map<String, Object> getDiagram(@RequestParam String encOption, @RequestParam String diagramType,
	    @RequestParam String diagramId) {
	String encyclopedia = encOption.substring(0, encOption.indexOf("/"));
	String workspace = encOption.substring(encOption.indexOf("/") + 1, encOption.length() - 1);

	Map<String, Object> result = new HashMap<>();
	result = sADatabaseService.getDiagram(encOption, diagramType, diagramId);

	Object imgName = result.get("DiagramName");
	Object imgObj = result.get("ImageData");

	String outFile = encyclopedia + "_" + workspace + "_" + imgName.toString() + ".pdf";
	byte[] imageInByte = (byte[]) imgObj;

	PDDocument document = null;

	if (imgObj != null && imageInByte.length > 0) {
	    result.put("svgImageData", new String(imageInByte));
	    // try {
	    // document = createImagePdf(outFile, imageInByte);
	    //
	    // } catch (IOException e) {
	    // e.printStackTrace();
	    // }
	    // result.put("pdfFile", document);
	    result.put("Status", "Success");
	} else {
	    result.put("Status", "Image Not Found");
	}
	return result;
    }

    public PDDocument createImagePdf(String outFile, byte[] imageInByte) throws IOException {
	InputStream in = new ByteArrayInputStream(imageInByte);
	BufferedImage bImage = ImageIO.read(in);

	PDDocument document = new PDDocument();
	// PDDocument.load(new
	// File("C:\\va_ea_dev\\workspace\\va_aes\\sawrapper\\src\\main\\resources\\Test.pdf"));
	// PDPage page = document.getPage(0);
	// float width = bImage.getWidth();
	// float height = bImage.getHeight();

	PDPage page = new PDPage();
	document.addPage(page);
	PDPageContentStream contentStream = new PDPageContentStream(document, page);
	PDImageXObject pdImage = LosslessFactory.createFromImage(document, bImage);
	float scale = 1f;
	contentStream.drawImage(pdImage, 20, 20, pdImage.getWidth() * scale, pdImage.getHeight() * scale);
	contentStream.close();
	in.close();
	document.save(outFile);
	document.close();
	return document;
    }

}
