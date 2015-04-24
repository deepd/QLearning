/**
 * Created by deep on 3/7/15.
 */

import java.util.*;
import java.text.DecimalFormat;

public class QLearning {
    public final DecimalFormat df = new DecimalFormat("#.##");
    public double gamma;
    public int[][] R;
    public double[][] Q;
    public int ROWS, COLS, goalState;
    public final int UP = 0, DOWN = 1, LEFT = 2, RIGHT = 3;
    public ArrayList<Double> SumQValues;
    public ArrayList<Double> QDiff;
    public int iterations;

    public QLearning() {
        R = new int[13][4];
        goalState = 7;
        Q = new double[13][4];
        gamma = 0.9;
        ROWS = 3;
        COLS = 4;
        R[6][RIGHT] = 100;
        R[3][DOWN] = 100;
        R[8][LEFT] = 100;
        R[11][UP] = 100;
        SumQValues = new ArrayList<Double>();
        QDiff = new ArrayList<Double>();
        iterations = 0;
    }

    //Main Function to do qlearning
    public void run() {
        Random rand = new Random();
        //10000 epochs
        int count = 0;
        for(int l = 0; l < 10000; l++) {
            double sum = 0;
            for(double[] arr : Q) {
                for(double x : arr) {
                    sum += x;
                }
            }
            int i = rand.nextInt(1000) % ROWS;
            int j = rand.nextInt(1000) % COLS;
            int state = i*COLS + j + 1;
            double qchange = 0;
            while(state != goalState) {
                int ii = i, jj = j;
                int direction, nextState = state;
                while(true) {
                    Random r = new Random();
                    direction = r.nextInt(1000) % 4;
                    if(isValid(i, j, direction)) {
                        break;
                    }
                }
                if(direction == LEFT)
                    jj = jj - 1;
                if(direction == RIGHT)
                    jj = jj + 1;
                if(direction == UP)
                    ii = ii - 1;
                if(direction == DOWN)
                    ii = ii + 1;
                nextState = ii*COLS + jj + 1;
                double q = getQ(state, direction);
                double maxQ = maxQ(ii, jj);
                int r = getR(state, direction);
                double value = r + gamma * maxQ;
                qchange += Math.abs(value - q);
                if(value > q) {
                    setQ(state, direction, value);
                }
                i = ii;
                j = jj;
                state = nextState;
            }
            SumQValues.add(qchange);
            double newsum = 0;
            for(double[] arr : Q) {
                for(double x : arr) {
                    newsum += x;
                }
            }
            if(sum == newsum)
                count++;
            else
                count = 0;
            iterations++;
            if(count >= 50)
                break;
        }
        for(int i = 1; i < 13; i++) {
            for(int j = 0; j < 4; j++)
                if(i != goalState && Q[i][j] == 0) Q[i][j] = -99;
        }
    }

    //return R value
    public int getR(int state, int direction) {
        return R[state][direction];
    }

    //return true if valid direction
    public boolean isValid(int i, int j, int dir) {
        int ni = i, nj = j;
        if(dir == LEFT)
            nj = nj - 1;
        if(dir == RIGHT)
            nj = nj + 1;
        if(dir == UP)
            ni = ni - 1;
        if(dir == DOWN)
            ni = ni + 1;
        //System.out.println("ni : " + ni + " nj : " + nj);
        if (ni >= 0 && ni < ROWS && nj >= 0 && nj < COLS) {
            //System.out.println("true value returned");
            return true;
        }
        return false;
    }

    //Set Q value;
    public void setQ(int state, int dir, double val) {
        Q[state][dir] = val;
    }

    //Get Q value;
    public double getQ(int state, int dir) {
        return Q[state][dir];
    }

    //Return maxQ
    public double maxQ(int i, int j) {
        double maxValue = Double.MIN_VALUE;
        int state = i*COLS + j + 1;
        if(j - 1 >= 0) {
            double value = getQ(state, LEFT);
            if(value > maxValue)
                maxValue = value;
        }
        if(j + 1 < COLS) {
            double value = getQ(state, RIGHT);
            if(value > maxValue)
                maxValue = value;
        }
        if(i - 1 >= 0) {
            double value = getQ(state, UP);
            if(value > maxValue)
                maxValue = value;
        }
        if(i + 1 < ROWS) {
            double value = getQ(state, DOWN);
            if(value > maxValue)
                maxValue = value;
        }
        return maxValue;
    }

    //print results
    public void printResult() {
        System.out.println("Print result");
        for (int i = 1; i < Q.length; i++) {
            //System.out.print("out from state "  + ":  ");
            for (int j = 0; j < Q[i].length; j++) {
                System.out.print(Q[i][j]);
                System.out.print("\t\t");
            }
            System.out.println();
        }
        for(int i = 0; i < SumQValues.size(); i++) {
            System.out.print(df.format(SumQValues.get(i)) + ";");
        }
        System.out.println();
        System.out.println("Number of Iterations : " + iterations);
    }

    public static void main(String[] args) {
        long BEGIN = System.currentTimeMillis();
        QLearning obj = new QLearning();
        obj.run();
        obj.printResult();
        long END = System.currentTimeMillis();
        System.out.println("Time: " + (END - BEGIN) / 1000.0 + " sec.");
    }
}
