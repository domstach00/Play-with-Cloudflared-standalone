package org.zeith.cloudflared.core.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class StandaloneConfigManager {
    private static final Logger LOGGER = LogManager.getLogger("ConfigManager");
    private static final File CONFIG_FILE = new File("standalone-client.properties");

    public static final String DEFAULT_HOST = "your.server.com_default";
    public static final String DEFAULT_PORT = "25565"; // Default for Minecraft server

    public static boolean configurationExists() {
        return CONFIG_FILE.exists();
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
        try (FileOutputStream out = new FileOutputStream(CONFIG_FILE)) {
            props.store(out, "Cloudflared Client Configuration");
        } catch (IOException e) {
            LOGGER.error("Failed to create configuration file:", e);
        }
    }

    public static StandaloneAppConfig loadConfig() {
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(CONFIG_FILE)) {
            props.load(in);
        } catch (IOException e) {
            LOGGER.error("Failed to load configuration file:", e);
            // Return an invalid config object
            return new StandaloneAppConfig(null, -1);
        }

        String remoteHostname = props.getProperty("remote_hostname");
        int localPort = Integer.parseInt(props.getProperty("local_port", DEFAULT_PORT));

        return new StandaloneAppConfig(remoteHostname, localPort);
    }

    private static boolean isValidPort(String port) {
        if (port == null || port.trim().isEmpty()) return false;

        try {
            int portIntValue = Integer.parseInt(port);
            return portIntValue >= 1 && portIntValue <= 65535;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isValidHostname(String hostname) {
        return hostname == null || hostname.trim().isEmpty();
    }
}
