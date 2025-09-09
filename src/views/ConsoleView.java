package views;

import controllers.GameController;
import controllers.ShotResult;
import models.*;
import java.util.*;
import utils.*;

public class ConsoleView {
    private GameController controller;
    private Scanner scanner;

    public ConsoleView() {
        this.controller = new GameController();
        this.scanner = new Scanner(System.in);
    }

    // começa o jogo, ativa o initializeGame do controller e coloca os navios
    public void startGame() {
        System.out.println("=== BATALHA NAVAL ===");
        System.out.println("Selecione a dificuldade:");
        System.out.println("1. Facil (10x10)");
        System.out.println("2. Medio (15x15)");
        System.out.println("3. Dificil (30x30)");

        int choice = getIntInput("Escolha: ", 1, 3);
        Difficulty difficulty;

        switch (choice) {
            case 1: difficulty = Difficulty.EASY; break;
            case 2: difficulty = Difficulty.MEDIUM; break;
            case 3: difficulty = Difficulty.HARD; break;
            default: difficulty = Difficulty.EASY;
        }

        controller.initializeGame(difficulty);
        placeUserShips();
        controller.placeComputerShips();

        gameLoop();
    }

    // interface de colocação de navios do usuário
    private void placeUserShips() {
        System.out.println("\n=== POSICIONAMENTO DE NAVIOS ===");
        System.out.println("Seu tabuleiro:");
        controller.getUserBoard().display(true);

        ShipType[] shipTypes = controller.getCurrentShipTypes();

        for (ShipType shipType : shipTypes) {
            for (int i = 0; i < shipType.getCount(); i++) {
                System.out.println("\nPosicione " + shipType.getName() + " (tamanho: " + shipType.getSize() + ")");

                boolean placed = false;
                while (!placed) {
                    try {
                        String coordInput = getStringInput("Informe a coordenada (ex: A1): ");
                        int[] coords = parseCoordinate(coordInput);

                        if (coords == null) {
                            System.out.println("Coordenada invalida!");
                            continue;
                        }

                        String direction = getStringInput("Horizontal? (s/n): ");
                        boolean isHorizontal = direction.equalsIgnoreCase("s");

                        Ship ship = controller.createShip(shipType);
                        placed = controller.placeUserShip(ship, coords[0], coords[1], isHorizontal);

                        if (!placed) {
                            System.out.println("Posicionamento invalido! Tente novamente.");
                        } else {
                            System.out.println("Navio posicionado com sucesso!");
                            controller.getUserBoard().display(true);
                        }
                    } catch (Exception e) {
                        System.out.println("Erro: " + e.getMessage());
                    }
                }
            }
        }

        System.out.println("\nTodos os navios posicionados! Preparando para começar o jogo...");
    }

    // interface loop do jogo
    private void gameLoop() {
        System.out.println("\n=== INICIO DO JOGO ===");

        while (!controller.isGameOver()) {
            if (controller.isPlayerTurn()) {
                playerTurn();
            } else {
                computerTurn();
            }

            controller.switchTurn();
        }

        System.out.println("\n=== FIM DE JOGO ===");
        System.out.println("Vencedor: " + controller.getWinner());

        System.out.println("\nSeu tabuleiro:");
        controller.getUserBoard().display(true);

        System.out.println("\nTabuleiro do computador:");
        controller.getComputerBoard().display(true);

        System.out.println("\nObrigado por jogar!");
    }

    // interface da vez do usuario
    private void playerTurn() {
        System.out.println("\n=== SUA VEZ ===");

        System.out.println("Seu tabuleiro:");
        controller.getUserBoard().display(true);

        System.out.println("\nTabuleiro do adversario:");
        controller.getComputerBoard().display(false);

        boolean validShot = false;
        while (!validShot) {
            try {
                String coordInput = getStringInput("Informe a coordenada para atirar (ex: A1): ");
                int[] coords = CoordinateUtils.parseCoordinate(coordInput);

                if (coords == null) {
                    System.out.println("Coordenada invalida!");
                    continue;
                }

                ShotResult result = controller.playerShot(coords[0], coords[1]);

                switch (result.getType()) {
                    case HIT:
                        System.out.println("Acertou um navio!");
                        validShot = true;
                        break;
                    case MISS:
                        System.out.println("Agua! Nao acertou nada.");
                        validShot = true;
                        break;
                    case SUNK:
                        System.out.println("Afundou um " + result.getShip().getName() + "!");
                        validShot = true;
                        break;
                    case ALREADY_SHOT:
                        System.out.println("Voce ja atirou nesta posicao! Tente novamente.");
                        break;
                    case INVALID:
                        System.out.println("Coordenada invalida! Tente novamente.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }

        List<Ship> computerShipsAlive = controller.getComputerShipsAlive();
        System.out.println("Navios restantes do computador: " + computerShipsAlive.size());
    }

    // interface da vez do computador
    private void computerTurn() {
        System.out.println("\n=== VEZ DO COMPUTADOR ===");

        ShotResult result = controller.computerShot();

        switch (result.getType()) {
            case HIT:
                System.out.println("O computador acertou seu navio em " +
                        CoordinateUtils.formatCoordinate(result.getX(), result.getY()) + "!");
                break;
            case MISS:
                System.out.println("O computador atirou em " +
                        CoordinateUtils.formatCoordinate(result.getX(), result.getY()) + " e errou!");
                break;
            case SUNK:
                System.out.println("O computador afundou seu " + result.getShip().getName() +
                        " em " + CoordinateUtils.formatCoordinate(result.getX(), result.getY()) + "!");
                break;
        }

        List<Ship> userShipsAlive = controller.getUserShipsAlive();
        System.out.println("Seus navios restantes: " + userShipsAlive.size());
    }

    // helper para forçar numero dentro de um intervalo (exemplo 1-3 na escolha da dificuldade) - só pra interface de console
    private int getIntInput(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = scanner.nextInt();
                scanner.nextLine();

                if (value >= min && value <= max) {
                    return value;
                } else {
                    System.out.println("Por favor, insira um número entre " + min + " e " + max);
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número.");
                scanner.nextLine();
            }
        }
    }

    // pega a mensagem do usuario (so pra interface de console)
    private String getStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
}