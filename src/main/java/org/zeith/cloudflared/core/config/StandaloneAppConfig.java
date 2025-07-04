package org.zeith.cloudflared.core.config;

import lombok.Getter;

@Getter
public class StandaloneAppConfig {
    private final String remoteHostname;
    private final int localPort;

    public StandaloneAppConfig(String remoteHostname, int localPort) {
        this.remoteHostname = remoteHostname;
        this.localPort = localPort;
    }

    public boolean isValid() {
        return remoteHostname != null && !remoteHostname.isEmpty() && !remoteHostname.equals(StandaloneConfigManager.DEFAULT_HOST);
    }
}
