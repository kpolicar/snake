package smart.snake;

import java.awt.Color;
import java.awt.Point;
/*  TYPES
    0: Apple
    1: Grapes
    2: Lemon
    3: Coconut
*/


public class FruitEntity
{
    int type;
    
    int score_increase;
    int tail_length_increase;
    
    Color color;
    Point position;
    
    
    public FruitEntity(int fruit_type)
    {
        type = fruit_type;
        position = GameWindow.getRandomCoordinates();
        
        switch(type)
        {
            default:
                color = Color.BLACK;
                score_increase = 10;
                tail_length_increase = 1;
                break;
            case 0:
                color = new Color(214, 19, 19);
                score_increase = 10;
                tail_length_increase = 2;
                break;
            case 1:
                color = new Color(147, 81, 201);
                score_increase = 15;
                tail_length_increase = 1;
                break;
            case 2:
                color = new Color(229, 255, 59);
                score_increase = 15;
                tail_length_increase = 1;
                break;
            case 3:
                color = new Color(250, 248, 240);
                score_increase = 25;
                tail_length_increase = 3;
                break;
        }
    }
    
    
    
}
