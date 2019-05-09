import java.util.Scanner;

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


    public TicTacToe() {
        board = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = String.valueOf(3 * i + j);
            }
        }
    }

    public TicTacToe(String[][] board) {
        this.board = new String[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                this.board[i][j] = board[i][j];
            }
        }
    }


    public void input(int index) throws Exception {
        if (index < 0 || index > 8)
            throw new Exception("Invalid input");
        if (!board[index / 3][index % 3].equals(String.valueOf(index))){
            throw new Exception("already taken");
        }
        board[index / 3][index % 3] = turn;
        turn = turn.equals("X") ? "O" : "X";
    }


    public void setTurn(String turn) {
        this.turn = turn;
    }

    public String[][] getBoard() {
        return board;
    }

    public static int[] minMax(TicTacToe ticTacToe) {

        int[] ans = new int[3];

        int minYAmax;
        if (ticTacToe.turn.equals("X")) {
            minYAmax = Integer.MAX_VALUE;
        } else {
            minYAmax = Integer.MIN_VALUE;
        }

        if (ticTacToe.endOfGame())
            return new int[]{-1, -1, ticTacToe.whoWin()};

        int bwI = 0;
        int bwJ = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                TicTacToe tmp = new TicTacToe(ticTacToe.board);
                tmp.turn = ticTacToe.turn;
                try {
                    tmp.input(i * 3 + j);
                } catch (Exception e) {
                    if (tmp.endOfGame())
                        return new int[]{-1, -1, ticTacToe.whoWin()};
                    continue;
                }
                int value = minMax(tmp)[2];
                if (ticTacToe.turn.equals("X") && minYAmax > value) {
                    minYAmax = value;
                    bwI = i;
                    bwJ = j;
                } else if (ticTacToe.turn.equals("O") && minYAmax < value) {
                    minYAmax = value;
                    bwI = i;
                    bwJ = j;
                }
            }
        }

        ans[0] = bwI;
        ans[1] = bwJ;
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
        System.out.println("--------------------");
        System.out.println("-  " + board[0][0] + "  -  " + board[0][1] + "  -  " + board[0][2] + "  -");
        System.out.println("-  " + board[1][0] + "  -  " + board[1][1] + "  -  " + board[1][2] + "  -");
        System.out.println("-  " + board[2][0] + "  -  " + board[2][1] + "  -  " + board[2][2] + "  -");
        System.out.println("--------------------");
    }


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TicTacToe ticTacToe = new TicTacToe();
        ticTacToe.turn = "X";
        System.out.println("Start!!");
        ticTacToe.printBoard();
        while (!ticTacToe.endOfGame()) {
            int goTo;
            if (ticTacToe.turn.equals("X"))
                goTo = scanner.nextInt();
            else {
                int[] tmp = minMax(ticTacToe);
                goTo = 3 * tmp[0] + tmp[1];
                System.out.println(goTo);
            }
            try {
                ticTacToe.input(goTo);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            ticTacToe.printBoard();
        }

        if(ticTacToe.whoWin() == 1)
            System.out.println("Excellent! You won the game... :)");
        else if(ticTacToe.whoWin() == -1)
            System.out.println("lose... :(");
        else
            System.out.println("draw :|");
    }

}