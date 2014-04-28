package translate;

import tree.*;

/*
 A specification of this class appears in Appel, 2nd, Chapter 7, page 150.
 */

class WhileExp extends LazyIRTree {

    private final LazyIRTree cond, e2;
    final LABEL w = new LABEL("while");
    final LABEL d = new LABEL("do");
    final LABEL join = new LABEL("end");

    WhileExp(LazyIRTree c, LazyIRTree whilebody) {
        assert c != null;
        assert whilebody != null;
        cond = c;
        e2 = whilebody;
    }

    public Exp asExp () {
        return null;
    }

    public Stm asStm () {
        final Stm seq;
        seq = new SEQ(new LABEL(w.label),new SEQ(new CJUMP(CJUMP.EQ, CONST.TRUE, cond.asExp(), d.label, join.label), new SEQ(new LABEL(d.label),
                new SEQ( e2.asStm(),new SEQ( new JUMP(w.label), 
                        new LABEL(join.label))))));
        return seq;
    }

    public Stm asCond (LABEL tt, LABEL ff) {
        return null;
    }
}
