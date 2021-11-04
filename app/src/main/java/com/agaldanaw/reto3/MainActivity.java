package com.agaldanaw.reto3;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    static final int DIALOG_DIFFICULTY_ID = 0;
    static final int DIALOG_QUIT_ID = 1;
    static final int DIALOG_ABOUT_ID = 2;

    public TicTacToeGame mGame;
    private Button mBoardButtons[];
    private TextView mInfoTextView;
    private TextView infoHumanWins;
    private TextView infoComputerWins;
    private TextView infoTies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGame = new TicTacToeGame();
        mBoardButtons = new Button[mGame.BOARD_SIZE];

        mBoardButtons[0] = (Button) findViewById(R.id.one);
        mBoardButtons[1] = (Button) findViewById(R.id.two);
        mBoardButtons[2] = (Button) findViewById(R.id.three);
        mBoardButtons[3] = (Button) findViewById(R.id.four);
        mBoardButtons[4] = (Button) findViewById(R.id.five);
        mBoardButtons[5] = (Button) findViewById(R.id.six);
        mBoardButtons[6] = (Button) findViewById(R.id.seven);
        mBoardButtons[7] = (Button) findViewById(R.id.eight);
        mBoardButtons[8] = (Button) findViewById(R.id.nine);

        mGame.SetButtons(mBoardButtons);

        mInfoTextView = (TextView) findViewById(R.id.information);
        infoHumanWins = (TextView) findViewById(R.id.humanWins);
        infoComputerWins = (TextView) findViewById(R.id.computerWins);
        infoTies = (TextView) findViewById(R.id.ties);

/*        findViewById(R.id.rebootGame).setOnClickListener(new ButtonRebootClickListener());*/
        findViewById(R.id.playAgain).setOnClickListener(new ButtonPlayAgainClickListener(mGame));

        startGame();
    }

    public void SetTextWins()
    {
        String human = getResources().getString(R.string.human_wins);
        String computer = getResources().getString(R.string.computer_wins);
        String ties = getResources().getString(R.string.ties);

        infoHumanWins.setText(human + " " +  mGame.GetHumanWins());
        infoComputerWins.setText(computer + " " + mGame.GetComputerWins());
        infoTies.setText(ties + " " + mGame.GetTies());
    }

    private void startGame()
    {
        mGame.clearBoard(true);

        // Reset all buttons
        for (int i = 0; i < mBoardButtons.length; i++) {
            mBoardButtons[i].setText("");
            mBoardButtons[i].setEnabled(true);
            mBoardButtons[i].setOnClickListener(new ButtonClickListener(i, mGame));
        }
        SetFirstTurn();
        SetTextWins();
    }

    public void SetFirstTurn()
    {
        char player = mGame.initPlayer();
        if(player == TicTacToeGame.HUMAN_PLAYER)
        {
            mInfoTextView.setText(R.string.first_human);
        }
        else
        {
            mInfoTextView.setText(R.string.first_computer);
            int winner = mGame.checkForWinner();
            setTextInfo(winner);
        }
    }

    public void setTextInfo(int winner)
    {
        if (winner == 0) {
            mInfoTextView.setText(R.string.turn_human);
        }
        else
        {
            if (winner == 1) {
                mGame.UpdateTies();
                mInfoTextView.setText(R.string.result_tie);
            }
            else if (winner == 2){
                mGame.UpdateHumanWins();
                mInfoTextView.setText(R.string.result_human_wins);
            }
            else {
                mGame.updateComputerWins();
                mInfoTextView.setText(R.string.result_computer_wins);
            }
            mGame.DisableAllbuttons();
        }
    }


    private class ButtonClickListener implements OnClickListener {
        private int location;
        private TicTacToeGame mGame;

        public ButtonClickListener(int location, TicTacToeGame mGame) {
            this.location = location;
            this.mGame = mGame;
        }

        public void onClick(View view) {
            if (mBoardButtons[location].isEnabled()) {
                mGame.setMove(TicTacToeGame.HUMAN_PLAYER, location);
                // If no winner yet, let the computer make a move
                int winner = mGame.checkForWinner();

                if (winner == 0) {
                    mInfoTextView.setText(R.string.turn_computer);
                    int move = mGame.GetComputerMove();
                    mGame.setMove(TicTacToeGame.COMPUTER_PLAYER, move);
                    winner = mGame.checkForWinner();
                }

                setTextInfo(winner);
                SetTextWins();
            }
        }
    }

    private class ButtonPlayAgainClickListener implements OnClickListener {

        private TicTacToeGame mGame;

        public ButtonPlayAgainClickListener(TicTacToeGame mGame) {
            this.mGame = mGame;
        }

        public void onClick(View view) {
            mGame.clearBoard(false);
            SetFirstTurn();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_game:
                startGame();
                return true;
            case R.id.ai_difficulty:
                ShowDialog(DIALOG_DIFFICULTY_ID);
                return true;
            case R.id.quit:
                ShowDialog(DIALOG_QUIT_ID);
                return true;
            case R.id.about:
                ShowDialog(DIALOG_ABOUT_ID);
                return true;
        }
        return false;
    }

    private int getDifficultyLevelInteger()
    {
        TicTacToeGame.DifficultyLevel currentLevel = mGame.getDifficultyLevel();
        if(currentLevel == TicTacToeGame.DifficultyLevel.Easy)
            return 0;
        else if(currentLevel == TicTacToeGame.DifficultyLevel.Harder)
            return 1;
        return 2;
    }

    private void setDifficultyLevelInteger(int level)
    {
        if(0 == level)
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Easy);
        else if(1 == level)
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Harder);
        else
            mGame.setDifficultyLevel(TicTacToeGame.DifficultyLevel.Expert);
    }

    public void ShowDialog(int id) {
        Dialog dialog = null;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        switch(id) {
            case DIALOG_DIFFICULTY_ID:
                builder.setTitle(R.string.difficulty_choose);
                final CharSequence[] levels = {
                        getResources().getString(R.string.difficulty_level_easy),
                        getResources().getString(R.string.difficulty_level_hard),
                        getResources().getString(R.string.difficulty_level_expert)};

                builder.setSingleChoiceItems(levels, getDifficultyLevelInteger(),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                dialog.dismiss(); // Close dialog
                                setDifficultyLevelInteger(item);
                                startGame();
                                Toast.makeText(getApplicationContext(), levels[item],
                                        Toast.LENGTH_SHORT).show();

                            }
                        });
                dialog = builder.create();
                break;
            case DIALOG_QUIT_ID:
                builder.setMessage(R.string.quit_question)
                        .setCancelable(false)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                MainActivity.this.finish();
                            }
                        })
                        .setNegativeButton(R.string.no, null);
                dialog = builder.create();
                break;
            case DIALOG_ABOUT_ID:
                Context context = getApplicationContext();
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.about_dialog, null);
                Drawable icon = context.getDrawable(R.drawable.icon);
                builder.setIcon(icon);
                builder.setView(layout);
                builder.setPositiveButton("OK", null);
                dialog = builder.create();
                dialog.setTitle(R.string.app_name);
                break;
        }

        dialog.show();
    }
}