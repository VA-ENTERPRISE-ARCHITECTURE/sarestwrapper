package gov.va.ea.sa.api.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.fop.svg.PDFTranscoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    }

    @RequestMapping(method = RequestMethod.GET, value = "/diagrams")
    public List<Map<String, Object>> getDiagrams(@RequestParam String encOption, @RequestParam String diagramType) {

	return sADatabaseService.getDiagrams(encOption, diagramType);

    }

    @RequestMapping(method = RequestMethod.GET, value = "/diagram")
    public Map<String, Object> getDiagram(@RequestParam String encOption, @RequestParam String diagramType,
	    @RequestParam String diagramId) {

	Map<String, Object> result = new HashMap<>();
	result = sADatabaseService.getDiagram(encOption, diagramType, diagramId);
	Object imgObj = result.get("ImageData");
	byte[] imageInByte = (byte[]) imgObj;
	if (imgObj != null && imageInByte.length > 0) {
	    result.put("svgImageData", new String(imageInByte));
	    result.put("Status", "Success");
	} else {
	    result.put("Status", "Image Not Found");
	}
	return result;
    }

    @RequestMapping(value = "/getpdf", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getPDF(@RequestParam String encOption, @RequestParam String diagramType,
	    @RequestParam String diagramId) {
	String encyclopedia = encOption.substring(0, encOption.indexOf("/"));
	String workspace = encOption.substring(encOption.indexOf("/") + 1, encOption.length() - 1);

	Map<String, Object> result = new HashMap<>();
	result = sADatabaseService.getDiagram(encOption, diagramType, diagramId);
	Object imgName = result.get("DiagramName");
	Object imgObj = result.get("ImageData");

	String outFile = encyclopedia + "_" + workspace + "_" + imgName.toString() + ".pdf";
	byte[] imageInByte = (byte[]) imgObj;

	if (imgObj != null && imageInByte.length > 0) {

	    try {
		String imageSVGString = new String(imageInByte, "UTF-8");
		imageSVGString = imageSVGString.replaceFirst("<svg ", "<svg xmlns=\"http://www.w3.org/2000/svg\" ");

		ByteArrayInputStream bais = new ByteArrayInputStream(imageSVGString.getBytes("UTF-8"));

		TranscoderInput input_svg_image = new TranscoderInput(bais);
		// Step-2: Define OutputStream to PDF file and attach to TranscoderOutput
		ByteArrayOutputStream pdf_ostream = new ByteArrayOutputStream();
		TranscoderOutput output_pdf_file = new TranscoderOutput(pdf_ostream);
		// Step-3: Create a PDF Transcoder and define hints
		Transcoder transcoder = new PDFTranscoder();
		// System.out.println("input svg image: " + imageSVGString.substring(0, 500));
		// Step-4: Write output to PDF format
		transcoder.transcode(input_svg_image, output_pdf_file);
		// Step 5- close / flush Output Stream
		pdf_ostream.flush();
		pdf_ostream.close();

		byte[] contents = pdf_ostream.toByteArray();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/pdf"));
		// Here you have to set the actual filename of your pdf
		String filename = imgName + ".pdf";
		headers.setContentDispositionFormData(filename, filename);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
		return response;
	    } catch (IOException | TranscoderException e) {
		throw new RuntimeException(e);
	    }

	} else {
	    throw new RuntimeException("Cannot generate pdf as image is not available");
	}

    }

}
