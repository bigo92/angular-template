package vnpt.net.syndata.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

import javax.sql.DataSource;
import javax.naming.NamingException;

@Configuration
public class SpringMVCConfiguration {
    @Autowired
    private Environment env;

    @Bean
    public HttpFirewall defaultHttpFirewall() {
        return new DefaultHttpFirewall();
    }

    @Bean(name = "dataSource", destroyMethod = "")
    @Primary
    public DataSource dataSource() throws IllegalArgumentException, NamingException{
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getProperty("spring.datasource.driver"));
        dataSource.setUrl(env.getProperty("spring.datasource.url")); //jdbc:mysql://localhost:3306/myDB
        dataSource.setUsername(env.getProperty("spring.datasource.username"));
        dataSource.setPassword(env.getProperty("spring.datasource.password"));
        return dataSource;
    }
//
//    @Bean("transactionManager")
//    public DataSourceTransactionManager transactionManager() throws IllegalArgumentException, NamingException {
//        DataSourceTransactionManager txManager = new DataSourceTransactionManager();
//
//        DataSource dataSource = this.dataSource();
//        txManager.setDataSource(dataSource);
//
//        return txManager;
//    }

//    @Bean("namedParameterJdbcTemplate")
//    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() throws IllegalArgumentException, NamingException {
//        return new NamedParameterJdbcTemplate(dataSource());
//    }
}
