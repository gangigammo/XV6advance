import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLOutput;

import static java.lang.Math.pow;

public class InodesBlock implements Field{
    private int addrs[][][] = new int[BITMAPBLOCKSTART - INODESBLOCKSTART][8][13];

    private int major[][] = new int[BITMAPBLOCKSTART - INODESBLOCKSTART][8];
    private int type[][] = new int[BITMAPBLOCKSTART - INODESBLOCKSTART][8];
    private int nlink[][] = new int[BITMAPBLOCKSTART - INODESBLOCKSTART][8];
    private int minor[][] = new int[BITMAPBLOCKSTART - INODESBLOCKSTART][8];
    private int size[][] = new int[BITMAPBLOCKSTART - INODESBLOCKSTART][8];
    private int countnlink[][] = new int[BITMAPBLOCKSTART - INODESBLOCKSTART][8];

    public InodesBlock(String[] args){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(args[1]);
            int data;
            int n = 0;
            int egg[] = new int[4];
            while ((data = fis.read()) != -1){
                egg[n%4] = data;
                if(n%4 == 3) {
                    if(INODESBLOCKSTART <= n/BSIZE && n/BSIZE < BITMAPBLOCKSTART){
                        int k = (int)(egg[0] + egg[1] * pow(BYTE,1) + egg[2] * pow(BYTE,2) + egg[3] * pow(BYTE,3));
                        for(int i=0;i<16;i++) {
                            for(int j =0;j<8;j++){
                                if(n%BSIZE == 4*i+3 + j * 64){
                                    if(3 <= i && i < 16){
                                        addrs[n/BSIZE - INODESBLOCKSTART][j][i-3] = k;
                                    }else if(i == 0){
                                        major[n/BSIZE - INODESBLOCKSTART][j] = (int)(egg[2] + egg[3] * pow(BYTE,1));
                                        type[n/BSIZE - INODESBLOCKSTART][j] = (int)(egg[0] + egg[1] * pow(BYTE,1));
                                    }else if(i == 1){
                                        nlink[n/BSIZE - INODESBLOCKSTART][j] = (int)(egg[2] + egg[3] * pow(BYTE,1));
                                        minor[n/BSIZE - INODESBLOCKSTART][j] = (int)(egg[0] + egg[1] * pow(BYTE,1));
                                    }
                                    else if(i == 2){
                                        size[n/BSIZE - INODESBLOCKSTART][j] = k;
                                    }
                                }
                            }
                        }
                        //System.out.print(k + ",");
                    }
                }
                if((n+1) % BSIZE == 0 && INODESBLOCKSTART <= n/BSIZE && n/BSIZE < BITMAPBLOCKSTART){
                    //System.out.println(" ");
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

    public void printinodesblock(){
        System.out.println("addrs = ");
        for(int i = 0;i < BITMAPBLOCKSTART-INODESBLOCKSTART;i++){
            for(int k = 0;k < 8;k++){
                for(int j = 0;j < 13;j++){
                    System.out.print(addrs[i][k][j] + ",");
                }
                System.out.println(" ");
            }
            System.out.println(" ");
        }
        System.out.println(" ");
    }

    public void inodeBlockcheck(String [] args){
        BitmapBlock bb = new BitmapBlock(args);
        DataBlock db = new DataBlock(args);
        bb.printBitmapBlock();
        int count;

        for(int i = 0;i < BITMAPBLOCKSTART-INODESBLOCKSTART;i++){
            for(int k = 0;k < 8;k++){
                count = 0;
                for(int j = 0;j < 13;j++){
                    if(addrs[i][k][j] != 0){
                        if(j == 12){
                            for(int l = 0;l < BSIZE/4;l++){
                                if(db.memory[addrs[i][k][j] - DATABLOCKSTART][l] != 0){
                                    bb.eachbit[db.memory[addrs[i][k][j]- DATABLOCKSTART][l]]--;
                                    for(int s = 0;s < 32;s++){
                                        int x = db.memory[addrs[i][k][j] - DATABLOCKSTART][s * 16 / 4] / (int)pow(256,2);
                                        int x1 = x / 256;
                                        //System.out.println(x1);
                                        countnlink[x1 / 8][x1 % 8]++;
                                    }
                                    count++;
                                }else{
                                    break;
                                }
                            }
                            bb.eachbit[addrs[i][k][j]]--;
                        }else{
                            bb.eachbit[addrs[i][k][j]]--;
                            for(int s = 0;s < 32;s++){
                                int x = db.memory[addrs[i][k][j]][s * 16 / 4] / (int)pow(256,2);
                                int x1 = x / 256;
                                //System.out.println(x1);
                                //countnlink[x1 / 8][x1 % 8]++;
                            }
                            count++;
                        }
                    }
                }
                /*
                if(size[i][k] % BSIZE != 0){
                    System.out.println(count + " " + (size[i][k]/BSIZE + 1));
                }else{
                    System.out.println(count + " " + size[i][k]/BSIZE);
                }
                */



                //System.out.println(major[i][k] + " " + type[i][k] + " " + nlink[i][k] + " " + minor[i][k] + " ");
            }
        }

        for(int i = 0;i < BITMAPBLOCKSTART-INODESBLOCKSTART;i++){
            for(int k = 0;k < 8;k++){
                System.out.println(nlink[i][k] + " " + countnlink[i][k]);
            }
        }


        int flag = 0;
        for(int i = 0;i < DATABLOCKSTART;i++){
            if(bb.eachbit[i] != 1){
                System.out.println(i + "番目のビットマップブロックが不正です bb.eachbit[i]=" + bb.eachbit[i]);
                flag = 1;
            }
        }
        for(int i = DATABLOCKSTART;i < BSIZE*8;i++){
            if(bb.eachbit[i] != 0){
                System.out.println(i + "番目のビットマップブロックが不正です bb.eachbit[i]=" + bb.eachbit[i]);
                flag = 1;
            }
        }



        if(flag == 0){
            System.out.println("データブロックは正しい");
        }
        bb.printBitmapBlock();
    }


}
