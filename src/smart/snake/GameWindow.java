package smart.snake;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.Timer;




public class GameWindow implements ActionListener, KeyListener {

    
    // Key bindings
    public final int[] UP_KEY = {KeyEvent.VK_UP, KeyEvent.VK_W, KeyEvent.VK_T, KeyEvent.VK_I, KeyEvent.VK_NUMPAD8};
    public final int[] DOWN_KEY = {KeyEvent.VK_DOWN, KeyEvent.VK_S, KeyEvent.VK_G, KeyEvent.VK_K, KeyEvent.VK_NUMPAD2};
    public final int[] LEFT_KEY = {KeyEvent.VK_LEFT, KeyEvent.VK_A, KeyEvent.VK_F, KeyEvent.VK_J, KeyEvent.VK_NUMPAD4};
    public final int[] RIGHT_KEY = {KeyEvent.VK_RIGHT, KeyEvent.VK_D, KeyEvent.VK_H, KeyEvent.VK_L, KeyEvent.VK_NUMPAD6};

    public JFrame jframe;
    public static Point PlayDimensions;

    public GameRenderPanel renderPanel;

    // Every 10 miliseconds run actionPerformed()
    public Timer timer = new Timer(10, this);


    public static boolean paused;
    public static boolean[] deadPlayers = new boolean[SmartSnake.PlayerAmount];
    public static boolean everybodyDead;
    
    public static int ticks = 0;

    public static int time;

    
    public static List<FruitEntity> fruits = new ArrayList<FruitEntity>();
    int chanceOfSpawn;
    

    public static Random random;


    // An array of snake objects of length PlayerAmount
    public static SnakeEntity[] snakes = new SnakeEntity[SmartSnake.PlayerAmount];


    public static Dimension dim;

    public GameWindow() {
        dim = Toolkit.getDefaultToolkit().getScreenSize();
        jframe = new JFrame("Snake Improved");
        jframe.setVisible(true);
        jframe.setSize(1600, 800);
        jframe.setResizable(false);
        jframe.setLocation(dim.width / 2 - jframe.getWidth() / 2, dim.height / 2 - jframe.getHeight() / 2);
        jframe.add(renderPanel = new GameRenderPanel());
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.addKeyListener(this);
        
        PlayDimensions = new Point((jframe.getWidth() - 160) / GameRenderPanel.GAME_SCALE, (jframe.getHeight() - 25) / GameRenderPanel.GAME_SCALE);


        for (int i = 0; i < SmartSnake.PlayerAmount; i++) {
            snakes[i] = new SnakeEntity();
        }

        restartGame();
        timer.start();
    }

    public void restartGame() {
        paused = false;
        time = 0;
        for (int i = 0; i < SmartSnake.PlayerAmount; i++)
        {
            snakes[i].dead = false;
            snakes[i].score = 0;
            snakes[i].speed = 7;
            snakes[i].fatness = 1;
            snakes[i].tailLength = 10;
            snakes[i].direction = snakes[i].DOWN;
            snakes[i].snakeHead = new Point((int) ((PlayDimensions.x / SmartSnake.PlayerAmount * (snakes[i].entity_id - 0.5))), -1);
            snakes[i].snakeParts.clear();
            snakes[i].removeAllEffects();
        }

        ticks = 0;
        fruits.clear();
        random = new Random();

        everybodyDead = false;
        for (int i = 0; i < deadPlayers.length; i++) {
            deadPlayers[i] = false;
        }

        timer.start();
    }

    public int howManyDead()
    {
        int deadAmount = 0;
        for (SnakeEntity snake : snakes)
        {
            if (snake.dead)
                deadAmount++;
        }
        return deadAmount;
    }
    


    @Override
    public void actionPerformed(ActionEvent arg0) {
        renderPanel.repaint();

        ticks++;

        if (!paused && !everybodyDead) {
            if(howManyDead() == SmartSnake.PlayerAmount)
                everybodyDead = true;

            for (int i = 0; i < SmartSnake.PlayerAmount; i++) {
                if ((ticks % snakes[i].speed == 0))
                    snakes[i].tick();
            }

        }

        // This one's for timers (checks if a second has passed)
        if (ticks % 100 == 0 && !paused && !everybodyDead) {
            
            time++;
            // Effects
            for (int i = 0; i < SmartSnake.PlayerAmount; i++) {
                decrementStackingEffectTimeout(snakes[i].grapes_effect_timeout, i, 1);
                decrementStackingEffectTimeout(snakes[i].lemon_effect_timeout, i, 2);
                decrementEffectTimeout(snakes[i].coconut_effect_timeout, i, 3);
                
            }
            
        }
        // This one's for effects
        if (ticks % (500 / SmartSnake.PlayerAmount) == 0 && !paused && !everybodyDead) {
            
            
            chanceOfSpawn = random.nextInt((GameRenderPanel.GAME_SCALE / 3));
            // GRAPES
            if (chanceOfSpawn == 0) {
                fruits.add(new FruitEntity(1));
            }
            // LEMON
            if (chanceOfSpawn == 1) {
                fruits.add(new FruitEntity(2));
            }
            
            chanceOfSpawn = random.nextInt((GameRenderPanel.GAME_SCALE / 3) * 3);
            // COCONUT
            if (SmartSnake.PlayerAmount > 1 && chanceOfSpawn == 0) {
                fruits.add(new FruitEntity(3));
            }

        }
    }

    private void decrementStackingEffectTimeout(List<Integer> effect_timeout, int entity_id, int fruit_type)
    {
        if(effect_timeout != null && effect_timeout.size() != 0)
        {
            if(effect_timeout.get(0) > 0)
            {
                effect_timeout.set(0, effect_timeout.get(0) - 1);
                if(effect_timeout.get(0) == 0)
                {
                    if(fruit_type == 1)
                        snakes[entity_id].speed--;
                    else if(fruit_type == 2)
                        snakes[entity_id].speed++;
                    
                    snakes[entity_id].removeStackingEffect(effect_timeout);
                }
            }
        }
        
    }
    private void decrementEffectTimeout(int effect_timeout, int entity_id, int fruit_type)
    {
        if(effect_timeout > 0)
        {
            if(fruit_type == 3)
                snakes[entity_id].coconut_effect_timeout--;
            System.out.println(effect_timeout);
            if(effect_timeout == 0)
                snakes[entity_id].removeEffect(effect_timeout);
        }
    }


    public static Point getRandomCoordinates()
    {
        return new Point(random.nextInt(PlayDimensions.x), random.nextInt(PlayDimensions.y));
    }



    @Override
    public void keyPressed(KeyEvent e) {
        int i = e.getKeyCode();

        for (int j = 0; j < SmartSnake.PlayerAmount; j++) {
            if (i == LEFT_KEY[snakes[j].entity_id - 1] && snakes[j].direction != SnakeEntity.RIGHT) {
                snakes[j].direction = SnakeEntity.LEFT;
            }

            if (i == RIGHT_KEY[snakes[j].entity_id - 1] && snakes[j].direction != SnakeEntity.LEFT) {
                snakes[j].direction = SnakeEntity.RIGHT;
            }

            if (i == UP_KEY[snakes[j].entity_id - 1] && snakes[j].direction != SnakeEntity.DOWN) {
                snakes[j].direction = SnakeEntity.UP;
            }

            if (i == DOWN_KEY[snakes[j].entity_id - 1] && snakes[j].direction != SnakeEntity.UP) {
                snakes[j].direction = SnakeEntity.DOWN;
            }
        }


        if (i == KeyEvent.VK_SPACE) {
            paused = !paused;
            renderPanel.repaint();
            if(paused)
                timer.stop();
            else
                timer.start();
            if(everybodyDead)
                restartGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

}