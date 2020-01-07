package smart.snake;

// IMPORTS

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;


public class GetPlayerAmountForm extends JFrame {
    private final JButton Left;
    private final JButton Right;
    private final JButton Continue;
    private final JLabel PlayerAmountDisplay;
    int PlayerAmount = 1;
    boolean Done = false;

    public GetPlayerAmountForm() {
        setSize(200, 120);
        setLocation(SmartSnake.dim.width / 2 - getWidth() / 2, SmartSnake.dim.height / 2 - getHeight() / 2);
        setResizable(false);
        setLayout(new FlowLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);


        add(new JLabel("Select number of players:"));
        Left = new JButton("<");
        add(Left);
        PlayerAmountDisplay = new JLabel("  " + Integer.toString(PlayerAmount) + "  ");
        PlayerAmountDisplay.setFont(new Font("Helvetica", Font.PLAIN, 20));
        add(PlayerAmountDisplay);
        Right = new JButton(">");
        add(Right);
        Continue = new JButton("CONTINUE");
        add(Continue);

        setVisible(true);

        // ADDING THE ACTION LISTENERS
        Left.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                LeftActionPerformed(evt); //Klic funkcije za levi gumb
            }
        });

        Right.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                RightActionPerformed(evt); //Klic funkcije za desni gumb
            }
        });

        Continue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                ContinueActionPerformed(evt); //Klic funkcije za continue gumb
            }
        });
    }

    // HANDLING THE ACTIONS
    public void LeftActionPerformed(ActionEvent evt) {
        if (PlayerAmount > 1) {
            PlayerAmount--;
            PlayerAmountDisplay.setText("  " + Integer.toString(PlayerAmount) + "  ");

        }
    }

    public void RightActionPerformed(ActionEvent evt) {
        if (PlayerAmount < 5) {
            PlayerAmount++;
            PlayerAmountDisplay.setText("  " + Integer.toString(PlayerAmount) + "  ");
        }
    }

    public void ContinueActionPerformed(ActionEvent evt) {
        Done = true;
        dispose();
        SmartSnake.resume();
    }

}
