package GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Board.Board;

public class GUI {
    public JPanel title_panel = new JPanel();
    public JFrame frame = new JFrame();
    public JLabel textfield = new JLabel();
    public DragLabelOnLayeredPane chessBoard;
    public JPanel[][] tiles = new JPanel[8][8];

    public GUI(Board board) {
        this.chessBoard = new DragLabelOnLayeredPane(board, this);
        title_panel.setLayout(new BorderLayout());
        title_panel.setPreferredSize(new Dimension(600,100));
        frame.setTitle("Chess");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(Color.black);
        frame.getContentPane().add(chessBoard);
        frame.setVisible(true);

        textfield.setBackground(new Color(25,25,25));
        textfield.setForeground(new Color(25,255,0));
        textfield.setFont(new Font("Inke Free", Font.BOLD, 75));
        textfield.setHorizontalAlignment(JLabel.CENTER);
        textfield.setText("White's Turn");
        textfield.setOpaque(true);

        
        title_panel.add(textfield);
        frame.add(title_panel, BorderLayout.NORTH);

        frame.pack();


        ImageIcon image = new ImageIcon("src\\Images\\logo.png");
        frame.setIconImage(image.getImage());
    }
}