package vnpt.net.syndata.configuration;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import vnpt.net.syndata.RunApplication;

public class ServletInitializer extends SpringBootServletInitializer {
 
     @Override
     protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
         return application.sources(RunApplication.class);
     }
}