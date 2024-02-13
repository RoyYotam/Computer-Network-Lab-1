import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigReader {

    private static final String CONFIG_FILE_PATH = "config.ini";
    private final Map<String, String> configMap;

    public ConfigReader() throws IOException {
        configMap = readConfig();
    }

    private Map<String, String> readConfig() throws IOException {
        Map<String, String> map = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(CONFIG_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Assuming each line in the config file is in the format key=value
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    map.put(key, value);
                }
            }
        }

        return map;
    }

    public String get(String key) {
        return configMap.get(key);
    }
}
