package translate;

import tree.Exp;
import tree.LABEL;
import tree.Stm;

public class ExpIRTree extends LazyIRTree{
    Exp exp;
    
    ExpIRTree(Exp s){
        exp=s;
    }

    @Override
    Exp asExp () {
        return exp;
    }

    @Override
    Stm asStm () {
        return null;
    }

    @Override
    Stm asCond (LABEL t, LABEL f) {
        // TODO Auto-generated method stub
        return null;
    }

}
