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
        final TEMP result = TEMP.generateTEMP();
        final Stm seq;
        if (e3 == null) {
            seq = new SEQ(cond.asCond(t, f), new SEQ(new LABEL(t.label), // T:
                    new SEQ(new MOVE(new TEMP(result.temp), e2.asExp()), // result
                                                                         // :=
                                                                         // then
                                                                         // expr
                            new LABEL(f.label)))); // F:
        } else {
            seq = new SEQ(cond.asCond(t, f), new SEQ(new LABEL(t.label),
                    new SEQ(new MOVE(new TEMP(result.temp), e2.asExp()), // result
                                                                         // :=
                                                                         // then
                                                                         // expr
                            new SEQ(new JUMP(join.label), // goto join
                                    new SEQ(new LABEL(f.label), // F:
                                            new SEQ(new MOVE(new TEMP(
                                                    result.temp), e3.asExp()), // result
                                                                               // :=
                                                                               // else
                                                                               // expr
                                                    new LABEL(join.label))))))); // join:
        }
        return new ESEQ(seq, new TEMP(result.temp));
    }

    public Stm asStm () {
        return null;
    }

    public Stm asCond (LABEL tt, LABEL ff) {
        return null;
    }
}
