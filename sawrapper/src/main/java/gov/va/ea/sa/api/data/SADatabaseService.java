package gov.va.ea.sa.api.data;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class SADatabaseService {

    @Autowired
    private Map<String, JdbcTemplate> jdbcTemplatesMap;

    private static final Logger LOG = Logger.getLogger(SADatabaseService.class.getName());

    public List<Map<String, Object>> getDiagramTypes(String encOption) {
	String encyclopedia = encOption;
	String workspaceId = "1";
	if (encOption.indexOf("/") > 1) {
	    encyclopedia = encOption.substring(0, encOption.indexOf("/"));
	    workspaceId = encOption.substring(encOption.indexOf("/") + 1);
	}
	if ("1".equals(workspaceId)) {
	    return jdbcTemplatesMap.get(encyclopedia).queryForList(DIAGRAM_TYPES_LIST_QUERY_NO_WID);
	} else {
	    return jdbcTemplatesMap.get(encyclopedia).queryForList(DIAGRAM_TYPES_LIST_QUERY,
		    Integer.valueOf(workspaceId));
	}
    }

    private static String DIAGRAM_TYPES_LIST_QUERY_NO_WID = "SELECT distinct TheTypes.Name, thetypes.type FROM (Select Type, Class, Activity from entity) as TheEntity, MetaData as TheTypes\r\n"
	    + "Where TheTypes.class = 1 and TheEntity.class = TheTypes.Class and TheEntity.type = TheTypes.Type\r\n"
	    + "and TheEntity.Activity <> 'D' Order by 1";

    private static String DIAGRAM_TYPES_LIST_QUERY = "SELECT distinct TheTypes.Name, thetypes.type FROM (Select Type, Class, WID , Activity from entity) as TheEntity, MetaData as TheTypes\r\n"
	    + "Where TheTypes.class = 1 and TheEntity.class = TheTypes.Class and TheEntity.type = TheTypes.Type\r\n"
	    + "and TheEntity.WID  = ?  and TheEntity.Activity <> 'D' Order by 1";

    public List<Map<String, Object>> getDiagrams(String encOption, String typeNumber) {
	String encyclopedia = encOption.substring(0, encOption.indexOf("/"));
	String workspaceId = encOption.substring(encOption.indexOf("/") + 1);
	if ("1".equals(workspaceId)) {
	    return jdbcTemplatesMap.get(encyclopedia).queryForList(DIAGRAMS_LIST_QUERY_NO_WID,
		    Integer.valueOf(typeNumber));
	} else {
	    return jdbcTemplatesMap.get(encyclopedia).queryForList(DIAGRAMS_LIST_QUERY, Integer.valueOf(typeNumber),
		    Integer.valueOf(workspaceId));
	}
    }

    private static String DIAGRAMS_LIST_QUERY_NO_WID = "SELECT TheEntity.name, TheEntity.ID FROM Entity as TheEntity Where TheEntity.class = 1 and "
	    + "TheEntity.Type =? AND TheEntity.Activity <> 'D'";

    private static String DIAGRAMS_LIST_QUERY = "SELECT TheEntity.name, TheEntity.ID FROM Entity as TheEntity Where TheEntity.class = 1 and "
	    + "TheEntity.Type =? AND TheEntity.WID  =? AND TheEntity.Activity <> 'D'";

    public Map<String, Object> getDiagram(String encOption, String typeNumber, String diagramId) {
	String encyclopedia = encOption.substring(0, encOption.indexOf("/"));
	String workspaceId = encOption.substring(encOption.indexOf("/") + 1);
	if ("1".equals(workspaceId)) {
	    return jdbcTemplatesMap.get(encyclopedia).queryForMap(DIAGRAM_QUERY_NO_WID, Integer.valueOf(typeNumber),
		    Integer.valueOf(diagramId));
	} else {
	    return jdbcTemplatesMap.get(encyclopedia).queryForMap(DIAGRAM_QUERY, Integer.valueOf(workspaceId),
		    Integer.valueOf(workspaceId), Integer.valueOf(typeNumber), Integer.valueOf(diagramId));
	}
    }

    private static String DIAGRAM_QUERY_NO_WID = "Select ImageFile.Data as ImageData, TheEntity.Name as DiagramName, imagefile.name as ImageFileName \r\n"
	    + "From\r\n" + "(Select Data, Name From Files Where name like 'D%.SVG')As ImageFile,\r\n"
	    + "(Select Name, ID,  Type, Class from Entity) as TheEntity,\r\n"
	    + "(Select distinct T.N.value('(../P[@N=\"DGX File Name\"]/@V)[1]', 'nvarchar(256)') \"Diagram_File\", entityxml.ID\r\n"
	    + "From Entityxml Cross Apply Propertiesxml.nodes('/R/P') As T(N) ) as TheXML\r\n" + "Where\r\n"
	    + "TheEntity.Class = 1                                              /*Leave set as 1 since this is a diagram specific query */\r\n"
	    + "And\r\n"
	    + "TheEntity.type =  ?						/* Set the Desired Diagram Type.  This is just for speed */\r\n"
	    + "And\r\n"
	    + "TheEntity.id = ?							/* Get the specific Diagram from the ID from previous query and user selection */\r\n"
	    + "And\r\n" + "TheXML.id = TheEntity.id \r\n" + "and\r\n"
	    + "imagefile.name like concat(Left(Right(TheXML.Diagram_File,len(thexml.diagram_file)-2),8),'%')";

    private static String DIAGRAM_QUERY = "Select ImageFile.Data as ImageData, TheEntity.Name as DiagramName, imagefile.name as ImageFileName \r\n"
	    + "From\r\n" + "(Select Data, Name, WID From Files Where WID=? and name like 'D%.SVG')As ImageFile,\r\n"
	    + "(Select Name, ID, WID, Type, Class from Entity) as TheEntity,\r\n"
	    + "(Select distinct T.N.value('(../P[@N=\"DGX File Name\"]/@V)[1]', 'nvarchar(256)') \"Diagram_File\", entityxml.ID, entityxml.WID\r\n"
	    + "From Entityxml Cross Apply Propertiesxml.nodes('/R/P') As T(N) ) as TheXML\r\n" + "Where\r\n"
	    + "TheEntity.wid = ?                                                /* Set the Desired Workspace ID */\r\n"
	    + "And TheXML.wid = TheEntity.WID and ImageFile.WID = theentity.WID\r\n" + "and\r\n"
	    + "TheEntity.Class = 1                                              /*Leave set as 1 since this is a diagram specific query */\r\n"
	    + "And\r\n"
	    + "TheEntity.type =  ?											/* Set the Desired Diagram Type.  This is just for speed */\r\n"
	    + "And\r\n"
	    + "TheEntity.id = ?											/* Get the specific Diagram from the ID from previous query and user selection */\r\n"
	    + "And\r\n" + "TheXML.id = TheEntity.id \r\n" + "and\r\n"
	    + "imagefile.name like concat(Left(Right(TheXML.Diagram_File,len(thexml.diagram_file)-2),8),'%')";

    public List<Map<String, Object>> getDefinitionTypes(String encOption) {
	String encyclopedia = encOption;
	String workspaceId = "1";
	if (encOption.indexOf("/") > 1) {
	    encyclopedia = encOption.substring(0, encOption.indexOf("/"));
	    workspaceId = encOption.substring(encOption.indexOf("/") + 1);
	}
	if ("1".equals(workspaceId)) {
	    return jdbcTemplatesMap.get(encyclopedia).queryForList(DEFINITION_TYPES_QUERY_NO_WID);
	} else {
	    return jdbcTemplatesMap.get(encyclopedia).queryForList(DEFINITION_TYPES_QUERY,
		    Integer.valueOf(workspaceId));
	}
    }

    private static String DEFINITION_TYPES_QUERY_NO_WID = "SELECT distinct TheTypes.Name, thetypes.type FROM (Select Type, Class, Activity from entity) as TheEntity, MetaData as TheTypes\r\n"
	    + "Where TheTypes.class = 3 and TheEntity.class = TheTypes.Class and TheEntity.type = TheTypes.Type\r\n"
	    + "and TheEntity.Activity <> 'D' Order by 1";

    private static String DEFINITION_TYPES_QUERY = "SELECT distinct TheTypes.Name, thetypes.type FROM (Select Type, Class, WID , Activity from entity) as TheEntity, MetaData as TheTypes\r\n"
	    + "Where TheTypes.class = 3 and TheEntity.class = TheTypes.Class and TheEntity.type = TheTypes.Type\r\n"
	    + "and TheEntity.WID  = ?  and TheEntity.Activity <> 'D' Order by 1";

     public List<Map<String, Object>> getDifinitionsVearLink(String encOption, String typeNumber) {
	String encyclopedia = encOption.substring(0, encOption.indexOf("/"));
	String workspaceId = encOption.substring(encOption.indexOf("/") + 1);
	List<Map<String, Object>> resultmap = null;
	if ("1".equals(workspaceId)) {
	    resultmap = jdbcTemplatesMap.get(encyclopedia).queryForList(DEFINITIONS_VEAR_LINK_QUERY_NO_WID,
		    Integer.valueOf(typeNumber), Integer.valueOf(typeNumber), Integer.valueOf(typeNumber));
	} else {
	    resultmap = jdbcTemplatesMap.get(encyclopedia).queryForList(DEFINITIONS_VEAR_LINK_QUERY,
		    Integer.valueOf(workspaceId), Integer.valueOf(typeNumber), Integer.valueOf(workspaceId),
		    Integer.valueOf(typeNumber), Integer.valueOf(workspaceId), Integer.valueOf(typeNumber));
	}
	return resultmap;
    }

    private static String DEFINITIONS_VEAR_LINK_QUERY_NO_WID = "Select TheEntity.name, (Select Substring(TheEntity.shortprops, (Select( TheEntity.LinkPos + 15)),(Select (TheEntity.DelPos - (Select( TheEntity.LinkPos + 15)))))) as VEAR_Link\r\n"
	    + "From\r\n"
	    + "(Select Name, ID, Type, Activity, Class, ShortProps, Charindex('[[[VEAR Link]]]',Entity.ShortProps) as LinkPos , Charindex('[[[Deleted in VEAR?]]]',Entity.ShortProps) as DelPos\r\n"
	    + " from Entity Where Entity.ShortProps like '%VEAR Link%' and Entity.ShortProps like '%Deleted in VEAR%' and Charindex('[[[VEAR Link]]]',Entity.ShortProps) <>0 and Charindex('[[[Deleted in VEAR?]]]',Entity.ShortProps)<>0 ) as TheEntity\r\n"
	    + "Where\r\n"
	    + "TheEntity.Class = 3                                              /*Leave set as 3 since this is a definition specific query */\r\n"
	    + "And\r\n"
	    + "TheEntity.type =  ?											/* Set the Desired Definition Type.  This is just for speed */\r\n"
	    + "And\r\n"
	    + "TheEntity.Activity <> 'D'											/* Is the definition marked for deletion? */\r\n"
	    + "UNION\r\n"
	    + "Select TheEntity.name, (Select Substring(TheEntity.Properties, (Select( TheEntity.LinkPos + 15)),(Select (TheEntity.DelPos - (Select( TheEntity.LinkPos + 15)))))) as VEAR_Link\r\n"
	    + "From\r\n"
	    + "(Select Name, ID, Type, Activity, Class, Properties, Charindex('[[[VEAR Link]]]',Entity.Properties) as LinkPos , Charindex('[[[Deleted in VEAR?]]]',Entity.Properties) as DelPos \r\n"
	    + "from Entity Where Entity.Properties like '%VEAR Link%' and Entity.Properties like '%Deleted in VEAR%' and Charindex('[[[VEAR Link]]]',Entity.Properties) <>0 and Charindex('[[[Deleted in VEAR?]]]',Entity.Properties)<>0 ) as TheEntity\r\n"
	    + "Where\r\n"
	    + "TheEntity.Class = 3                                              /*Leave set as 3 since this is a definition specific query */\r\n"
	    + "And\r\n"
	    + "TheEntity.type =  ?											/* Set the Desired Definition Type.  This is just for speed */\r\n"
	    + "And\r\n"
	    + "TheEntity.Activity <> 'D'											/* Is the definition marked for deletion? */\r\n"
	    + "UNION\r\n" + "Select TheEntity.name, '' as VEAR_Link\r\n" + "From\r\n"
	    + "(Select Name, ID, Type, Activity, Class\r\n"
	    + "from Entity Where (Charindex('aac.dva.va.gov/ee/request/',Entity.Properties) = 0 AND Charindex('aac.dva.va.gov/ee/request/',Entity.ShortProps)=0) )as TheEntity\r\n"
	    + "Where\r\n"
	    + "TheEntity.Class = 3                                              /*Leave set as 3 since this is a definition specific query */\r\n"
	    + "And\r\n"
	    + "TheEntity.type =  ?											/* Set the Desired Definition Type.  This is just for speed */\r\n"
	    + "And\r\n" + "TheEntity.Activity <> 'D'	\r\n" + "order by 1";

    private static String DEFINITIONS_VEAR_LINK_QUERY = "Select TheEntity.name, (Select Substring(TheEntity.shortprops, (Select( TheEntity.LinkPos + 15)),(Select (TheEntity.DelPos - (Select( TheEntity.LinkPos + 15)))))) as VEAR_Link\r\n"
	    + "From\r\n"
	    + "(Select Name, ID, WID, Type, Activity, Class, ShortProps, Charindex('[[[VEAR Link]]]',Entity.ShortProps) as LinkPos , Charindex('[[[Deleted in VEAR?]]]',Entity.ShortProps) as DelPos\r\n"
	    + " from Entity Where Entity.ShortProps like '%VEAR Link%' and Entity.ShortProps like '%Deleted in VEAR%' and Charindex('[[[VEAR Link]]]',Entity.ShortProps) <>0 and Charindex('[[[Deleted in VEAR?]]]',Entity.ShortProps)<>0 ) as TheEntity\r\n"
	    + "Where\r\n"
	    + "TheEntity.wid = ?                                                /* Set the Desired Workspace ID */\r\n"
	    + "and\r\n"
	    + "TheEntity.Class = 3                                              /*Leave set as 3 since this is a definition specific query */\r\n"
	    + "And\r\n"
	    + "TheEntity.type =  ?											/* Set the Desired Definition Type.  This is just for speed */\r\n"
	    + "And\r\n"
	    + "TheEntity.Activity <> 'D'											/* Is the definition marked for deletion? */\r\n"
	    + "UNION\r\n"
	    + "Select TheEntity.name, (Select Substring(TheEntity.Properties, (Select( TheEntity.LinkPos + 15)),(Select (TheEntity.DelPos - (Select( TheEntity.LinkPos + 15)))))) as VEAR_Link\r\n"
	    + "From\r\n"
	    + "(Select Name, ID, WID, Type, Activity, Class, Properties, Charindex('[[[VEAR Link]]]',Entity.Properties) as LinkPos , Charindex('[[[Deleted in VEAR?]]]',Entity.Properties) as DelPos \r\n"
	    + "from Entity Where Entity.Properties like '%VEAR Link%' and Entity.Properties like '%Deleted in VEAR%' and Charindex('[[[VEAR Link]]]',Entity.Properties) <>0 and Charindex('[[[Deleted in VEAR?]]]',Entity.Properties)<>0 ) as TheEntity\r\n"
	    + "Where\r\n"
	    + "TheEntity.wid = ?                                              /* Set the Desired Workspace ID */\r\n"
	    + "and\r\n"
	    + "TheEntity.Class = 3                                              /*Leave set as 3 since this is a definition specific query */\r\n"
	    + "And\r\n"
	    + "TheEntity.type =  ?											/* Set the Desired Definition Type.  This is just for speed */\r\n"
	    + "And\r\n"
	    + "TheEntity.Activity <> 'D'											/* Is the definition marked for deletion? */\r\n"
	    + "UNION\r\n" + "Select TheEntity.name, '' as VEAR_Link\r\n" + "From\r\n"
	    + "(Select Name, ID, WID, Type, Activity, Class\r\n"
	    + "from Entity Where (Charindex('aac.dva.va.gov/ee/request/',Entity.Properties) = 0 AND Charindex('aac.dva.va.gov/ee/request/',Entity.ShortProps)=0) )as TheEntity\r\n"
	    + "Where\r\n"
	    + "TheEntity.wid = ?                                                /* Set the Desired Workspace ID */\r\n"
	    + "and\r\n"
	    + "TheEntity.Class = 3                                              /*Leave set as 3 since this is a definition specific query */\r\n"
	    + "And\r\n"
	    + "TheEntity.type =  ?											/* Set the Desired Definition Type.  This is just for speed */\r\n"
	    + "And\r\n" + "TheEntity.Activity <> 'D'	\r\n" + "order by 1";

    public boolean isSADBAccessible() {
	Timestamp t = null;
	try {
	    t = jdbcTemplatesMap.get("VAEA_Workspaces").queryForObject("SELECT GETDATE() ", Timestamp.class);
	} catch (Exception e) {
	    LOG.log(Level.SEVERE, "Connection failed", e);
	    LOG.log(Level.SEVERE, "Not able to establish connection to SA SQL server Database.");
	    return false;
	}

	if (t != null) {
	    LOG.log(Level.INFO, "Successfully connected to SA SQL server Database.");
	    return true;
	}
	return false;
    }

}
