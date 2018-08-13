package com.alkenart.coffeeshop.config.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value = "classpath:hazelcast-server.properties")
public class ConfigServiceImpl {

	@Value("${hazelcast.persistence.dir}")
	private String hazelcastPersistenceDir;

	@Value("${hazelcast.persistence.report.data.ttl:30}")
	private String hazelcastAPIDataPersistenceTTL;

	@Value("${hazelcast.instance.members:[127.0.0.1]}")
	private String[] hazelcastMembers;

	@Value("${hazelcast.instance.numbers:4}")
	private int HCInstanceNumbers;

	@Value("${hazelcast.instance.baseport:5701}")
	private int HCInstanceBasePort;

	public int getHCInstanceBasePort() {
		return HCInstanceBasePort;
	}

	public void setHCInstanceBasePort(int hCInstanceBasePort) {
		HCInstanceBasePort = hCInstanceBasePort;
	}

	public int getHCInstanceNumbers() {
		return HCInstanceNumbers;
	}

	public void setHCInstanceNumbers(int hCInstanceNumbers) {
		HCInstanceNumbers = hCInstanceNumbers;
	}

	public String[] getHazelcastMembers() {
		return hazelcastMembers;
	}

	public void setHazelcastMembers(String[] hazelcastMembers) {
		this.hazelcastMembers = hazelcastMembers;
	}

	public String getHazelcastPersistenceDir() {
		return hazelcastPersistenceDir;
	}

	public void setHazelcastPersistenceDir(String hazelcastPersistenceDir) {
		this.hazelcastPersistenceDir = hazelcastPersistenceDir;
	}

	public String getHazelcastAPIDataPersistenceTTL() {
		return hazelcastAPIDataPersistenceTTL;
	}

	public void setHazelcastAPIDataPersistenceTTL(String hazelcastAPIDataPersistenceTTL) {
		this.hazelcastAPIDataPersistenceTTL = hazelcastAPIDataPersistenceTTL;
	}

	public ConfigServiceImpl() {
	}
}