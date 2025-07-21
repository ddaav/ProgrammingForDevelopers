public class TechStartupRevenue {

    /**
     * Sorts the array of projects in ascending order of capital required using Bubble Sort.
     * Each project is represented as a pair: [capitalRequired, revenueGenerated]
     *
     * @param projects A 2D array where each element is [capital, revenue]
     */
    public static void sortByCapital(int[][] projects) {
        int n = projects.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (projects[j][0] > projects[j + 1][0]) {
                    // Swap the two project entries based on capital
                    int[] temp = projects[j];
                    projects[j] = projects[j + 1];
                    projects[j + 1] = temp;
                }
            }
        }
    }

    /**
     * Calculates the maximum capital that can be earned by completing at most 'k' projects.
     *
     * Approach:
     * 1. Sort all projects by their required capital.
     * 2. Use a manual Max-Heap to always select the project with the highest profit
     *    that can be started with current available capital.
     * 3. Update capital after each project.
     * 4. Repeat up to 'k' times or until no further projects are possible.
     *
     * @param k           Maximum number of projects that can be completed
     * @param c           Initial capital
     * @param revenues    Profit from each project
     * @param investments Capital required for each project
     * @return Final capital after doing the best 'k' projects
     */
    public static int maximizeCapital(int k, int c, int[] revenues, int[] investments) {
        int n = revenues.length;

        // Step 1: Combine investments and profits into a single 2D array
        int[][] projects = new int[n][2];
        for (int i = 0; i < n; i++) {
            projects[i][0] = investments[i]; // required capital
            projects[i][1] = revenues[i];    // expected profit
        }

        // Step 2: Sort projects by required capital
        sortByCapital(projects);

        // Step 3: Manual max heap to store eligible project profits
        int[] maxHeap = new int[n];
        int heapSize = 0;
        int index = 0; // Points to the current project in sorted array

        // Step 4: Attempt to complete at most k projects
        for (int i = 0; i < k; i++) {

            // Move all affordable projects into the max heap
            while (index < n && projects[index][0] <= c) {
                insertToMaxHeap(maxHeap, heapSize, projects[index][1]);
                heapSize++;
                index++;
            }

            // If no eligible projects, break early
            if (heapSize == 0) {
                break;
            }

            // Pick the most profitable project from the max heap
            c += extractMax(maxHeap, heapSize);
            heapSize--; // After extraction, decrease heap size
        }

        return c; // Final capital after doing up to k best projects
    }

    /**
     * Inserts a value into a manual max-heap represented by an array.
     * Heap property is maintained by percolating up.
     *
     * @param heap  The heap array
     * @param size  Current number of elements in heap
     * @param value The new value to insert
     */
    public static void insertToMaxHeap(int[] heap, int size, int value) {
        heap[size] = value;
        int current = size;

        // Percolate up to maintain max-heap property
        while (current > 0) {
            int parent = (current - 1) / 2;
            if (heap[parent] < heap[current]) {
                // Swap parent and current
                int temp = heap[parent];
                heap[parent] = heap[current];
                heap[current] = temp;
                current = parent;
            } else {
                break;
            }
        }
    }

    /**
     * Extracts and returns the maximum value from the max-heap.
     * After extraction, heap structure is maintained by heapifying down.
     *
     * @param heap The heap array
     * @param size The current number of elements in heap
     * @return The maximum value (root of the heap)
     */
    public static int extractMax(int[] heap, int size) {
        int max = heap[0];
        heap[0] = heap[size - 1]; // Replace root with last element
        int current = 0;

        // Percolate down to restore heap property
        while (true) {
            int left = 2 * current + 1;
            int right = 2 * current + 2;
            int largest = current;

            if (left < size - 1 && heap[left] > heap[largest]) {
                largest = left;
            }
            if (right < size - 1 && heap[right] > heap[largest]) {
                largest = right;
            }

            if (largest != current) {
                int temp = heap[current];
                heap[current] = heap[largest];
                heap[largest] = temp;
                current = largest;
            } else {
                break;
            }
        }

        return max;
    }

    /**
     * Driver method to test the implementation with two examples.
     */
    public static void main(String[] args) {
        // Test Case 1
        int k = 2;
        int c = 0;
        int[] revenues = {2, 5, 8};
        int[] investments = {0, 2, 3};
        System.out.println("Final Capital (Test 1): " + maximizeCapital(k, c, revenues, investments)); // Expected: 7

        // Test Case 2
        int k2 = 3;
        int c2 = 1;
        int[] rev2 = {3, 6, 10};
        int[] inv2 = {1, 3, 5};
        System.out.println("Final Capital (Test 2): " + maximizeCapital(k2, c2, rev2, inv2)); // Expected: 20
    }
}
