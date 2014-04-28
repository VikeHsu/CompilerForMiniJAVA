package translate;

import tree.*;

public class LessIRTree extends LazyIRTree {

    final LABEL t = new LABEL("if", "then");
    final LABEL f = new LABEL("if", "else");
    final LABEL join = new LABEL("if", "end");

    public final Exp left, right;

    LessIRTree(Exp l, Exp r) {
        left = l;
        right = r;
    }

    @Override
    public
    Exp asExp () {
        final TEMP result = TEMP.generateTEMP();
        final Stm seq;
        seq = new SEQ(new CJUMP(CJUMP.LT, left, right, t.label, f.label), new SEQ(new LABEL(t.label), new SEQ(
                new MOVE(new TEMP(result.temp), CONST.TRUE), // result
                                                             // :=
                                                             // then
                                                             // expr
                new SEQ(new JUMP(join.label), // goto join
                        new SEQ(new LABEL(f.label), // F:
                                new SEQ(new MOVE(new TEMP(result.temp),
                                        CONST.FALSE), // result
                                                     // :=
                                                     // else
                                                     // expr
                                        new LABEL(join.label))))))); // join:
        return new ESEQ(seq, new TEMP(result.temp));
    }

    @Override
    public
    Stm asStm () {

        return null;
    }

    @Override
    public
    Stm asCond (LABEL t, LABEL f) {
        return null;
    }

}
