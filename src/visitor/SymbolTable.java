package visitor;

import global.Verboser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import syntax.Type;

public class SymbolTable {
    private static final String INT_TYPE = Type.THE_INTEGER_TYPE.toString();
    private static final String INT_ARRAY_TYPE = Type.THE_INT_ARRAY_TYPE
            .toString();
    private static final String BOOLEAN_TYPE = Type.THE_BOOLEAN_TYPE.toString();

    LinkedHashMap<String, Class> classes = new LinkedHashMap<String, Class>();
    int level = 0; // level: 0:global, 1:class, 2,Method;
    public String currentClass;
    public String currentMethod;

    public void addClass (String className, String extendedClassname) {
        classes.put(className, new Class(className, extendedClassname));
        level = 1;
        currentClass = className;
    }

    public void addVar (String varName, String varType) {
        switch (level) {
        case 1:
            classes.get(currentClass).field.put(varName, varType);
            break;
        case 2:
            classes.get(currentClass).methods.get(currentMethod).local.put(
                    varName, varType);
            break;
        }
    }

    public void IdLookup () {

    }

    public void addMethod (String methodName, String returnType) {
        if (level == 1) {
            classes.get(currentClass).methods.put(methodName, new Method(
                    methodName, returnType));
            level = 2;
            currentMethod = methodName;
        }
    }

    public void addFormal (String formalName, String formalType) {
        if (level == 2) {
            classes.get(currentClass).methods.get(currentMethod).formalIds
                    .add(formalName);
            classes.get(currentClass).methods.get(currentMethod).formalTypes
                    .add(formalType);
        }
    }

    public void setField (int lvl, String currentName) {
        this.level = lvl;
        if (level == 1)
            currentClass = currentName;
        else if (level == 2)
            currentMethod = currentName;
    }

    public String getType (String idName) {
        Class c = classes.get(currentClass);
        if (level == 2) {
            Method m = c.methods.get(currentMethod);
            if (m.formalIds.contains(idName))
                return m.formalTypes.get(m.formalIds.indexOf(idName));
            else if (m.local.containsKey(idName))
                return m.local.get(idName);
        }
        return findField(c, idName);
    }

    private String findField (Class c, String idName) {
        if (c.field.containsKey(idName)) {
            return c.field.get(idName);
        } else if (!c.extendedClassName.isEmpty()) {
            Class ec = classes.get(c.extendedClassName);
            return findField(ec, idName);
        }
        Verboser.Err();
        System.err.println("error on use id: " + idName + " Not definded.");
        return null;
    }

    public String getVarInfo (String idName) {
        Class c = classes.get(currentClass);
        if (level == 2) {
            Method m = c.methods.get(currentMethod);
            if (m.formalIds.contains(idName)) {
                String type = m.formalTypes.get(m.formalIds.indexOf(idName));
                String out = "formal parameter '" + idName + "' in method '"
                        + c.Name + "." + m.Name + "'.  Type=" + type;
                return out;
            } else if (m.local.containsKey(idName)) {
                String type = m.local.get(idName);
                String out = "local variable '" + idName + "' in method '"
                        + c.Name + "." + m.Name + "'.  Type=" + type;
                return out;
            }
        }
        return findFieldInfo(c, idName);
    }

    private String findFieldInfo (Class c, String idName) {
        if (c.field.containsKey(idName)) {
            String type = c.field.get(idName);
            String out = "field variable '" + idName + "' in class '" + c.Name
                    + "'.  Type=" + type;
            return out;
        } else if (!c.extendedClassName.isEmpty()) {
            Class ec = classes.get(c.extendedClassName);
            return findField(ec, idName);
        }
        Verboser.Err();
        System.err.println("error on use id: " + idName + " Not definded.");
        return null;
    }

    public boolean findMethodfFormal (String className, String methodName,
            String formalType, int formalIndex, int maxSize) {
        if (classes.containsKey(className)) {
            Class c = classes.get(className);
            if (c.methods.containsKey(methodName)) {
                Method m = c.methods.get(methodName);
                if (m.formalTypes.size() == maxSize)
                    if (m.formalTypes.get(formalIndex).equals(formalType))
                        return true;
                    else {
                        Verboser.Err();
                        System.err.println("Method argument not match");
                    }
                else {
                    Verboser.Err();
                    System.err.println("Method argument amount not match.");
                }

            } else {
                return findMethodfFormal(c.extendedClassName, methodName,
                        formalType, formalIndex, maxSize);
            }
            return false;
        }
        Verboser.Err();
        System.err.println("Method not defined.");
        return false;
    }

    public String findMethodReturnType (String className, String methodName) {
        if (classes.containsKey(className)) {
            Class c = classes.get(className);
            if (c.methods.containsKey(methodName)) {
                return c.methods.get(methodName).ReturnType;
            } else {
                return findMethodReturnType(c.extendedClassName, methodName);
            }
        } else {
            Verboser.Err();
            System.err.println("Method not defined.");
            return null;
        }
    }

    private boolean findType (String typeName) {// false: not found; true: Type
                                                // found;
        if (typeName.equals(BOOLEAN_TYPE) | typeName.equals(INT_ARRAY_TYPE)
                | typeName.equals(INT_TYPE))
            return true;
        else if (classes.containsKey(typeName)) {
            return true;
        }
        return false;
    }

    public boolean checkExtendsCircle (String ClassName,
            ArrayList<String> extendLink) {
        // true: have circle; false: no circle:
        if (classes.get(ClassName).extendedClassName.equals(""))
            return true;// good
        else if (!extendLink.contains(ClassName)) {
            extendLink.add(ClassName);
            return checkExtendsCircle(classes.get(ClassName).extendedClassName,
                    extendLink);
        } else
            return false;
    }

    public void checkDefinedType () {
        for (Class c : classes.values()) {
            if (!c.field.isEmpty())
                for (String s : c.field.keySet()) {
                    String v = c.field.get(s);
                    if (!findType(v)) {
                        Verboser.Err();
                        System.err.println("error on define " + s
                                + " using type " + v);
                    }
                }
            if (!c.methods.isEmpty())
                for (Method m : c.methods.values()) {
                    if (!m.formalIds.isEmpty())
                        for (String s : m.formalIds) {
                            String v = m.formalTypes
                                    .get(m.formalIds.indexOf(s));
                            if (!findType(v)) {
                                Verboser.Err();
                                System.err.println("error on define " + s
                                        + " using type " + v);
                            }
                        }
                    if (!m.local.isEmpty())
                        for (String s : m.local.keySet()) {
                            String v = m.local.get(s);
                            if (!findType(v)) {
                                Verboser.Err();
                                System.err.println("error on define " + s
                                        + " using type " + v);
                            }
                        }
                }
            if (!checkExtendsCircle(c.Name, new ArrayList<String>()))
                Verboser.DefLoop(c.Name, c.extendedClassName);
        }
    }

    public int getTotalFieldNumber (String className) {
        int i = 0;
        i += classes.get(className).field.size();
        if (!classes.get(className).extendedClassName.equals("")) {
            i += getTotalFieldNumber(classes.get(className).extendedClassName);
        }
        return i;
    }

    public int getFieldIndex (String className, String fieldName) {
        int index = 0;
        Class c = classes.get(className);
        for (String str : c.field.keySet()) {
            if (str.equals(fieldName)) {
                return index;
            }
            index++;
        }
        if (!classes.get(className).extendedClassName.equals("")) {
            index += getFieldIndex(classes.get(className).extendedClassName,
                    fieldName);
        }
        return index;
    }

    public int getLocalIndex (String className, String methodName,
            String fieldName) {
        int index = 0;
        Class c = classes.get(className);
        Method m = c.methods.get(methodName);
        for (String str : m.local.keySet()) {
            if (str.equals(fieldName)) {
                return index;
            }
            index++;
        }
        return index;
    }

    public int getArgIndex (String className, String methodName, String ArgName) {
        Method m = classes.get(className).methods.get(methodName);
        return m.formalIds.indexOf(ArgName);
    }

    public int getVarLocation (String className, String methodName,
            String varName) {
        Method m = classes.get(className).methods.get(methodName);
        if (m.formalIds.contains(varName)) {
            return 1;//formal
        } else if (m.local.containsKey(varName)) {
            return 0;//local
        }
        return 2;//field
    }
    
    public LinkedHashMap<String,String>  getfuncName(){
        LinkedHashMap<String,String> funcName=new  LinkedHashMap<String,String> ();
        int id=0;
        for(Class c:classes.values()){
            for(Method m:c.methods.values()){
                String nameKey=c.Name+"$"+m.Name;
                funcName.put(nameKey, nameKey+"$"+id);
                id++;
            }
        }
        return funcName;
    }
}

class Class {
    String Name;
    String extendedClassName;
    LinkedHashMap<String, String> field = new LinkedHashMap<String, String>();
    HashMap<String, Method> methods = new HashMap<String, Method>();

    Class(String cName, String eName) {
        this.Name = cName;
        this.extendedClassName = eName;
    }
}

class Method {
    String Name;
    String ReturnType;
    ArrayList<String> formalIds = new ArrayList<String>();
    ArrayList<String> formalTypes = new ArrayList<String>();

    LinkedHashMap<String, String> local = new LinkedHashMap<String, String>();// <
                                                                              // var
                                                                              // name
                                                                              // ,

    // var type>

    Method(String cName, String rType) {
        this.Name = cName;
        this.ReturnType = rType;
    }
}

class formal {
    String id;
    String type;

    formal(String fId, String fType) {
        this.id = fId;
        this.type = fType;
    }
}
