import ui.ConsoleMenu;

public class Main {
    public static void main(String[] args) {
        try {
            ConsoleMenu menu = new ConsoleMenu();
            menu.start();
        } catch (Exception e) {
            System.out.println("Критическая ошибка при запуске программы: " + e.getMessage());
            System.out.println("Перезапустите приложение.");
        }
    }
}