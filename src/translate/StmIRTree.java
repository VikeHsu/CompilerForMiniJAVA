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
    Exp asExp () {
        return null;
    }

    @Override
    Stm asStm () {
        // TODO Auto-generated method stub
        return stm;
    }

    @Override
    Stm asCond (LABEL t, LABEL f) {
        // TODO Auto-generated method stub
        return null;
    }

}
