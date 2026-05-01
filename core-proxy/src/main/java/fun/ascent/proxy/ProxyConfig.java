package fun.ascent.proxy;

import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

record ProxyConfig(List<ProxyRoute> routes, String defaultServer) {

    private static final String CONFIG_FILE = "ascent-proxy.properties";

    static ProxyConfig load(Path dataDirectory, Logger logger) {
        Path configFile = dataDirectory.resolve(CONFIG_FILE);
        createDefaultConfig(configFile, logger);

        Properties loadedProperties = new Properties();
        try (InputStream input = Files.newInputStream(configFile)) {
            loadedProperties.load(input);
        } catch (IOException e) {
            logger.warn("Could not read {}. Falling back to default proxy routes.", configFile, e);
            loadedProperties = defaults();
        }

        final Properties properties = loadedProperties;
        String defaultServer = properties.getProperty("default-server", "lobby").trim();
        List<ProxyRoute> routes = new ArrayList<>();

        properties.stringPropertyNames().stream()
                .filter(name -> name.startsWith("server."))
                .sorted(Comparator.naturalOrder())
                .forEach(name -> {
                    String target = name.substring("server.".length()).trim();
                    List<String> commands = split(properties.getProperty(name));
                    if (!target.isBlank() && !commands.isEmpty()) {
                        routes.add(new ProxyRoute(target, commands));
                    }
                });

        if (routes.isEmpty()) {
            routes.add(new ProxyRoute("lobby", List.of("lobby", "hub")));
            routes.add(new ProxyRoute("skyblock", List.of("skyblock", "sb", "island")));
        }

        return new ProxyConfig(List.copyOf(routes), defaultServer);
    }

    private static void createDefaultConfig(Path configFile, Logger logger) {
        if (Files.exists(configFile)) {
            return;
        }

        try {
            Files.createDirectories(configFile.getParent());
            try (OutputStream output = Files.newOutputStream(configFile)) {
                defaults().store(output, "Ascent Velocity command routing");
            }
        } catch (IOException e) {
            logger.warn("Could not create default proxy config at {}.", configFile, e);
        }
    }

    private static Properties defaults() {
        Properties properties = new Properties();
        properties.setProperty("default-server", "lobby");
        properties.setProperty("server.lobby", "lobby,hub");
        properties.setProperty("server.skyblock", "skyblock,sb,island");
        return properties;
    }

    private static List<String> split(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }

        List<String> values = new ArrayList<>();
        for (String part : value.split(",")) {
            String trimmed = part.trim().toLowerCase();
            if (!trimmed.isBlank() && !values.contains(trimmed)) {
                values.add(trimmed);
            }
        }
        return values;
    }
}
