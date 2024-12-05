import java.util.Scanner;

public class TicTacToe {
    private static final String EXIT_COMMAND = "exit";
    private static final int BOARD_SIZE = 3;
    private static final char EMPTY_CELL = ' ';
    private static final char PLAYER_X = 'X';
    private static final char PLAYER_O = 'O';

    public static void main(String[] args) {
        char[][] board = initializeBoard();
        char currentPlayer = PLAYER_X;
        boolean isGameRunning = true;

        System.out.println("Welcome to Tic-Tac-Toe!");
        System.out.println("Players can type '" + EXIT_COMMAND + "' to quit the game.");
        printBoard(board);

        Scanner scanner = new Scanner(System.in);

        while (isGameRunning) {
            System.out.println("Player " + currentPlayer + "'s turn. Enter your move (row and column) as 'row,col':");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase(EXIT_COMMAND)) {
                System.out.println("Player " + currentPlayer + " has chosen to exit. Game over.");
                isGameRunning = false;
                continue;
            }

            if (isValidInput(input, board)) {
                int[] move = parseMove(input);
                board[move[0]][move[1]] = currentPlayer;
                printBoard(board);

                if (isWinner(board, currentPlayer)) {
                    System.out.println("Player " + currentPlayer + " wins!");
                    isGameRunning = false;
                } else if (isDraw(board)) {
                    System.out.println("It's a draw!");
                    isGameRunning = false;
                } else {
                    currentPlayer = switchPlayer(currentPlayer);
                }
            } else {
                System.out.println("Invalid move! Please try again.");
            }
        }

        System.out.println("Thank you for playing!");
        scanner.close();
    }

    private static char[][] initializeBoard() {
        char[][] board = new char[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = EMPTY_CELL;
            }
        }
        return board;
    }

    private static void printBoard(char[][] board) {
        System.out.println("Current board:");
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(" " + board[i][j] + " ");
                if (j < BOARD_SIZE - 1) {
                    System.out.print("|");
                }
            }
            System.out.println();
            if (i < BOARD_SIZE - 1) {
                System.out.println("---+---+---");
            }
        }
    }

    private static boolean isValidInput(String input, char[][] board) {
        input = input.replace(" ", ",");
        if (!input.matches("\\d,\\d")) {
            return false;
        }
        int[] move = parseMove(input);
        int row = move[0];
        int col = move[1];

        return row >= 0 && row < BOARD_SIZE &&
                col >= 0 && col < BOARD_SIZE &&
                board[row][col] == EMPTY_CELL;
    }

    private static int[] parseMove(String input) {
        input = input.replace(" ", ",");
        String[] parts = input.split(",");
        int row = Integer.parseInt(parts[0]);
        int col = Integer.parseInt(parts[1]);
        return new int[]{row, col};
    }

    private static char switchPlayer(char currentPlayer) {
        return currentPlayer == PLAYER_X ? PLAYER_O : PLAYER_X;
    }

    private static boolean isWinner(char[][] board, char player) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            if ((board[i][0] == player && board[i][1] == player && board[i][2] == player) ||
                    (board[0][i] == player && board[1][i] == player && board[2][i] == player)) {
                return true;
            }
        }
        return (board[0][0] == player && board[1][1] == player && board[2][2] == player) ||
                (board[0][2] == player && board[1][1] == player && board[2][0] == player);
    }

    private static boolean isDraw(char[][] board) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == EMPTY_CELL) {
                    return false;
                }
            }
        }
        return true;
    }
}
