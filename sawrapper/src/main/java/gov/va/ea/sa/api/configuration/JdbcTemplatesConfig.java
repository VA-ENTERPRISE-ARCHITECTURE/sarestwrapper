package gov.va.ea.sa.api.configuration;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class JdbcTemplatesConfig implements InitializingBean {

    protected static ApplicationContext ctx = new ClassPathXmlApplicationContext("applicationContext.xml");
    protected static JdbcTemplate vaeawJdbcTemplate = (JdbcTemplate) ctx.getBean("vaeawJdbcTemplate");
    protected static JdbcTemplate fmbtJdbcTemplate = (JdbcTemplate) ctx.getBean("fmbtJdbcTemplate");
    protected static JdbcTemplate eaMetaJdbcTemplate = (JdbcTemplate) ctx.getBean("eaMetaJdbcTemplate");
    protected static JdbcTemplate ncaJdbcTemplate = (JdbcTemplate) ctx.getBean("ncaJdbcTemplate");
    protected static JdbcTemplate vbaJdbcTemplate = (JdbcTemplate) ctx.getBean("vbaJdbcTemplate");

    Map<String, JdbcTemplate> jdbcTemplatesMap;

    @Override
    public void afterPropertiesSet() throws Exception {
	jdbcTemplatesMap = new HashMap<>();
	jdbcTemplatesMap.put("VAEA_Workspaces", vaeawJdbcTemplate);
	jdbcTemplatesMap.put("VEAR_FMBT_Baseline", fmbtJdbcTemplate);
	jdbcTemplatesMap.put("VEAR_MetaModel", eaMetaJdbcTemplate);
	jdbcTemplatesMap.put("VEAR_NCA", ncaJdbcTemplate);
	jdbcTemplatesMap.put("VEAR_VBA_Workspaces", vbaJdbcTemplate);
    }

    @Bean(name = "jdbcTemplatesMap")
    public Map<String, JdbcTemplate> getJdbcTemplatesMap() {
	return jdbcTemplatesMap;

    }

}
