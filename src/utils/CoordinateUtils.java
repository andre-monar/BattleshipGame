package utils;

public class CoordinateUtils {

    public static int[] parseCoordinate(String input) {
        if (input == null || input.length() < 2) return null;

        int i = 0;
        while (i < input.length() && Character.isLetter(input.charAt(i))) {
            i++;
        }

        if (i == 0) return null;

        String letterPart = input.substring(0, i).toUpperCase();
        String numberPart = input.substring(i);

        try {
            int col = 0;
            for (int j = 0; j < letterPart.length(); j++) {
                col = col * 26 + (letterPart.charAt(j) - 'A');
            }

            int row = Integer.parseInt(numberPart) - 1;

            return new int[]{col, row};
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static String formatCoordinate(int x, int y) {
        StringBuilder colBuilder = new StringBuilder();
        int col = x;

        do {
            colBuilder.insert(0, (char)('A' + (col % 26)));
            col = col / 26 - 1;
        } while (col >= 0);

        return colBuilder.toString() + (y + 1);
    }
}