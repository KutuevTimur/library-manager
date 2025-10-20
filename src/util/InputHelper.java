package util;

import java.util.Scanner;

public class InputHelper {
    private static Scanner scanner = new Scanner(System.in);

    // ввод целого числа с диапазоном
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            try {
                System.out.print(prompt);
                int number = Integer.parseInt(scanner.nextLine().trim());
                if (number >= min && number <= max) {
                    return number;
                }
                System.out.println("Ошибка, число должно быть от " + min + " до " + max);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка, введите целое число.");
            }
        }
    }

    // ввод чисел без диапазона
    public static int readInt(String prompt) {
        return readInt(prompt, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    // ввод непустой строки
    // просит ввести строку, то того момента пока не введут непустую строку
    public static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Ошибка, это поле не может быть пустым");
        }
    }

    // корректый ввод года
    public static int readYear(String prompt) {
        int currentYear = java.time.Year.now().getValue(); //текущий год
        return readInt(prompt, 1000, currentYear);
    }

    // для подтверждения действий

    //InputHelper.confirm() получает строку делает + "да/нет"
    // затем проссит ввести да или нет, если да -> возвращает true, если нет -> возвращает false
    public static boolean confirm(String prompt) {
        while (true) {
            System.out.print(prompt + " (да/нет): ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("да")  || input.equals("yes")) {
                return true;
            } else if (input.equals("нет") || input.equals("no")) {
                return false;
            }
            System.out.println("введите 'да' или 'нет'");
        }
    }

    // для ввода индекса из списка  (-1 от индекса)
    public static int readListIndex(String prompt, int listSize) {
        return readInt(prompt, 1, listSize) - 1; // преобразуем в 0-based индекс
    }
}