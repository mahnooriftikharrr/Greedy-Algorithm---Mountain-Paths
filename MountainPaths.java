
import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class MountainPaths {

    private static final long RAND_SEED = 1234L;
    private static Random randGen = new Random( RAND_SEED );
    
    public static void main(String[] args) throws Exception{
        final int WIDTH = 840;
        final int HEIGHT = 480;
        final String INPUT_FILE = "Colorado_840x480.dat";
     
        DrawingCanvas panel = new DrawingCanvas(WIDTH, HEIGHT);
        Graphics g = panel.getGraphics();

        int[][] grid = ElevationData.load(INPUT_FILE, WIDTH, HEIGHT);
       
        int row;
        int col;
        int min = grid[0][0];
        for (row = 0; row < HEIGHT; ++row){
            for (col = 0; col < WIDTH; ++col){
                if (grid[row][col] < min){
                    min = grid[row][col];
                }
            }
        }
        System.out.println("Min value in map: " + min);

        // max
        int max = grid[0][0];
        for (row = 0; row < HEIGHT; ++row){
            for (col = 0; col < WIDTH; ++col){
                if (grid[row][col] > max){
                    max = grid[row][col];
                }
            }
        }
        System.out.println("Max value in map: " + max);
        
        double r;
        for (row = 0; row < HEIGHT; ++row){
            for (col = 0; col < WIDTH; ++col){
                r = (grid[row][col] - min) / ((double)max - min);
                int c = (int)Math.round(r * 255);
                g.setColor(new Color(c, c, c));
                g.fillRect(col, row, 1, 1);
            }
        }
        
        int minElev = grid[0][0];
        int minIndex = 0;
        for (row = 0; row < HEIGHT; ++row){
            if (grid[row][0] < minElev){
                minElev = grid[row][0];
                minIndex = row;
            }
        }
        System.out.println("Row with lowest val in col 0: " + minIndex);

        int curLoca = minIndex;
        int minGrid;
        int forward, forwardUp, forwardDown;
        int forwardDif, forwardDownDif, forwardUpDif;
        int minDif;
        int ElevChange = 0;
        for (col = 0; col < WIDTH - 1; ++col){
            forward = grid[curLoca][col + 1];
            if (curLoca - 1 < 0){
                forwardUp = Integer.MAX_VALUE;
            }
            else {
            forwardUp = grid[curLoca - 1][col + 1];
            }
            if (curLoca + 1 > HEIGHT){
                forwardDown = Integer.MAX_VALUE;
            }
            else {
            forwardDown = grid[curLoca + 1][col + 1];
            }
            forwardDif = Math.abs(grid[curLoca][col] - forward);
            forwardUpDif = Math.abs(grid[curLoca][col] - forwardUp);
            forwardDownDif = Math.abs(grid[curLoca][col] - forwardDown);
            minDif = Math.min(forwardDif, Math.min(forwardUpDif, forwardDownDif));
            
            if (minDif == forwardDif){
                curLoca = curLoca;
                ElevChange += forwardDif;
            }
            else if ((minDif == forwardUpDif) && (minDif == forwardDownDif)){
                int coin = randGen.nextInt(2);
                if (coin == 0){
                    curLoca = curLoca - 1;
                    ElevChange += forwardUpDif;
                }
                else {
                    curLoca = curLoca + 1;
                    ElevChange += forwardDownDif;
                }
            }
            else if (minDif == forwardUpDif){
                curLoca = curLoca - 1;
                ElevChange += forwardUpDif;
            }
            else {
                curLoca = curLoca + 1;
                ElevChange += forwardDownDif;
            }
            g.setColor(new Color(255, 0, 0));
            g.fillRect(col, curLoca, 1, 1);
        }
        System.out.println(ElevChange);
    }
}
