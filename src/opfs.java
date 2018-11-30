
public class opfs {
    public static void main(String[]args){
        //SuperBlock sb = new SuperBlock();
        //sb.superblockcheck(args);


        InodesBlock ib = new InodesBlock(args);
        ib.inodeBlockcheck(args);
    }
}