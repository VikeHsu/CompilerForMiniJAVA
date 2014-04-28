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
    public
    Exp asExp () {
        return exp;
    }

    @Override
    public
    Stm asStm () {
        return null;
    }

    @Override
    public
    Stm asCond (LABEL t, LABEL f) {
        // TODO Auto-generated method stub
        return null;
    }

}
