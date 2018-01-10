package kamilmilik.androiduczelniacwiczenia_cw2_ver2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private boolean isCheckedComputerMode;
    private  TextView whoseMoveTextView;
    private Board board;
    private int turn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newGameSetUp();
    }
    public void setUpTextView(){
        whoseMoveTextView = findViewById(R.id.whoseMoveTextView);
        whoseMoveTextView.setVisibility(View.VISIBLE);
        whoseMoveTextView.setText("Teraz ruch gracza: X");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch (item.getItemId()) {
            case R.id.menu_computer_move:
                if(item.isChecked()){
                    isCheckedComputerMode = false;
                    item.setChecked(false);
                }else{
                    Toast.makeText(this, "Computer Action", Toast.LENGTH_SHORT).show();
                    isCheckedComputerMode = true;
                    item.setChecked(true);
                }
                return true;
            case R.id.menu_new_game:
                newGameSetUp();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void buttonsBehavior(View view){
        Button buttonClicked = (Button) view;
        for(int i = 0 ; i < board.NUM_OF_COLS; i++){
            for(int j = 0 ; j < board.NUM_OF_ROWS ; j++){
                if(board.getBoardButtons()[i][j].getId() == buttonClicked.getId()){
                    Log.i(TAG,"board.getBoard()[i][j] " + board.getBoard()[i][j] + "");
                    if(!board.getWhoWin().isGameOver()) {
                        Log.i(TAG,"!isGameOver " + !board.getWhoWin().isGameOver() + "");
                        if (board.getBoard()[i][j] == Identifier.BLANK_IDENTIFIER) {
                            Log.i(TAG,"turn is: " + turn);
                            if (turn == Identifier.CIRCLE_INDETIFIER) {
                                board.makeMoveToGivenPoint(new Point(i, j), turn);
                                whoseMoveTextView.setText("Teraz ruch gracza: X");
                                buttonClicked.setBackground(getResources().getDrawable(R.drawable.circle));
                                turn = Identifier.CROSS_INDETIFIER;
                                if (isCheckedComputerMode) {
                                    computerMoveAction(turn);
                                }
                            } else if (turn == Identifier.CROSS_INDETIFIER) {
                                board.makeMoveToGivenPoint(new Point(i, j), turn);
                                whoseMoveTextView.setText("Teraz ruch gracza: O");
                                buttonClicked.setBackground(getResources().getDrawable(R.drawable.cross));
                                turn = Identifier.CIRCLE_INDETIFIER;
                                if (isCheckedComputerMode) {
                                    computerMoveAction(turn);
                                }
                            }
                        }
                    }
                    if (board.getWhoWin().hasXWon()) {
                        whoseMoveTextView.setText("Wygral gracz : X");
                    } else if (board.getWhoWin().hasOWon()) {
                        whoseMoveTextView.setText("Wygral gracz : O");
                    } else if(board.getWhoWin().isGameOver()){
                        whoseMoveTextView.setText("Remis");
                    }
                }
            }
        }
    }
    public void randomMoveAction(int turn){
        int randi = (int) (Math.random() * 3);
        int randj = (int) (Math.random() * 3);
        if(!board.getWhoWin().isGameOver()) {
            if (board.getBoard()[randi][randj] == Identifier.BLANK_IDENTIFIER) {
                board.makeMoveToGivenPoint(new Point(randi, randj), turn);
                if (turn == Identifier.CIRCLE_INDETIFIER) {
                    board.getBoardButtons()[randi][randj].setBackground(getResources().getDrawable(R.drawable.circle));
                    whoseMoveTextView.setText("Teraz ruch gracza: X");
                    this.turn = Identifier.CROSS_INDETIFIER;
                    ;
                } else if (turn == Identifier.CROSS_INDETIFIER) {
                    board.getBoardButtons()[randi][randj].setBackground(getResources().getDrawable(R.drawable.cross));
                    whoseMoveTextView.setText("Teraz ruch gracza: O");
                    this.turn = Identifier.CIRCLE_INDETIFIER;
                }
            } else {
                randomMoveAction(turn);//losuj od nowa liczbe jak nie trafiles z ta wczesniejsza
            }
        }
    }
    private WhoWin whoWin;
    private MinimaxAlgorithm minimaxAlgorithm;
    public void computerMoveAction(int turn){
        Log.i(TAG, "computerMoveAction turn : " + turn);
       // randomMoveAction(turn);
        if(!board.getWhoWin().isGameOver()) {
            minimaxAlgorithm.minimax(0, turn);
            board.makeMoveToGivenPoint(minimaxAlgorithm.getComputerMove(), turn);
            int i = minimaxAlgorithm.getComputerMove().x;
            int j = minimaxAlgorithm.getComputerMove().y;
            Log.i(TAG, "bestMove " + i + " " + j);
            if (turn == Identifier.CIRCLE_INDETIFIER) {
                board.getBoardButtons()[i][j].setBackground(getResources().getDrawable(R.drawable.circle));
                whoseMoveTextView.setText("Teraz ruch gracza: X");
                this.turn = Identifier.CROSS_INDETIFIER;
                ;
            } else if (turn == Identifier.CROSS_INDETIFIER) {
                board.getBoardButtons()[i][j].setBackground(getResources().getDrawable(R.drawable.cross));
                whoseMoveTextView.setText("Teraz ruch gracza: O");
                this.turn = Identifier.CIRCLE_INDETIFIER;
            }
        }
    }
    public void newGameButtonClicked(View view){
        newGameSetUp();
    }
    private void newGameSetUp() {
        setUpTextView();
        board = new Board(this);
        whoWin = new WhoWin(board);
        minimaxAlgorithm = new MinimaxAlgorithm(board,whoWin);
        board.setUpBoardButtons();
        turn = Identifier.CROSS_INDETIFIER;
        board.initializeGame();
    }
}
