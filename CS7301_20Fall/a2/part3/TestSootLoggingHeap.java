import java.io.File;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import soot.*;
import soot.jimple.AssignStmt;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.IntConstant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.toolkits.annotation.arraycheck.ClassFieldAnalysis;
import soot.options.Options;

public class TestSootLoggingHeap extends BodyTransformer {

    private static SootMethodRef logFieldAccMethod;

    public static void main(String[] args) {

        String mainclass = "HelloThread";

        // set classpath
        String javapath = System.getProperty("java.class.path");
        String jredir = System.getProperty("java.home") + "/lib/rt.jar";
        String path = javapath + File.pathSeparator + jredir;
        Scene.v().setSootClassPath(path);

        // add an intra-procedural analysis phase to Soot
        TestSootLoggingHeap analysis = new TestSootLoggingHeap();
        PackManager.v().getPack("jtp").add(new Transform("jtp.TestSootLoggingHeap", analysis));

        // load and set main class
        Options.v().set_app(true);
        SootClass appclass = Scene.v().loadClassAndSupport(mainclass);
        Scene.v().setMainClass(appclass);
        SootClass logClass = Scene.v().loadClassAndSupport("Log");
        logFieldAccMethod = logClass.getMethod("void logFieldAcc(java.lang.Object,java.lang.String,boolean,boolean)")
                .makeRef();
        Scene.v().loadNecessaryClasses();

        // start working
        PackManager.v().runPacks();

        PackManager.v().writeOutput();
    }

    public static void logFieldAcc(final Object o, String name, final boolean isStatic, final boolean writeFlag) {
        if (isStatic)
            System.out.println("Thread " + Thread.currentThread().getName() + (writeFlag ? " wrote" : " read")
                    + " static field " + name);
        else
            System.out.println("Thread " + Thread.currentThread().getName() + (writeFlag ? " wrote" : " read")
                    + " instance field " + name + " of object " + o);
    }

    @Override
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {

        // we don't instrument Log class
        if (!b.getMethod().getDeclaringClass().getName().equals("Log")) {
            Iterator<Unit> it = b.getUnits().snapshotIterator();
            while (it.hasNext()) {
                Stmt stmt = (Stmt) it.next();
                if (stmt.containsFieldRef()) {
                    // your code starts here
                    boolean staticField = stmt.getFieldRef().getField().isStatic();
                    String valueBox = stmt.getFieldRef().getField().toString();
                    boolean writable = true;

                    if (stmt instanceof AssignStmt) {
                        AssignStmt ass = (AssignStmt) stmt;
                        Value rightValue = ass.getRightOp();
                        Value leftValue = ass.getLeftOp();

                        if (rightValue.toString().equals(stmt.getFieldRefBox().getValue().toString())) {
                            writable = false;
                        } else if (leftValue.toString().equals(stmt.getFieldRefBox().getValue().toString())) {
                            writable = true;
                        }
                    }
                    TestSootLoggingHeap.logFieldAcc(Thread.currentThread(), valueBox, staticField, writable);
                }
            }
        }
    }
}
