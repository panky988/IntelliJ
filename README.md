# SimpleVCS - Egyéni verziókezelő rendszer

## Leírás
A **SimpleVCS** egy egyszerű, saját verziókezelő rendszer, amely lehetővé teszi fájlok követését és módosításainak naplózását. A program hasonlóan működik a Git verziókezelőhöz, de egyéni megoldásként lett megvalósítva Java nyelven.

## Főbb funkciók
- **init**: Inicializálja a verziókezelő rendszert.
- **add <fájl>**: Hozzáad egy fájlt a követéshez.
- **commit <üzenet>**: Elmenti az aktuális állapotot egy commit üzenettel együtt.
- **log**: Listázza az eddigi commitokat.
- **checkout <commit_id>**: Visszaáll egy korábbi commit állapotára.
- **exit**: Kilép a programból.

## Telepítés és futtatás
1. Győződj meg róla, hogy telepítve van a **Java 11 vagy újabb verziója**.
2. Klónozd a repository-t:
   ```sh
   git clone https://github.com/panky988/IntelliJ.git
   ```
3. Lépj be a projekt könyvtárába:
   ```sh
   cd IntelliJ
   ```
4. Fordítsd le és futtasd a programot:
   ```sh
   javac -d . src/SimpleVCS.java
   java SimpleVCS
   ```
5. **Parancsok használata:**
   ```sh
   > init  # Inicializálja a verziókezelő rendszert
   > add test.txt  # Hozzáadja a 'test.txt' fájlt a verziókezeléshez
   > commit "Első commit"  # Lementi a változásokat az adott üzenettel
   > log  # Megjeleníti az eddigi commitokat
   > checkout abc123  # Visszaáll egy adott commit állapotára
   > exit #Leállítja a programot
   ```

## Kódszerkezet

```java
import java.util.Scanner;

public class SimpleVCS {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);  // Felhasználói bemenet olvasása
        System.out.println("SimpleVCS - Egyéni verziókezelő rendszer");
        
        while (true) {
            System.out.print("\n> ");
            String command = scanner.nextLine().trim();  // Felhasználói parancs beolvasása
            String[] parts = command.split(" ", 2);
            String action = parts[0];  // Az első szó lesz a parancs neve
            String argument = (parts.length > 1) ? parts[1] : "";  // Második rész az argumentum

            switch (action) {
                case "init":
                    initializeRepository();  // Inicializálás
                    break;
                case "add":
                    addFileToRepository(argument);  // Fájl hozzáadása
                    break;
                case "commit":
                    commitChanges(argument);  // Változások mentése
                    break;
                case "log":
                    listCommits();  // Commit lista megjelenítése
                    break;
                case "checkout":
                    checkoutCommit(argument);  // Adott commit visszaállítása
                    break;
                case "exit":
                    System.out.println("Kilépés...");
                    return;
                default:
                    System.out.println("Ismeretlen parancs! Elérhető parancsok: init, add <fájl>, commit <üzenet>, log, checkout <commit_id>, exit");
            }
        }
    }
}
```

## Fejlesztő
- **panky988** - Fő fejlesztő

## Licenc
Ez a projekt nyílt forráskódú és szabadon használható az MIT licenc alatt.

