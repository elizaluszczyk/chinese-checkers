# Chinese Checkers Multiplayer Game

## Opis projektu
Ten projekt implementuje grę wieloosobową w chińskie warcaby z obsługą serwera i klienta. Aplikacja umożliwia graczom dołączanie do gry, przesyłanie wiadomości tekstowych oraz wykonywanie ruchów na planszy, które są synchronizowane między wszystkimi klientami. Gra obsługuje różne warianty liczby graczy (2, 3, 4 lub 6).

## Funkcjonalności
- Obsługa gry wieloosobowej z komunikacją klient-serwer.
- Wybór liczby graczy (2, 3, 4, 6) oraz wariantu gry.
- Synchronizacja stanu planszy między wszystkimi graczami.
- Walidacja ruchów graczy.
- Wysyłanie i odbieranie wiadomości tekstowych w czasie rzeczywistym.
- Obsługa błędów, np. niewłaściwych ruchów.

## Architektura
Projekt został napisany w języku Java z podziałem na logiczne komponenty:
- **Serwer**: Obsługuje komunikację między klientami i zarządza stanem gry.
- **Klient**: Łączy się z serwerem, umożliwia graczowi interakcję z grą.
- **Pakiety**: Wykorzystywane do wymiany danych między serwerem a klientami.
- **Plansza**: Implementuje logikę gry w chińskie warcaby.

## Technologia
- **Język programowania**: Java
- **Wykorzystane wzorce projektowe**:
    - Fabryka (`BoardFactory`) – do tworzenia plansz w zależności od liczby graczy.
    - MVC (Model-View-Controller) – logiczny podział na model (plansza), kontroler (serwer) i widok (klient).
- **Serializacja**: Wykorzystywana do przesyłania obiektów między serwerem a klientem.

## Wymagania
- Java 11 lub nowsza
- Maven

## Instalacja
 **Sklonuj repozytorium**:
   git clone https://github.com/twoje-repo/chinese-checkers.git

## Instrukcja obsługi
- Uruchom serwer komendą: mvn exec:java -Dexec.args="server"
- Uruchom aplikację w wersji klienta komendą: mvn exec:java -Dexec.args="client"
- Pierwszy połączony klient wybiera ilosc graczy, rodzaj planszy oraz ustawia swój nick.
- Pozostali gracze również ustawiają swoje nicki.
- Po dołączeniu wszystkich graczy, gra rozpocznie się automatycznie.
- Gracze mogą przesyłać wiadomości tekstowe lub wykonywać ruchy w formacie: MOVE x1,y1 TO x2,y2
- Aby wyjść z rozrywki należy wpisac: exit

## Rozszerzenia
- Dodanie graficznego interfejsu użytkownika (GUI).
- Obsługa dodatkowych wariantów gier.
- Zapis stanu gry i wznawianie rozgrywki.

## Autorzy
Eliza Łuszczyk i Martyna Ciećkiewicz