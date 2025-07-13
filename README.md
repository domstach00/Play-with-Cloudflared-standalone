# Cloudflared Game Client

This application solves a common problem for gamers: connecting to a game server that is hosted behind a Cloudflare Tunnel. Many games only allow connecting via an IP address and port, and cannot connect to a URL (like `my-server.trycloudflare.com`). 

This tool works for **any TCP-based game** (e.g., 7 Days to Die, Minecraft, Terraria, Valheim, etc.) by creating a local proxy on your computer. You connect your game to a local address (e.g., `127.0.0.1:26900`), and the application securely forwards all traffic to the remote game server through the Cloudflare network.

## How to Use

1.  **Download:** Grab the latest `standalone-cloudflared-client-1.0.0.jar` from the releases page or build it yourself. Place it in a convenient folder.

2.  **First Run (Configuration):** Run the application from your terminal:
    ```bash
    java -jar standalone-cloudflared-client-1.0.0.jar
    ```
    The application will notice that there is no configuration file. It will create a `standalone-client.properties` file in the same directory and then exit.

3.  **Edit Configuration:** Open the newly created `standalone-client.properties` file with a text editor. You will see the following content:
    ```properties
    #Cloudflared Client Configuration
    remote_hostname=your.server.com_default
    local_port=25565
    ```
    *   Change `your.server.com_default` to the public hostname of your game server (e.g., `my.game.pl`).
    *   The `local_port` is the port the application will open on your computer. The default `25565` is for Minecraft. Change it if your game uses a different port. This is not required, but is recommended.

4.  **Run the Application:** With the configuration saved, run the application again:
    ```bash
    java -jar standalone-cloudflared-client-1.0.0.jar
    ```
    Alternatively, you can specify the remote hostname and local port directly as arguments, overriding the `client.properties` file:
    ```bash
    java -jar standalone-cloudflared-client-1.0.0.jar <remote_hostname> <local_port>
    ```
    For example:
    ```bash
    java -jar standalone-cloudflared-client-1.0.0.jar my.game.pl 25565
    ```
    You can choose any free local port, but it is recommended to use the default ports for the specific game.
    If everything is correct, you will see a success message telling you which local address to connect to.

5.  **Play the Game:** Leave the application window open. Launch your game and connect to the address shown in the terminal (e.g., `127.0.0.1:26900`).

To stop the tunnel, simply close the application window or press `Ctrl+C` in the terminal.
