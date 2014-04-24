package translate;
import java.util.List;

import syntax.*;
import tree.*;
import visitor.SymbolTable;

public class Translater implements SyntaxTreeVisitor<Object>{

    public SymbolTable table;
    public Translater(SymbolTable t) {
        this.table = t;
    }

    @Override
    public Void visit (Program n) {
        n.m.accept(this);
        for (ClassDecl c : n.cl)
            c.accept(this);
        return null;
    }

    @Override
    public Void visit (MainClass n) {
        table.setField(1, n.i1.toString());
        table.setField(2, "main");

        n.s.accept(this);
        return null;
    }

    @Override
    public Void visit (SimpleClassDecl n) {
        table.setField(1, n.i.toString());
        for(MethodDecl m: n.ml){
            m.accept(this);
        }
        return null;
    }

    @Override
    public Void visit (ExtendingClassDecl n) {
        table.setField(1, n.i.toString());
        for(MethodDecl m:n.ml)
            m.accept(this);
        return null;
    }

    @Override
    public Void visit (VarDecl n) {
        // TODO need visit VarDecl
        return null;
    }

    public static SEQ fromList (final Stm... args) {
        final int l= args.length;
        SEQ s = new SEQ (args[l-2], args[l-1]);
        for (int i=l-3; i>=0; i--) {
           s = new SEQ (args[i], s);
        }
        return s;
     }

    
    @Override
    public Void visit (MethodDecl n) {
        
        return null;
    }

    @Override
    public Void visit (Formal n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (IntArrayType n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (BooleanType n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (IntegerType n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (IdentifierType n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (Block n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (If n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (While n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (Print n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (Assign n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (ArrayAssign n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public BINOP visit (And n) {
        // TODO Auto-generated method stub
        return new BINOP(BINOP.AND,n.e1.accept(this),n.e2.accept(this));
    }

    @Override
    public Void visit (LessThan n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (Plus n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (Minus n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (Times n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (ArrayLookup n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (ArrayLength n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (Call n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CONST visit (IntegerLiteral n) {
        CONST c= new CONST(n.i);
        return c;
    }

    @Override
    public Void visit (True n) {
        return null;
    }

    @Override
    public Void visit (False n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (IdentifierExp n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (This n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (NewArray n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (NewObject n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (Not n) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Void visit (Identifier n) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
