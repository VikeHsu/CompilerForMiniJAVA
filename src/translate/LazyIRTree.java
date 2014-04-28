package translate;
/*
 * This abstract class is given in its entirety in Appel, Section 7.2, page 159.
 * But the class name 'Exp' seems a particularly bad choice; so do the method names.
 *
 */

import tree.*;

// Lazy view of intermediate representation trees
abstract public class LazyIRTree {
   public abstract Exp asExp();                      // ESEQ (asStm(), CONST(0))
   public abstract Stm asStm();                      // EVAL (asExp())
   public abstract Stm asCond (LABEL t, LABEL f);    // CJUMP (=, asExp(), CONST(0), t, f)
}
