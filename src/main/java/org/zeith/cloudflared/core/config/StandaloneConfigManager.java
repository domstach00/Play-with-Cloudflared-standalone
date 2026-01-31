package org.zeith.cloudflared.core.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class StandaloneConfigManager {
    private static final Logger LOGGER = LogManager.getLogger("ConfigManager");

    public static final String DEFAULT_HOST = "your.server.com_default";
    public static final String DEFAULT_PORT = "25565"; // Default for Minecraft server

    private static Path getAppDir() {
        try {
            URI uri = StandaloneConfigManager.class.getProtectionDomain()
                    .getCodeSource().getLocation().toURI();

            Path path = Paths.get(uri);
            return Files.isDirectory(path) ? path : path.getParent();
        } catch (URISyntaxException e) {
            LOGGER.warn("Cannot resolve app directory from CodeSource. Falling back to user.dir", e);
            return Paths.get(System.getProperty("user.dir"));
        }
    }

    private static Path getConfigPath() {
        return getAppDir().resolve("standalone-client.properties");
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean configurationExists() {
        return Files.exists(getConfigPath());
    }

    public static void ensureConfigExists() {
        if (!configurationExists()) {
            LOGGER.info("Config not found. Creating default at {}", getConfigPath().toAbsolutePath());
            createDefaultConfig();
        }
    }

    public static void createDefaultConfig() {
        createConfig(DEFAULT_HOST, DEFAULT_PORT);
    }

    public static void createConfig(String hostname, String port) {
        if (!isValidHostname(hostname)) {
            LOGGER.warn("Invalid hostname - using default value: {}", DEFAULT_HOST);
            hostname = DEFAULT_HOST;
        }
        if (!isValidPort(port)) {
            LOGGER.warn("Invalid port - using default value: {}", DEFAULT_PORT);
            port = DEFAULT_PORT;
        }

        Properties props = new Properties();
        props.setProperty("remote_hostname", hostname);
        props.setProperty("local_port", port);

        Path cfg = getConfigPath();
        try {
            // upewnij się, że katalog istnieje (zwykle istnieje, ale na wszelki wypadek)
            Files.createDirectories(cfg.getParent());

            try (OutputStream out = Files.newOutputStream(cfg,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                props.store(out, "Cloudflared Client Configuration");
            }
        } catch (IOException e) {
            LOGGER.error("Failed to create configuration file at: " + cfg.toAbsolutePath(), e);
        }
    }

    public static StandaloneAppConfig loadConfig() {
        ensureConfigExists();

        Properties props = new Properties();
        Path cfg = getConfigPath();

        try (InputStream in = Files.newInputStream(cfg)) {
            props.load(in);
        } catch (IOException e) {
            LOGGER.error("Failed to load configuration file at: " + cfg.toAbsolutePath(), e);
            return new StandaloneAppConfig(null, -1);
        }

        String remoteHostname = props.getProperty("remote_hostname");
        String portStr = props.getProperty("local_port", DEFAULT_PORT);

        int localPort;
        try {
            localPort = Integer.parseInt(portStr);
        } catch (NumberFormatException e) {
            LOGGER.warn("Invalid port value in config: {}. Using default {}", portStr, DEFAULT_PORT);
            localPort = Integer.parseInt(DEFAULT_PORT);
        }

        return new StandaloneAppConfig(remoteHostname, localPort);
    }

    private static boolean isValidPort(String port) {
        if (port == null || port.trim().isEmpty()) return false;
        try {
            int p = Integer.parseInt(port.trim());
            return p >= 1 && p <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isValidHostname(String hostname) {
        return hostname != null && !hostname.trim().isEmpty();
    }
}
