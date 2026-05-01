package fun.ascent.skyblock.config;

import net.minestom.server.Auth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public record ServerConfig(String host, int port, String velocitySecret) {

    private static final Path CONFIG_FILE = Path.of("skyblock.properties");

    public static ServerConfig load() {
        createDefaultConfig();

        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + CONFIG_FILE.toAbsolutePath(), e);
        }

        String host = properties.getProperty("server.host", "0.0.0.0").trim();
        int port = Integer.parseInt(properties.getProperty("server.port", "25566").trim());
        String secret = resolveVelocitySecret(properties);
        return new ServerConfig(host, port, secret);
    }

    public Auth auth() {
        if (velocitySecret == null || velocitySecret.isBlank()) {
            return new Auth.Offline();
        }
        return new Auth.Velocity(velocitySecret);
    }

    private static String resolveVelocitySecret(Properties properties) {
        String envSecret = System.getenv("ASCENT_VELOCITY_SECRET");
        if (envSecret != null && !envSecret.isBlank()) {
            return envSecret.trim();
        }

        String secretFile = properties.getProperty("velocity.secret-file", "forwarding.secret").trim();
        Path path = Path.of(secretFile);
        if (Files.isRegularFile(path)) {
            try {
                return Files.readString(path).trim();
            } catch (IOException e) {
                throw new IllegalStateException("Could not read Velocity forwarding secret from " + path.toAbsolutePath(), e);
            }
        }

        return properties.getProperty("velocity.secret", "").trim();
    }

    private static void createDefaultConfig() {
        if (Files.exists(CONFIG_FILE)) {
            return;
        }

        Properties properties = new Properties();
        properties.setProperty("server.host", "0.0.0.0");
        properties.setProperty("server.port", "25566");
        properties.setProperty("velocity.secret-file", "forwarding.secret");
        properties.setProperty("velocity.secret", "");

        try (OutputStream output = Files.newOutputStream(CONFIG_FILE)) {
            properties.store(output, "Ascent Skyblock backend settings");
        } catch (IOException e) {
            throw new IllegalStateException("Could not create " + CONFIG_FILE.toAbsolutePath(), e);
        }
    }
}
