import java.util.Scanner;


/**
 * @author Yalda Yarandi
 * tic-tac-toe game with AI (min-max algorithm)
 */



public class TicTacToe {

    private String[][] board;

    private String turn;

    private int[][] winSet = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6}
    };



    //main constructor
    public TicTacToe() {
        board = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = String.valueOf(3 * i + j);
            }
        }
    }


    //copy of a tictactoe board
    public TicTacToe(String[][] board) {
        this.board = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.board[i][j] = board[i][j];
            }
        }
    }


    /**
     * @param index of slot
     * @throws Exception : invalid input (out of bound)
     * @throws Exception : The slot has been full
     * for both of the exceptions the user must to re-enter slot number
     */
    public void input(int index) throws Exception {
        if (index < 0 || index > 8)
            throw new Exception("Invalid input");
        if (!board[index / 3][index % 3].equals(String.valueOf(index))){
            throw new Exception("The Slot has been full");
        }
        board[index / 3][index % 3] = turn;
        turn = turn.equals("X") ? "O" : "X";
    }


    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String getTurn() {
        return turn;
    }


    public static int[] minMax(TicTacToe ticTacToe) {

        int[] ans = new int[3];

        int minYAmax;
        if (ticTacToe.getTurn().equals("X")) {
            minYAmax = Integer.MAX_VALUE;
        } else {
            minYAmax = Integer.MIN_VALUE;
        }

        if (ticTacToe.endOfGame())
            return new int[]{-1, -1, ticTacToe.whoWin()};

        int bestWayI = 0;
        int bestWayJ = 0;
        in : for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(ticTacToe.getTurn().equals("X") && minYAmax == -1)
                    break in;
                if(ticTacToe.getTurn().equals("O") && minYAmax == 1)
                    break in;
                TicTacToe tmp = new TicTacToe(ticTacToe.board);
                tmp.setTurn(ticTacToe.getTurn());
                try {
                    tmp.input(i * 3 + j);
                } catch (Exception e) {
                    if (tmp.endOfGame()) {
                        return new int[]{-1, -1, ticTacToe.whoWin()};
                    }
                    continue;
                }
                int value = minMax(tmp)[2];
                if (ticTacToe.getTurn().equals("X") && minYAmax > value) {
                    minYAmax = value;
                    bestWayI = i;
                    bestWayJ = j;
                }if (ticTacToe.getTurn().equals("O") && minYAmax < value) {
                    minYAmax = value;
                    bestWayI = i;
                    bestWayJ = j;
                }
            }
        }

        ans[0] = bestWayI;
        ans[1] = bestWayJ;
        ans[2] = minYAmax;

        return ans;
    }


    public int whoWin() {
        for (int i = 0; i < 8; i++) {
            int place1X = winSet[i][0] / 3;
            int place1Y = winSet[i][0] % 3;
            int place2X = winSet[i][1] / 3;
            int place2Y = winSet[i][1] % 3;
            int place3X = winSet[i][2] / 3;
            int place3Y = winSet[i][2] % 3;
            if (board[place1X][place1Y].equals("O") && board[place2X][place2Y].equals("O") && board[place3X][place3Y].equals("O"))
                return 1;

            if (board[place1X][place1Y].equals("X") && board[place2X][place2Y].equals("X") && board[place3X][place3Y].equals("X"))
                return -1;
        }
        return 0;
    }


    public boolean endOfGame() {
        if (whoWin() == 1 || whoWin() == -1) {
            return true;
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j].equals(String.valueOf(3 * i + j)))
                    return false;
            }
        }
        return true;
    }


    public void printBoard() {
        System.out.println("-------------------");
        System.out.println("|  " + board[0][0] + "  |  " + board[0][1] + "  |  " + board[0][2] + "  |");
        System.out.println("-------------------");
        System.out.println("|  " + board[1][0] + "  |  " + board[1][1] + "  |  " + board[1][2] + "  |");
        System.out.println("-------------------");
        System.out.println("|  " + board[2][0] + "  |  " + board[2][1] + "  |  " + board[2][2] + "  |");
        System.out.println("-------------------");
    }



    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TicTacToe ticTacToe = new TicTacToe();
        ticTacToe.setTurn("X");
        System.out.println();
        System.out.println("Welcome to one player Tic Tac Toe.");
        System.out.println("X's will play first. Enter a slot number to place X in:");


        ticTacToe.printBoard();


        while (!ticTacToe.endOfGame()) {
            int slot;
            if (ticTacToe.turn.equals("X"))
                slot = scanner.nextInt();
            else {
                int[] tmp = minMax(ticTacToe);
                slot = 3 * tmp[0] + tmp[1];
                System.out.println(slot);
            }
            try {
                ticTacToe.input(slot);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            ticTacToe.printBoard();
        }

        System.out.println();

        if(ticTacToe.whoWin() == -1)
            System.out.println("Congratulations! you have won! :)" + "\n" + "Thanks for playing.");
        else if(ticTacToe.whoWin() == 1)
            System.out.println("lose... :(( "+ "\n" + "Thanks for playing.");
        else
            System.out.println("It's a draw! :|" + "\n" +"Thanks for playing.");
    }

}