import java.util.*;

public class Lab1 {

    static final int[][] goalState = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}};
    static final int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    static final char[] moveChar = {'U', 'D', 'L', 'R'};

    public static void main(String[] args) {
        int[][] initialState = {{5, 3, 1}, {0, 8, 7}, {2, 6, 4}}; // Updated initial state
        solveWithBFS(initialState);
        solveWithIDS(initialState);
    }

    static void solveWithBFS(int[][] initialState) {
        Queue<State> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        State initial = new State(initialState, 2, 2, ""); // Initialize with zero's position
        queue.add(initial);
        visited.add(Arrays.deepToString(initialState));

        while (!queue.isEmpty()) {
            State current = queue.poll();
            if (Arrays.deepEquals(current.board, goalState)) {
                System.out.println("BFS - Moves to reach the solved state:");
                System.out.println(current.moves);
                return;
            }

            for (int d = 0; d < 4; d++) {
                int newX = current.zeroX + directions[d][0];
                int newY = current.zeroY + directions[d][1];

                if (isValid(newX, newY)) {
                    int[][] newState = new int[3][3];
                    for (int i = 0; i < 3; i++) {
                        newState[i] = Arrays.copyOf(current.board[i], 3);
                    }

                    newState[current.zeroX][current.zeroY] = newState[newX][newY];
                    newState[newX][newY] = 0;

                    if (!visited.contains(Arrays.deepToString(newState))) {
                        queue.add(new State(newState, newX, newY, current.moves + moveChar[d]));
                        visited.add(Arrays.deepToString(newState));
                    }
                }
            }
        }
        System.out.println("BFS - No solution found.");
    }

    static void solveWithIDS(int[][] initialState) {
        for (int depth = 0; depth < Integer.MAX_VALUE; depth++) {
            Set<String> visited = new HashSet<>();
            boolean found = depthLimitedDFS(initialState, depth, visited, "");
            if (found) {
                System.out.println("IDS - Moves to reach the solved state:");
                return;
            }
        }
        System.out.println("IDS - No solution found.");
    }

    static boolean depthLimitedDFS(int[][] state, int depth, Set<String> visited, String moves) {
        if (depth == 0 && Arrays.deepEquals(state, goalState)) {
            System.out.println(moves);
            return true;
        }
        if (depth == 0) return false;

        String stateString = Arrays.deepToString(state);
        if (visited.contains(stateString)) return false;
        visited.add(stateString);

        int zeroX = -1, zeroY = -1;
        outerloop:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (state[i][j] == 0) {
                    zeroX = i;
                    zeroY = j;
                    break outerloop;
                }
            }
        }

        for (int d = 0; d < 4; d++) {
            int newX = zeroX + directions[d][0];
            int newY = zeroY + directions[d][1];

            if (isValid(newX, newY)) {
                int[][] newState = new int[3][3];
                for (int i = 0; i < 3; i++) {
                    newState[i] = Arrays.copyOf(state[i], 3);
                }

                newState[zeroX][zeroY] = newState[newX][newY];
                newState[newX][newY] = 0;

                boolean found = depthLimitedDFS(newState, depth - 1, visited, moves + moveChar[d]);
                if (found) return true;
            }
        }
        return false;
    }

    static boolean isValid(int x, int y) {
        return x >= 0 && x < 3 && y >= 0 && y < 3;
    }

    static class State {
        int[][] board;
        int zeroX, zeroY;
        String moves;

        State(int[][] board, int zeroX, int zeroY, String moves) {
            this.board = board;
            this.zeroX = zeroX;
            this.zeroY = zeroY;
            this.moves = moves;
        }
    }
}
