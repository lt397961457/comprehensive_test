package java8.stream.parallestream.forkjoin;

import java.util.concurrent.RecursiveTask;

/**
 *  Fork/Join 框架：
 *  在必要的情况下，将一个大任务，进行拆分（Fork）成若干个小任务（拆分到不可再拆时），
 *  在将一个个小任务运算的结果汇总（Join）。
 *  RecursiveTask:递归任务
 */
public class ForkJoinCaculate extends RecursiveTask<Long>{

    private long start;
    private long end;
    private static final long THERSHOLD = 10000;

    public ForkJoinCaculate(long start, long end) {
        this.start = start;
        this.end = end;
    }

    //计算累加
    @Override
    protected Long compute() {
        long lenth = end - start;
        if(lenth <= THERSHOLD){
            long sum = 0;
            for(long i =start;i<=end;i++){
                sum += i;
            }
            return sum;
        }else {
            long middle = (start + end)/2;
            ForkJoinCaculate left =new ForkJoinCaculate(start,middle);
            left.fork();//拆分子任务，同时压入线程队列

            ForkJoinCaculate right = new ForkJoinCaculate(middle+1,end);
            right.fork();

            return left.join() + right.join();
        }
    }
}
