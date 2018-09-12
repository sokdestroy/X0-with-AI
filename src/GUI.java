import jdk.nashorn.internal.scripts.JD;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class GUI extends JFrame {
    private GamePanel gamePanel;
    private char[][]status;
    private Bot bot;
    private int hasWinner;

    private char playerFigure;
    private char opponent;
    private boolean playerMove;

    public static void main(String []args) {
        new GUI().go();
    }

    public GUI() {
        gameInit();

        gamePanel = new GamePanel();
        gamePanel.setStatus(status);

        addMouseListener(new CheckMuouseListener());
        getContentPane().add(gamePanel);
        setBounds(200,200,400,400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("X0 game");
        setVisible(true);
    }

    public void gameInit() {
        JDialog initDialog = new JDialog(this,"Крестик или нолик?",true);
        JButton crossBtn = new JButton("Крестик");
        JButton zeroBtn = new JButton("Нолик");

        initDialog.setLayout(new FlowLayout());

        crossBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerFigure = 'X';
                opponent = '0';
                bot = new Bot(opponent);

                initDialog.setVisible(false);
            }
        });

        zeroBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playerFigure = '0';
                opponent = 'X';
                bot = new Bot(opponent);

                initDialog.setVisible(false);
            }
        });

        status = new char[][] {{'-','-','-'},{'-','-','-'},{'-','-','-'}};

        int move = (int)(Math.random() + 0.5);
        if (move == 1) {
            playerMove = true;
        }
        else {
            playerMove = false;
        }

        hasWinner = -1;

        initDialog.getContentPane().add(crossBtn);
        initDialog.getContentPane().add(zeroBtn);
        initDialog.setBounds(250,250,150,100);
        initDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        initDialog.setVisible(true);
    }

    public void go() {
        while (hasWinner == -1) {
            char[][] a = status.clone();
            if (!playerMove)  {
                status = bot.getOpinion(status);
                gamePanel.setStatus(status);
                gamePanel.repaint();
                playerMove = true;
            }
            checkWinner(a);
        }

        String endStatus;
        if (hasWinner == 0)
            endStatus = "Игра окончена. Результат - ничья";
        else if (hasWinner == 1)
            endStatus ="Игра окончена. Победа ваша";
        else endStatus ="Игра окончена. Победил компьтер";

        JDialog endDialog = new JDialog(this,"Игра окончена",true);
        JLabel quastion = new JLabel(endStatus);
        JButton noBtn = new JButton("Выйти");
        JButton yesBtn = new JButton("Сыграть еще");

        yesBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                endDialog.setVisible(false);
                setVisible(false);

                main(new String[]{""});
            }
        });

        noBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        endDialog.setLayout(new FlowLayout());
        endDialog.getContentPane().add(quastion);
        endDialog.getContentPane().add(yesBtn);
        endDialog.getContentPane().add(noBtn);
        endDialog.setBounds(150,150,300,150);
        endDialog.setVisible(true);
    }


    public void checkWinner(char[][] a) {
       if (checkWinOrLose(status, playerFigure)) hasWinner = 1;
       else if (checkWinOrLose(status, opponent)) hasWinner = 2;
       else if (!checkEmptyPole(status)) hasWinner = 0;
    }

    public boolean checkEmptyPole(char[][] stat) {
        for (char[] i : stat)
            for (char j : i)
                if (j == '-') return true;
        return false;
    }

    public boolean checkWinOrLose(char[][] stat, int fig) {
        //проверка столбцов
        if (stat[0][0] == fig && stat[1][0] == fig && stat[2][0] == fig) return true;
        if (stat[0][1] == fig && stat[1][1] == fig && stat[2][1] == fig) return true;
        if (stat[0][2] == fig && stat[1][2] == fig && stat[2][2] == fig) return true;
        //проверка строк
        if (stat[0][0] == fig && stat [0][1] == fig && stat[0][2] == fig) return true;
        if (stat[1][0] == fig && stat [1][1] == fig && stat[1][2] == fig) return true;
        if (stat[2][0] == fig && stat [2][1] == fig && stat[2][2] == fig) return true;
        //проверка диагоналей
        if (stat[0][0] == fig && stat[1][1] == fig && stat[2][2] == fig) return true;
        if (stat[2][0] == fig && stat[1][1] == fig && stat[0][2] == fig) return true;
        return false;
    }

    public class CheckMuouseListener implements MouseListener {
        public void mouseEntered(MouseEvent ev) {}
        public void mouseReleased(MouseEvent ev) {}
        public void mouseExited(MouseEvent ev) {}
        public void mousePressed(MouseEvent ev) {}

        public void mouseClicked(MouseEvent ev) {
            int x = ev.getX(), y = ev.getY();
            int i, j;

            if (x < getWidth() / 3) i = 0;
            else if (x > getWidth() / 3 && x < getWidth() / 3 * 2) i = 1;
            else i = 2;

            if (y < getHeight() / 3) j = 0;
            else if (y > getHeight() / 3 && y < getHeight() / 3 * 2) j = 1;
            else j = 2;
            if (playerMove && status[i][j] != opponent) {
                status[i][j] = playerFigure;
                gamePanel.setStatus(status);
                gamePanel.repaint();
                playerMove = false;
                checkWinner(status);
            }
        }
    }


}
