# Instruction

## Tool Installation
First, follow the assignment2 introduction to create a java project and download the soot-3.3.0.jar into the target directory. More information about soot can be found in [soot-github](https://github.com/soot-oss/soot.git).

Notice: be sure that the java version you install is no later than [Java8](https://www.oracle.com/java/technologies/javase-downloads.html), and for the [eclipse](https://www.eclipse.org/eclipseide/) you can use the latest version.

Notice: the soot plug-in is not available for the latest version of eclipse, so try to only use the soot-3.3.0.jar and import soot as dependency project.

## Environment Setting
### Add soot to the class path
Choose and right click your java project folder, select "Build Path" and then "Configure Build Path", in the appeared properties window, choose "Classpath" and click the "Add External JARs..." in the right-hand side, then browse your local folder to locate where your "soot-3.3.0.jar" is. Finally, click "Apply and Close" and the soot project will be added in your class path.

### Add Java to the JRE library
The same as before, but this time choose "Modulepath - JRE System library" rather than "Classpath", then choose "Workspace default JRE (Java SE 8)" and click "Finish".

### Create package and add files
Right click the "src" folder under your project, and choose "New - Package" to create a package and put all the java files in the following tasks into this package.

Note: If the "src" already has a default package, just put the java files in that package.

## Illustration for the 3 parts of assignment2
For understanding the principle of how soot works for retrieving information of the programs, please refer to [soot-tutorial](http://www.iro.umontreal.ca/~dufour/cours/ift6315/docs/soot-tutorial.pdf).

For the field attribute API reference, you can check [stmt-field](https://www.sable.mcgill.ca/soot/doc/soot/jimple/Stmt.html) to find more useful tools that can be used in your stmt configuration.

The detailed implementation and codes can be seen in the "answers" folder.
