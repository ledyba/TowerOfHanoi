package hanoi.game;

/**
 * <p>�^�C�g��: �n�m�C�̓��@�����񓚃v���O����</p>
 *
 * <p>����: �Q�[�����Ԃɍ���Ȃ������̂ŁB�B�B�B</p>
 *
 * <p>���쌠: Copyright (c) 2007 PSI</p>
 *
 * <p>��Ж�: Pegasus</p>
 *
 * @author ������
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
        //���߂ȏ���
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
