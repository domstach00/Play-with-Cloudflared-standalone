# Klient Cloudflared dla Gier

Ta aplikacja rozwiązuje częsty problem graczy: jak połączyć się z serwerem gry, który jest udostępniony przez Tunel Cloudflare. Wiele gier pozwala na łączenie się tylko przez adres IP i port, a nie potrafi użyć adresu URL (np. `moj-serwer.trycloudflare.com`).

To narzędzie działa z **dowolną grą opartą na protokole TCP** (np. 7 Days to Die, Minecraft, Terraria, Valheim itp.), tworząc lokalnego pośrednika (proxy) na Twoim komputerze. Łączysz swoją grę z lokalnym adresem (np. `127.0.0.1:26900`), a aplikacja bezpiecznie przekierowuje cały ruch do zdalnego serwera gry przez sieć Cloudflare.

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
