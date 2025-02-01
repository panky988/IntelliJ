import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

public class SimpleVCS {
    private static final String REPO_DIR = ".simplevcs";
    private static final String COMMITS_DIR = REPO_DIR + "/commits";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("SimpleVCS - Egyéni verziókezelő rendszer");

        while (true) {
            System.out.print("\n> ");
            String command = scanner.nextLine().trim();
            String[] parts = command.split(" ", 2);
            String action = parts[0];
            String argument = (parts.length > 1) ? parts[1] : "";

            switch (action) {
                case "init":
                    initializeRepository();
                    break;
                case "add":
                    addFileToRepository(argument);
                    break;
                case "commit":
                    commitChanges(argument);
                    break;
                case "log":
                    listCommits();
                    break;
                case "checkout":
                    checkoutCommit(argument);
                    break;
                case "exit":
                    System.out.println("Kilépés...");
                    return;
                default:
                    System.out.println("Ismeretlen parancs! Elérhető parancsok: init, add <fájl>, commit <üzenet>, log, checkout <commit_id>, exit");
            }
        }
    }

    private static void initializeRepository() {
        File repoDir = new File(REPO_DIR);
        if (!repoDir.exists() && repoDir.mkdir()) {
            System.out.println("Verziókezelő inicializálva: " + REPO_DIR);
        }

        File commitsDir = new File(COMMITS_DIR);
        if (!commitsDir.exists() && commitsDir.mkdir()) {
            System.out.println("Commit mappa létrehozva.");
        }
    }

    private static void addFileToRepository(String fileName) {
        File sourceFile = new File(fileName);
        File destinationFile = new File(REPO_DIR, fileName);

        if (!sourceFile.exists()) {
            System.out.println("Hiba: A megadott fájl nem létezik: " + fileName);
            return;
        }

        try {
            Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Fájl hozzáadva a verziókezelőhöz: " + fileName);
        } catch (IOException e) {
            System.out.println("Hiba történt a fájl másolásakor: " + e.getMessage());
        }
    }

    private static void commitChanges(String message) {
        String commitId = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File commitFolder = new File(COMMITS_DIR, commitId);

        if (!commitFolder.mkdir()) {
            System.out.println("Hiba: Nem sikerült létrehozni a commit mappát!");
            return;
        }

        File[] files = new File(REPO_DIR).listFiles();
        if (files == null) return;

        for (File file : files) {
            if (file.isFile()) {
                try {
                    Files.copy(file.toPath(), new File(commitFolder, file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    System.out.println("Hiba történt a fájl commitálásakor: " + file.getName());
                }
            }
        }

        try (FileWriter writer = new FileWriter(new File(commitFolder, "message.txt"))) {
            writer.write(message);
        } catch (IOException e) {
            System.out.println("Hiba történt a commit üzenet mentésekor.");
        }

        System.out.println("Commit létrehozva: " + commitId + " - " + message);
    }

    private static void listCommits() {
        File commitsDir = new File(COMMITS_DIR);
        File[] commitFolders = commitsDir.listFiles(File::isDirectory);

        if (commitFolders == null || commitFolders.length == 0) {
            System.out.println("Nincsenek elérhető commitok.");
            return;
        }

        System.out.println("Elérhető commitok:");
        for (File commit : commitFolders) {
            System.out.println(commit.getName());
        }
    }

    private static void checkoutCommit(String commitId) {
        File commitFolder = new File(COMMITS_DIR, commitId);
        if (!commitFolder.exists()) {
            System.out.println("Hiba: A megadott commit nem létezik: " + commitId);
            return;
        }

        File[] files = commitFolder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (!file.getName().equals("message.txt")) {
                try {
                    Files.copy(file.toPath(), new File(file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Fájl visszaállítva: " + file.getName());
                } catch (IOException e) {
                    System.out.println("Hiba a visszaállításnál: " + file.getName());
                }
            }
        }

        System.out.println("Visszaállítva a commit: " + commitId);
    }

    private static void checkoutLatestCommit() {
        File commitsDir = new File(COMMITS_DIR);
        File[] commitFolders = commitsDir.listFiles(File::isDirectory);

        if (commitFolders == null || commitFolders.length == 0) {
            System.out.println("Nincsenek commitok.");
            return;
        }

        File latestCommit = commitFolders[commitFolders.length - 1];
        checkoutCommit(latestCommit.getName());
    }
}
