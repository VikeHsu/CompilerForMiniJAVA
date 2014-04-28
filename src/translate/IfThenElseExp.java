package translate;

import tree.*;

/*
 A specification of this class appears in Appel, 2nd, Chapter 7, page 150.
 */

class IfThenElseExp extends LazyIRTree {

    private final LazyIRTree cond, e2, e3;
    final LABEL t = new LABEL("if", "then");
    final LABEL f = new LABEL("if", "else");
    final LABEL join = new LABEL("if", "end");

    IfThenElseExp(LazyIRTree c, LazyIRTree thenClause, LazyIRTree elseClause) {
        assert c != null;
        assert thenClause != null;
        cond = c;
        e2 = thenClause;
        e3 = elseClause;
    }

    public Exp asExp () {
        return null;
    }

    public Stm asStm () {
        final Stm seq;
        if (e3 == null) {
            seq = new SEQ(new CJUMP(CJUMP.EQ, CONST.TRUE, cond.asExp(), t.label, f.label), new SEQ(new LABEL(t.label),
                    new SEQ(e2.asStm(), new LABEL(f.label))));
        } else {
            seq = new SEQ(new CJUMP(CJUMP.EQ, CONST.TRUE, cond.asExp(), t.label, f.label), new SEQ(new LABEL(t.label),
                    new SEQ(e2.asStm(), new SEQ(new JUMP(join.label), new SEQ(
                            new LABEL(f.label), new SEQ(e3.asStm(), new LABEL(
                                    join.label)))))));
        }
        return seq;
    }

    public Stm asCond (LABEL tt, LABEL ff) {
        return null;
    }
}
