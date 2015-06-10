package ru.trylogic.spring.boot.dockerproperties;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.InspectContainerResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.PropertySource;

@Slf4j
public class DockerPropertySource extends PropertySource<DockerClient> {

    public static final String DOCKER_PREFIX = "docker.";

    final DockerClient dockerClient;

    public DockerPropertySource(String name, DockerClient dockerClient) {
        super(name);
        this.dockerClient = dockerClient;
    }

    @Override
    public Object getProperty(String name) {
        if (!name.startsWith(DOCKER_PREFIX)) {
            return null;
        }

        String[] parts = name.split("\\.");

        if (parts.length < 3) {
            return null;
        }

        String containerName = parts[1];

        final InspectContainerResponse response;
        try {
            response = dockerClient.inspectContainerCmd(containerName).exec();
        } catch (Exception e) {
            log.warn("Failed to get property from Docker", e);
            return null;
        }

        String subproperty = parts[2];
        switch (subproperty) {
            case "ip_address":
                return response.getNetworkSettings().getIpAddress();
            default:
                log.warn("Unknown docker subproperty: {}", subproperty);
                return null;
        }
    }
};
