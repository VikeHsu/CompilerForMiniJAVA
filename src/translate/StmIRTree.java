package translate;

import tree.Exp;
import tree.LABEL;
import tree.Stm;

public class StmIRTree extends LazyIRTree{
    Stm stm;
    
    StmIRTree(Stm s){
        stm=s;
    }
    @Override
    public
    Exp asExp () {
        return null;
    }

    @Override
    public
    Stm asStm () {
        // TODO Auto-generated method stub
        return stm;
    }

    @Override
    public
    Stm asCond (LABEL t, LABEL f) {
        // TODO Auto-generated method stub
        return null;
    }

}
