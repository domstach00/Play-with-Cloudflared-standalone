package org.zeith.cloudflared.core.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeith.cloudflared.core.CloudflaredAPI;
import org.zeith.cloudflared.core.CloudflaredAPIFactory;
import org.zeith.cloudflared.core.api.StandaloneGameProxy;
import org.zeith.cloudflared.core.config.StandaloneAppConfig;
import org.zeith.cloudflared.core.exceptions.CloudflaredNotFoundException;
import org.zeith.cloudflared.core.process.CFDAccess;

import java.io.File;

public class StandaloneTunnelService {
    private static final Logger LOGGER = LogManager.getLogger("TunnelService");

    private final StandaloneAppConfig config;

    public StandaloneTunnelService(StandaloneAppConfig config) {
        this.config = config;
    }

    public void startTunnel() throws CloudflaredNotFoundException, InterruptedException {
        LOGGER.info("Initializing CloudflaredAPI...");
        File dataDir = new File(System.getProperty("user.home"), ".cloudflared-client");
        StandaloneGameProxy proxy = new StandaloneGameProxy(dataDir);
        CloudflaredAPI api = CloudflaredAPIFactory.builder()
                .gameProxy(proxy)
                .build()
                .createApi();

        LOGGER.info("Starting access tunnel to {} on local port {}...", config.getRemoteHostname(), config.getLocalPort());

        CFDAccess accessTunnel = new CFDAccess(api, config.getRemoteHostname(), config.getLocalPort());
        accessTunnel.start();

        LOGGER.info("=====================================================================");
        LOGGER.info("SUCCESS! The tunnel is running.");
        LOGGER.info("In your game, connect to the address: 127.0.0.1:{}", config.getLocalPort());
        LOGGER.info("This window must remain open while you play. Press Ctrl+C to exit.");
        LOGGER.info("=====================================================================");

        // Keep the main thread alive. The API's shutdown hook will handle termination.
        Thread.currentThread().join();
    }
}
