package translate;

import java.util.ArrayList;
import java.util.List;

import syntax.*;
import tree.*;
import visitor.SymbolTable;

public class Translater implements SyntaxTreeVisitor<LazyIRTree> {

    public SymbolTable table;
    public ArrayList<LazyIRTree> fragments = new ArrayList<LazyIRTree>();

    public Translater(SymbolTable t) {
        this.table = t;
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
        n.s.accept(this);
        return null;
    }

    @Override
    public LazyIRTree visit (SimpleClassDecl n) {
        table.setField(1, n.i.toString());
        for (MethodDecl m : n.ml) {
            fragments.add(m.accept(this));
        }
        return null;
    }

    @Override
    public LazyIRTree visit (ExtendingClassDecl n) {
        table.setField(1, n.i.toString());
        for (MethodDecl m : n.ml)
            fragments.add(m.accept(this));
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
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LazyIRTree visit (If n) {
        return new IfThenElseExp(n.e.accept(this),n.s1.accept(this),n.s2.accept(this));
    }

    @Override
    public LazyIRTree visit (While n) {
        return new WhileExp(n.e.accept(this),n.s.accept(this));
    }

    @Override
    public LazyIRTree visit (Print n) {
        CALL c=new CALL(new NameOfLabel("_print"),n.e.accept(this).asExp());
        EVAL e=new EVAL(c);
        return new StmIRTree(e);
    }

    @Override
    public LazyIRTree visit (Assign n) {
        Exp e1=n.i.accept(this).asExp();
        Exp e2=n.e.accept(this).asExp();
        
        MOVE m= new MOVE(e1,e2);
        return new StmIRTree(m);
    }

    @Override
    public LazyIRTree visit (ArrayAssign n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LazyIRTree visit (And n) {
        return new ExpIRTree(new BINOP(BINOP.AND, n.e1.accept(this).asExp(),
                n.e2.accept(this).asExp()));
    }

    @Override
    public LazyIRTree visit (LessThan n) {
        return new LessIRTree(n.e1.accept(this).asExp(),n.e2.accept(this).asExp());
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LazyIRTree visit (ArrayLength n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LazyIRTree visit (Call n) {
        
        // TODO Auto-generated method stub
        return null;
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
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LazyIRTree visit (This n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LazyIRTree visit (NewArray n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LazyIRTree visit (NewObject n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LazyIRTree visit (Not n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public LazyIRTree visit (Identifier n) {
        // TODO Auto-generated method stub
        return null;
    }

}
