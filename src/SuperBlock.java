import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import static java.lang.Math.pow;

public class SuperBlock implements Field{
    int size;
    int nblocks;
    int ninodes;
    int nlog;
    int logstart;
    int inodestart;
    int bmapstart;

    public void setsuperblockinstance(String[] args){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(args[1]);
            int data;
            int n = 0;
            int egg[] = new int[4];
            while ((data = fis.read()) != -1){
                egg[n%4] = data;
                if(n%4 == 3) {
                    if(SUPERBLOCKSTART == n/BSIZE){
                        int k = (int)(egg[0] + egg[1] * pow(BYTE,1) + egg[2] * pow(BYTE,2) + egg[3] * pow(BYTE,3));
                        if(n%BSIZE == 3){
                            size = k;
                        }else if(n%BSIZE == 7){
                            nblocks = k;
                        }else if(n%BSIZE == 11){
                            ninodes = k;
                        }else if(n%BSIZE == 15){
                            nlog = k;
                        }else if(n%BSIZE == 19){
                            logstart = k;
                        }else if(n%BSIZE == 23){
                            inodestart = k;
                        }else if(n%BSIZE == 27){
                            bmapstart = k;
                        }
                        System.out.print(k + ",");
                    }
                }
                if((n+1) % BSIZE == 0){
                    if(SUPERBLOCKSTART == n/512){
                        System.out.println(" ");
                    }
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

    public void showbitstream(int start,int stop,String []args){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(args[1]);
            int data;
            int n = 0;
            while ((data = fis.read()) != -1){
                if(start <= n/BSIZE &&  n/BSIZE <= stop){
                    System.out.print(data + ",");
                }
                if((n+1) % BSIZE == 0){
                    if(start <= n/512 && n/512 <= stop){
                        System.out.println("");
                        System.out.println("");
                    }
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



    public void printissuperblock(){
        System.out.println("size = " + size);
        System.out.println("nblocks = " + nblocks);
        System.out.println("ninodes = " + ninodes);
        System.out.println("nlog = " + nlog);
        System.out.println("logstart = " + logstart);
        System.out.println("inodestart = " + inodestart);
        System.out.println("bmapstart = " + bmapstart);
        System.out.println(" ");
        int flag = 0;
        if(size != 1000){
            System.out.println("sizeが間違っている");
            flag = 1;
        }
        if(nblocks != 941){
            System.out.println("nblocksが間違っている");
            flag = 1;
        }
        if(ninodes != 200){
            System.out.println("ninodesが間違っている");
            flag = 1;
        }
        if(nlog != 30){
            System.out.println("nlogが間違っている");
            flag = 1;
        }
        if(logstart != 2){
            System.out.println("logstartが間違っている");
            flag = 1;
        }
        if(inodestart != 32){
            System.out.println("inodestartが間違っている");
            flag = 1;
        }
        if(bmapstart != 58){
            System.out.println("bmapstartが間違っている");
            flag = 1;
        }

        if(flag == 0){
            System.out.println("全て正しい");
        }
    }

    public void superblockcheck(String[]args){
        setsuperblockinstance(args);
        printissuperblock();
    }
}

