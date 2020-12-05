import java.io.File;

import java.util.Iterator;
import java.util.Map;
import java.util.Arrays;

import soot.*;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.jimple.IntConstant;
import soot.jimple.JasminClass;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.StringConstant;
import soot.util.JasminOutputStream;

public class SootConfigure {
	public static void main(String[] args)	{

		Scene.v().loadClassAndSupport("java.lang.Object");
		Scene.v().loadClassAndSupport("java.lang.System");
		
	//	Create SootClass
		SootClass testClass = new SootClass("HelloWorld", Modifier.PUBLIC);
		
		//make the class extend java.lang.Object
		testClass.setSuperclass(Scene.v().getSootClass("java.lang.Object"));
		
		//Add testClass to Scene object
		Scene.v().addClass(testClass);
		
//		Define Method
	    SootMethod mainMethod = new SootMethod(
	    		"main",                 
	    	    Arrays.asList(new Type[] {ArrayType.v(RefType.v("java.lang.String"), 1)}),
	    	    VoidType.v(), Modifier.PUBLIC | Modifier.STATIC
	    		);
	    
	    //Add Method
	    testClass.addMethod(mainMethod);
	    
	    //create jimple body
	    JimpleBody jBody = Jimple.v().newBody(mainMethod);
	    mainMethod.setActiveBody(jBody);
	    
	    //Add local
	    PatchingChain<Unit> units = jBody.getUnits();
	    
	    Local arg = Jimple.v().newLocal("r0", ArrayType.v(RefType.v("java.lang.String"), 1));
	    jBody.getLocals().add(arg);
	    
	    Local i0 = Jimple.v().newLocal("$i0", RefType.v("int"));
	    jBody.getLocals().add(i0);
	    
	    Local tmpRef1 = Jimple.v().newLocal("$r1", RefType.v("java.io.PrintStream"));
	    Local tmpRef2 = Jimple.v().newLocal("$r2", RefType.v("java.io.PrintStream"));
	    jBody.getLocals().add(tmpRef1);
	    jBody.getLocals().add(tmpRef2);
	    
	    Local r2 = Jimple.v().newLocal("$r2", RefType.v("java.lang.String"));
	    jBody.getLocals().add(r2);
	    
	    //Adding a Unit
	    units.add(Jimple.v().newAssignStmt(arg,
	            Jimple.v().newStaticFieldRef( Scene.v().getField("<java.lang.System: java.io.PrintStream out>").makeRef())));
	    
	 // insert "tmpRef.println("Hello world!")"
	    SootMethod toCall = Scene.v().getMethod("<java.io.PrintStream: void println(java.lang.String)>");
	    Unit iftarget = Jimple.v().newInvokeStmt(Jimple.v().newVirtualInvokeExpr(tmpRef1, toCall.makeRef(), StringConstant.v("Hello world!")));
	    
	    Unit ifunit = Jimple.v().newIfStmt(i0, iftarget);
		
	}
}
