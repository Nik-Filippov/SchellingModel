import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class BoardPanel extends JPanel {
    private Board board;
    private int squareSize;
    private Timer timer;
    private double tolerance = 0.3;
    private boolean running = false;
    private int updateStep = 0; // To control the update steps

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
        timer = new Timer(16, new ActionListener() { // Update every 16 ms (~60 FPS)
            @Override
            public void actionPerformed(ActionEvent e) {
                if (running) {
                    updateBoard();
                }
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

    private void updateBoard() {
        int width = board.getBoard().length;
        int height = board.getBoard()[0].length;

        // Update a limited number of cells per timer tick
        for (int step = 0; step < 100; step++) {
            int i = updateStep / height;
            int j = updateStep % height;

            if (i < width && j < height) {
                if (board.getBoard()[i][j].color != Square.Color.White && !board.getBoard()[i][j].satisfied) {
                    board.leaveOrStay(i, j, tolerance);
                }
            }

            updateStep++;
            if (updateStep >= width * height) {
                updateStep = 0;
            }
        }
        System.out.println("Board updated, current step: " + updateStep);
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

    public void setTolerance(double tolerance) {
        System.out.println("Setting tolerance to: " + tolerance);
        this.tolerance = tolerance;
    }

    public void start() {
        System.out.println("Starting simulation");
        running = true;
    }

    public void stop() {
        System.out.println("Stopping simulation");
        running = false;
    }

    public void restart(Board newBoard) {
        System.out.println("Restarting simulation");
        this.board = newBoard;
        adjustSquareSize();
        repaint();
        running = false;
        updateStep = 0;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Board");
        frame.setLayout(new BorderLayout());

        JPanel controlPanel = new JPanel(new GridLayout(0, 2));
        JButton goButton = new JButton("Go");
        JButton stopButton = new JButton("Stop");
        JButton restartButton = new JButton("Restart");
        JSlider toleranceSlider = new JSlider(0, 100, 30);
        JLabel toleranceLabel = new JLabel("Tolerance: 0.30");

        JLabel widthLabel = new JLabel("Width:");
        JTextField widthField = new JTextField("100");
        JLabel heightLabel = new JLabel("Height:");
        JTextField heightField = new JTextField("100");
        JLabel numRedLabel = new JLabel("Number of Red Squares:");
        JTextField numRedField = new JTextField("4000");
        JLabel numBlueLabel = new JLabel("Number of Blue Squares:");
        JTextField numBlueField = new JTextField("4000");

        controlPanel.add(widthLabel);
        controlPanel.add(widthField);
        controlPanel.add(heightLabel);
        controlPanel.add(heightField);
        controlPanel.add(numRedLabel);
        controlPanel.add(numRedField);
        controlPanel.add(numBlueLabel);
        controlPanel.add(numBlueField);
        controlPanel.add(goButton);
        controlPanel.add(stopButton);
        controlPanel.add(restartButton);
        controlPanel.add(new JLabel(""));
        controlPanel.add(toleranceSlider);
        controlPanel.add(toleranceLabel);

        Board board = new Board(100, 100, 4000, 4000);
        BoardPanel boardPanel = new BoardPanel(board);

        goButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardPanel.start();
            }
        });

        stopButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boardPanel.stop();
            }
        });

        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int width = Integer.parseInt(widthField.getText());
                int height = Integer.parseInt(heightField.getText());
                int numRed = Integer.parseInt(numRedField.getText());
                int numBlue = Integer.parseInt(numBlueField.getText());

                Board newBoard = new Board(width, height, numRed, numBlue);
                boardPanel.restart(newBoard);
            }
        });

        toleranceSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                int value = toleranceSlider.getValue();
                double tolerance = value / 100.0;
                boardPanel.setTolerance(tolerance);
                toleranceLabel.setText(String.format("Tolerance: %.2f", tolerance));
            }
        });

        frame.add(controlPanel, BorderLayout.NORTH);
        frame.add(boardPanel, BorderLayout.CENTER);

        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
