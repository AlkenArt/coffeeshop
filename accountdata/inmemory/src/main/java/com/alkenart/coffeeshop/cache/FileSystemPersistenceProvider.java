package com.alkenart.coffeeshop.cache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.hazelcast.core.MapLoader;
import com.hazelcast.core.MapStore;

@Component
public class FileSystemPersistenceProvider implements MapLoader<String, Object>, MapStore<String, Object> {
	private final static Logger logger = LoggerFactory.getLogger(FileSystemPersistenceProvider.class);
	private String mapName = "default";
	private String path = "default";

	public FileSystemPersistenceProvider() {
	}

	public FileSystemPersistenceProvider(String mapName, String path) {
		this.mapName = mapName;
		this.path = path;
		File dir = new File(getPersistencePath());
		if (!dir.exists())
			dir.mkdir();
	}

	private String getPersistencePath() {
		return this.path;
	}

	@Override
	public void store(String key, Object value) {
		try {
			String path = getPersistencePath() + File.separator + mapName + File.separator;
			File dir = new File(path);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			logger.debug("Storing key {}, value {} in store: {}", key, value, dir);
			OutputStream file = new FileOutputStream(path + key + ".ser");
			OutputStream buffer = new BufferedOutputStream(file);
			ObjectOutput output = new ObjectOutputStream(buffer);
			output.writeObject(value);
			output.close();
			buffer.close();
			file.close();
		} catch (IOException ex) {
			logger.error("Storing key {} value {} in store failed: {}", key, value, ex);
		}
	}

	@Override
	public void storeAll(Map<String, Object> keyValueMap) {
		for (String key : keyValueMap.keySet()) {
			store(key, keyValueMap.get(key));
		}
	}

	@Override
	public void delete(String key) {
		String path = getPersistencePath() + File.separator + mapName + File.separator;
		File file = new File(path + key + ".ser");
		logger.debug("Deleting key : {}", key);
		if (file.exists()) {
			file.delete();
		}
	}

	@Override
	public void deleteAll(Collection<String> keys) {
		for (String key : keys) {
			delete(key);
		}
	}

	@Override
	public Object load(String key) {
		// I don't implement this because the grid is ALL IN CACHE
		// so you will never have something in persistence that is
		// not in memory and don't want a hit on the file system (slow)
		return null;
	}

	public Object loadFromPersistence(String key) {
		String path = getPersistencePath() + File.separator + mapName + File.separator;
		File file = new File(path + key + ".ser");
		if (!file.exists())
			return null;

		try {
			InputStream inputStream = new FileInputStream(path + key + ".ser");
			InputStream buffer = new BufferedInputStream(inputStream);
			ObjectInput input = new ObjectInputStream(buffer);
			logger.debug("Loading key{} from persistence", key);
			Object readObj = input.readObject();
			input.close();
			buffer.close();
			inputStream.close();
			return readObj;
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public Map<String, Object> loadAll(Collection<String> keys) {
		Map<String, Object> values = new HashMap<String, Object>();

		for (String key : keys) {
			values.put(key, loadFromPersistence(key));
		}
		return values;
	}

	@Override
	public Set<String> loadAllKeys() {
		Set<String> keys = new HashSet<String>();
		File dir = new File(getPersistencePath());
		String[] exts = { "ser" };
		Collection<File> files = FileUtils.listFiles(dir, exts, true);
		if (files.isEmpty())
			return keys;

		for (File file : files) {
			if (file.getParentFile().getName().equalsIgnoreCase(mapName)) {
				keys.add(file.getName().substring(0, file.getName().lastIndexOf(".")));
			}
		}
		logger.debug(" Store:  " + mapName + " Loaded keys: " + keys);
		return keys;
	}

	public void clear() {
		File dir = new File(getPersistencePath());
		String[] exts = { "ser" };
		Collection<File> files = FileUtils.listFiles(dir, exts, true);
		for (File file : files) {
			if (file.getParentFile().getName().equalsIgnoreCase(mapName)) {
				file.delete();
			}
		}
	}
}