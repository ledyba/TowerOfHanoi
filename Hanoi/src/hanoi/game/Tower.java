package hanoi.game;

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
public class Tower {
    private Ring[] Stack;
    private int StackIndex = 0;
    private int MaxRings;
    public Tower(int max_rings) {
        MaxRings = max_rings;
        Stack = new Ring[max_rings];
    }

    public Ring pop() {
        if (StackIndex > 0) {
            StackIndex--;
            return Stack[StackIndex];

        }
        return null;
    }

    public boolean push(Ring ring) {
        if (StackIndex < MaxRings) {
            for (int i = 0; i < StackIndex; i++) {
                if (Stack[i].getSize() < ring.getSize()) {
                    return false;
                }
            }
            Stack[StackIndex] = ring;
            StackIndex++;
            return true;
        }
        return false;
    }
    private int ItIndex = 0;
    public void resetIterator(){
        ItIndex = 0;
    }
    public Ring nextRing(){
        if(ItIndex >= StackIndex){
            return null;
        }
        Ring ret = Stack[ItIndex];
        ItIndex++;
        return ret;
    }
    public int getRings(){
        return StackIndex;
    }
}
