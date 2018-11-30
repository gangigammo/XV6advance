import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static java.lang.Math.pow;

public class BitmapBlock implements Field {
    int eachbit[] = new int[BSIZE * 8];

    public BitmapBlock(String []args){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(args[1]);
            int data;
            int n = 0;
            while ((data = fis.read()) != -1){
                if(BITMAPBLOCKSTART == n/BSIZE){
                    for(int i = 0;i<8;i++){
                        eachbit[n%BSIZE*8 + i] = (data & (int)pow(2,i)) >> i;
                    }
                }
                if((n+1) % BSIZE == 0 && BITMAPBLOCKSTART == n/BSIZE){
                    System.out.println(" ");
                }
                n++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
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

    public void printBitmapBlock(){
        System.out.println("bitmapblock=");
        for(int i = 0;i < BSIZE*8;i++){
            System.out.print(eachbit[i] + ",");
        }
        System.out.println("");
    }
}
