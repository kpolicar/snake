package smart.snake;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JPanel;
import static smart.snake.GameWindow.snakes;


public class GameRenderPanel extends JPanel {
    
    public static final short GAME_SCALE = 10;
    
    public final Color background_color = new Color(135, 194, 214);
    public final Color menu_color = new Color(63, 151, 181);

    // SNAKE COLORS
    public final Color lime_head_color = new Color(32, 227, 78);
    public final Color lime_body_color = new Color(51, 242, 96);
    
    public final Color gold_head_color = new Color(181, 159, 9);
    public final Color gold_body_color = new Color(222, 196, 13);
    
    public final Color orange_head_color = new Color(191, 91, 10);
    public final Color orange_body_color = new Color(250, 121, 15);
    
    public final Color red_head_color = new Color(199, 28, 36);
    public final Color red_body_color = new Color(247, 35, 45);
    
    public final Color pink_head_color = new Color(196, 75, 186);
    public final Color pink_body_color = new Color(242, 131, 233);
    
    public final Color dead_body_color = new Color(168, 168, 168);
    // SNAKE COLORS
    
    public Color[][] snakeColors = {
        {lime_head_color, lime_body_color},
        {gold_head_color, gold_body_color},
        {orange_head_color, orange_body_color},
        {red_head_color, red_body_color},
        {pink_head_color, pink_body_color}
    };

    public final Color apple_menu_color = new Color(214, 19, 19);
    public final Color grapes_menu_color = new Color(147, 81, 201);
    public final Color lemon_menu_color = new Color(229, 255, 59);
    public final Color coconut_menu_color = new Color(248, 242, 218);
    
    Font default_Font = getFont();
    Font default_boldFont = new Font(default_Font.getFontName(), Font.BOLD, default_Font.getSize());

    public String string;
    
    public int screenWidth;
    public int gameWidth;
    public int screenHeight;
    
    
    @Override
    protected void paintComponent(Graphics g) {
        screenWidth = getWidth();
        screenHeight = getHeight();
        gameWidth = screenWidth - 158;
        
        super.paintComponent(g);
        g.setColor(background_color);
        g.fillRect(0, 0, screenWidth - 155, screenHeight);
        g.setColor(menu_color);
        g.fillRect(gameWidth, 0, screenWidth, screenHeight);
        
        
        
        //SNAKES
        for (int j = 0; j < SmartSnake.PlayerAmount; j++) { // For each snake object
            for (Point point : GameWindow.snakes[j].snakeParts) { // For each snake body part
                if(snakes[j].dead)
                    g.setColor(dead_body_color);
                else
                    g.setColor(snakeColors[j][1]);
                g.fillRect(point.x * GAME_SCALE, point.y * GAME_SCALE, GAME_SCALE, GAME_SCALE);
            }
            // Snake head
            g.setColor(snakeColors[j][0]);
            g.fillRoundRect(GameWindow.snakes[j].snakeHead.x * GAME_SCALE, GameWindow.snakes[j].snakeHead.y * GAME_SCALE, GAME_SCALE, GAME_SCALE, 0, 5);
            
            g.setFont(default_boldFont);
            string = "Player " + (j + 1) + " stats:";
            g.drawString(string, (int) (screenWidth- string.length() * 6.75f - 10), 130 * j + 30);
            
            g.setFont(default_Font);
            string = "Score: " + snakes[j].score;
            g.drawString(string, (int) (screenWidth - string.length() - 100), 130 * j + 45);
            string = "Length: " + snakes[j].tailLength;
            g.drawString(string, (int) (screenWidth - string.length() - 100), 130 * j + 60);
            string = "Speed: " + snakes[j].speed;
            g.drawString(string, (int) (screenWidth - string.length() - 100), 130 * j + 75);
        }
        
        
        
        
        
        
        // DISPLAY THE FRUITS
        for(int i = 0; i < GameWindow.fruits.size(); i++)
        {
            Point fruit_coordinates = GameWindow.fruits.get(i).position;
            int fruit_type = GameWindow.fruits.get(i).type;
            addFruit(g, GameWindow.fruits.get(i).color, fruit_coordinates.x, fruit_coordinates.y);
        }
        
        // DISPLAY THE FRUIT EFFECTS
        for (int i = 0; i < SmartSnake.PlayerAmount; i++) { // For each snake object
            //GRAPES EFFECT
            if(GameWindow.snakes[i].grapes_effect_timeout.size() != 0)
            {
                g.setColor(grapes_menu_color);
                displayFruitEffect(g, "Grapes", GameWindow.snakes[i].grapes_effect_timeout.get(0), snakes[i].entity_id, GameWindow.snakes[i].grapes_effect_timeout.size());
            }
            //LEMON EFFECT
            if(GameWindow.snakes[i].lemon_effect_timeout.size() != 0)
            {
                g.setColor(lemon_menu_color);
                displayFruitEffect(g, "Lemon", GameWindow.snakes[i].lemon_effect_timeout.get(0), snakes[i].entity_id, GameWindow.snakes[i].lemon_effect_timeout.size());
            }
            //COCONUT EFFECT
            if(GameWindow.snakes[i].coconut_effect_timeout > 0)
            {
                g.setColor(coconut_menu_color);
                displayFruitEffect(g, "Coconut", GameWindow.snakes[i].coconut_effect_timeout, snakes[i].entity_id, 0);
            }
            
        }
        
        

        g.setColor(Color.white);

        string = "Time: " + GameWindow.time;
        g.drawString(string, (int) ((gameWidth) / 2 - string.length() * 2.5f), 10);


        if (GameWindow.everybodyDead) {
            string = "Game over!";
            g.drawString(string, (int) ((gameWidth) / 2 - string.length() * 2.5f), (int) GameWindow.dim.getHeight() / 4);
            string = "Press [space] to play again";
            g.drawString(string, (int) ((gameWidth) / 2 - string.length() * 2.5f), (int) GameWindow.dim.getHeight() / 4 + 15);
        }

        if (GameWindow.paused) {
            string = "Paused!";
            g.drawString(string, (int) (screenWidth / 2 - 77 - string.length() * 2.5f), (int) GameWindow.dim.getHeight() / 4);
        }

    }

    // Without effect
    private void addFruit(Graphics g, Color color, int x, int y) {
        g.setColor(color);
        g.fillOval(x * GAME_SCALE, y * GAME_SCALE, GAME_SCALE, GAME_SCALE);
    }

    private void displayFruitEffect(Graphics g, String name, int effect_timeout, int entity_id, int effectLevel)
    {
        if (effect_timeout > 0) {
            string = name + " " + toRomanNumber(effectLevel) + " : " + effect_timeout;
            g.drawString(string, (int) (getWidth() - string.length() - 95), 130 * (entity_id - 1) + 90);
        }
    }
    
    private String toRomanNumber(int number)
    {
        
        switch(number)
        {
            case 0:
                return "";
            case 1:
                return "I";
            case 2:
                return "II";
            case 3:
                return "III";
            case 4:
                return "IV";
            case 5:
                return "V";
            case 6:
                return "VI";
            case 7:
                return "VII";
            case 8:
                return "VIII";
            case 9:
                return "IX";
            case 10:
                return "X";
        }
        
        return "Error at converting to roman number.";
    }
    

}
