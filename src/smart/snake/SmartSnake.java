package smart.snake;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Toolkit;

public class SmartSnake {
    
    public static Dimension dim;
    
    public static int PlayerAmount;

    static GetPlayerAmountForm form;
    

    public static void main(String[] args) {
        
        dim = Toolkit.getDefaultToolkit().getScreenSize();
        
        form = new GetPlayerAmountForm();
    }

    public static void resume() {
        PlayerAmount = form.PlayerAmount;

        form = null;

        System.out.println("Number of players: " + PlayerAmount);

        GameWindow window = new GameWindow();
    }

}
