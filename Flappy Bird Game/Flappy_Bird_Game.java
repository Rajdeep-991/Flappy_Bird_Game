// Importing necessary libraries
import javax.swing.*; // For GUI components like JFrame, JPanel, etc.
import java.awt.*; // For graphics and layout-related classes
import java.awt.event.ActionEvent; // For handling action events
import java.awt.event.ActionListener; // To listen for action events (like timer ticks)
import java.awt.event.KeyEvent; // For handling keyboard key events
import java.awt.event.KeyListener; // To listen for keyboard input
import java.util.ArrayList; // To store the pipes in a list
import java.util.Random; // To generate random heights for pipes

// Main class for the game, extends JPanel for GUI and implements listeners
public class Flappy_Bird_Game extends JPanel implements ActionListener, KeyListener
{
    // Game configuration constants
    private final int TILE_SIZE = 25; // Size of one tile (square block)
    private final int GRID_SIZE = 25; // Number of tiles in grid (for both width and height)
    private final int SCORE_ROW_HEIGHT = 2 * TILE_SIZE; // Height reserved for displaying score
    private final int GAME_SPEED = 20; // Delay in milliseconds between game updates (lower = faster)
    private final int PIPE_WIDTH = 50; // Width of each pipe
    private final int PIPE_GAP = 150; // Vertical gap between upper and lower pipes
    private final int BIRD_SIZE = 20; // Size of the bird (a square)

    // Bird's current position and movement variables
    private int birdX, birdY; // Bird's current (x, y) position
    private int velocityY; // Vertical speed of the bird
    private int score; // Player's current score

    // Game state flags
    private boolean running = false; // Whether the game is currently running
    private boolean showScoreWindow = false; // Whether to show score window after collision
    private boolean gameStarted = false; // Whether the game has started
    private boolean spacePressed = false; // To handle continuous space key press

    // List to store all pipes on screen
    private ArrayList<Rectangle> pipes;

    // Random number generator for pipe height
    private Random random;

    // Timers to control game loop and score screen delay
    private Timer timer;
    private Timer scoreTimer;

    // Constructor: initializes the game
    public Flappy_Bird_Game()
    {
        // Set panel size and background
        setPreferredSize(new Dimension(GRID_SIZE * TILE_SIZE + SCORE_ROW_HEIGHT, GRID_SIZE * TILE_SIZE + SCORE_ROW_HEIGHT));
        setBackground(Color.black);

        // Enable focus for keyboard input
        setFocusable(true);
        addKeyListener(this); // Add key listener for controls

        // Initialize pipe list and random number generator
        pipes = new ArrayList<>();
        random = new Random();

        // Start the game
        startGame();
    }

    // This method is called whenever the panel needs to be repainted
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g); // Call parent class to clear screen

        if (!gameStarted) // If game hasn't started
        {
            // Draw start screen with instructions
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, GRID_SIZE * TILE_SIZE + SCORE_ROW_HEIGHT, GRID_SIZE * TILE_SIZE + SCORE_ROW_HEIGHT);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String orMessage = "OR";
            FontMetrics metrics = g.getFontMetrics();

            int orX = (GRID_SIZE * TILE_SIZE + SCORE_ROW_HEIGHT - metrics.stringWidth(orMessage)) / 2;
            int orY = (GRID_SIZE * TILE_SIZE + SCORE_ROW_HEIGHT - metrics.getHeight()) / 2;
            g.drawString(orMessage, orX, orY);

            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String startMessage = "Press ENTER To Start The Game";

            int startX = (GRID_SIZE * TILE_SIZE + SCORE_ROW_HEIGHT - metrics.stringWidth(startMessage)) / 2;
            int startY = orY - 70;
            g.drawString(startMessage, startX, startY);

            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String quitMessage = "Press ESC To Quit The Game";

            int quitX = (GRID_SIZE * TILE_SIZE + SCORE_ROW_HEIGHT - metrics.stringWidth(quitMessage)) / 2;
            int quitY = orY + 70;
            g.drawString(quitMessage, quitX, quitY);
        }
        else if (running) // If the game is running
        {
            // Draw score area
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, GRID_SIZE * TILE_SIZE + SCORE_ROW_HEIGHT, SCORE_ROW_HEIGHT);

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String scoreMessage = "Score : " + score;
            FontMetrics metrics = g.getFontMetrics();

            int scoreX = (GRID_SIZE * TILE_SIZE + SCORE_ROW_HEIGHT - metrics.stringWidth(scoreMessage)) / 2;
            int scoreY = (SCORE_ROW_HEIGHT + metrics.getHeight()) / 2 - 5;
            g.drawString(scoreMessage, scoreX, scoreY);

            // Draw the bird (yellow square)
            g.setColor(Color.YELLOW);
            g.fillRect(birdX, birdY, BIRD_SIZE, BIRD_SIZE);

            // Draw the bird's beak
            g.setColor(Color.ORANGE);
            int[] beakX = {birdX + BIRD_SIZE, birdX + BIRD_SIZE + 8, birdX + BIRD_SIZE};
            int[] beakY = {birdY + 6, birdY + BIRD_SIZE / 2, birdY + BIRD_SIZE - 6};
            g.fillPolygon(beakX, beakY, 3);

            // Draw all pipes
            g.setColor(Color.GREEN);
            for (int i = 0; i < pipes.size(); i++)
            {
                Rectangle pipe = pipes.get(i);
                g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height);
            }
        }
        else if (showScoreWindow) // If showing score screen after game over
        {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, GRID_SIZE * TILE_SIZE + SCORE_ROW_HEIGHT, GRID_SIZE * TILE_SIZE + SCORE_ROW_HEIGHT);

            g.setColor(Color.BLUE);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            String scoreMessage = "Score : " + score;
            FontMetrics metrics = g.getFontMetrics();

            int scoreX = (GRID_SIZE * TILE_SIZE + SCORE_ROW_HEIGHT - metrics.stringWidth(scoreMessage)) / 2;
            int scoreY = (GRID_SIZE * TILE_SIZE + SCORE_ROW_HEIGHT) / 2;
            g.drawString(scoreMessage, scoreX, scoreY);
        }
        else // After game over but before restarting
        {
            // Draw restart screen with options
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, GRID_SIZE * TILE_SIZE + SCORE_ROW_HEIGHT, GRID_SIZE * TILE_SIZE + SCORE_ROW_HEIGHT);

            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String orMessage = "OR";
            FontMetrics metrics = g.getFontMetrics();

            int orX = (GRID_SIZE * TILE_SIZE + SCORE_ROW_HEIGHT - metrics.stringWidth(orMessage)) / 2;
            int orY = (GRID_SIZE * TILE_SIZE + SCORE_ROW_HEIGHT - metrics.getHeight()) / 2;
            g.drawString(orMessage, orX, orY);

            g.setColor(Color.GREEN);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String restartMessage = "Press ENTER To Restart The Game";

            int restartX = (GRID_SIZE * TILE_SIZE + SCORE_ROW_HEIGHT - metrics.stringWidth(restartMessage)) / 2;
            int restartY = orY - 70;
            g.drawString(restartMessage, restartX, restartY);

            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String quitMessage = "Press ESC To Quit The Game";

            int quitX = (GRID_SIZE * TILE_SIZE + SCORE_ROW_HEIGHT - metrics.stringWidth(quitMessage)) / 2;
            int quitY = orY + 70;
            g.drawString(quitMessage, quitX, quitY);
        }
    }

    // Starts or restarts the game
    private void startGame()
    {
        running = true;
        birdX = 100; // Starting x-position of the bird
        birdY = GRID_SIZE * TILE_SIZE / 2; // Centered y-position
        velocityY = 0;
        score = 0;

        pipes.clear(); // Remove old pipes
        addPipe(); // Add new pipe

        // Stop any existing timer
        if (timer != null)
        {
            timer.stop();
        }

        // Start game loop timer
        timer = new Timer(GAME_SPEED, this);
        timer.start();
    }

    // Adds a pair of pipes (top and bottom)
    private void addPipe()
    {
        int pipeHeight = random.nextInt(200) + 50; // Random height for top pipe

        // Top pipe
        pipes.add(new Rectangle(GRID_SIZE * TILE_SIZE, SCORE_ROW_HEIGHT, PIPE_WIDTH, pipeHeight));

        // Bottom pipe
        pipes.add(new Rectangle(GRID_SIZE * TILE_SIZE, SCORE_ROW_HEIGHT + pipeHeight + PIPE_GAP, PIPE_WIDTH, GRID_SIZE * TILE_SIZE - pipeHeight - PIPE_GAP));
    }

    // Moves bird and pipes, checks for collisions
    private void move()
    {
        velocityY += 1; // Gravity effect
        birdY += velocityY; // Update bird's vertical position

        // Move all pipes to the left
        for (Rectangle pipe : pipes)
        {
            pipe.x -= 5;
        }

        // If first pipe moves off-screen, remove it and add new pipe
        if (pipes.get(0).x + PIPE_WIDTH < 0)
        {
            pipes.remove(0);
            pipes.remove(0);
            addPipe();
            score++; // Increase score
        }

        checkCollision(); // Check for collision with pipes or walls

        // If collision occurred, show score for a short delay
        if (showScoreWindow) {
            if (scoreTimer != null)
            {
                scoreTimer.stop();
            }

            scoreTimer = new Timer(2000, new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    showScoreWindow = false;
                    repaint();
                    scoreTimer.stop();
                }
            });

            scoreTimer.setRepeats(false);
            scoreTimer.start();
        }
    }

    // Checks if bird hits a pipe or the screen edges
    private void checkCollision()
    {
        if (birdY < SCORE_ROW_HEIGHT) // Hits top wall
        {
            running = false;
            showScoreWindow = true;
        }

        if (birdY + BIRD_SIZE > GRID_SIZE * TILE_SIZE + SCORE_ROW_HEIGHT) // Hits ground
        {
            running = false;
            showScoreWindow = true;
        }

        // Check collision with each pipe
        for (Rectangle pipe : pipes)
        {
            if (birdX < pipe.x + pipe.width && birdX + BIRD_SIZE > pipe.x && birdY < pipe.y + pipe.height && birdY + BIRD_SIZE > pipe.y)
            {
                running = false;
                showScoreWindow = true;
            }
        }
    }

    // Called every timer tick
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (running)
        {
            move(); // Move bird and pipes
        }
        repaint(); // Redraw screen
    }

    // Handles key press events
    @Override
    public void keyPressed(KeyEvent e)
    {
        if (!gameStarted) // Before game starts
        {
            if (e.getKeyCode() == KeyEvent.VK_ENTER)
            {
                gameStarted = true;
                startGame();
            }
            else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            {
                System.exit(0); // Exit game
            }
        }
        else if (showScoreWindow) // While score is being shown
        {
            // Do nothing
        }
        else if (!running) // After game ends
        {
            if (e.getKeyCode() == KeyEvent.VK_ENTER)
            {
                gameStarted = true;
                startGame(); // Restart game
            }
            else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
            {
                System.exit(0); // Exit game
            }
        }
        else // While game is running
        {
            if (e.getKeyCode() == KeyEvent.VK_SPACE)
            {
                if (!spacePressed)
                {
                    velocityY = -10; // Bird jumps
                    spacePressed = true;
                }
            }
        }
    }

    // Handles key release events
    @Override
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            spacePressed = false;
        }
    }

    // Not used, but required by interface
    @Override
    public void keyTyped(KeyEvent e) {}

    // Main method to launch the game
    public static void main(String args[])
    {
        JFrame frame = new JFrame("Flappy Bird"); // Create game window
        Flappy_Bird_Game game = new Flappy_Bird_Game(); // Create game instance
        frame.add(game); // Add game to window
        frame.pack(); // Set window size
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close app on exit
        frame.setLocationRelativeTo(null); // Center window
        frame.setVisible(true); // Show window
    }
}