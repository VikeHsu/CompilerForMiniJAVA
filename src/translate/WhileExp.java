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
        final TEMP result = TEMP.generateTEMP();
        final Stm seq;
        seq = new SEQ(new LABEL(w.label),new SEQ(cond.asCond(d, join), new SEQ(new LABEL(d.label), // T:
                new SEQ(new MOVE(new TEMP(result.temp), e2.asExp()), // result
                                                                     // :=
                                                                     // then
                                                                     // expr
                        new LABEL(join.label))))); // F:
        return new ESEQ(seq, new TEMP(result.temp));
    }

    public Stm asStm () {
        return null;
    }

    public Stm asCond (LABEL tt, LABEL ff) {
        return null;
    }
}
