package gov.va.ea.sa.api.controller;

import java.util.regex.Pattern;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import gov.va.ea.sa.api.configuration.SaRestLogin;
import gov.va.ea.sa.api.model.Encyclopedia;
import gov.va.ea.sa.api.model.Type;
import gov.va.ea.sa.api.web.model.SearchOption;

@Controller
public class SASearchController {

    protected static ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    protected static SaRestLogin saRestLogin = (SaRestLogin) ctx.getBean("saRestLogin");

    private final RestTemplate restTemplate;
    private static final Pattern DOUBLE_QUOTE_PARSER = Pattern.compile("\"([^\"]*)\"");

    private final String SA_URL = saRestLogin.getRestUrl();

    public SASearchController(RestTemplateBuilder builder) {
	this.restTemplate = builder.basicAuthorization(saRestLogin.getRestUser(), saRestLogin.getRestPassword())
		.build();
    }

    @GetMapping("/search")
    public String psForm(Model model) {
	SearchOption searchOption = new SearchOption();
	// searchOption.setPickList(getPickList());
	model.addAttribute("searchOption", searchOption);
	return "search";
    }

    @GetMapping("/result")
    public String getResults(Model model) {
	// searchOption.setPickList(getPickList());
	String URL = "http://vaausdarapp83.aac.dva.va.gov:27000/SARest/SQL/vaausdarsql80.aac.dva.va.gov/VAEA_Workspaces/3/Definitions/System";
	Encyclopedia encyclopedia = this.restTemplate.getForObject(URL, Encyclopedia.class);
	model.addAttribute("definitions", encyclopedia.getDefinitions().getSystems());
	return "result";
    }

    @PostMapping("/search")
    public String psSubmit(@ModelAttribute SearchOption searchOption) {
	String URL = SA_URL + "/" + searchOption.getEncOption().trim() + "/Definitions";
	searchOption.setDefinitions(this.restTemplate.getForObject(URL, Encyclopedia.class).getDefinitions());
	URL = SA_URL + "/" + searchOption.getEncOption().trim() + "/Diagrams";
	searchOption.setDiagrams(this.restTemplate.getForObject(URL, Encyclopedia.class).getDiagrams());

	// checkDiagrams(searchOption);
	// checkDefinitions(searchOption);

	return "search";
    }

    public void checkDiagrams(SearchOption searchOption) {
	for (Type type : searchOption.getDiagrams().getTypes()) {

	    try {
		String response = this.restTemplate.getForObject(type.getHref(), String.class);
		if (response.contains("Object reference not set to an instance of an object"))
		    System.out.println(searchOption.getEncOption() + " : " + type.getHref() + ": NOT ACTIVE");
		else
		    System.out.println(searchOption.getEncOption() + " : " + type.getName() + ":" + response);
	    } catch (RestClientException e) {
		System.out.println(searchOption.getEncOption() + " : " + type.getName() + ": " + e.getMessage());
	    }

	}
    }

    public void checkDefinitions(SearchOption searchOption) {
	for (Type type : searchOption.getDefinitions().getTypes()) {

	    try {
		String response = this.restTemplate.getForObject(type.getHref(), String.class);
		System.out.println(searchOption.getEncOption() + " : " + type.getName() + ":" + response);
	    } catch (RestClientException e) {
		System.out.println(searchOption.getEncOption() + " : " + type.getName() + ": " + e.getMessage());
	    }

	}
    }

    // public List<PickList> getPickList() {
    // List<PickList> picklist = new ArrayList<>();
    // String encName = null;
    // List<Workspace> workspaces = null;
    //
    // // Get the list of Encylopedias --- Using a REGEX parser to get the list
    // String encResp = this.restTemplate.getForObject(SA_URL, String.class);
    // Scanner scanner = new Scanner(encResp);
    // List<String> results = scanner.findAll(DOUBLE_QUOTE_PARSER).map(mr ->
    // mr.group(1)).filter(Objects::nonNull)
    // .collect(Collectors.toList());
    //
    // // For each Encylopedia get the Workspaces. Ignore the ROOT workspace if
    // there
    // // are more than 1 workspaces for each Encyclopedia
    // for (String result : results) {
    // if (result.startsWith("http")) {
    // workspaces = this.restTemplate.getForObject(result,
    // Encyclopedia.class).getWorkspaceList()
    // .getWorkspaces();
    //
    // if (workspaces.size() > 1) {
    // for (Workspace workspace : workspaces) {
    // if (!workspace.getName().equalsIgnoreCase("ROOT")) {
    // PickList pl = new PickList();
    // pl.setId(encName + "/" + workspace.getId());
    // pl.setName(encName + " - " + workspace.getName());
    // picklist.add(pl);
    // }
    // }
    // } else {
    // PickList pl = new PickList();
    // pl.setId(encName + "/1");
    // pl.setName(encName);
    // picklist.add(pl);
    // }
    // } else {
    // encName = result;
    // }
    // }
    //
    // scanner.close();
    // return picklist;
    // }

}