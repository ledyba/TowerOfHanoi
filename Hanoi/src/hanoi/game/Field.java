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
public class Field {
    Tower[] TowerArray;
    private int MaxRings;
    public static final int MAX_TOWER = 3;
    public Field(int max_rings) {
        init(max_rings);
    }

    private void init(int max_rings) {
        MaxRings = max_rings;
        TowerArray = new Tower[MAX_TOWER];
        for (int i = 0; i < MAX_TOWER; i++) {
            TowerArray[i] = new Tower(max_rings);
        }
        for (int i = max_rings; i > 0; i--) {
            TowerArray[0].push(new Ring(i));
        }
    }

    public boolean moveRing(int src, int dst, int size) {
        //だめな条件
        if (size < 1 || size > MaxRings || src < 0 || src >= MAX_TOWER ||
            dst < 0 || dst >= MAX_TOWER) {
            return false;
        }
        Ring ring = TowerArray[src].pop();
        if(ring.getSize() != size){
            TowerArray[src].push(ring);
            return false;
        }
        return TowerArray[dst].push(ring);
    }
    public Tower getTower(int i){
        if(i < 0 || i >= MAX_TOWER){
            return null;
        }
        return (TowerArray[i]);
    }
    public int getMaxRings(){
        return this.MaxRings;
    }
}
