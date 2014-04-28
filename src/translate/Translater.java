package translate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import syntax.*;
import tree.*;
import visitor.SymbolTable;

public class Translater implements SyntaxTreeVisitor<LazyIRTree> {

    private static final String preludeEnd="$preludeEnd";
    private static final String epilogBegin="$epilogBegin";
    public SymbolTable table;
    public ArrayList<Stm> fragments = new ArrayList<Stm>();
    public static final int WORD_LENGTH = 4;
    LinkedHashMap<String, String> funcName;

    public Translater(SymbolTable t) {
        this.table = t;
        funcName = t.getfuncName();
    }

    @Override
    public LazyIRTree visit (Program n) {
        n.m.accept(this);
        for (ClassDecl c : n.cl)
            c.accept(this);
        return null;
    }

    @Override
    public LazyIRTree visit (MainClass n) {
        table.setField(1, n.i1.toString());
        table.setField(2, "main");
        
        String name=table.currentClass+"$"+table.currentMethod;
        Stm s = n.s.accept(this).asStm();
        Stm f = new SEQ(new LABEL(name+preludeEnd),new SEQ(s,new JUMP(name+epilogBegin)));
        fragments.add(f);
        return null;
    }

    @Override
    public LazyIRTree visit (SimpleClassDecl n) {
        table.setField(1, n.i.toString());
        for (MethodDecl m : n.ml) {
            table.setField(2, m.i.toString());
            String name=table.currentClass+"$"+table.currentMethod;
            Stm s = m.accept(this).asStm();
            Stm f = new SEQ(new LABEL(name+preludeEnd),new SEQ(s,new JUMP(name+epilogBegin)));
            fragments.add(f);
        }
        return null;
    }

    @Override
    public LazyIRTree visit (ExtendingClassDecl n) {
        table.setField(1, n.i.toString());
        for (MethodDecl m : n.ml) {
            table.setField(2, m.i.toString());
            String name=table.currentClass+"$"+table.currentMethod;
            Stm s = m.accept(this).asStm();
            Stm f = new SEQ(new LABEL(name+preludeEnd),new SEQ(s,new JUMP(name+epilogBegin)));
            fragments.add(f);
        }
        return null;
    }

    @Override
    public LazyIRTree visit (VarDecl n) {
        return null;
    }

    public static SEQ fromList (final Stm... args) {
        final int l = args.length;
        SEQ s = new SEQ(args[l - 2], args[l - 1]);
        for (int i = l - 3; i >= 0; i--) {
            s = new SEQ(args[i], s);
        }
        return s;
    }

    @Override
    public LazyIRTree visit (MethodDecl n) {
        Stm ret = new MOVE(new TEMP("%i0"), n.e.accept(this).asExp());
        int size = n.sl.size();
        Stm[] ss = new Stm[size];
        int i = 0;
        for (Statement s : n.sl) {
            ss[i] = s.accept(this).asStm();
        }
        if (size == 1) {
            return new StmIRTree(new SEQ(ss[0], ret));
        } else
            return new StmIRTree(new SEQ(SEQ.fromList(ss), ret));
    }

    @Override
    public LazyIRTree visit (Formal n) {
        return null;
    }

    @Override
    public LazyIRTree visit (IntArrayType n) {
        return null;
    }

    @Override
    public LazyIRTree visit (BooleanType n) {
        return null;
    }

    @Override
    public LazyIRTree visit (IntegerType n) {
        return null;
    }

    @Override
    public LazyIRTree visit (IdentifierType n) {
        return null;
    }

    @Override
    public LazyIRTree visit (Block n) {
        int size = n.sl.size();
        Stm[] ss = new Stm[size];
        int i = 0;
        for (Statement s : n.sl) {
            ss[i] = s.accept(this).asStm();
        }
        if (size == 1) {
            return new StmIRTree(ss[0]);
        } else
            return new StmIRTree(SEQ.fromList(ss));
    }

    public static SEQ fromList1 (final Stm... args) {
        final int l = args.length;
        SEQ s = new SEQ(args[l - 2], args[l - 1]);
        for (int i = l - 3; i >= 0; i--) {
            s = new SEQ(args[i], s);
        }
        return s;
    }

    @Override
    public LazyIRTree visit (If n) {
        return new IfThenElseExp(n.e.accept(this), n.s1.accept(this),
                n.s2.accept(this));
    }

    @Override
    public LazyIRTree visit (While n) {
        return new WhileExp(n.e.accept(this), n.s.accept(this));
    }

    @Override
    public LazyIRTree visit (Print n) {
        CALL c = new CALL(new NameOfLabel("_print"), n.e.accept(this).asExp());
        EVAL e = new EVAL(c);
        return new StmIRTree(e);
    }

    @Override
    public LazyIRTree visit (Assign n) {
        Exp e1 = n.i.accept(this).asExp();
        Exp e2 = n.e.accept(this).asExp();

        MOVE m = new MOVE(e1, e2);
        return new StmIRTree(m);
    }

    @Override
    public LazyIRTree visit (ArrayAssign n) {
        Exp i = n.i.accept(this).asExp();
        Exp e1 = n.e1.accept(this).asExp();
        Exp e2 = n.e2.accept(this).asExp();
        Exp index = new BINOP(BINOP.PLUS, new BINOP(BINOP.MUL, new CONST(
                WORD_LENGTH), e1), new CONST(WORD_LENGTH));
        Exp e = new MEM(new BINOP(BINOP.PLUS, i, index));

        return new StmIRTree(new MOVE(e, e2));
    }

    @Override
    public LazyIRTree visit (And n) {
        Exp e1 = n.e1.accept(this).asExp();
        Exp e2 = n.e1.accept(this).asExp();
        LABEL lt = new LABEL("lTrue");
        LABEL rt = new LABEL("rTrue");
        LABEL f = new LABEL("false");
        LABEL join = new LABEL("end");
        TEMP t = TEMP.generateTEMP();

        ESEQ e = new ESEQ(new SEQ(new CJUMP(CJUMP.EQ, CONST.TRUE, e1, lt.label,
                f.label), new SEQ(new LABEL(lt.label), new SEQ(new CJUMP(
                CJUMP.EQ, CONST.TRUE, e2, rt.label, f.label), new SEQ(
                new LABEL(rt.label), new SEQ(new MOVE(t, CONST.TRUE), new SEQ(
                        new JUMP(join.label), new SEQ(new LABEL(f.label),
                                new MOVE(t, CONST.FALSE)))))))), t);
        return new ExpIRTree(e);
    }

    @Override
    public LazyIRTree visit (LessThan n) {
        return new LessIRTree(n.e1.accept(this).asExp(), n.e2.accept(this)
                .asExp());
    }

    @Override
    public LazyIRTree visit (Plus n) {
        return new ExpIRTree(new BINOP(BINOP.PLUS, n.e1.accept(this).asExp(),
                n.e2.accept(this).asExp()));
    }

    @Override
    public LazyIRTree visit (Minus n) {
        return new ExpIRTree(new BINOP(BINOP.MINUS, n.e1.accept(this).asExp(),
                n.e2.accept(this).asExp()));
    }

    @Override
    public LazyIRTree visit (Times n) {
        return new ExpIRTree(new BINOP(BINOP.MUL, n.e1.accept(this).asExp(),
                n.e2.accept(this).asExp()));
    }

    @Override
    public LazyIRTree visit (ArrayLookup n) {
        Exp i = n.e1.accept(this).asExp();
        Exp e1 = n.e2.accept(this).asExp();
        Exp index = new BINOP(BINOP.PLUS, new BINOP(BINOP.MUL, new CONST(
                WORD_LENGTH), e1), new CONST(WORD_LENGTH));
        Exp e = new MEM(new BINOP(BINOP.PLUS, i, index));

        return new ExpIRTree(e);
    }

    @Override
    public LazyIRTree visit (ArrayLength n) {
        Exp i = n.e.accept(this).asExp();
        Exp e = new MEM(new BINOP(BINOP.PLUS, i, new CONST(0)));

        return new ExpIRTree(e);
    }

    @Override
    public LazyIRTree visit (Call n) {
        String methodName = n.i.toString();
        String className = null;
        if (n.e instanceof IdentifierExp) {
            IdentifierExp o = (IdentifierExp) n.e;
            className = table.getType(o.toString());
        } else if (n.e instanceof NewObject) {
            NewObject o = (NewObject) n.e;
            className = o.i.toString();
        } else if (n.e instanceof This) {
            className = table.currentClass;
        }

        Exp instance = n.e.accept(this).asExp();
        List<Exp> expList = new ArrayList<Exp>();
        expList.add(instance);
        for (Expression e : n.el) {
            expList.add(e.accept(this).asExp());
        }
        String nameKey = className + "$" + methodName;

        Exp e = new CALL(new NAME(funcName.get(nameKey)), fromList(expList));
        // TODO Auto-generated method stub
        return new ExpIRTree(e);
    }

    public ExpList fromList (List<Exp> l) {
        final Exp[] el = l.toArray(new Exp[] {});
        ExpList tail = null;
        for (int i = el.length - 1; i >= 0; i--) {
            tail = new ExpList(el[i], tail);
        }
        return tail;
    }

    @Override
    public LazyIRTree visit (IntegerLiteral n) {
        CONST c = new CONST(n.i);
        return new ExpIRTree(c);
    }

    @Override
    public LazyIRTree visit (True n) {
        return new ExpIRTree(CONST.TRUE);
    }

    @Override
    public LazyIRTree visit (False n) {
        return new ExpIRTree(CONST.FALSE);
    }

    @Override
    public LazyIRTree visit (IdentifierExp n) {

        int index;
        int type = table.getVarLocation(table.currentClass,
                table.currentMethod, n.toString());
        if (type == 0) {
            index = table.getLocalIndex(table.currentClass,
                    table.currentMethod, n.toString());
            return new ExpIRTree(new MEM(new BINOP(BINOP.MINUS,
                    new TEMP("%fp"), new CONST(WORD_LENGTH
                            + (index * WORD_LENGTH)))));
        } else if (type == 1) {
            index = table.getArgIndex(table.currentClass, table.currentMethod,
                    n.toString());
            return new ExpIRTree(new MEM(new TEMP("%i"
                    + String.valueOf(index + 1))));
        } else {
            index = table.getFieldIndex(table.currentClass, n.toString());
            return new ExpIRTree(new MEM(new BINOP(BINOP.MINUS, new MEM(
                    new TEMP("%i0")), new CONST(WORD_LENGTH
                    + (index * WORD_LENGTH)))));
        }
    }

    @Override
    public LazyIRTree visit (This n) {
        Exp e = new MEM(new TEMP("%i0"));
        return new ExpIRTree(e);
    }

    @Override
    public LazyIRTree visit (NewArray n) {
        TEMP t = TEMP.generateTEMP();
        String alloc = "_alloc_object";
        Exp arrayLength = n.e.accept(this).asExp();
        Exp length = new BINOP(BINOP.PLUS, arrayLength, new CONST(1));
        Stm e = new MOVE(t, new CALL(new NameOfLabel(alloc), new BINOP(
                BINOP.MUL, new CONST(WORD_LENGTH), length)));

        TEMP i = TEMP.generateTEMP();
        MOVE m1 = new MOVE(i, new CONST(0));
        MOVE mLength = new MOVE(new MEM(t), arrayLength);
        MOVE m2 = new MOVE(new MEM(new BINOP(BINOP.PLUS, t, i)), new CONST(0));
        MOVE m3 = new MOVE(i, new BINOP(BINOP.PLUS, i, new CONST(WORD_LENGTH)));
        Stm body = new SEQ(m2, m3);
        Stm head = new SEQ(new SEQ(m1, mLength), m3);

        Exp v = new ESEQ(new SEQ(e, new SEQ(head, new WhileExp(new LessIRTree(
                i, length), new StmIRTree(body)).asStm())), t);
        return new ExpIRTree(v);
    }

    @Override
    public LazyIRTree visit (NewObject n) {
        String alloc = "_alloc_object";
        TEMP t = TEMP.generateTEMP();
        int size = table.getTotalFieldNumber(n.i.toString());
        Exp e = new CALL(new NameOfLabel(alloc), new CONST(size * 4));
        Stm s = new MOVE(t, e);
        if (size == 0) {
            return new ExpIRTree(e);
        } else {
            TEMP i = TEMP.generateTEMP();
            MOVE m1 = new MOVE(i, new CONST(0));
            MOVE m2 = new MOVE(new MEM(new BINOP(BINOP.PLUS, t, i)), new CONST(
                    0));
            MOVE m3 = new MOVE(i, new BINOP(BINOP.PLUS, i, new CONST(
                    WORD_LENGTH)));

            Stm body = new SEQ(m2, m3);

            Exp v = new ESEQ(new SEQ(s, new SEQ(m1,
                    new WhileExp(new LessIRTree(i, new CONST(size)),
                            new StmIRTree(body)).asStm())), t);
            return new ExpIRTree(v);
        }
    }

    @Override
    public LazyIRTree visit (Not n) {

        final Exp seq;
        final LABEL t = new LABEL("true");
        final LABEL f = new LABEL("false");
        final LABEL join = new LABEL("end");

        TEMP i = TEMP.generateTEMP();

        seq = new ESEQ(new SEQ(new CJUMP(CJUMP.EQ, n.e.accept(this).asExp(),
                CONST.TRUE, t.label, f.label), new SEQ(new LABEL(t.label),
                new SEQ(new MOVE(i, CONST.FALSE), new SEQ(new JUMP(join.label),
                        new SEQ(new LABEL(f.label), new SEQ(new MOVE(i,
                                CONST.TRUE), new LABEL(join.label))))))), i);

        return new ExpIRTree(seq);
    }

    @Override
    public LazyIRTree visit (Identifier n) {
        int index;
        int type = table.getVarLocation(table.currentClass,
                table.currentMethod, n.toString());
        if (type == 0) {
            index = table.getLocalIndex(table.currentClass,
                    table.currentMethod, n.toString());
            return new ExpIRTree(new MEM(new BINOP(BINOP.MINUS,
                    new TEMP("%fp"), new CONST(WORD_LENGTH
                            + (index * WORD_LENGTH)))));
        } else if (type == 1) {
            index = table.getArgIndex(table.currentClass, table.currentMethod,
                    n.toString());
            return new ExpIRTree(new MEM(new TEMP("%i"
                    + String.valueOf(index + 1))));
        } else {
            index = table.getFieldIndex(table.currentClass, n.toString());
            return new ExpIRTree(new MEM(new BINOP(BINOP.MINUS, new MEM(
                    new TEMP("%i0")), new CONST(WORD_LENGTH
                    + (index * WORD_LENGTH)))));
        }
    }
}
