package org.zeith.cloudflared.core.api;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StandaloneGameProxy implements IGameProxy {
    private static final Logger LOGGER = LogManager.getLogger("StandaloneProxy");
    private static final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
    private final List<IGameListener> listeners = new ArrayList<>();
    private final File dataDir;

    public StandaloneGameProxy(File dataDir) {
        this.dataDir = dataDir;
        this.dataDir.mkdirs();
    }

    @Override
    public ExecutorService getBackgroundExecutor() {
        return EXECUTOR;
    }

    @Override
    public File getExtraDataDir() {
        return dataDir;
    }

    @Override
    public void addListener(IGameListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(IGameListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void sendChatMessage(String message) {
        LOGGER.info(message);
    }

    @Override
    public void createToast(InfoLevel level, String title, String subtitle) {
        LOGGER.info("[{}] {}: {}", level, title, subtitle);
    }
}