/*
 * Copyright 2018 ARRIS Enterprises, Inc. All rights reserved.
 *
 * This program is confidential and proprietary to ARRIS Enterprises, Inc. (ARRIS), 
 * and may not be copied, reproduced, modified, disclosed to others, 
 * published or used, in whole or in part, without the express prior 
 * written permission of ARRIS.
 * 
 * CHANGE HISTORY:
 * Date            Author                      Changes
 * -------------------------------------------------------------------------------------
 * 06/27/2017      Lokesh Nayak           Initial creation
 * -------------------------------------------------------------------------------------
 */
package com.alkenart.coffeeshop.cache;

/**
 * 
 * Hazelcast main config file to instantiate the instance
 * 
 * @author lokesh
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alkenart.coffeeshop.config.ApplicationConstants;
import com.alkenart.coffeeshop.config.service.ConfigServiceImpl;
import com.hazelcast.config.Config;
import com.hazelcast.config.InterfacesConfig;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.MulticastConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spring.cache.HazelcastCacheManager;

@Configuration
public class HazelcastConfig {

	@Autowired
	ConfigServiceImpl configService;

	@Bean
	public HazelcastInstance hazelcastInstance() {
		Config config = new Config();

		NetworkConfig networkConfig = new NetworkConfig();

		MulticastConfig multicastConfig = new MulticastConfig();
		multicastConfig.setEnabled(false);

		TcpIpConfig tcpIpConfig = new TcpIpConfig();
		tcpIpConfig.setEnabled(true);

		InterfacesConfig interfacesConfig = new InterfacesConfig();
		interfacesConfig.setEnabled(true);

		for (String member : configService.getHazelcastMembers()) {
			tcpIpConfig.addMember(member);
			interfacesConfig.addInterface(member);
		}

		JoinConfig joinConfig = new JoinConfig();
		joinConfig.setMulticastConfig(multicastConfig);
		joinConfig.setTcpIpConfig(tcpIpConfig);

		networkConfig.setPortAutoIncrement(true);
		networkConfig.setJoin(joinConfig);
		networkConfig.setPort(configService.getHCInstanceBasePort());
		networkConfig.setPortCount(configService.getHCInstanceNumbers());

		networkConfig.setInterfaces(interfacesConfig);

		config.setNetworkConfig(networkConfig);

		String persistencePath = configService.getHazelcastPersistenceDir();

		MapStoreConfig orderMapStoreConfig = new MapStoreConfig();
		orderMapStoreConfig.setEnabled(true);
		orderMapStoreConfig.setImplementation(
				new FileSystemPersistenceProvider(ApplicationConstants.ORDER_DATA_STORE, persistencePath));

		config.getMapConfig(ApplicationConstants.ORDER_DATA_STORE).setMapStoreConfig(orderMapStoreConfig)
				.setTimeToLiveSeconds(new Integer(configService.getHazelcastAPIDataPersistenceTTL()) * 24 * 60 * 60);

		MapStoreConfig userMapStoreConfig = new MapStoreConfig();
		userMapStoreConfig.setEnabled(true);
		userMapStoreConfig.setImplementation(
				new FileSystemPersistenceProvider(ApplicationConstants.USER_DATA_STORE, persistencePath));

		config.getMapConfig(ApplicationConstants.USER_DATA_STORE).setMapStoreConfig(userMapStoreConfig)
				.setTimeToLiveSeconds(new Integer(configService.getHazelcastAPIDataPersistenceTTL()) * 24 * 60 * 60);

		return Hazelcast.newHazelcastInstance(config);
	}

	@Bean
	public CacheManager cacheManager() {
		return new HazelcastCacheManager(hazelcastInstance());
	}
}
