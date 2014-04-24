package visitor;

import syntax.*;

public class DefineChecker implements SyntaxTreeVisitor<Void> {

    public SymbolTable table = new SymbolTable();

    @Override
    public Void visit (Program n) {
        if (n==null) {
         } else if (n.m==null) {
         } else {
            n.m.accept (this);
            for (ClassDecl c: n.cl) c.accept (this);
         }
        table.checkDefinedType();
        return null;
    }

    @Override
    public Void visit (MainClass n) {
        table.addClass(n.i1.s, "");
        table.addMethod("main", "void");
        return null;
    }

    @Override
    public Void visit (SimpleClassDecl n) {
        table.addClass(n.i.s, "");
        for (VarDecl v: n.vl){
            table.addVar(v.i.s, v.t.toString());
        }
        for (MethodDecl m: n.ml){
            table.addMethod(m.i.s, m.t.toString());
            for (Formal f: m.fl){
                table.addFormal(f.i.s, f.t.toString());
            }
            for(VarDecl v:m.vl){
                table.addVar(v.i.s, v.t.toString());
            }
        }
        return null;
    }

    @Override
    public Void visit (ExtendingClassDecl n) {
        table.addClass(n.i.s, n.j.s);
        for (VarDecl v: n.vl){
            table.addVar(v.i.s, v.t.toString());
        }
        for (MethodDecl m: n.ml){
            table.addMethod(m.i.s, m.t.toString());
            for (Formal f: m.fl){
                table.addFormal(f.i.s, f.t.toString());
            }
            for(VarDecl v:m.vl){
                table.addVar(v.i.s, v.t.toString());
            }
        }
        return null;
    }

    @Override
    public Void visit (VarDecl n) {
        return null;
    }

    @Override
    public Void visit (MethodDecl n) {
        
        return null;
    }

    @Override
    public Void visit (Formal n) {
        return null;
    }

    @Override
    public Void visit (IntArrayType n) {
        return null;
    }

    @Override
    public Void visit (BooleanType n) {
        return null;
    }

    @Override
    public Void visit (IntegerType n) {
        return null;
    }

    @Override
    public Void visit (IdentifierType n) {
        return null;
    }

    @Override
    public Void visit (Block n) {
        return null;
    }

    @Override
    public Void visit (If n) {
        return null;
    }

    @Override
    public Void visit (While n) {
        return null;
    }

    @Override
    public Void visit (Print n) {
        return null;
    }

    @Override
    public Void visit (Assign n) {
        return null;
    }

    @Override
    public Void visit (ArrayAssign n) {
        return null;
    }

    @Override
    public Void visit (And n) {
        return null;
    }

    @Override
    public Void visit (LessThan n) {
        return null;
    }

    @Override
    public Void visit (Plus n) {
        return null;
    }

    @Override
    public Void visit (Minus n) {
        return null;
    }

    @Override
    public Void visit (Times n) {
        return null;
    }

    @Override
    public Void visit (ArrayLookup n) {
        return null;
    }

    @Override
    public Void visit (ArrayLength n) {
        return null;
    }

    @Override
    public Void visit (Call n) {
        return null;
    }

    @Override
    public Void visit (IntegerLiteral n) {
        return null;
    }

    @Override
    public Void visit (True n) {
        return null;
    }

    @Override
    public Void visit (False n) {
        return null;
    }

    @Override
    public Void visit (IdentifierExp n) {
        return null;
    }

    @Override
    public Void visit (This n) {
        return null;
    }

    @Override
    public Void visit (NewArray n) {
        return null;
    }

    @Override
    public Void visit (NewObject n) {
        return null;
    }

    @Override
    public Void visit (Not n) {
        return null;
    }

    @Override
    public Void visit (Identifier n) {
        return null;
    }

}
