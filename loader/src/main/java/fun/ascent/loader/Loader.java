package fun.ascent.loader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class Loader {

    // ANSI colour helpers
    private static final String RESET  = "\u001B[0m";
    private static final String BOLD   = "\u001B[1m";
    private static final String CYAN   = "\u001B[36m";
    private static final String YELLOW = "\u001B[33m";
    private static final String GREEN  = "\u001B[32m";
    private static final String RED    = "\u001B[31m";
    private static final String PURPLE = "\u001B[35m";
    private static final String DIM    = "\u001B[2m";

    /** All child processes we've spawned — cleaned up on shutdown. */
    private static final List<Process> children = new ArrayList<>();

    private Loader() {}

    public static void main(String[] args) {
        // Kill all children when this loader is terminated
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            for (Process p : children) {
                if (p.isAlive()) p.destroyForcibly();
            }
        }));

        // Direct CLI mode: java -jar AscentLoader.jar LOBBY
        if (args.length > 0) {
            launch(args[0].trim().toUpperCase());
            waitForChildren();
            return;
        }

        printBanner();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                printMenu();
                System.out.print(BOLD + CYAN + " ▸ " + RESET);

                if (!scanner.hasNextLine()) break;
                String input = scanner.nextLine().trim().toUpperCase();

                switch (input) {
                    case "1", "LOBBY"           -> launch("LOBBY");
                    case "2", "SKYBLOCK", "SB"  -> launch("SKYBLOCK");
                    case "3", "PROXY"           -> launch("PROXY");
                    case "Q", "QUIT", "EXIT"    -> {
                        System.out.println(GREEN + " ✓ Shutting down all servers..." + RESET);
                        return;
                    }
                    default -> System.out.println(RED + " ✗ Unknown option: " + input + RESET);
                }
            }
        }
    }

    // ── Launch logic ────────────────────────────────────────────────────────

    private static void launch(String type) {
        switch (type) {
            case "LOBBY"    -> spawnServer("fun.ascent.lobby.Main", "LOBBY");
            case "SKYBLOCK" -> spawnServer("fun.ascent.skyblock.Main", "SKYBLOCK");
            case "PROXY"    -> spawnProxy();
            default -> {
                System.out.println(RED + " ✗ Unknown server type: " + type + RESET);
                System.out.println("   Usage: java -jar AscentLoader.jar [LOBBY|SKYBLOCK|PROXY]");
            }
        }
    }

    /**
     * Spawns a Minestom server (Lobby or Skyblock) as a separate child
     * process, re-using this same fat jar's classpath.
     */
    private static void spawnServer(String mainClass, String label) {
        System.out.println();
        System.out.println(GREEN + " ▶ Launching " + label + " in a new process..." + RESET);

        try {
            // Resolve our own jar / classpath
            String classpath = System.getProperty("java.class.path");
            String javaHome  = System.getProperty("java.home");
            String javaBin   = Path.of(javaHome, "bin", "java").toString();

            ProcessBuilder pb = new ProcessBuilder(
                    javaBin,
                    "-cp", classpath,
                    "--enable-native-access=ALL-UNNAMED",
                    mainClass
            );
            pb.inheritIO();
            pb.environment().putIfAbsent("REDIS_HOST", "127.0.0.1");

            Process process = pb.start();
            children.add(process);

            long pid = process.pid();
            System.out.println(GREEN + " ✓ " + label + " started" + RESET
                    + DIM + "  (PID " + pid + ")" + RESET);
            System.out.println();
        } catch (IOException e) {
            System.out.println(RED + " ✗ Failed to start " + label + ": " + e.getMessage() + RESET);
        }
    }

    /**
     * Spawns the Velocity proxy as a child process.
     */
    private static void spawnProxy() {
        System.out.println();
        System.out.println(GREEN + " ▶ Launching PROXY (Velocity)..." + RESET);

        Path proxyDir = Path.of("proxy").toAbsolutePath();
        File velocityJar = proxyDir.resolve("velocity-3.5.0-SNAPSHOT-593.jar").toFile();

        if (!velocityJar.isFile()) {
            System.out.println(RED + " ✗ Velocity jar not found at: " + velocityJar.getAbsolutePath() + RESET);
            System.out.println("   Make sure 'proxy/velocity-3.5.0-SNAPSHOT-593.jar' exists.");
            return;
        }

        // Copy the built plugin if needed
        File pluginsDir = proxyDir.resolve("plugins").toFile();
        if (!pluginsDir.isDirectory()) pluginsDir.mkdirs();

        File coreProxyJar = new File(pluginsDir, "CoreProxy.jar");
        File builtProxy = Path.of("core-proxy", "target", "CoreProxy.jar").toFile();

        if (builtProxy.isFile()) {
            try {
                java.nio.file.Files.deleteIfExists(coreProxyJar.toPath());
                java.nio.file.Files.copy(builtProxy.toPath(), coreProxyJar.toPath());
                System.out.println(YELLOW + "   Copied CoreProxy.jar → proxy/plugins/" + RESET);
            } catch (IOException e) {
                System.out.println(YELLOW + "   Warning: could not copy CoreProxy.jar – " + e.getMessage() + RESET);
            }
        }

        try {
            String javaHome = System.getProperty("java.home");
            String javaBin  = Path.of(javaHome, "bin", "java").toString();

            ProcessBuilder pb = new ProcessBuilder(
                    javaBin,
                    "-XX:MaxRAMPercentage=75",
                    "-jar", velocityJar.getName()
            );
            pb.directory(proxyDir.toFile());
            pb.inheritIO();
            pb.environment().putIfAbsent("REDIS_HOST", "127.0.0.1");

            Process process = pb.start();
            children.add(process);

            long pid = process.pid();
            System.out.println(GREEN + " ✓ PROXY started" + RESET
                    + DIM + "  (PID " + pid + ")" + RESET);
            System.out.println();
        } catch (IOException e) {
            System.out.println(RED + " ✗ Failed to start Velocity: " + e.getMessage() + RESET);
        }
    }

    /** Block until all children exit (used in CLI mode). */
    private static void waitForChildren() {
        for (Process p : children) {
            try { p.waitFor(); } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // ── UI helpers ──────────────────────────────────────────────────────────

    private static void printBanner() {
        System.out.println();
        System.out.println(PURPLE + BOLD + "    ╔═══════════════════════════════════════════╗" + RESET);
        System.out.println(PURPLE + BOLD + "    ║          " + CYAN + "A S C E N T   L O A D E R" + PURPLE + "         ║" + RESET);
        System.out.println(PURPLE + BOLD + "    ╚═══════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    private static void printMenu() {
        System.out.println(BOLD + "    ┌─────────────────────────────────────────┐" + RESET);
        System.out.println(BOLD + "    │  " + GREEN  + "[1]" + RESET + BOLD + "  LOBBY       " + RESET + "– Minestom lobby       " + BOLD + "│" + RESET);
        System.out.println(BOLD + "    │  " + YELLOW + "[2]" + RESET + BOLD + "  SKYBLOCK    " + RESET + "– Minestom skyblock    " + BOLD + "│" + RESET);
        System.out.println(BOLD + "    │  " + CYAN   + "[3]" + RESET + BOLD + "  PROXY       " + RESET + "– Velocity proxy       " + BOLD + "│" + RESET);
        System.out.println(BOLD + "    │  " + RED    + "[Q]" + RESET + BOLD + "  QUIT                              " + BOLD + "│" + RESET);
        System.out.println(BOLD + "    └─────────────────────────────────────────┘" + RESET);
    }
}
