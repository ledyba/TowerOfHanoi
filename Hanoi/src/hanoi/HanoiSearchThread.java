package hanoi;

import hanoi.game.Field;

/**
 * <p>タイトル: ハノイの塔　自動回答プログラム</p>
 *
 * <p>説明: ゲームが間に合わなかったので。。。。</p>
 *
 * <p>著作権: Copyright (c) 2007 PSI</p>
 *
 * <p>会社名: Pegasus</p>
 *
 * @author 未入力
 * @version 1.0
 */
public class HanoiSearchThread extends Thread {
    private final int MaxRings;
    HanoiPanel Hanoi;
    public HanoiSearchThread(Field field,HanoiPanel hanoi){
        Hanoi = hanoi;
        MaxRings = field.getMaxRings();
    }
    private boolean finished = false;
    public void run(){
        search(MaxRings,0,1,2);
        finished = true;
    }
    public boolean hasFinished(){
        return finished;
    }
    /**
     * AからCに移動する。
     * @param n int
     * @param a int
     * @param b int
     * @param c int
     */
    private void search(int n,int a,int b,int c){
        if(n > 0){
            //aの、移動したい上の部分をbに移動しておく。
            search(n - 1, a, c, b);
            //move a to c
            Hanoi.moveRing(a,c,n);
            //bをCへ移動する。
            search(n - 1, b, a, c);
        }
    }
}
