package visitor;

import global.Verboser;

import syntax.And;
import syntax.ArrayAssign;
import syntax.ArrayLength;
import syntax.ArrayLookup;
import syntax.Assign;
import syntax.Block;
import syntax.BooleanType;
import syntax.Call;
import syntax.ClassDecl;
import syntax.ExtendingClassDecl;
import syntax.False;
import syntax.Formal;
import syntax.Identifier;
import syntax.IdentifierExp;
import syntax.IdentifierType;
import syntax.If;
import syntax.IntArrayType;
import syntax.IntegerLiteral;
import syntax.IntegerType;
import syntax.LessThan;
import syntax.MainClass;
import syntax.MethodDecl;
import syntax.Minus;
import syntax.NewArray;
import syntax.NewObject;
import syntax.Not;
import syntax.Plus;
import syntax.Print;
import syntax.Program;
import syntax.SimpleClassDecl;
import syntax.Statement;
import syntax.SyntaxTreeVisitor;
import syntax.This;
import syntax.Times;
import syntax.True;
import syntax.Type;
import syntax.VarDecl;
import syntax.While;

public class TypeChecker implements SyntaxTreeVisitor<String> {

    private final String INT_TYPE = Type.THE_INTEGER_TYPE.toString();
    private final String BOOLEAN_TYPE = Type.THE_BOOLEAN_TYPE.toString();
    private final String INT_ARRAY_TYPE = Type.THE_INT_ARRAY_TYPE.toString();
    public SymbolTable table;

    private void println (String s) {
        Verboser.Err();
        System.err.println(s);
    }

    private boolean checkType (String t1, String t2) {
        if (t1.equals(t2))
            return true;
        else
            return false;
    }

    public TypeChecker(SymbolTable t) {
        this.table = t;
    }

    @Override
    public String visit (Program n) {
        if (n == null) {
        } else if (n.m == null) {
        } else {
            n.m.accept(this);
            for (ClassDecl c : n.cl)
                c.accept(this);
        }
        return null;
    }

    @Override
    public String visit (MainClass n) {
        Verboser.DecClass(true, n.i1.toString());
        Verboser.DecMethod("main", n.i1.toString());
        table.setField(1, n.i1.toString());
        n.s.accept(this);
        return null;
    }

    @Override
    public String visit (SimpleClassDecl n) {
        Verboser.DecClass(false, n.i.toString());
        table.setField(1, n.i.toString());
        for (MethodDecl m : n.ml) {
            Verboser.DecMethod(m.i.toString(), n.i.toString());
            m.accept(this);
        }
        return null;
    }

    @Override
    public String visit (ExtendingClassDecl n) {
        Verboser.DecClass(false, n.i.toString(), n.j.toString());
        table.setField(1, n.i.toString());
        for (VarDecl v : n.vl) {
            v.accept(this);
        }
        for (MethodDecl m : n.ml) {
            Verboser.DecMethod(m.i.toString(), n.i.toString());
            m.accept(this);
        }
        return null;
    }

    @Override
    public String visit (VarDecl n) {
        switch (table.level) {
        case 1:
            Verboser.DecField(n.i.toString(), table.currentClass);
            break;
        case 2:
            Verboser.DecLocal(n.i.toString(), table.currentClass, table.currentMethod);
            break;
        }
        return null;
    }

    @Override
    public String visit (MethodDecl n) {
        table.setField(2, n.i.toString());
        for (Formal f : n.fl) {
            f.accept(this);
        }
        for (VarDecl v : n.vl) {
            v.accept(this);
        }
        for (Statement s : n.sl)
            s.accept(this);
        String returnedType = n.e.accept(this);
        if (!checkType(n.t.toString(), returnedType))
            println("Return Type not consistant: " + n.t.toString() + " and "
                    + returnedType);
        return null;
    }

    @Override
    public String visit (Formal n) {
        Verboser.DecFormal(n.i.toString(), table.currentClass,
                table.currentMethod, n.t.toString());
        return null;
    }

    @Override
    public String visit (IntArrayType n) {
        return n.toString();
    }

    @Override
    public String visit (BooleanType n) {
        return n.toString();
    }

    @Override
    public String visit (IntegerType n) {
        return n.toString();
    }

    @Override
    public String visit (IdentifierType n) {
        return n.getName();
    }

    @Override
    public String visit (Block n) {
        for (Statement s : n.sl) {
            s.accept(this);
        }
        return null;
    }

    @Override
    public String visit (If n) {
        if (!checkType(n.e.accept(this), BOOLEAN_TYPE)) {
            println("If condition not boolean");
        }
        n.s1.accept(this);
        n.s2.accept(this);
        return null;
    }

    @Override
    public String visit (While n) {
        if (!checkType(n.e.accept(this), BOOLEAN_TYPE)) {
            println("While condition not boolean");
        }
        n.s.accept(this);
        return null;
    }

    @Override
    public String visit (Print n) {
        if (!checkType(INT_TYPE, n.e.accept(this))) {
            println("Print expression not integer.");
        }
        return null;
    }

    @Override
    public String visit (Assign n) {
        if (!checkType(n.i.accept(this), n.e.accept(this))) {
            println("Assign type not consisite.");
        }
        return null;
    }

    @Override
    public String visit (ArrayAssign n) {
        if (!checkType(INT_ARRAY_TYPE, n.i.accept(this)))
            println("Array Id not int array type.");
        if (!checkType(INT_TYPE, n.e1.accept(this)))
            println("Array index not integer.");
        if (!checkType(INT_TYPE, n.e2.accept(this)))
            println("Array expression not integer");
        return null;
    }

    @Override
    public String visit (And n) {

        if (!(checkType(BOOLEAN_TYPE, n.e1.accept(this)) && checkType(
                BOOLEAN_TYPE, n.e2.accept(this))))
            println("Expression arround AND not boolean type.");
        return BOOLEAN_TYPE;
    }

    @Override
    public String visit (LessThan n) {
        if (!(checkType(INT_TYPE, n.e1.accept(this)) && checkType(INT_TYPE,
                n.e2.accept(this))))
            println("Expression arround LESSTHAN not INT type.");
        return BOOLEAN_TYPE;
    }

    @Override
    public String visit (Plus n) {
        if (!(checkType(INT_TYPE, n.e1.accept(this)) && checkType(INT_TYPE,
                n.e2.accept(this))))
            println("Expression arround PLUS not INT type.");
        return INT_TYPE;
    }

    @Override
    public String visit (Minus n) {
        if (!(checkType(INT_TYPE, n.e1.accept(this)) && checkType(INT_TYPE,
                n.e2.accept(this))))
            println("Expression arround MINUS not INT type.");
        return INT_TYPE;
    }

    @Override
    public String visit (Times n) {
        if (!(checkType(INT_TYPE, n.e1.accept(this)) && checkType(INT_TYPE,
                n.e2.accept(this))))
            println("Expression arround TIMES not INT type.");
        return INT_TYPE;
    }

    @Override
    public String visit (ArrayLookup n) {
        if (!checkType(INT_ARRAY_TYPE, n.e1.accept(this)))
            println("Array Id not int array type.");
        if (!checkType(INT_TYPE, n.e2.accept(this)))
            println("Array index not integer.");
        return INT_TYPE;
    }

    @Override
    public String visit (ArrayLength n) {
        if (!checkType(INT_ARRAY_TYPE, n.e.accept(this)))
            println("Array Id not int array type.");
        return INT_TYPE;
    }

    @Override
    public String visit (Call n) {
        String InstanceType = n.e.accept(this);
        String returnType=table.findMethodReturnType(InstanceType, n.i.toString());
        Verboser.UseCall(n.i.toString(), InstanceType, returnType);
        for (int i = 0; i < n.el.size(); i++) {
            table.findMethodfFormal(InstanceType, n.i.toString(), n.el.get(i)
                    .accept(this), i, n.el.size());
        }
        return returnType;
    }

    @Override
    public String visit (IntegerLiteral n) {
        return INT_TYPE;
    }

    @Override
    public String visit (True n) {
        return BOOLEAN_TYPE;
    }

    @Override
    public String visit (False n) {
        return BOOLEAN_TYPE;
    }

    @Override
    public String visit (IdentifierExp n) {
        Verboser.UseVar(table.getVarInfo(n.toString()));
        return table.getType(n.toString());
    }

    @Override
    public String visit (This n) {
        Verboser.UseThis(table.currentClass, table.currentMethod);
        return table.currentClass;
    }

    @Override
    public String visit (NewArray n) {
        if (!checkType(INT_TYPE, n.e.accept(this)))
            println("Array index not int type.");
        return INT_ARRAY_TYPE;
    }

    @Override
    public String visit (NewObject n) {
        Verboser.UseNew(n.i.toString());
        return n.i.toString();
    }

    @Override
    public String visit (Not n) {
        if (!checkType(BOOLEAN_TYPE, n.e.accept(this)))
            println("Expression after NOT is not boolean type.");
        return BOOLEAN_TYPE;
    }

    @Override
    public String visit (Identifier n) {
        Verboser.UseVar(table.getVarInfo(n.toString()));
        return table.getType(n.toString());
    }
}
