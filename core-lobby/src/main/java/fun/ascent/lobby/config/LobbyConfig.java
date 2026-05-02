package fun.ascent.lobby.config;

import net.minestom.server.Auth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public record LobbyConfig(String host, int port, String velocitySecret) {

    private static final Path CONFIG_FILE = Path.of("lobby.properties");

    public static LobbyConfig load() {
        createDefaultConfig();

        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + CONFIG_FILE.toAbsolutePath(), e);
        }

        String host = envOrProperty("ASCENT_SERVER_HOST", properties, "server.host", "0.0.0.0");
        int port = Integer.parseInt(envOrProperty("ASCENT_SERVER_PORT", properties, "server.port", "25567"));
        port = findFreePort(port);
        String secret = resolveVelocitySecret(properties);
        return new LobbyConfig(host, port, secret);
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

        String secretFile = envOrProperty("ASCENT_VELOCITY_SECRET_FILE", properties, "velocity.secret-file", "forwarding.secret");
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

    private static String envOrProperty(String envName, Properties properties, String propertyName, String fallback) {
        String env = System.getenv(envName);
        if (env != null && !env.isBlank()) {
            return env.trim();
        }
        return properties.getProperty(propertyName, fallback).trim();
    }

    /**
     * Tries to bind to {@code preferred}. If it is already in use,
     * scans upward from 25565 until a free port is found.
     */
    private static int findFreePort(int preferred) {
        if (isPortAvailable(preferred)) {
            return preferred;
        }
        System.out.println("[Lobby] Port " + preferred + " is busy, scanning for a free port...");
        for (int p = 25565; p <= 65535; p++) {
            if (isPortAvailable(p)) {
                System.out.println("[Lobby] Found free port: " + p);
                return p;
            }
        }
        throw new IllegalStateException("No free port found in range 25565-65535");
    }

    private static boolean isPortAvailable(int port) {
        try (java.net.ServerSocket socket = new java.net.ServerSocket(port)) {
            socket.setReuseAddress(true);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static void createDefaultConfig() {
        if (Files.exists(CONFIG_FILE)) {
            return;
        }

        Properties properties = new Properties();
        properties.setProperty("server.host", "0.0.0.0");
        properties.setProperty("server.port", "25567");
        properties.setProperty("velocity.secret-file", "forwarding.secret");
        properties.setProperty("velocity.secret", "");

        try (OutputStream output = Files.newOutputStream(CONFIG_FILE)) {
            properties.store(output, "Ascent Lobby backend settings");
        } catch (IOException e) {
            throw new IllegalStateException("Could not create " + CONFIG_FILE.toAbsolutePath(), e);
        }
    }
}
