package edu.miracostacollege.cs134.theescape;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import edu.miracostacollege.cs134.theescape.model.Direction;
import edu.miracostacollege.cs134.theescape.model.Player;
import edu.miracostacollege.cs134.theescape.model.Zombie;

import static edu.miracostacollege.cs134.theescape.model.BoardValues.FREE;
import static edu.miracostacollege.cs134.theescape.model.BoardValues.EXIT;
import static edu.miracostacollege.cs134.theescape.model.BoardValues.OBST;
import static edu.miracostacollege.cs134.theescape.model.Direction.DOWN;
import static edu.miracostacollege.cs134.theescape.model.Direction.LEFT;
import static edu.miracostacollege.cs134.theescape.model.Direction.RIGHT;
import static edu.miracostacollege.cs134.theescape.model.Direction.UP;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private int wins = 0;
    private int losses = 0;

    public static final int TOTAL_ROWS = 8;
    public static final int TOTAL_COLS = 8;

    public static final int PLAYER_ROW = 1;
    public static final int PLAYER_COL = 1;

    public static final int ZOMBIE_ROW = 2;
    public static final int ZOMBIE_COL = 4;

    public static final int EXIT_ROW = 5;
    public static final int EXIT_COL = 7;

    private static final float FLING_THRESHOLD = 500f;

    private LinearLayout boardLinearLayout;
    private TextView winsTextView;
    private TextView lossesTextView;
    private GestureDetector gestureDetector;

    private Player player;
    private Zombie zombie;

    final int gameBoard[][] = {
            {OBST, OBST, OBST, OBST, OBST, OBST, OBST, OBST},
            {OBST, FREE, FREE, FREE, FREE, FREE, OBST, OBST},
            {OBST, FREE, OBST, FREE, FREE, FREE, FREE, OBST},
            {OBST, FREE, OBST, FREE, FREE, FREE, FREE, OBST},
            {OBST, FREE, OBST, OBST, FREE, FREE, OBST, OBST},
            {OBST, FREE, FREE, FREE, FREE, FREE, FREE, EXIT},
            {OBST, FREE, OBST, FREE, FREE, FREE, FREE, OBST},
            {OBST, OBST, OBST, OBST, OBST, OBST, OBST, OBST}
    };

    ImageView viewBoard[][] = new ImageView[TOTAL_ROWS][TOTAL_COLS];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boardLinearLayout = findViewById(R.id.boardLinearLayout);
        winsTextView = findViewById(R.id.winsTextView);
        lossesTextView = findViewById(R.id.lossesTextView);

        gestureDetector = new GestureDetector(this, this);
        startNewGame();
    }

    //Override onTouchEvent method
    //Distributes touch event to entire activity
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private void startNewGame() {

        //Nested for loop
        for(int i = 0; i < TOTAL_ROWS; i++){
            //Get each row
            LinearLayout row = (LinearLayout) boardLinearLayout.getChildAt(i);
            //Get each imageView in that row
            for(int j = 0; j < TOTAL_COLS; j++){
                ImageView imageView = (ImageView) row.getChildAt(j);
                //Put it into the board
                viewBoard[i][j] = imageView;
                //Determine what image should be put in the image view based on the gameBoard
                switch (gameBoard[i][j]){
                    case OBST:
                        imageView.setImageResource(R.drawable.obstacle);
                        break;
                    case EXIT:
                        imageView.setImageResource(R.drawable.exit);
                        break;
                    case FREE:
                        imageView.setImageDrawable(null);
                        break;
                }
            }
        }

        // Player starts at 1, 1
        player = new Player(PLAYER_ROW, PLAYER_COL);
        viewBoard[PLAYER_ROW][PLAYER_COL].setImageResource(R.drawable.male_player);

        zombie = new Zombie(ZOMBIE_ROW, ZOMBIE_COL);
        viewBoard[ZOMBIE_ROW][ZOMBIE_COL].setImageResource(R.drawable.zombie);

        //DONE: Loop through the viewBoard and initialize each of the ImageViews
        //DONE: to the children of the LinearLayouts
        //DONE: Use the gameBoard to determine which image to assign:

        //DONE: OBST = R.drawable.obstacle
        //DONE: EXIT = R.drawable.exit
        //DONE: FREE = null (no image to load)

        //DONE: Instantiate a new Player object at PLAYER_ROW, PLAYER_COL
        //DONE: Set the imageView at that position to R.drawable.player

        //DONE: Instantiate a new Zombie object at ZOMBIE_ROW, ZOMBIE_COL
        //DONE: Set the imageView at that position to R.drawable.zombie
    }

    private void movePlayer(float velocityX, float velocityY) {
        //DONE: Set the player's current image view drawable to null
        viewBoard[player.getRow()][player.getCol()].setImageDrawable(null);
        Direction direction;

        //DONE: Determine the direction of the fling (based on velocityX and velocityY)

        float absX = Math.abs(velocityX);
        float absY = Math.abs(velocityY);

        if(absY > absX){
            if(velocityY < 0)
                direction = UP;
            else
                direction = DOWN;
        } else {
            if(velocityX < 0)
                direction = LEFT;
            else
                direction = RIGHT;
        }

        player.move(gameBoard, direction);
        //DONE: The velocity must exceed FLING_THRESHOLD to count (otherwise, it's not really a move)
        //DONE: Move the player
        //DONE: Set the player's current image view to R.drawable.player after the move

        viewBoard[player.getRow()][player.getCol()].setImageResource(R.drawable.male_player);
    }

    private void moveZombie() {
        //DONE: Set the zombie's current image view drawable to null
        viewBoard[zombie.getRow()][zombie.getCol()].setImageDrawable(null);
        //DONE: Move the zombie
        zombie.move(gameBoard, player.getRow(), player.getCol());
        //DONE: Set the zombie's current image view to R.drawable.zombie after the move
        viewBoard[zombie.getRow()][zombie.getCol()].setImageResource(R.drawable.zombie);
    }

    private void determineOutcome() {
        //DONE: Determine the outcome of the game (win or loss)
        //DONE: It's a win if the player's row/col is the same as the exit row/col
        //DONE: Call the handleWin() method

        //DONE: It's a loss if the player's row/col is the same as the zombie's row/col
        //DONE: Call the handleLoss() method

        //DONE: Otherwise, do nothing, just return.

        if(player.getRow() == zombie.getRow() && player.getCol() == zombie.getCol()){
            handleLoss();
        }else if(player.getRow() == EXIT_ROW && player.getCol() == EXIT_COL){
            handleWin();
        } else {
            return;
        }
    }

    private void handleWin()
    {
        //DONE: Implement the handleWin() method by accomplishing the following:
        //DONE: Increment the wins
        wins++;
        //DONE: Set the imageView (at the zombie's row/col) to the R.drawable.bunny
        viewBoard[zombie.getRow()][zombie.getCol()].setImageResource(R.drawable.bunny);
        //TODO: Start an animation

        //TODO: Wait 2 seconds, then start a new game
        startNewGame();
    }

    private void handleLoss()
    {
        //DONE: Implement the handleLoss() method by accomplishing the following:
        //DONE: Increment the losses
        losses++;
        //DONE: Set the imageView (at the player's row/col) to the R.drawable.blood
        viewBoard[player.getRow()][player.getCol()].setImageResource(R.drawable.blood);
        //TODO: Start an animation

        //TODO: Wait 2 seconds, then start a new game

        startNewGame();
    }


    Runnable newGameRunnable = new Runnable() {
        @Override
        public void run() {
            startNewGame();
        }
    };

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        movePlayer(velocityX, velocityY);
        moveZombie();
        determineOutcome();
        return true;
    }
}
