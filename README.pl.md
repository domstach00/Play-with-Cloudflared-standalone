# Klient Cloudflared dla Gier
[![en](https://img.shields.io/badge/lang-en-red.svg)](./README.md)

## Problem: Dlaczego nie mogę połączyć się bezpośrednio?

Tunele Cloudflare są doskonałym narzędziem do zabezpieczania serwerów, ale stanowią wyzwanie dla gier takich jak Minecraft, Terraria czy Valheim. Problem jest przy **niezgodności typów ruchu**.
<br /> Standardowe tunele Cloudflare zostały zaprojektowane głównie z myślą o ruchu HTTP/HTTPS. Gry sieciowe natomiast wysyłają dane jako surowy ruch TCP (czasami UDP), który nie jest rozpoznawany przez klasyczny routing HTTP w tunelu.
<br /> Chociaż Cloudflare pozwala na skonfigurowanie tunelu TCP (np. za pomocą cloudflared access tcp lub service: tcp://localhost:PORT), to nadal:    
- tunel po stronie serwera nie rozwiązuje problemu po stronie gracza
- sam klient gry nie jest w stanie użyć tunelu Cloudflare bez dodatkowego proxy

## Rozwiązanie

Ta aplikacja eliminuje powyższe ograniczenia, tworząc lokalnego pośrednika (proxy TCP) na komputerze gracza.
<br /> Działa to tak:
- Aplikacja nasłuchuje na wskazanym porcie lokalnym (np. 127.0.0.1:25565),
- Gdy gra próbuje się połączyć z tym portem, aplikacja nawiązuje zaszyfrowane połączenie przez tunel Cloudflare do zdalnego serwera (np. moj-serwer.trycloudflare.com),
- Gra otrzymuje stabilne połączenie, jakby łączyła się z lokalnym serwerem.

Dzięki temu:
- nie trzeba otwierać portów ani posiadać publicznego IP,
- nie trzeba konfigurować klienta cloudflared,
- rozwiązanie działa z dowolną grą korzystającą z TCP,
- gracz nie musi znać się na sieciach – wystarczy uruchomić aplikację.

## Wymagania
- Java 11 lub nowsza
- Jakaś gra online oparta na protokole TCP

## Jak używać

1.  **Pobieranie:** Pobierz najnowszy plik `standalone-cloudflared-client-1.0.0.jar` z sekcji "releases" lub zbuduj go samodzielnie. Umieść go w wygodnym dla siebie folderze.

2.  **Pierwsze uruchomienie (Konfiguracja):** Uruchom aplikację w terminalu:
    ```bash
    java -jar standalone-cloudflared-client-1.0.0.jar
    ```
    Aplikacja zauważy, że nie ma pliku konfiguracyjnego. Stworzy plik `standalone-client.properties` w tym samym katalogu, po czym się zamknie.

3.  **Edycja konfiguracji:** Otwórz nowo utworzony plik `standalone-client.properties` za pomocą edytora tekstu. Zobaczysz następującą zawartość:
    ```properties
    #Cloudflared Client Configuration
    remote_hostname=your.server.com_default
    local_port=25565
    ```
    *   Zmień `your.server.com_default` na publiczny adres Twojego serwera gry (np. `my.game.pl`).
    *   `local_port` to port, który aplikacja otworzy na Twoim komputerze. Domyślna wartość `25565` jest przeznaczona dla gry Minecraft. Zmień ją, jeśli Twoja gra używa innego portu. Nie jest to wymagane, ale jest zalecane.

4.  **Uruchomienie aplikacji:** Po zapisaniu konfiguracji, uruchom aplikację ponownie:
    ```bash
    java -jar standalone-cloudflared-client-1.0.0.jar
    ```
    Alternatywnie, możesz podać nazwę hosta zdalnego i port lokalny bezpośrednio jako argumenty, nadpisując plik `client.properties`:
    ```bash
    java -jar standalone-cloudflared-client-1.0.0.jar <nazwa_hosta_zdalnego> <port_lokalny>
    ```
    Na przykład:
    ```bash
    java -jar standalone-cloudflared-client-1.0.0.jar moj.gra.pl 25565
    ```
    Możesz wybrać dowolny wolny port lokalny, ale zaleca się używanie domyślnych portów dla danej gry.
    Jeśli wszystko jest w porządku, zobaczysz komunikat o sukcesie informujący, z jakim adresem lokalnym należy się połączyć.

5.  **Graj:** Zostaw okno aplikacji otwarte. Uruchom grę i połącz się z adresem pokazanym w terminalu (np. `127.0.0.1:26900`).

Aby zatrzymać tunel, po prostu zamknij okno aplikacji lub wciśnij `Ctrl+C` w terminalu.
