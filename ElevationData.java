import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ElevationData {

    public static int[][] load( String path, int columns, int rows  ) {
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            System.err.println("File " + path + " not found.");
            System.exit(1);
        }

        Scanner scan = new Scanner(fstream);
        int [][] elev = new int[rows][columns];
        for( int row = 0; row < rows; row++ ) {
            for( int col = 0; col < columns; col++ ) {
                elev[row][col] = scan.nextInt();
            }
        }

        scan.close();
        return elev;

    }
}
