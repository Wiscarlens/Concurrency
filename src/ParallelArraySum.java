class ParallelArraySum implements Runnable {
    private int start;
    private int end;
    private int[] arr;
    private long partialSum;

    public ParallelArraySum(int start, int end, int[] arr) {
        this.start = start;
        this.end = end;
        this.arr = arr;
    }

    public ParallelArraySum() {

    }

    @Override
    public void run() {
        for (int i = start; i < end; i++) {
            partialSum += arr[i];
        }
    }

    public long getPartialSum() {
        return partialSum;
    }
}