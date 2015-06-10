package ru.trylogic.spring.boot.dockerproperties;

import com.github.dockerjava.api.DockerClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.trylogic.spring.boot.dockerproperties.annotation.EnableDockerProperties;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DockerPropertiesAutoConfigurationTest.class)
@IntegrationTest
@EnableDockerProperties
public class DockerPropertiesAutoConfigurationTest {

    @Autowired
    Environment environment;

    @Autowired
    DockerClient dockerClient;

    @Test
    public void testDockerPropertiesLookup() {
        String containerId = dockerClient.createContainerCmd("busybox").withCmd("top").exec().getId();

        dockerClient.startContainerCmd(containerId).exec();

        try {
            String property = environment.getProperty("docker." + containerId + ".ip_address");
            assertNotNull(property);
        } finally {
            dockerClient.stopContainerCmd(containerId);
            dockerClient.removeContainerCmd(containerId);
        }
    }
}