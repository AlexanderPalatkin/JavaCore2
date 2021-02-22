package lesson05;

public class Main {

    public static void main (String[] args) {
        oneThread();
        twoThread();
    }

    static final int SIZE = 10000000;
    static final int HALF = SIZE / 2;

    public static float[] compute(float[] arr, int offset) {
        for (int i = 0; i < arr.length; i++)
            arr[i] = (float) (arr[i] *
                    Math.sin(0.2f + (i + offset) / 5) *
                    Math.cos(0.2f + (i + offset) / 5) *
                    Math.cos(0.4f + (i + offset) / 2));
        return arr;
    }

    public static void oneThread() {
        float[] arr = new float[SIZE];
        for (int i = 0; i < arr.length; i++) arr[i] = 1.0f;
        long a = System.currentTimeMillis();
        compute(arr, 0);
        System.out.println("One thread time: " + (System.currentTimeMillis() - a));
    }

    public static void twoThread() {

        float[] arr = new float[SIZE];
        float[] arr1 = new float[HALF];
        float[] arr2 = new float[HALF];

        for (int i = 0; i < arr.length; i++) arr[i] = 1.0f;
        long a = System.currentTimeMillis();

        System.arraycopy(arr, 0, arr1, 0, HALF);
        System.arraycopy(arr, HALF, arr2, 0, HALF);

        Thread firstThread = new Thread(() -> {
                float[] a1 = compute(arr1, 0);
                System.arraycopy(a1, 0, arr1, 0, a1.length);
        });

        Thread secondThread = new Thread(() -> {
                float[] a2 = compute(arr2, HALF);
                System.arraycopy(a2, 0, arr2, 0, a2.length);
        });

        firstThread.start();
        secondThread.start();

        try {
            firstThread.join();
            secondThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.arraycopy(arr1, 0, arr, 0, HALF);
        System.arraycopy(arr2, 0, arr, HALF, HALF);

        System.out.println("Two thread time: " + (System.currentTimeMillis() - a));
    }
}
