package com.ashu.practice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "hazelcast")
@Data
public class HazelcastConfigProperties {

	private String instanceName = "hazelcast-instance";

	private String mapName = "book-service";

	private String clusterName = "dev";

	private String discoveryType = "multicast";

	private String serviceName = "hazelcast-service";

	private String namespace = "default";

}
