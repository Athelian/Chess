package GUI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import Board.Board;

public class DraggablePane extends JLayeredPane {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static final int HEIGHT = 600;
    public static final int WIDTH = 600;

    private static final int GRID_ROWS = 8;
    private static final int GRID_COLS = 8;
    private static final Dimension LAYERED_PANE_SIZE = new Dimension(WIDTH, HEIGHT);
    private GridLayout gridlayout = new GridLayout(GRID_ROWS, GRID_COLS);
    private JPanel backingPanel = new JPanel(gridlayout);
    private JPanel[][] panelGrid = new JPanel[GRID_ROWS][GRID_COLS];

    public DraggablePane(Board board, GUI gui) {
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                panelGrid[row][column] = new JPanel(new GridBagLayout());
                backingPanel.add(panelGrid[row][column]);

                if ((row + column) % 2 == 0) {
                    panelGrid[row][column].setBackground(new Color(184,139,74));
                } else {
                    panelGrid[row][column].setBackground(new Color(227,193,111));
                }

                if (board.board[row][column] != null) {
                    JLabel pieceIcon = new JLabel(board.board[row][column].SYMBOL, SwingConstants.CENTER);
                    pieceIcon.setPreferredSize(new Dimension(60, 60));
                    pieceIcon.setFont(new Font("TimesRoman", Font.PLAIN, 40));

                    panelGrid[row][column].add(pieceIcon);
                }


            }
        }
        JLabel signature = new JLabel();
        signature.setText("EAF");
        panelGrid[7][7].add(signature);

        backingPanel.setSize(WIDTH, HEIGHT);
        setPreferredSize(LAYERED_PANE_SIZE);
        add(backingPanel, JLayeredPane.DEFAULT_LAYER);
        MyMouseAdapter myMouseAdapter = new MyMouseAdapter(board, gui);
        addMouseListener(myMouseAdapter);
        addMouseMotionListener(myMouseAdapter);
    }

    private class MyMouseAdapter extends MouseAdapter {
        private JLabel dragLabel = null;
        private int dragLabelWidthDiv2;
        private int dragLabelHeightDiv2;
        private JPanel clickedPanel = null;
        private int[] clickedPanelPos = new int[2];
        private Board board;
        private GUI gui;

        public MyMouseAdapter(Board board, GUI gui) {
            this.board = board;
            this.gui = gui;
        }

        @Override
        public void mousePressed(MouseEvent me) {
            clickedPanel = (JPanel) backingPanel.getComponentAt(me.getPoint());
            Component[] components = clickedPanel.getComponents();
            if (components.length == 0) {
                gui.textfield.setFont(new Font("Inke Free", Font.BOLD, 25));
                gui.textfield.setText("<html><body>There is no piece here!<br>Please try again</body></html>");
                return;
            }
            // if we click on jpanel that holds a jlabel
            if (components[0] instanceof JLabel) {
                searchPanelGrid: for (int row = 0; row < panelGrid.length; row++) {
                    for (int col = 0; col < panelGrid[row].length; col++) {
                        if (panelGrid[row][col] == clickedPanel) {
                            clickedPanelPos[0] = row;
                            clickedPanelPos[1] = col;
                            break searchPanelGrid;
                        }
                    }
                }

                if(!board.currentPlayer.WHITE) {
                    clickedPanelPos[0] = 7 - clickedPanelPos[0];
                    clickedPanelPos[1] = 7 - clickedPanelPos[1];
                }
                
                if (board.board[clickedPanelPos[0]][clickedPanelPos[1]].WHITE != board.currentPlayer.WHITE) {
                    gui.textfield.setFont(new Font("Inke Free", Font.BOLD, 25));
                    gui.textfield.setText("<html><body>This is not your piece!<br>Please try again</body></html>");
                    return;
                }

                // remove label from panel
                dragLabel = (JLabel) components[0];
                clickedPanel.remove(dragLabel);
                clickedPanel.revalidate();
                clickedPanel.repaint();

                dragLabelWidthDiv2 = dragLabel.getWidth() / 2;
                dragLabelHeightDiv2 = dragLabel.getHeight() / 2;

                int x = me.getPoint().x - dragLabelWidthDiv2;
                int y = me.getPoint().y - dragLabelHeightDiv2;
                dragLabel.setLocation(x, y);
                add(dragLabel, JLayeredPane.DRAG_LAYER);
                repaint();
            }
        }

        @Override
        public void mouseDragged(MouseEvent me) {
            if (dragLabel == null) {
                return;
            }
            int x = me.getPoint().x - dragLabelWidthDiv2;
            int y = me.getPoint().y - dragLabelHeightDiv2;
            dragLabel.setLocation(x, y);
            repaint();
        }

        @Override
        public void mouseReleased(MouseEvent me) {
            int[] droppedPanelPos = new int[2];
            if (dragLabel == null) {
                return;
            }
            remove(dragLabel); // remove dragLabel for drag layer of JLayeredPane
            JPanel droppedPanel = (JPanel) backingPanel.getComponentAt(me.getPoint());
            if (droppedPanel == null) {
                // if off the grid, return label to home
                if (clickedPanel.getComponents().length == 0) {
                    clickedPanel.add(dragLabel);
                } else {
                    JLabel eaf = (JLabel) clickedPanel.getComponent(0);
                    clickedPanel.remove(eaf);
                    clickedPanel.add(dragLabel);
                    clickedPanel.add(eaf);
                }
                clickedPanel.revalidate();
                repaint();
                dragLabel = null;
                return;
            } else {
                searchPanelGrid: for (int row = 0; row < panelGrid.length; row++) {
                    for (int col = 0; col < panelGrid[row].length; col++) {
                        if (panelGrid[row][col] == droppedPanel) {
                            droppedPanelPos[0] = row;
                            droppedPanelPos[1] = col;
                            break searchPanelGrid;
                        }
                    }
                }

                if (!board.currentPlayer.WHITE) {
                    droppedPanelPos[0] = 7 - droppedPanelPos[0];
                    droppedPanelPos[1] = 7 - droppedPanelPos[1];
                }

                if (!board.board[clickedPanelPos[0]][clickedPanelPos[1]].legalMove(droppedPanelPos, board)) {
                    gui.textfield.setFont(new Font("Inke Free", Font.BOLD, 25));
                    gui.textfield.setText("<html><body>You cannot move this piece like that!<br>Please try again</body></html>");
                    if (clickedPanel.getComponents().length == 0) {
                        clickedPanel.add(dragLabel);
                    } else {
                        JLabel eaf = (JLabel) clickedPanel.getComponent(0);
                        clickedPanel.remove(eaf);
                        clickedPanel.add(dragLabel);
                        clickedPanel.add(eaf);
                    }
                    clickedPanel.revalidate();
                    repaint();
                    dragLabel = null;
                    return;
                }

                    if (board.board[droppedPanelPos[0]][droppedPanelPos[1]] != null && board.board[droppedPanelPos[0]][droppedPanelPos[1]].WHITE == board.currentPlayer.WHITE) {
                        gui.textfield.setFont(new Font("Inke Free", Font.BOLD, 25));
                        gui.textfield.setText("<html><body>You cannot move onto one of your own pieces!<br>Please try again</body></html>");
                        if (clickedPanel.getComponents().length == 0) {
                            clickedPanel.add(dragLabel);
                        } else {
                            JLabel eaf = (JLabel) clickedPanel.getComponent(0);
                            clickedPanel.remove(eaf);
                            clickedPanel.add(dragLabel);
                            clickedPanel.add(eaf);
                        }
                        clickedPanel.revalidate();
                        repaint();
                        dragLabel = null;
                        return;
                    }
    
                    if (board.willBeChecked(clickedPanelPos, droppedPanelPos, true)) {
                        gui.textfield.setFont(new Font("Inke Free", Font.BOLD, 25));
                        gui.textfield.setText("<html><body>You cannot check yourself!<br>Please try again</body></html>");
                        if (clickedPanel.getComponents().length == 0) {
                            clickedPanel.add(dragLabel);
                        } else {
                            JLabel eaf = (JLabel) clickedPanel.getComponent(0);
                            clickedPanel.remove(eaf);
                            clickedPanel.add(dragLabel);
                            clickedPanel.add(eaf);
                        }
                        clickedPanel.revalidate();
                        repaint();
                        dragLabel = null;
                        return;
                    }

                    if (board.board[droppedPanelPos[0]][droppedPanelPos[1]] != null && board.board[droppedPanelPos[0]][droppedPanelPos[1]].WHITE != board.currentPlayer.WHITE) {
                        if (!board.currentPlayer.WHITE) {
                            Component[] a = panelGrid[7 - droppedPanelPos[0]][7 - droppedPanelPos[1]].getComponents();
                            for (Component component : a) {
                                JLabel b = (JLabel) component;
                                if (b.getText() != "EAF") {
                                    panelGrid[7 - droppedPanelPos[0]][7 - droppedPanelPos[1]].remove(b);
                                }
                            }
                        } else {
                            Component[] a = panelGrid[droppedPanelPos[0]][droppedPanelPos[1]].getComponents();
                            for (Component component : a) {
                                JLabel b = (JLabel) component;
                                if (b.getText() != "EAF") {
                                    panelGrid[droppedPanelPos[0]][droppedPanelPos[1]].remove(b);
                                }
                            }
                        }
                    }
                    if (droppedPanel.getComponents().length == 0) {
                        droppedPanel.add(dragLabel);
                    } else {
                        JLabel eaf = (JLabel) droppedPanel.getComponent(0);
                        droppedPanel.remove(eaf);
                        droppedPanel.add(dragLabel);
                        droppedPanel.add(eaf);
                    }
                    droppedPanel.revalidate();
                    board.movePiece(clickedPanelPos, droppedPanelPos, true);
                    board.turnEnd();
                    if (board.currentPlayer.WHITE) {
                        gui.textfield.setFont(new Font("Inke Free", Font.BOLD, 75));
                        gui.textfield.setText("White's turn");
                    } else {
                        gui.textfield.setFont(new Font("Inke Free", Font.BOLD, 75));
                        gui.textfield.setText("Black's turn");
                    }
                    board.checkCheckMated();
                }
            repaint();
            dragLabel = null;
        }
    }
}