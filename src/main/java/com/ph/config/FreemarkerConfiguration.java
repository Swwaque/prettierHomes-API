package com.ph.config;

import freemarker.template.TemplateException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;

import java.io.IOException;

@Configuration
public class FreemarkerConfiguration {
    @Bean
    public FreeMarkerConfigurationFactoryBean freemarkerConfig() {
        FreeMarkerConfigurationFactoryBean configFactoryBean = new FreeMarkerConfigurationFactoryBean();
        configFactoryBean.setTemplateLoaderPath("classpath:/templates");
        configFactoryBean.setDefaultEncoding("UTF-8");
        return configFactoryBean;
    }

    @Bean
    public freemarker.template.Configuration configuration() throws TemplateException, IOException {
        return freemarkerConfig().createConfiguration();
    }
}
