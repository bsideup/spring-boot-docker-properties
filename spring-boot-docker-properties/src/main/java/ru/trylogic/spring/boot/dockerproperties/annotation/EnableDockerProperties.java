package ru.trylogic.spring.boot.dockerproperties.annotation;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import ru.trylogic.spring.boot.dockerproperties.DockerPropertiesAutoConfiguration;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Configuration
@Import({DockerPropertiesAutoConfiguration.class})
public @interface EnableDockerProperties {
}
