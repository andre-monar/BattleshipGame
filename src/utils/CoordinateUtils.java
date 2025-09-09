package utils;

public class CoordinateUtils {

    // converte input do usuario em letras "A4" para coordenadas numericas "0,4"
    public int[] parseCoordinate(String input) {
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

    // formata as coordenadas numericas (0,0; 13,5 lidas pelo back-end em letras B6, AB6 para output)
    public static String formatCoordinate(int x, int y) {
        if (x < 0) return "?" + (y + 1);

        StringBuilder sb = new StringBuilder();
        int num = x;

        while (num >= 0) {
            sb.insert(0, (char) ('A' + (num % 26)));
            num = (num / 26) - 1;
        }

        return sb.toString() + (y + 1);
    }

}