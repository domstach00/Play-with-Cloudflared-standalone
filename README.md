# Cloudflared Client for Games
[![pl](https://img.shields.io/badge/lang-pl-blue.svg)](./README.pl.md)

## The Problem: Why can't I connect directly?

Cloudflare Tunnels are a great tool for securing servers, but they pose a challenge for games like Minecraft, Terraria, or Valheim. The problem lies in the **mismatch of traffic types**.
<br /> Standard Cloudflare Tunnels are designed primarily for HTTP/HTTPS traffic. Networked games, on the other hand, send data as raw TCP (sometimes UDP) traffic, which is not recognized by the classic HTTP routing in the tunnel.
<br /> Although Cloudflare allows you to configure a TCP tunnel (e.g., using `cloudflared access tcp` or `service: tcp://localhost:PORT`), it still:
- a server-side tunnel does not solve the problem on the player's side
- the game client itself is not able to use the Cloudflare tunnel without an additional proxy

## The Solution

This application eliminates the above limitations by creating a local TCP proxy on the player's computer.
<br /> It works like this:
- The application listens on a specified local port (e.g., 127.0.0.1:25565),
- When the game tries to connect to this port, the application establishes an encrypted connection through the Cloudflare tunnel to the remote server (e.g., my-server.trycloudflare.com),
- The game receives a stable connection as if it were connecting to a local server.

Thanks to this:
- you don't need to open ports or have a public IP,
- you don't need to configure the `cloudflared` client,
- the solution works with any game that uses TCP,
- the player does not need to know about networking - just run the application.

## Requirements
- Java 11 or newer
- Any online game based on TCP protocol

## How to use

1.  **Download:** Download the latest `standalone-cloudflared-client-1.0.0.jar` file from the "releases" section or build it yourself. Place it in a convenient folder.

2.  **First run (Configuration):** Run the application in the terminal:
    ```bash
    java -jar standalone-cloudflared-client-1.0.0.jar
    ```
    The application will notice that there is no configuration file. It will create a `standalone-client.properties` file in the same directory and then exit.

3.  **Edit configuration:** Open the newly created `standalone-client.properties` file with a text editor. You will see the following content:
    ```properties
    #Cloudflared Client Configuration
    remote_hostname=your.server.com_default
    local_port=25565
    ```
    *   Change `your.server.com_default` to the public address of your game server (e.g., `my.game.pl`).
    *   `local_port` is the port that the application will open on your computer. The default value `25565` is for Minecraft. Change it if your game uses a different port. This is not required, but it is recommended.

4.  **Run the application:** After saving the configuration, run the application again:
    ```bash
    java -jar standalone-cloudflared-client-1.0.0.jar
    ```
    Alternatively, you can provide the remote hostname and local port directly as arguments, overriding the `client.properties` file:
    ```bash
    java -jar standalone-cloudflared-client-1.0.0.jar <remote_hostname> <local_port>
    ```
    For example:
    ```bash
    java -jar standalone-cloudflared-client-1.0.0.jar my.game.pl 25565
    ```
    You can choose any free local port, but it is recommended to use the default ports for the given game.
    If everything is correct, you will see a success message informing you which local address to connect to.

5.  **Play:** Leave the application window open. Start the game and connect to the address shown in the terminal (e.g., `127.0.0.1:26900`).

To stop the tunnel, simply close the application window or press `Ctrl+C` in the terminal.
