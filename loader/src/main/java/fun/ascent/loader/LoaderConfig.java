package fun.ascent.loader;

import lombok.Getter;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Getter
public class LoaderConfig {
    private String redisHost = "127.0.0.1";
    private int redisPort = 6379;
    private String mongodbUri = "mongodb://127.0.0.1:27017";

    public static LoaderConfig load() {
        File file = new File("global_config.json");
        LoaderConfig config = new LoaderConfig();
        
        if (!file.exists()) {
            config.save();
            return config;
        }

        try {
            String content = Files.readString(file.toPath());
            JSONObject json = new JSONObject(content);
            config.redisHost = json.optString("redis_host", "127.0.0.1");
            config.redisPort = json.optInt("redis_port", 6379);
            config.mongodbUri = json.optString("mongodb_uri", "mongodb://127.0.0.1:27017");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return config;
    }

    public void save() {
        JSONObject json = new JSONObject();
        json.put("redis_host", redisHost);
        json.put("redis_port", redisPort);
        json.put("mongodb_uri", mongodbUri);
        
        try {
            Files.writeString(new File("global_config.json").toPath(), json.toString(4));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
