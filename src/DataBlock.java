import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static java.lang.Math.pow;

public class DataBlock implements Field{
    int memory[][] = new int [SIZE - DATABLOCKSTART][BSIZE / 4];

    public DataBlock(String[]args){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(args[1]);
            int data;
            int n = 0;
            int egg[] = new int[4];
            while ((data = fis.read()) != -1){
                egg[n%4] = data;
                if(n%4 == 3) {
                    if(DATABLOCKSTART <= n/BSIZE && n/BSIZE < SIZE){
                        int k = (int)(egg[0] + egg[1] * pow(BYTE,1) + egg[2] * pow(BYTE,2) + egg[3] * pow(BYTE,3));
                        memory[n/BSIZE - DATABLOCKSTART][n%BSIZE / 4] = k;
                        //System.out.print(k + ",");
                    }
                }
                if((n+1) % BSIZE == 0){
                    if(DATABLOCKSTART <= n/BSIZE && n/BSIZE < SIZE){
                        //System.out.println(" ");
                    }
                }
                n++;
            }
        } catch (
                FileNotFoundException e) {
            e.printStackTrace();
        } catch (
                IOException e) {
            e.printStackTrace();
        } finally{
            try {
                if (fis != null){
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void printDataBlock(int n){
        System.out.println(n + "ブロック目のブロックは");
        for(int i = 0;i < BSIZE/4;i++){
            System.out.print(memory[n + DATABLOCKSTART][i] + ",");
        }
        System.out.println("");
    }

}
