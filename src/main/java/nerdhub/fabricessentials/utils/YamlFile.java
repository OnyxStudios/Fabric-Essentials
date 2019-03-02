package nerdhub.fabricessentials.utils;

import com.google.common.collect.Maps;

import java.io.*;
import java.util.Map;

public class YamlFile {

    private File yamlFile;
    private Map<String, Object> configValues = Maps.newHashMap();

    public YamlFile(String path) {
        this.yamlFile = new File(path);
    }

    public YamlFile(File file) {
        this.yamlFile = file;
    }

    public File getConfigFile() {
        return yamlFile;
    }

    public boolean exists() {
        return yamlFile.exists();
    }

    public void load() {
        try (BufferedReader reader = new BufferedReader(new FileReader(yamlFile))) {
            String line;

            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("#")) {
                    if (line.contains(":")) {
                        String key = line.split(": ")[0];
                        Object value = line.split(": ")[1];
                        this.configValues.put(key, value);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getInt(String name) {
        if (this.configValues.containsKey(name)) {
            try {
                return Integer.parseInt((String) this.configValues.get(name));
            } catch (NumberFormatException ex) {
                return 0;
            }
        }

        return 0;
    }

    public double getDouble(String name) {
        if (this.configValues.containsKey(name)) {
            try {
                return Double.parseDouble((String) this.configValues.get(name));
            } catch (NumberFormatException ex) {
                return 0;
            }
        }

        return 0;
    }

    public long getLong(String name) {
        if (this.configValues.containsKey(name)) {
            try {
                return Long.parseLong((String) this.configValues.get(name));
            } catch (NumberFormatException ex) {
                return 0;
            }
        }

        return 0;
    }

    public String getString(String name) {
        if (this.configValues.containsKey(name)) {
            return (String) this.configValues.get(name);
        }

        return null;
    }

    public boolean getBoolean(String name) {
        if (this.configValues.containsKey(name)) {
            return Boolean.parseBoolean((String) this.configValues.get(name));
        }

        return false;
    }

    public Object get(String name) {
        if (this.configValues.containsKey(name)) {
            return this.configValues.get(name);
        }

        return null;
    }

    public boolean doesValueExist(String name) {
        return this.configValues.containsKey(name);
    }
}