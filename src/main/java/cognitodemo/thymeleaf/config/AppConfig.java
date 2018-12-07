/** \file
 * 
 * Nov 27, 2018
 *
 * Copyright Ian Kaplan 2018
 *
 * @author Ian Kaplan, www.bearcave.com, iank@bearcave.com
 */
package cognitodemo.thymeleaf.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * <h4>
 * AppConfig
 * </h4>
 * <p>
 * Configuration for the Thymeleaf processor.
 * </p>
 * <p>
 * Nov 29, 2018
 * </p>
 * 
 * @author Ian Kaplan, iank@bearcave.com
 */
@Configuration
@EnableWebMvc
@ComponentScan("bonbinibay.controller")
public class AppConfig implements WebMvcConfigurer, ApplicationContextAware {
    private ApplicationContext applicationContext = null;
    
    /**
     * This method is needed to allow the resource files (css, js, img) to be loaded vial the @ link annotation
     * in the .html files.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
        .addResourceHandler("/**")
        .addResourceLocations("/resources/css", "/resources/js", "/resources/img")
        .resourceChain(true);
    }
    
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    
    @Bean
    @Description("Thymeleaf Template Resolver")
    public SpringResourceTemplateResolver templateResolver() {
       SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
       templateResolver.setApplicationContext(applicationContext);
       templateResolver.setPrefix("/WEB-INF/views/");
       templateResolver.setSuffix(".html");
       templateResolver.setTemplateMode(TemplateMode.HTML);
       // Template cache is true by default. Set to false if you want
       // templates to be automatically updated when modified.
       templateResolver.setCacheable(false);
       return templateResolver;
    }
    
    /**
     * <p>
     * From https://www.baeldung.com/thymeleaf-in-spring-mvc
     * </p>
     * <blockquote>
     * The th:text=”#{key}” tag attribute can be used to display values from property files. For this to work the property file must be 
     * configured as messageSource bean.
     * </blockquote>
     * 
     */
    @Bean
    @Description("Spring Message Resolver")
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages");
        return messageSource;
    }
    
    /**
     * Examples and documentation list this as a bean (@Bean). However, when this is done with Spring 4
     * there is an error stating that the bean is already defined. However, this function is needed for the
     * viewResolver(), since it returns a locally constructed SpringTemplateEngine object.
     */
    @Description("Thymeleaf Template Engine")
    public SpringTemplateEngine templateEngine() {
       SpringTemplateEngine templateEngine = new SpringTemplateEngine();
       templateEngine.setTemplateResolver(templateResolver());
       templateEngine.setTemplateEngineMessageSource(messageSource());
       templateEngine.setEnableSpringELCompiler(true);
       return templateEngine;
    }

    
    @Bean
    @Description("Thymeleaf View Resolver")
    public ThymeleafViewResolver viewResolver(){
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setTemplateEngine(templateEngine());
        // NOTE 'order' and 'viewNames' are optional
        viewResolver.setOrder(1);
        viewResolver.setViewNames(new String[] {".html"});
        return viewResolver;
    }

}
