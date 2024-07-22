import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class BoardPanel extends JPanel {
    private final Board board;
    private int squareSize;

    public BoardPanel(Board board) {
        this.board = board;
        adjustSquareSize();

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustSquareSize();
                repaint();
            }
        });

        // Create a Timer to perform periodic updates
        Timer timer = new Timer(1, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        timer.start();
    }

    private void adjustSquareSize() {
        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int boardWidth = board.getBoard().length;
        int boardHeight = board.getBoard()[0].length;
        squareSize = Math.min(panelWidth / boardWidth, panelHeight / boardHeight);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Square[][] squares = board.getBoard();
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                switch (squares[i][j].color) {
                    case Red:
                        g.setColor(java.awt.Color.RED);
                        break;
                    case Blue:
                        g.setColor(java.awt.Color.BLUE);
                        break;
                    case White:
                        g.setColor(java.awt.Color.WHITE);
                        break;
                }
                g.fillRect(i * squareSize, j * squareSize, squareSize, squareSize);
                g.setColor(java.awt.Color.BLACK);
                g.drawRect(i * squareSize, j * squareSize, squareSize, squareSize);
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        JFrame frame = new JFrame("Board");
        Board board = new Board(50, 50, 1000, 1000);
        BoardPanel boardPanel = new BoardPanel(board);
        frame.add(boardPanel);
        frame.setSize(800, 800); // Set an initial size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        while(true){
            for(int i = 0; i < 50; i++) {
                for (int j = 0; j < 50; j++) {
                    if(board.getBoard()[i][j].color != Square.Color.White && !board.getBoard()[i][j].satisfied) {
                        board.leaveOrStay(i, j, 3);
                        Thread.sleep(1);
                    }
                }
            }
        }
    }
}