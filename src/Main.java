import views.ConsoleView;

import static utils.CoordinateUtils.formatCoordinate;

public class Main {
    public static void main(String[] args) {
        ConsoleView view = new ConsoleView();
        // No início do gameLoop ou em outro método visível
        System.out.println();
        view.startGame();
    }
}