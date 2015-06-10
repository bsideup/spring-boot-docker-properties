package ru.trylogic.spring.boot.dockerproperties;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SystemEnvironmentPropertySource;

@Configuration
@Slf4j
public class DockerPropertiesAutoConfiguration {

    @Autowired
    private ConfigurableEnvironment environment;

    @Bean
    @ConditionalOnMissingBean
    public DockerClient dockerClient() {
        return DockerClientBuilder.getInstance().build();
    }

    @Bean
    public PropertySource<DockerClient> dockerClientPropertySource(final DockerClient dockerClient) {
        PropertySource<DockerClient> propertySource = new DockerPropertySource("docker", dockerClient);

        MutablePropertySources propertySources = environment.getPropertySources();

        String systemEnvironmentPropertySourceName = null;

        for (PropertySource<?> source : propertySources) {
            if (source instanceof SystemEnvironmentPropertySource) {
                systemEnvironmentPropertySourceName = source.getName();
                break;
            }
        }

        if (systemEnvironmentPropertySourceName != null) {
            propertySources.addAfter(systemEnvironmentPropertySourceName, propertySource);
        } else {
            propertySources.addFirst(propertySource);
        }
        return propertySource;
    }
}
