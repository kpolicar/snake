package smart.snake;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;



public class SnakeEntity {
    public static short objectCount = 0;
    public int entity_id;

    public static final short UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;

    // Entity's properties
    public short direction;
    public short speed, fatness;
    public int tailLength;
    public int score;
    public boolean dead;
    List<Integer> grapes_effect_timeout = new LinkedList(),
                  lemon_effect_timeout = new LinkedList();
    int coconut_effect_timeout;
    public int effects;

    // Entity's building blocks
    public List<Point> snakeParts = new ArrayList<Point>();
    
    
    
    public Point snakeHead;

    public SnakeEntity() {
        objectCount++;
        entity_id = objectCount;
    }

    public void tick() {
        if (!dead) {
            snakeParts.add(new Point(snakeHead.x, snakeHead.y));
            move();
        }
    }

    public void move() {
        if (direction == UP) {
            if (snakeHead.y - 1 >= 0 && noTailAt(snakeHead.x, snakeHead.y - 1) && (noOtherTailAt(snakeHead.x, snakeHead.y - 1) || coconut_effect_timeout > 0)) {
                snakeHead = new Point(snakeHead.x, snakeHead.y - 1);
            } else {
                die();
            }
        }

        if (direction == DOWN) {
            if (snakeHead.y + 1 < GameWindow.PlayDimensions.y && noTailAt(snakeHead.x, snakeHead.y + 1) && (noOtherTailAt(snakeHead.x, snakeHead.y + 1) || coconut_effect_timeout > 0)) {
                snakeHead = new Point(snakeHead.x, snakeHead.y + 1);
            } else {
                die();
            }
        }

        if (direction == LEFT) {
            if (snakeHead.x - 1 >= 0 && noTailAt(snakeHead.x - 1, snakeHead.y) && (noOtherTailAt(snakeHead.x - 1, snakeHead.y) || coconut_effect_timeout > 0)) {
                snakeHead = new Point(snakeHead.x - 1, snakeHead.y);
            } else {
                die();
            }
        }

        if (direction == RIGHT) {
            if (snakeHead.x + 1 < GameWindow.PlayDimensions.x && noTailAt(snakeHead.x + 1, snakeHead.y) && (noOtherTailAt(snakeHead.x + 1, snakeHead.y) || coconut_effect_timeout > 0)) {
                snakeHead = new Point(snakeHead.x + 1, snakeHead.y);
            } else {
                die();
            }
        }


        if (snakeParts.size() > tailLength) {
            snakeParts.remove(0);

        }

        //CHECK IF SNAKE HAS HIT A FRUIT
        for(int i = 0; i < GameWindow.fruits.size(); i++)
        {
            Point fruit_position = GameWindow.fruits.get(i).position;
            if (snakeHead.equals(fruit_position))
            {
                score += GameWindow.fruits.get(i).score_increase;
                tailLength += GameWindow.fruits.get(i).tail_length_increase;
                
                switch(GameWindow.fruits.get(i).type)
                {
                    case 1:
                        speed += 1;
                        addEffect(grapes_effect_timeout, 10);
                        break;
                    case 2:
                        speed -= 1;
                        addEffect(lemon_effect_timeout, 10);
                        break;
                    case 3:
                        coconut_effect_timeout += 15;
                        break;
                }
                
                GameWindow.fruits.remove(i); // Does it also dereference the object???
            }
        }
        
    }

    public boolean noTailAt(int x, int y) {
        for (Point point : snakeParts)
        {
            if (point.equals(new Point(x, y)))
            {
                return false;
            }
        }
        return true;
    }
    
    public boolean noOtherTailAt(int x, int y)
    {
        if(SmartSnake.PlayerAmount > 1)
        {
            for (int i = 0; i < SmartSnake.PlayerAmount; i++)
            {
                for (Point point : GameWindow.snakes[i].snakeParts)
                {
                    if (point.equals(new Point(x, y)))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void die()
    {
        dead = true;
        GameWindow.deadPlayers[entity_id - 1] = true;
        removeAllEffects();
        
    }
    public void removeAllEffects()
    {
        grapes_effect_timeout.clear();
        lemon_effect_timeout.clear();
        coconut_effect_timeout = 0;
        effects = 0;
    }
    public void addEffect(List<Integer> effect, int duration)
    {
        effect.add(duration);
    }
    public void removeStackingEffect(List<Integer> effect)
    {
        
        if(effect.size() > 1)
        {
            effect.set(0, effect.get(1));
            effect.remove(1);
        }
        else
        {
            effect.remove(0);
        }
    }
    public void removeEffect(int effect)
    {
        effect = 0;
    }


}
