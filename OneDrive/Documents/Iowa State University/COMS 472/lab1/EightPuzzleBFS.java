import java.util.*;

public class EightPuzzleBFS {

    static final int[][] goalState = {{1, 2, 3}, {4, 5, 6}, {7, 8, 0}}; // Goal state

    public static void main(String[] args) {
        int[][] initialState = {{1, 2, 3}, {4, 6, 8}, {7, 5, 0}}; // Initial state

        System.out.println("Initial State:");
        printState(initialState);

        // Solve the puzzle
        bfs(initialState);

        System.out.println("Initial State:");
        printState(initialState);

        // Solve the puzzle
        ids(initialState);

        System.out.println("Initial State:");
        printState(initialState);

        // Solve the puzzle
        aStar(initialState);

        aStarManhattan(initialState);
    }

    static void aStarManhattan(int[][] initialState) {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(node -> node.f));
        Map<String, Integer> gValues = new HashMap<>();
        pq.offer(new Node(initialState, 0, calculateH2(initialState)));
        gValues.put(Arrays.deepToString(initialState), 0);

        while (!pq.isEmpty()) {
            Node currentNode = pq.poll();
            int[][] currentState = currentNode.state;

            if (Arrays.deepEquals(currentState, goalState)) {
                System.out.println("Goal State Reached:");
                printState(currentState);
                return;
            }

            List<int[][]> nextStates = generateNextStates(currentState);
            for (int[][] nextState : nextStates) {
                int tentativeG = gValues.get(Arrays.deepToString(currentState)) + 1;
                if (!gValues.containsKey(Arrays.deepToString(nextState)) || tentativeG < gValues.get(Arrays.deepToString(nextState))) {
                    gValues.put(Arrays.deepToString(nextState), tentativeG);
                    int hValue = calculateH2(nextState);
                    pq.offer(new Node(nextState, tentativeG, hValue));
                }
            }
        }

        System.out.println("No solution found.");
    }

    static int calculateH2(int[][] state) {
        int h = 0;
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                int value = state[i][j];
                if (value != 0) {
                    int targetX = (value - 1) / 3;
                    int targetY = (value - 1) % 3;
                    h += Math.abs(i - targetX) + Math.abs(j - targetY);
                }
            }
        }
        return h;
    }

    static void aStar(int[][] initialState) {
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(node -> node.f));
        Map<String, Integer> gValues = new HashMap<>();
        pq.offer(new Node(initialState, 0, calculateH1(initialState)));
        gValues.put(Arrays.deepToString(initialState), 0);

        while (!pq.isEmpty()) {
            Node currentNode = pq.poll();
            int[][] currentState = currentNode.state;

            if (Arrays.deepEquals(currentState, goalState)) {
                System.out.println("Goal State Reached:");
                printState(currentState);
                return;
            }

            List<int[][]> nextStates = generateNextStates(currentState);
            for (int[][] nextState : nextStates) {
                int tentativeG = gValues.get(Arrays.deepToString(currentState)) + 1;
                if (!gValues.containsKey(Arrays.deepToString(nextState)) || tentativeG < gValues.get(Arrays.deepToString(nextState))) {
                    gValues.put(Arrays.deepToString(nextState), tentativeG);
                    int hValue = calculateH1(nextState);
                    pq.offer(new Node(nextState, tentativeG, hValue));
                }
            }
        }

        System.out.println("No solution found.");
    }

    static int calculateH1(int[][] state) {
        int h = 0;
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                if (state[i][j] != goalState[i][j]) {
                    h++;
                }
            }
        }
        return h;
    }

    static class Node {
        int[][] state;
        int g; // Cost from initial state to current state
        int h; // Heuristic value
        int f; // Evaluation function f = g + h

        Node(int[][] state, int g, int h) {
            this.state = state;
            this.g = g;
            this.h = h;
            this.f = g + h;
        }
    }

    static void ids(int[][] initialState) {
        int maxDepth = 30; // Maximum depth to explore
        for (int depth = 0; depth <= maxDepth; depth++) {
            boolean found = dfs(initialState, depth);
            if (found) {
                System.out.println("Goal state found at depth: " + depth);
                return;
            }
        }
        System.out.println("Goal state not found within max depth.");
    }

    static boolean dfs(int[][] currentState, int depth) {
        if (depth == 0 && Arrays.deepEquals(currentState, goalState)) {
            System.out.println("Goal State Reached:");
            printState(currentState);
            return true;
        }
        if (depth > 0) {
            List<int[][]> nextStates = generateNextStates(currentState);
            for (int[][] nextState : nextStates) {
                if (dfs(nextState, depth - 1)) {
                    return true;
                }
            }
        }
        return false;
    }


    static void bfs(int[][] initialState) {
        Queue<int[][]> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        queue.offer(initialState);
        visited.add(Arrays.deepToString(initialState));

        while (!queue.isEmpty()) {
            int[][] currentState = queue.poll();
            if (Arrays.deepEquals(currentState, goalState)) {
                System.out.println("Goal State Reached:");
                printState(currentState);
                return;
            }

            List<int[][]> nextStates = generateNextStates(currentState);
            for (int[][] nextState : nextStates) {
                if (!visited.contains(Arrays.deepToString(nextState))) {
                    queue.offer(nextState);
                    visited.add(Arrays.deepToString(nextState));
                }
            }
        }

        System.out.println("No solution found.");
    }

    static List<int[][]> generateNextStates(int[][] currentState) {
        List<int[][]> nextStates = new ArrayList<>();
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}}; // Up, Down, Left, Right

        int zeroX = -1, zeroY = -1; // Position of the empty cell (0)
        outerloop:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (currentState[i][j] == 0) {
                    zeroX = i;
                    zeroY = j;
                    break outerloop;
                }
            }
        }

        for (int[] dir : directions) {
            int newX = zeroX + dir[0];
            int newY = zeroY + dir[1];

            if (newX >= 0 && newX < 3 && newY >= 0 && newY < 3) {
                int[][] nextState = new int[3][3];
                for (int i = 0; i < 3; i++) {
                    for (int j = 0; j < 3; j++) {
                        nextState[i][j] = currentState[i][j];
                    }
                }
                // Swap the empty cell with the adjacent cell
                nextState[zeroX][zeroY] = currentState[newX][newY];
                nextState[newX][newY] = 0;
                nextStates.add(nextState);
            }
        }

        return nextStates;
    }

    static void printState(int[][] state) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(state[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
