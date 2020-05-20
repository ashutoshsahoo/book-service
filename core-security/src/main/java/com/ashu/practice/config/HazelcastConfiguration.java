package com.ashu.practice.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class HazelcastConfiguration {

	@Bean
	@ConditionalOnProperty(name = "discovery-type", prefix = "hazelcast", havingValue = "multicast", matchIfMissing = true)
	public Config hazelCastConfig(HazelcastConfigProperties configProperties) {
		log.debug("Loading multicast configuration");
		Config config = new Config();
		config.setClusterName(configProperties.getClusterName());
		config.setInstanceName(configProperties.getInstanceName());
		MapConfig mapConfig = new MapConfig();
		mapConfig.setName(configProperties.getMapName()).setBackupCount(2).setTimeToLiveSeconds(300); //TODO: update to 300
		config.addMapConfig(mapConfig);
		return config;
	}

	@Bean
	@ConditionalOnProperty(name = "discovery-type", prefix = "hazelcast", havingValue = "kubernetes")
	public Config config(HazelcastConfigProperties configProperties) {
		log.debug("Loading kubernetes configuration");
		Config config = new Config();
		config.setClusterName(configProperties.getClusterName());
		config.setInstanceName(configProperties.getInstanceName());
		JoinConfig joinConfig = config.getNetworkConfig().getJoin();
		joinConfig.getTcpIpConfig().setEnabled(false);
		joinConfig.getMulticastConfig().setEnabled(false);
		joinConfig.getKubernetesConfig().setEnabled(true).setProperty("namespace", configProperties.getNamespace())
				.setProperty("service-name", configProperties.getServiceName());
		MapConfig mapConfig = new MapConfig();
		mapConfig.setName(configProperties.getMapName()).setBackupCount(2).setTimeToLiveSeconds(300);
		config.addMapConfig(mapConfig);
		return config;
	}

}
