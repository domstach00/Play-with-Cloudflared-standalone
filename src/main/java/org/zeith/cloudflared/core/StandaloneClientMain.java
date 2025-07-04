package org.zeith.cloudflared.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeith.cloudflared.core.config.StandaloneAppConfig;
import org.zeith.cloudflared.core.config.StandaloneConfigManager;
import org.zeith.cloudflared.core.service.StandaloneTunnelService;

public class StandaloneClientMain {
    private static final Logger LOGGER = LogManager.getLogger("StandaloneCloudflaredClient");

    public static void main(String[] args) {

        if (args.length >= 2 ) {
            LOGGER.info("Passed host and port arguments, creating a 'client.properties'.");
            String hostArg = args[0];
            String portArg = args[1];
            StandaloneConfigManager.createConfig(hostArg, portArg);
        }

        if (!StandaloneConfigManager.configurationExists()) {
            LOGGER.info("Configuration file not found. Creating a default 'client.properties'.");
            LOGGER.info("Please edit this file with your server details and restart the application.");
            StandaloneConfigManager.createDefaultConfig();
            System.out.println("Go to configure 'standalone-client.properties' file.");
            return;
        }

        StandaloneAppConfig config = StandaloneConfigManager.loadConfig();

        if (!config.isValid()) {
            LOGGER.error("Configuration is invalid. Please set 'remote_hostname' in 'client.properties' to your server's public address.");
            return;
        }

        try {
            StandaloneTunnelService standaloneTunnelService = new StandaloneTunnelService(config);
            standaloneTunnelService.startTunnel();
        } catch (Exception e) {
            LOGGER.error("An unexpected error occurred:", e);
        }
    }
}