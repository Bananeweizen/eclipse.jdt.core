/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jdt.core.tests.compiler.regression;

import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;

/**
 * Tests to verify that Compiler options work well for Javadoc.
 * This class does not tests syntax error option as it's considered already
 * tested by other JavadocTest* classes.
 * @see <a href="http://bugs.eclipse.org/bugs/show_bug.cgi?id=46854">46854</a>
 * @see <a href="http://bugs.eclipse.org/bugs/show_bug.cgi?id=46976">46976</a>
 */
public class JavadocTestOptions extends JavadocTest {

	String reportInvalidJavadoc = null;
	String reportInvalidJavadocTagsVisibility = null;
	String reportInvalidJavadocTags = null;
	String reportMissingJavadocTags = null;
	String reportMissingJavadocTagsVisibility = null;
	String reportMissingJavadocTagsOverriding = null;
	String reportMissingJavadocComments = null;
	String reportMissingJavadocCommentsVisibility = null;
	String reportMissingJavadocCommentsOverriding = null;
	
	private final int PUBLIC_VISIBILITY = 0;
	private final int PROTECTED_VISIBILITY = 1;
	private final int DEFAULT_VISIBILITY = 2;
	private final int PRIVATE_VISIBILITY = 3;

	private static String JavadocWithInvalidReferences = "	/**\n" + 
		"	 * @see X.X_dep\n" + 
		"	 * @see X.X_priv\n" + 
		"	 * @see X.Unknown\n" + 
		"	 * @see X#X(int)\n" + 
		"	 * @see X#X(String)\n" + 
		"	 * @see X#X()\n" + 
		"	 * @see X#x_dep\n" + 
		"	 * @see X#x_priv\n" + 
		"	 * @see X#unknown\n" + 
		"	 * @see X#foo_dep()\n" + 
		"	 * @see X#foo_priv()\n" + 
		"	 * @see X#foo_dep(String)\n" + 
		"	 * @see X#unknown()\n" + 
		"	 */\n";
	private static String JavadocWithInvalidReferencesForMethod = "	/**\n" + 
		"	 * @param str\n" + 
		"	 * @param str\n" + 
		"	 * @param xxx\n" + 
		"	 * @throws IllegalArgumentException\n" + 
		"	 * @throws IllegalArgumentException\n" + 
		"	 * @throws java.io.IOException\n" + 
		"	 * @throws Unknown\n" + 
		"	 * @see X.X_dep\n" + 
		"	 * @see X.X_priv\n" + 
		"	 * @see X.Unknown\n" + 
		"	 * @see X#X(int)\n" + 
		"	 * @see X#X(String)\n" + 
		"	 * @see X#X()\n" + 
		"	 * @see X#x_dep\n" + 
		"	 * @see X#x_priv\n" + 
		"	 * @see X#unknown\n" + 
		"	 * @see X#foo_dep()\n" + 
		"	 * @see X#foo_priv()\n" + 
		"	 * @see X#foo_dep(String)\n" + 
		"	 * @see X#unknown()\n" + 
		"	 */\n";
	private static String refClassForInvalidJavadoc =
		"public class X {\n" + 
		"	/** @deprecated */\n" + 
		"	class X_dep{}\n" + 
		"	private class X_priv{}\n" + 
		"	/** @deprecated */\n" + 
		"	int x_dep;\n" + 
		"	private int x_priv;\n" + 
		"	/** @deprecated */\n" + 
		"	X() {}\n" + 
		"	private X(int x) {}\n" + 
		"	/** @deprecated */\n" + 
		"	void foo_dep() {}\n" + 
		"	private void foo_priv() {}\n" + 
		"	}\n";
	private static String[] InvalidReferencesClassJavadocComments = {
		"X.java",
		refClassForInvalidJavadoc,
		"Y.java",
		"public class Y {\n" + 
			JavadocWithInvalidReferences + 
			"	public class X_pub {}\n" + 
			JavadocWithInvalidReferences + 
			"	protected class X_prot {}\n" + 
			JavadocWithInvalidReferences + 
			"	class X_pack {}\n" + 
			JavadocWithInvalidReferences + 
			"	private class X_priv {}\n" + 
			"}\n" + 
			"\n"
	};
	private static String[] InvalidReferencesFieldJavadocComments = {
		"X.java",
		refClassForInvalidJavadoc,
		"Y.java",
		"public class Y {\n" + 
			JavadocWithInvalidReferences + 
			"	public int x_pub;\n" + 
			JavadocWithInvalidReferences + 
			"	protected int x_prot;\n" + 
			JavadocWithInvalidReferences + 
			"	int x_pack;\n" + 
			JavadocWithInvalidReferences+ 
			"	private int x_priv;\n" + 
			"}\n" + 
			"\n"
	};
	private static String[] InvalidReferencesMethodJavadocComments = {
		"X.java",
		refClassForInvalidJavadoc,
		"Y.java",
		"public class Y {\n" + 
			JavadocWithInvalidReferencesForMethod + 
			"	public void foo_pub(String str) throws IllegalArgumentException {}\n" +
			JavadocWithInvalidReferencesForMethod + 
			"	protected void foo_pro(String str) throws IllegalArgumentException {}\n" + 
			JavadocWithInvalidReferencesForMethod + 
			"	void foo_pack(String str) throws IllegalArgumentException {}\n" + 
			JavadocWithInvalidReferencesForMethod + 
			"	private void foo_priv(String str) throws IllegalArgumentException {}\n" +
			"}\n" + 
			"\n"
	};
	private static String[] InvalidReferencesConstructorJavadocComments = {
		"X.java",
		refClassForInvalidJavadoc,
		"Y.java",
		"public class Y {\n" + 
			JavadocWithInvalidReferencesForMethod + 
			"	public Y(int str) {}\n" +
			JavadocWithInvalidReferencesForMethod + 
			"	protected Y(long str) {}\n" + 
			JavadocWithInvalidReferencesForMethod + 
			"	Y(float str) {}\n" + 
			JavadocWithInvalidReferencesForMethod + 
			"	private Y(double str) {}\n" +
			"}\n" + 
			"\n"
	};
	private static String[] MissingTags = {
		"X.java",
		"public class X {\n" + 
			"	// public\n" + 
			"	/** */\n" + 
			"	public class PublicClass {}\n" + 
			"	/** */\n" + 
			"	public int publicField;\n" + 
			"	/** */\n" + 
			"	public X(int i) {}\n" + 
			"	/** */\n" + 
			"	public int publicMethod(long l) { return 0;}\n" + 
			"	// protected\n" + 
			"	/** */\n" + 
			"	protected class ProtectedClass {}\n" + 
			"	/** */\n" + 
			"	protected int protectedField;\n" + 
			"	/** */\n" + 
			"	protected X(long l) {}\n" + 
			"	/** */\n" + 
			"	protected int protectedMethod(long l) { return 0; }\n" + 
			"	// default\n" + 
			"	/** */\n" + 
			"	class PackageClass {}\n" + 
			"	/** */\n" + 
			"	int packageField;\n" + 
			"	/** */\n" + 
			"	X(float f) {}\n" + 
			"	/** */\n" + 
			"	int packageMethod(long l) { return 0;}\n" + 
			"	// private\n" + 
			"	/** */\n" + 
			"	private class PrivateClass {}\n" + 
			"	/** */\n" + 
			"	private int privateField;\n" + 
			"	/** */\n" + 
			"	private X(double d) {}\n" + 
			"	/** */\n" + 
			"	private int privateMethod(long l) { return 0;}\n" + 
			"}\n" + 
		"\n",
		"Y.java",
		"/** */\n" + 
			"public class Y extends X {\n" + 
			"	public Y(int i) { super(i); }\n" + 
			"	//methods\n" + 
			"	/** */\n" + 
			"	public int publicMethod(long l) { return 0;}\n" + 
			"	/** */\n" + 
			"	protected int protectedMethod(long l) { return 0;}\n" + 
			"	/** */\n" + 
			"	int packageMethod(long l) { return 0;}\n" + 
			"	/** */\n" + 
			"	private int privateMethod(long l) { return 0;}\n" + 
			"}\n"
	};
	private static String[] MissingComments = {
		"X.java",
		"/** */\n" + 
			"public class X {\n" + 
			"	// public\n" + 
			"	public class PublicClass {}\n" + 
			"	public int publicField;\n" + 
			"	public X(int i) {}\n" + 
			"	public int publicMethod(long l) { return 0;}\n" + 
			"	// protected\n" + 
			"	protected class ProtectedClass {}\n" + 
			"	protected int protectedField;\n" + 
			"	protected X(long l) {}\n" + 
			"	protected int protectedMethod(long l) { return 0; }\n" + 
			"	// default\n" + 
			"	class PackageClass {}\n" + 
			"	int packageField;\n" + 
			"	X(float f) {}\n" + 
			"	int packageMethod(long l) { return 0;}\n" + 
			"	// private\n" + 
			"	private class PrivateClass {}\n" + 
			"	private int privateField;\n" + 
			"	private X(double d) {}\n" + 
			"	private int privateMethod(long l) { return 0;}\n" + 
			"}\n" + 
			"\n",
		"Y.java",
		"/** */\n" + 
			"public class Y extends X {\n" + 
			"	/** */\n" + 
			"	public Y(int i) { super(i); }\n" + 
			"	public int publicMethod(long l) { return 0;}\n" + 
			"	protected int protectedMethod(long l) { return 0;}\n" + 
			"	int packageMethod(long l) { return 0;}\n" + 
			"	private int privateMethod(long l) { return 0;}\n" + 
			"}\n"
	};

	private static String[] resultForInvalidTagsReferencesClassOrField = {
		"1. ERROR in Y.java (at line 3)\n" + 
			"	* @see X.X_dep\n" + 
			"	       ^^^^^^^\n" + 
			"Javadoc: The type X.X_dep is deprecated\n" + 
			"----------\n" + 
			"2. ERROR in Y.java (at line 4)\n" + 
			"	* @see X.X_priv\n" + 
			"	       ^^^^^^^^\n" + 
			"Javadoc: The type X.X_priv is not visible\n" + 
			"----------\n" + 
			"3. ERROR in Y.java (at line 5)\n" + 
			"	* @see X.Unknown\n" + 
			"	       ^^^^^^^^^\n" + 
			"Javadoc: X.Unknown cannot be resolved to a type\n" + 
			"----------\n" + 
			"4. ERROR in Y.java (at line 6)\n" + 
			"	* @see X#X(int)\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X(int) is not visible\n" + 
			"----------\n" + 
			"5. ERROR in Y.java (at line 7)\n" + 
			"	* @see X#X(String)\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X(String) is undefined\n" + 
			"----------\n" + 
			"6. ERROR in Y.java (at line 8)\n" + 
			"	* @see X#X()\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X() is deprecated\n" + 
			"----------\n" + 
			"7. ERROR in Y.java (at line 9)\n" + 
			"	* @see X#x_dep\n" + 
			"	         ^^^^^\n" + 
			"Javadoc: The field X.x_dep is deprecated\n" + 
			"----------\n" + 
			"8. ERROR in Y.java (at line 10)\n" + 
			"	* @see X#x_priv\n" + 
			"	         ^^^^^^\n" + 
			"Javadoc: The field x_priv is not visible\n" + 
			"----------\n" + 
			"9. ERROR in Y.java (at line 11)\n" + 
			"	* @see X#unknown\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: unknown cannot be resolved or is not a field\n" + 
			"----------\n" + 
			"10. ERROR in Y.java (at line 12)\n" + 
			"	* @see X#foo_dep()\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method foo_dep() from the type X is deprecated\n" + 
			"----------\n" + 
			"11. ERROR in Y.java (at line 13)\n" + 
			"	* @see X#foo_priv()\n" + 
			"	         ^^^^^^^^\n" + 
			"Javadoc: The method foo_priv() from the type X is not visible\n" + 
			"----------\n" + 
			"12. ERROR in Y.java (at line 14)\n" + 
			"	* @see X#foo_dep(String)\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method foo_dep() in the type X is not applicable for the arguments (String)\n" + 
			"----------\n" + 
			"13. ERROR in Y.java (at line 15)\n" + 
			"	* @see X#unknown()\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method unknown() is undefined for the type X\n" + 
			"----------\n",
		"14. ERROR in Y.java (at line 19)\n" + 
			"	* @see X.X_dep\n" + 
			"	       ^^^^^^^\n" + 
			"Javadoc: The type X.X_dep is deprecated\n" + 
			"----------\n" + 
			"15. ERROR in Y.java (at line 20)\n" + 
			"	* @see X.X_priv\n" + 
			"	       ^^^^^^^^\n" + 
			"Javadoc: The type X.X_priv is not visible\n" + 
			"----------\n" + 
			"16. ERROR in Y.java (at line 21)\n" + 
			"	* @see X.Unknown\n" + 
			"	       ^^^^^^^^^\n" + 
			"Javadoc: X.Unknown cannot be resolved to a type\n" + 
			"----------\n" + 
			"17. ERROR in Y.java (at line 22)\n" + 
			"	* @see X#X(int)\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X(int) is not visible\n" + 
			"----------\n" + 
			"18. ERROR in Y.java (at line 23)\n" + 
			"	* @see X#X(String)\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X(String) is undefined\n" + 
			"----------\n" + 
			"19. ERROR in Y.java (at line 24)\n" + 
			"	* @see X#X()\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X() is deprecated\n" + 
			"----------\n" + 
			"20. ERROR in Y.java (at line 25)\n" + 
			"	* @see X#x_dep\n" + 
			"	         ^^^^^\n" + 
			"Javadoc: The field X.x_dep is deprecated\n" + 
			"----------\n" + 
			"21. ERROR in Y.java (at line 26)\n" + 
			"	* @see X#x_priv\n" + 
			"	         ^^^^^^\n" + 
			"Javadoc: The field x_priv is not visible\n" + 
			"----------\n" + 
			"22. ERROR in Y.java (at line 27)\n" + 
			"	* @see X#unknown\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: unknown cannot be resolved or is not a field\n" + 
			"----------\n" + 
			"23. ERROR in Y.java (at line 28)\n" + 
			"	* @see X#foo_dep()\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method foo_dep() from the type X is deprecated\n" + 
			"----------\n" + 
			"24. ERROR in Y.java (at line 29)\n" + 
			"	* @see X#foo_priv()\n" + 
			"	         ^^^^^^^^\n" + 
			"Javadoc: The method foo_priv() from the type X is not visible\n" + 
			"----------\n" + 
			"25. ERROR in Y.java (at line 30)\n" + 
			"	* @see X#foo_dep(String)\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method foo_dep() in the type X is not applicable for the arguments (String)\n" + 
			"----------\n" + 
			"26. ERROR in Y.java (at line 31)\n" + 
			"	* @see X#unknown()\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method unknown() is undefined for the type X\n" + 
			"----------\n",
		"27. ERROR in Y.java (at line 35)\n" + 
			"	* @see X.X_dep\n" + 
			"	       ^^^^^^^\n" + 
			"Javadoc: The type X.X_dep is deprecated\n" + 
			"----------\n" + 
			"28. ERROR in Y.java (at line 36)\n" + 
			"	* @see X.X_priv\n" + 
			"	       ^^^^^^^^\n" + 
			"Javadoc: The type X.X_priv is not visible\n" + 
			"----------\n" + 
			"29. ERROR in Y.java (at line 37)\n" + 
			"	* @see X.Unknown\n" + 
			"	       ^^^^^^^^^\n" + 
			"Javadoc: X.Unknown cannot be resolved to a type\n" + 
			"----------\n" + 
			"30. ERROR in Y.java (at line 38)\n" + 
			"	* @see X#X(int)\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X(int) is not visible\n" + 
			"----------\n" + 
			"31. ERROR in Y.java (at line 39)\n" + 
			"	* @see X#X(String)\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X(String) is undefined\n" + 
			"----------\n" + 
			"32. ERROR in Y.java (at line 40)\n" + 
			"	* @see X#X()\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X() is deprecated\n" + 
			"----------\n" + 
			"33. ERROR in Y.java (at line 41)\n" + 
			"	* @see X#x_dep\n" + 
			"	         ^^^^^\n" + 
			"Javadoc: The field X.x_dep is deprecated\n" + 
			"----------\n" + 
			"34. ERROR in Y.java (at line 42)\n" + 
			"	* @see X#x_priv\n" + 
			"	         ^^^^^^\n" + 
			"Javadoc: The field x_priv is not visible\n" + 
			"----------\n" + 
			"35. ERROR in Y.java (at line 43)\n" + 
			"	* @see X#unknown\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: unknown cannot be resolved or is not a field\n" + 
			"----------\n" + 
			"36. ERROR in Y.java (at line 44)\n" + 
			"	* @see X#foo_dep()\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method foo_dep() from the type X is deprecated\n" + 
			"----------\n" + 
			"37. ERROR in Y.java (at line 45)\n" + 
			"	* @see X#foo_priv()\n" + 
			"	         ^^^^^^^^\n" + 
			"Javadoc: The method foo_priv() from the type X is not visible\n" + 
			"----------\n" + 
			"38. ERROR in Y.java (at line 46)\n" + 
			"	* @see X#foo_dep(String)\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method foo_dep() in the type X is not applicable for the arguments (String)\n" + 
			"----------\n" + 
			"39. ERROR in Y.java (at line 47)\n" + 
			"	* @see X#unknown()\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method unknown() is undefined for the type X\n" + 
			"----------\n",
		"40. ERROR in Y.java (at line 51)\n" + 
			"	* @see X.X_dep\n" + 
			"	       ^^^^^^^\n" + 
			"Javadoc: The type X.X_dep is deprecated\n" + 
			"----------\n" + 
			"41. ERROR in Y.java (at line 52)\n" + 
			"	* @see X.X_priv\n" + 
			"	       ^^^^^^^^\n" + 
			"Javadoc: The type X.X_priv is not visible\n" + 
			"----------\n" + 
			"42. ERROR in Y.java (at line 53)\n" + 
			"	* @see X.Unknown\n" + 
			"	       ^^^^^^^^^\n" + 
			"Javadoc: X.Unknown cannot be resolved to a type\n" + 
			"----------\n" + 
			"43. ERROR in Y.java (at line 54)\n" + 
			"	* @see X#X(int)\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X(int) is not visible\n" + 
			"----------\n" + 
			"44. ERROR in Y.java (at line 55)\n" + 
			"	* @see X#X(String)\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X(String) is undefined\n" + 
			"----------\n" + 
			"45. ERROR in Y.java (at line 56)\n" + 
			"	* @see X#X()\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X() is deprecated\n" + 
			"----------\n" + 
			"46. ERROR in Y.java (at line 57)\n" + 
			"	* @see X#x_dep\n" + 
			"	         ^^^^^\n" + 
			"Javadoc: The field X.x_dep is deprecated\n" + 
			"----------\n" + 
			"47. ERROR in Y.java (at line 58)\n" + 
			"	* @see X#x_priv\n" + 
			"	         ^^^^^^\n" + 
			"Javadoc: The field x_priv is not visible\n" + 
			"----------\n" + 
			"48. ERROR in Y.java (at line 59)\n" + 
			"	* @see X#unknown\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: unknown cannot be resolved or is not a field\n" + 
			"----------\n" + 
			"49. ERROR in Y.java (at line 60)\n" + 
			"	* @see X#foo_dep()\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method foo_dep() from the type X is deprecated\n" + 
			"----------\n" + 
			"50. ERROR in Y.java (at line 61)\n" + 
			"	* @see X#foo_priv()\n" + 
			"	         ^^^^^^^^\n" + 
			"Javadoc: The method foo_priv() from the type X is not visible\n" + 
			"----------\n" + 
			"51. ERROR in Y.java (at line 62)\n" + 
			"	* @see X#foo_dep(String)\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method foo_dep() in the type X is not applicable for the arguments (String)\n" + 
			"----------\n" + 
			"52. ERROR in Y.java (at line 63)\n" + 
			"	* @see X#unknown()\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method unknown() is undefined for the type X\n" + 
			"----------\n"
	};

	private static String[] resultForInvalidTagsReferencesMethodOrConstructor = {
//		"1. ERROR in Y.java (at line 4)\n" + 
//			"	* @param str\n" + 
//			"	         ^^^\n" + 
//			"Javadoc: Duplicate tag for parameter\n" + 
//			"----------\n" + 
//			"2. ERROR in Y.java (at line 5)\n" + 
//			"	* @param xxx\n" + 
//			"	         ^^^\n" + 
//			"Javadoc: Parameter xxx is not declared\n" + 
//			"----------\n" + 
//			"3. ERROR in Y.java (at line 7)\n" + 
//			"	* @throws IllegalArgumentException\n" + 
//			"	          ^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
//			"Javadoc: Duplicate tag for thrown exception\n" + 
//			"----------\n" + 
//			"4. ERROR in Y.java (at line 8)\n" + 
//			"	* @throws java.io.IOException\n" + 
//			"	          ^^^^^^^^^^^^^^^^^^^\n" + 
//			"Javadoc: Exception IOException is not declared\n" + 
//			"----------\n" + 
//			"5. ERROR in Y.java (at line 9)\n" + 
//			"	* @throws Unknown\n" + 
//			"	          ^^^^^^^\n" + 
//			"Javadoc: Unknown cannot be resolved to a type\n" + 
//			"----------\n" + 
//			"6. ERROR in Y.java (at line 10)\n" + 
//			"	* @see X.X_dep\n" + 
//			"	       ^^^^^^^\n" + 
//			"Javadoc: The type X.X_dep is deprecated\n" + 
//			"----------\n" + 
//			"7. ERROR in Y.java (at line 11)\n" + 
//			"	* @see X.X_priv\n" + 
//			"	       ^^^^^^^^\n" + 
//			"Javadoc: The type X.X_priv is not visible\n" + 
//			"----------\n" + 
//			"8. ERROR in Y.java (at line 12)\n" + 
//			"	* @see X.Unknown\n" + 
//			"	       ^^^^^^^^^\n" + 
//			"Javadoc: X.Unknown cannot be resolved to a type\n" + 
//			"----------\n" + 
//			"9. ERROR in Y.java (at line 13)\n" + 
//			"	* @see X#X(int)\n" + 
//			"	         ^\n" + 
//			"Javadoc: The constructor X(int) is not visible\n" + 
//			"----------\n" + 
//			"10. ERROR in Y.java (at line 14)\n" + 
//			"	* @see X#X(String)\n" + 
//			"	         ^\n" + 
//			"Javadoc: The constructor X(String) is undefined\n" + 
//			"----------\n" + 
//			"11. ERROR in Y.java (at line 15)\n" + 
//			"	* @see X#X()\n" + 
//			"	         ^\n" + 
//			"Javadoc: The constructor X() is deprecated\n" + 
//			"----------\n" + 
//			"12. ERROR in Y.java (at line 16)\n" + 
//			"	* @see X#x_dep\n" + 
//			"	         ^^^^^\n" + 
//			"Javadoc: The field X.x_dep is deprecated\n" + 
//			"----------\n" + 
//			"13. ERROR in Y.java (at line 17)\n" + 
//			"	* @see X#x_priv\n" + 
//			"	         ^^^^^^\n" + 
//			"Javadoc: The field x_priv is not visible\n" + 
//			"----------\n" + 
//			"14. ERROR in Y.java (at line 18)\n" + 
//			"	* @see X#unknown\n" + 
//			"	         ^^^^^^^\n" + 
//			"Javadoc: unknown cannot be resolved or is not a field\n" + 
//			"----------\n" + 
//			"15. ERROR in Y.java (at line 19)\n" + 
//			"	* @see X#foo_dep()\n" + 
//			"	         ^^^^^^^\n" + 
//			"Javadoc: The method foo_dep() from the type X is deprecated\n" + 
//			"----------\n" + 
//			"16. ERROR in Y.java (at line 20)\n" + 
//			"	* @see X#foo_priv()\n" + 
//			"	         ^^^^^^^^\n" + 
//			"Javadoc: The method foo_priv() from the type X is not visible\n" + 
//			"----------\n" + 
//			"17. ERROR in Y.java (at line 21)\n" + 
//			"	* @see X#foo_dep(String)\n" + 
//			"	         ^^^^^^^\n" + 
//			"Javadoc: The method foo_dep() in the type X is not applicable for the arguments (String)\n" + 
//			"----------\n" + 
//			"18. ERROR in Y.java (at line 22)\n" + 
//			"	* @see X#unknown()\n" + 
//			"	         ^^^^^^^\n" + 
//			"Javadoc: The method unknown() is undefined for the type X\n" + 
//			"----------\n",
//		"19. ERROR in Y.java (at line 27)\n" + 
//			"	* @param str\n" + 
//			"	         ^^^\n" + 
//			"Javadoc: Duplicate tag for parameter\n" + 
//			"----------\n" + 
//			"20. ERROR in Y.java (at line 28)\n" + 
//			"	* @param xxx\n" + 
//			"	         ^^^\n" + 
//			"Javadoc: Parameter xxx is not declared\n" + 
//			"----------\n" + 
//			"21. ERROR in Y.java (at line 30)\n" + 
//			"	* @throws IllegalArgumentException\n" + 
//			"	          ^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
//			"Javadoc: Duplicate tag for thrown exception\n" + 
//			"----------\n" + 
//			"22. ERROR in Y.java (at line 31)\n" + 
//			"	* @throws java.io.IOException\n" + 
//			"	          ^^^^^^^^^^^^^^^^^^^\n" + 
//			"Javadoc: Exception IOException is not declared\n" + 
//			"----------\n" + 
//			"23. ERROR in Y.java (at line 32)\n" + 
//			"	* @throws Unknown\n" + 
//			"	          ^^^^^^^\n" + 
//			"Javadoc: Unknown cannot be resolved to a type\n" + 
//			"----------\n" + 
//			"24. ERROR in Y.java (at line 33)\n" + 
//			"	* @see X.X_dep\n" + 
//			"	       ^^^^^^^\n" + 
//			"Javadoc: The type X.X_dep is deprecated\n" + 
//			"----------\n" + 
//			"25. ERROR in Y.java (at line 34)\n" + 
//			"	* @see X.X_priv\n" + 
//			"	       ^^^^^^^^\n" + 
//			"Javadoc: The type X.X_priv is not visible\n" + 
//			"----------\n" + 
//			"26. ERROR in Y.java (at line 35)\n" + 
//			"	* @see X.Unknown\n" + 
//			"	       ^^^^^^^^^\n" + 
//			"Javadoc: X.Unknown cannot be resolved to a type\n" + 
//			"----------\n" + 
//			"27. ERROR in Y.java (at line 36)\n" + 
//			"	* @see X#X(int)\n" + 
//			"	         ^\n" + 
//			"Javadoc: The constructor X(int) is not visible\n" + 
//			"----------\n" + 
//			"28. ERROR in Y.java (at line 37)\n" + 
//			"	* @see X#X(String)\n" + 
//			"	         ^\n" + 
//			"Javadoc: The constructor X(String) is undefined\n" + 
//			"----------\n" + 
//			"29. ERROR in Y.java (at line 38)\n" + 
//			"	* @see X#X()\n" + 
//			"	         ^\n" + 
//			"Javadoc: The constructor X() is deprecated\n" + 
//			"----------\n" + 
//			"30. ERROR in Y.java (at line 39)\n" + 
//			"	* @see X#x_dep\n" + 
//			"	         ^^^^^\n" + 
//			"Javadoc: The field X.x_dep is deprecated\n" + 
//			"----------\n" + 
//			"31. ERROR in Y.java (at line 40)\n" + 
//			"	* @see X#x_priv\n" + 
//			"	         ^^^^^^\n" + 
//			"Javadoc: The field x_priv is not visible\n" + 
//			"----------\n" + 
//			"32. ERROR in Y.java (at line 41)\n" + 
//			"	* @see X#unknown\n" + 
//			"	         ^^^^^^^\n" + 
//			"Javadoc: unknown cannot be resolved or is not a field\n" + 
//			"----------\n" + 
//			"33. ERROR in Y.java (at line 42)\n" + 
//			"	* @see X#foo_dep()\n" + 
//			"	         ^^^^^^^\n" + 
//			"Javadoc: The method foo_dep() from the type X is deprecated\n" + 
//			"----------\n" + 
//			"34. ERROR in Y.java (at line 43)\n" + 
//			"	* @see X#foo_priv()\n" + 
//			"	         ^^^^^^^^\n" + 
//			"Javadoc: The method foo_priv() from the type X is not visible\n" + 
//			"----------\n" + 
//			"35. ERROR in Y.java (at line 44)\n" + 
//			"	* @see X#foo_dep(String)\n" + 
//			"	         ^^^^^^^\n" + 
//			"Javadoc: The method foo_dep() in the type X is not applicable for the arguments (String)\n" + 
//			"----------\n" + 
//			"36. ERROR in Y.java (at line 45)\n" + 
//			"	* @see X#unknown()\n" + 
//			"	         ^^^^^^^\n" + 
//			"Javadoc: The method unknown() is undefined for the type X\n" + 
//			"----------\n",
//		"37. ERROR in Y.java (at line 50)\n" + 
//			"	* @param str\n" + 
//			"	         ^^^\n" + 
//			"Javadoc: Duplicate tag for parameter\n" + 
//			"----------\n" + 
//			"38. ERROR in Y.java (at line 51)\n" + 
//			"	* @param xxx\n" + 
//			"	         ^^^\n" + 
//			"Javadoc: Parameter xxx is not declared\n" + 
//			"----------\n" + 
//			"39. ERROR in Y.java (at line 53)\n" + 
//			"	* @throws IllegalArgumentException\n" + 
//			"	          ^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
//			"Javadoc: Duplicate tag for thrown exception\n" + 
//			"----------\n" + 
//			"40. ERROR in Y.java (at line 54)\n" + 
//			"	* @throws java.io.IOException\n" + 
//			"	          ^^^^^^^^^^^^^^^^^^^\n" + 
//			"Javadoc: Exception IOException is not declared\n" + 
//			"----------\n" + 
//			"41. ERROR in Y.java (at line 55)\n" + 
//			"	* @throws Unknown\n" + 
//			"	          ^^^^^^^\n" + 
//			"Javadoc: Unknown cannot be resolved to a type\n" + 
//			"----------\n" + 
//			"42. ERROR in Y.java (at line 56)\n" + 
//			"	* @see X.X_dep\n" + 
//			"	       ^^^^^^^\n" + 
//			"Javadoc: The type X.X_dep is deprecated\n" + 
//			"----------\n" + 
//			"43. ERROR in Y.java (at line 57)\n" + 
//			"	* @see X.X_priv\n" + 
//			"	       ^^^^^^^^\n" + 
//			"Javadoc: The type X.X_priv is not visible\n" + 
//			"----------\n" + 
//			"44. ERROR in Y.java (at line 58)\n" + 
//			"	* @see X.Unknown\n" + 
//			"	       ^^^^^^^^^\n" + 
//			"Javadoc: X.Unknown cannot be resolved to a type\n" + 
//			"----------\n" + 
//			"45. ERROR in Y.java (at line 59)\n" + 
//			"	* @see X#X(int)\n" + 
//			"	         ^\n" + 
//			"Javadoc: The constructor X(int) is not visible\n" + 
//			"----------\n" + 
//			"46. ERROR in Y.java (at line 60)\n" + 
//			"	* @see X#X(String)\n" + 
//			"	         ^\n" + 
//			"Javadoc: The constructor X(String) is undefined\n" + 
//			"----------\n" + 
//			"47. ERROR in Y.java (at line 61)\n" + 
//			"	* @see X#X()\n" + 
//			"	         ^\n" + 
//			"Javadoc: The constructor X() is deprecated\n" + 
//			"----------\n" + 
//			"48. ERROR in Y.java (at line 62)\n" + 
//			"	* @see X#x_dep\n" + 
//			"	         ^^^^^\n" + 
//			"Javadoc: The field X.x_dep is deprecated\n" + 
//			"----------\n" + 
//			"49. ERROR in Y.java (at line 63)\n" + 
//			"	* @see X#x_priv\n" + 
//			"	         ^^^^^^\n" + 
//			"Javadoc: The field x_priv is not visible\n" + 
//			"----------\n" + 
//			"50. ERROR in Y.java (at line 64)\n" + 
//			"	* @see X#unknown\n" + 
//			"	         ^^^^^^^\n" + 
//			"Javadoc: unknown cannot be resolved or is not a field\n" + 
//			"----------\n" + 
//			"51. ERROR in Y.java (at line 65)\n" + 
//			"	* @see X#foo_dep()\n" + 
//			"	         ^^^^^^^\n" + 
//			"Javadoc: The method foo_dep() from the type X is deprecated\n" + 
//			"----------\n" + 
//			"52. ERROR in Y.java (at line 66)\n" + 
//			"	* @see X#foo_priv()\n" + 
//			"	         ^^^^^^^^\n" + 
//			"Javadoc: The method foo_priv() from the type X is not visible\n" + 
//			"----------\n" + 
//			"53. ERROR in Y.java (at line 67)\n" + 
//			"	* @see X#foo_dep(String)\n" + 
//			"	         ^^^^^^^\n" + 
//			"Javadoc: The method foo_dep() in the type X is not applicable for the arguments (String)\n" + 
//			"----------\n" + 
//			"54. ERROR in Y.java (at line 68)\n" + 
//			"	* @see X#unknown()\n" + 
//			"	         ^^^^^^^\n" + 
//			"Javadoc: The method unknown() is undefined for the type X\n" + 
//			"----------\n",
//		"55. ERROR in Y.java (at line 73)\n" + 
//			"	* @param str\n" + 
//			"	         ^^^\n" + 
//			"Javadoc: Duplicate tag for parameter\n" + 
//			"----------\n" + 
//			"56. ERROR in Y.java (at line 74)\n" + 
//			"	* @param xxx\n" + 
//			"	         ^^^\n" + 
//			"Javadoc: Parameter xxx is not declared\n" + 
//			"----------\n" + 
//			"57. ERROR in Y.java (at line 76)\n" + 
//			"	* @throws IllegalArgumentException\n" + 
//			"	          ^^^^^^^^^^^^^^^^^^^^^^^^\n" + 
//			"Javadoc: Duplicate tag for thrown exception\n" + 
//			"----------\n" + 
//			"58. ERROR in Y.java (at line 77)\n" + 
//			"	* @throws java.io.IOException\n" + 
//			"	          ^^^^^^^^^^^^^^^^^^^\n" + 
//			"Javadoc: Exception IOException is not declared\n" + 
//			"----------\n" + 
//			"59. ERROR in Y.java (at line 78)\n" + 
//			"	* @throws Unknown\n" + 
//			"	          ^^^^^^^\n" + 
//			"Javadoc: Unknown cannot be resolved to a type\n" + 
//			"----------\n" + 
//			"60. ERROR in Y.java (at line 79)\n" + 
//			"	* @see X.X_dep\n" + 
//			"	       ^^^^^^^\n" + 
//			"Javadoc: The type X.X_dep is deprecated\n" + 
//			"----------\n" + 
//			"61. ERROR in Y.java (at line 80)\n" + 
//			"	* @see X.X_priv\n" + 
//			"	       ^^^^^^^^\n" + 
//			"Javadoc: The type X.X_priv is not visible\n" + 
//			"----------\n" + 
//			"62. ERROR in Y.java (at line 81)\n" + 
//			"	* @see X.Unknown\n" + 
//			"	       ^^^^^^^^^\n" + 
//			"Javadoc: X.Unknown cannot be resolved to a type\n" + 
//			"----------\n" + 
//			"63. ERROR in Y.java (at line 82)\n" + 
//			"	* @see X#X(int)\n" + 
//			"	         ^\n" + 
//			"Javadoc: The constructor X(int) is not visible\n" + 
//			"----------\n" + 
//			"64. ERROR in Y.java (at line 83)\n" + 
//			"	* @see X#X(String)\n" + 
//			"	         ^\n" + 
//			"Javadoc: The constructor X(String) is undefined\n" + 
//			"----------\n" + 
//			"65. ERROR in Y.java (at line 84)\n" + 
//			"	* @see X#X()\n" + 
//			"	         ^\n" + 
//			"Javadoc: The constructor X() is deprecated\n" + 
//			"----------\n" + 
//			"66. ERROR in Y.java (at line 85)\n" + 
//			"	* @see X#x_dep\n" + 
//			"	         ^^^^^\n" + 
//			"Javadoc: The field X.x_dep is deprecated\n" + 
//			"----------\n" + 
//			"67. ERROR in Y.java (at line 86)\n" + 
//			"	* @see X#x_priv\n" + 
//			"	         ^^^^^^\n" + 
//			"Javadoc: The field x_priv is not visible\n" + 
//			"----------\n" + 
//			"68. ERROR in Y.java (at line 87)\n" + 
//			"	* @see X#unknown\n" + 
//			"	         ^^^^^^^\n" + 
//			"Javadoc: unknown cannot be resolved or is not a field\n" + 
//			"----------\n" + 
//			"69. ERROR in Y.java (at line 88)\n" + 
//			"	* @see X#foo_dep()\n" + 
//			"	         ^^^^^^^\n" + 
//			"Javadoc: The method foo_dep() from the type X is deprecated\n" + 
//			"----------\n" + 
//			"70. ERROR in Y.java (at line 89)\n" + 
//			"	* @see X#foo_priv()\n" + 
//			"	         ^^^^^^^^\n" + 
//			"Javadoc: The method foo_priv() from the type X is not visible\n" + 
//			"----------\n" + 
//			"71. ERROR in Y.java (at line 90)\n" + 
//			"	* @see X#foo_dep(String)\n" + 
//			"	         ^^^^^^^\n" + 
//			"Javadoc: The method foo_dep() in the type X is not applicable for the arguments (String)\n" + 
//			"----------\n" + 
//			"72. ERROR in Y.java (at line 91)\n" + 
//			"	* @see X#unknown()\n" + 
//			"	         ^^^^^^^\n" + 
//			"Javadoc: The method unknown() is undefined for the type X\n" + 
//			"----------\n"
		"1. ERROR in Y.java (at line 4)\n" + 
			"	* @param str\n" + 
			"	         ^^^\n" + 
			"Javadoc: Duplicate tag for parameter\n" + 
			"----------\n" + 
			"2. ERROR in Y.java (at line 5)\n" + 
			"	* @param xxx\n" + 
			"	         ^^^\n" + 
			"Javadoc: Parameter xxx is not declared\n" + 
			"----------\n" + 
			"3. ERROR in Y.java (at line 8)\n" + 
			"	* @throws java.io.IOException\n" + 
			"	          ^^^^^^^^^^^^^^^^^^^\n" + 
			"Javadoc: Exception IOException is not declared\n" + 
			"----------\n" + 
			"4. ERROR in Y.java (at line 9)\n" + 
			"	* @throws Unknown\n" + 
			"	          ^^^^^^^\n" + 
			"Javadoc: Unknown cannot be resolved to a type\n" + 
			"----------\n" + 
			"5. ERROR in Y.java (at line 10)\n" + 
			"	* @see X.X_dep\n" + 
			"	       ^^^^^^^\n" + 
			"Javadoc: The type X.X_dep is deprecated\n" + 
			"----------\n" + 
			"6. ERROR in Y.java (at line 11)\n" + 
			"	* @see X.X_priv\n" + 
			"	       ^^^^^^^^\n" + 
			"Javadoc: The type X.X_priv is not visible\n" + 
			"----------\n" + 
			"7. ERROR in Y.java (at line 12)\n" + 
			"	* @see X.Unknown\n" + 
			"	       ^^^^^^^^^\n" + 
			"Javadoc: X.Unknown cannot be resolved to a type\n" + 
			"----------\n" + 
			"8. ERROR in Y.java (at line 13)\n" + 
			"	* @see X#X(int)\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X(int) is not visible\n" + 
			"----------\n" + 
			"9. ERROR in Y.java (at line 14)\n" + 
			"	* @see X#X(String)\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X(String) is undefined\n" + 
			"----------\n" + 
			"10. ERROR in Y.java (at line 15)\n" + 
			"	* @see X#X()\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X() is deprecated\n" + 
			"----------\n" + 
			"11. ERROR in Y.java (at line 16)\n" + 
			"	* @see X#x_dep\n" + 
			"	         ^^^^^\n" + 
			"Javadoc: The field X.x_dep is deprecated\n" + 
			"----------\n" + 
			"12. ERROR in Y.java (at line 17)\n" + 
			"	* @see X#x_priv\n" + 
			"	         ^^^^^^\n" + 
			"Javadoc: The field x_priv is not visible\n" + 
			"----------\n" + 
			"13. ERROR in Y.java (at line 18)\n" + 
			"	* @see X#unknown\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: unknown cannot be resolved or is not a field\n" + 
			"----------\n" + 
			"14. ERROR in Y.java (at line 19)\n" + 
			"	* @see X#foo_dep()\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method foo_dep() from the type X is deprecated\n" + 
			"----------\n" + 
			"15. ERROR in Y.java (at line 20)\n" + 
			"	* @see X#foo_priv()\n" + 
			"	         ^^^^^^^^\n" + 
			"Javadoc: The method foo_priv() from the type X is not visible\n" + 
			"----------\n" + 
			"16. ERROR in Y.java (at line 21)\n" + 
			"	* @see X#foo_dep(String)\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method foo_dep() in the type X is not applicable for the arguments (String)\n" + 
			"----------\n" + 
			"17. ERROR in Y.java (at line 22)\n" + 
			"	* @see X#unknown()\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method unknown() is undefined for the type X\n" + 
			"----------\n",
		"18. ERROR in Y.java (at line 27)\n" + 
			"	* @param str\n" + 
			"	         ^^^\n" + 
			"Javadoc: Duplicate tag for parameter\n" + 
			"----------\n" + 
			"19. ERROR in Y.java (at line 28)\n" + 
			"	* @param xxx\n" + 
			"	         ^^^\n" + 
			"Javadoc: Parameter xxx is not declared\n" + 
			"----------\n" + 
			"20. ERROR in Y.java (at line 31)\n" + 
			"	* @throws java.io.IOException\n" + 
			"	          ^^^^^^^^^^^^^^^^^^^\n" + 
			"Javadoc: Exception IOException is not declared\n" + 
			"----------\n" + 
			"21. ERROR in Y.java (at line 32)\n" + 
			"	* @throws Unknown\n" + 
			"	          ^^^^^^^\n" + 
			"Javadoc: Unknown cannot be resolved to a type\n" + 
			"----------\n" + 
			"22. ERROR in Y.java (at line 33)\n" + 
			"	* @see X.X_dep\n" + 
			"	       ^^^^^^^\n" + 
			"Javadoc: The type X.X_dep is deprecated\n" + 
			"----------\n" + 
			"23. ERROR in Y.java (at line 34)\n" + 
			"	* @see X.X_priv\n" + 
			"	       ^^^^^^^^\n" + 
			"Javadoc: The type X.X_priv is not visible\n" + 
			"----------\n" + 
			"24. ERROR in Y.java (at line 35)\n" + 
			"	* @see X.Unknown\n" + 
			"	       ^^^^^^^^^\n" + 
			"Javadoc: X.Unknown cannot be resolved to a type\n" + 
			"----------\n" + 
			"25. ERROR in Y.java (at line 36)\n" + 
			"	* @see X#X(int)\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X(int) is not visible\n" + 
			"----------\n" + 
			"26. ERROR in Y.java (at line 37)\n" + 
			"	* @see X#X(String)\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X(String) is undefined\n" + 
			"----------\n" + 
			"27. ERROR in Y.java (at line 38)\n" + 
			"	* @see X#X()\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X() is deprecated\n" + 
			"----------\n" + 
			"28. ERROR in Y.java (at line 39)\n" + 
			"	* @see X#x_dep\n" + 
			"	         ^^^^^\n" + 
			"Javadoc: The field X.x_dep is deprecated\n" + 
			"----------\n" + 
			"29. ERROR in Y.java (at line 40)\n" + 
			"	* @see X#x_priv\n" + 
			"	         ^^^^^^\n" + 
			"Javadoc: The field x_priv is not visible\n" + 
			"----------\n" + 
			"30. ERROR in Y.java (at line 41)\n" + 
			"	* @see X#unknown\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: unknown cannot be resolved or is not a field\n" + 
			"----------\n" + 
			"31. ERROR in Y.java (at line 42)\n" + 
			"	* @see X#foo_dep()\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method foo_dep() from the type X is deprecated\n" + 
			"----------\n" + 
			"32. ERROR in Y.java (at line 43)\n" + 
			"	* @see X#foo_priv()\n" + 
			"	         ^^^^^^^^\n" + 
			"Javadoc: The method foo_priv() from the type X is not visible\n" + 
			"----------\n" + 
			"33. ERROR in Y.java (at line 44)\n" + 
			"	* @see X#foo_dep(String)\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method foo_dep() in the type X is not applicable for the arguments (String)\n" + 
			"----------\n" + 
			"34. ERROR in Y.java (at line 45)\n" + 
			"	* @see X#unknown()\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method unknown() is undefined for the type X\n" + 
			"----------\n",
		"35. ERROR in Y.java (at line 50)\n" + 
			"	* @param str\n" + 
			"	         ^^^\n" + 
			"Javadoc: Duplicate tag for parameter\n" + 
			"----------\n" + 
			"36. ERROR in Y.java (at line 51)\n" + 
			"	* @param xxx\n" + 
			"	         ^^^\n" + 
			"Javadoc: Parameter xxx is not declared\n" + 
			"----------\n" + 
			"37. ERROR in Y.java (at line 54)\n" + 
			"	* @throws java.io.IOException\n" + 
			"	          ^^^^^^^^^^^^^^^^^^^\n" + 
			"Javadoc: Exception IOException is not declared\n" + 
			"----------\n" + 
			"38. ERROR in Y.java (at line 55)\n" + 
			"	* @throws Unknown\n" + 
			"	          ^^^^^^^\n" + 
			"Javadoc: Unknown cannot be resolved to a type\n" + 
			"----------\n" + 
			"39. ERROR in Y.java (at line 56)\n" + 
			"	* @see X.X_dep\n" + 
			"	       ^^^^^^^\n" + 
			"Javadoc: The type X.X_dep is deprecated\n" + 
			"----------\n" + 
			"40. ERROR in Y.java (at line 57)\n" + 
			"	* @see X.X_priv\n" + 
			"	       ^^^^^^^^\n" + 
			"Javadoc: The type X.X_priv is not visible\n" + 
			"----------\n" + 
			"41. ERROR in Y.java (at line 58)\n" + 
			"	* @see X.Unknown\n" + 
			"	       ^^^^^^^^^\n" + 
			"Javadoc: X.Unknown cannot be resolved to a type\n" + 
			"----------\n" + 
			"42. ERROR in Y.java (at line 59)\n" + 
			"	* @see X#X(int)\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X(int) is not visible\n" + 
			"----------\n" + 
			"43. ERROR in Y.java (at line 60)\n" + 
			"	* @see X#X(String)\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X(String) is undefined\n" + 
			"----------\n" + 
			"44. ERROR in Y.java (at line 61)\n" + 
			"	* @see X#X()\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X() is deprecated\n" + 
			"----------\n" + 
			"45. ERROR in Y.java (at line 62)\n" + 
			"	* @see X#x_dep\n" + 
			"	         ^^^^^\n" + 
			"Javadoc: The field X.x_dep is deprecated\n" + 
			"----------\n" + 
			"46. ERROR in Y.java (at line 63)\n" + 
			"	* @see X#x_priv\n" + 
			"	         ^^^^^^\n" + 
			"Javadoc: The field x_priv is not visible\n" + 
			"----------\n" + 
			"47. ERROR in Y.java (at line 64)\n" + 
			"	* @see X#unknown\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: unknown cannot be resolved or is not a field\n" + 
			"----------\n" + 
			"48. ERROR in Y.java (at line 65)\n" + 
			"	* @see X#foo_dep()\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method foo_dep() from the type X is deprecated\n" + 
			"----------\n" + 
			"49. ERROR in Y.java (at line 66)\n" + 
			"	* @see X#foo_priv()\n" + 
			"	         ^^^^^^^^\n" + 
			"Javadoc: The method foo_priv() from the type X is not visible\n" + 
			"----------\n" + 
			"50. ERROR in Y.java (at line 67)\n" + 
			"	* @see X#foo_dep(String)\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method foo_dep() in the type X is not applicable for the arguments (String)\n" + 
			"----------\n" + 
			"51. ERROR in Y.java (at line 68)\n" + 
			"	* @see X#unknown()\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method unknown() is undefined for the type X\n" + 
			"----------\n",
		"52. ERROR in Y.java (at line 73)\n" + 
			"	* @param str\n" + 
			"	         ^^^\n" + 
			"Javadoc: Duplicate tag for parameter\n" + 
			"----------\n" + 
			"53. ERROR in Y.java (at line 74)\n" + 
			"	* @param xxx\n" + 
			"	         ^^^\n" + 
			"Javadoc: Parameter xxx is not declared\n" + 
			"----------\n" + 
			"54. ERROR in Y.java (at line 77)\n" + 
			"	* @throws java.io.IOException\n" + 
			"	          ^^^^^^^^^^^^^^^^^^^\n" + 
			"Javadoc: Exception IOException is not declared\n" + 
			"----------\n" + 
			"55. ERROR in Y.java (at line 78)\n" + 
			"	* @throws Unknown\n" + 
			"	          ^^^^^^^\n" + 
			"Javadoc: Unknown cannot be resolved to a type\n" + 
			"----------\n" + 
			"56. ERROR in Y.java (at line 79)\n" + 
			"	* @see X.X_dep\n" + 
			"	       ^^^^^^^\n" + 
			"Javadoc: The type X.X_dep is deprecated\n" + 
			"----------\n" + 
			"57. ERROR in Y.java (at line 80)\n" + 
			"	* @see X.X_priv\n" + 
			"	       ^^^^^^^^\n" + 
			"Javadoc: The type X.X_priv is not visible\n" + 
			"----------\n" + 
			"58. ERROR in Y.java (at line 81)\n" + 
			"	* @see X.Unknown\n" + 
			"	       ^^^^^^^^^\n" + 
			"Javadoc: X.Unknown cannot be resolved to a type\n" + 
			"----------\n" + 
			"59. ERROR in Y.java (at line 82)\n" + 
			"	* @see X#X(int)\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X(int) is not visible\n" + 
			"----------\n" + 
			"60. ERROR in Y.java (at line 83)\n" + 
			"	* @see X#X(String)\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X(String) is undefined\n" + 
			"----------\n" + 
			"61. ERROR in Y.java (at line 84)\n" + 
			"	* @see X#X()\n" + 
			"	         ^\n" + 
			"Javadoc: The constructor X() is deprecated\n" + 
			"----------\n" + 
			"62. ERROR in Y.java (at line 85)\n" + 
			"	* @see X#x_dep\n" + 
			"	         ^^^^^\n" + 
			"Javadoc: The field X.x_dep is deprecated\n" + 
			"----------\n" + 
			"63. ERROR in Y.java (at line 86)\n" + 
			"	* @see X#x_priv\n" + 
			"	         ^^^^^^\n" + 
			"Javadoc: The field x_priv is not visible\n" + 
			"----------\n" + 
			"64. ERROR in Y.java (at line 87)\n" + 
			"	* @see X#unknown\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: unknown cannot be resolved or is not a field\n" + 
			"----------\n" + 
			"65. ERROR in Y.java (at line 88)\n" + 
			"	* @see X#foo_dep()\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method foo_dep() from the type X is deprecated\n" + 
			"----------\n" + 
			"66. ERROR in Y.java (at line 89)\n" + 
			"	* @see X#foo_priv()\n" + 
			"	         ^^^^^^^^\n" + 
			"Javadoc: The method foo_priv() from the type X is not visible\n" + 
			"----------\n" + 
			"67. ERROR in Y.java (at line 90)\n" + 
			"	* @see X#foo_dep(String)\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method foo_dep() in the type X is not applicable for the arguments (String)\n" + 
			"----------\n" + 
			"68. ERROR in Y.java (at line 91)\n" + 
			"	* @see X#unknown()\n" + 
			"	         ^^^^^^^\n" + 
			"Javadoc: The method unknown() is undefined for the type X\n" + 
			"----------\n"
	};

	private String resultForInvalidTagsReferencesClassOrField(int visibility) {
		String result = "----------\n";
		for (int i=0; i<=visibility; i++) {
			result += resultForInvalidTagsReferencesClassOrField[i];
		}
		return result;
	}

	private String resultForInvalidTagsReferencesMethodOrConstructor(int visibility) {
		String result = "----------\n";
		for (int i=0; i<=visibility; i++) {
			result += resultForInvalidTagsReferencesMethodOrConstructor[i];
		}
		return result;
	}
	
	private static String[] X_ResultForMissingTags = {
		"1. ERROR in X.java (at line 8)\n" + 
			"	public X(int i) {}\n" + 
			"	             ^\n" + 
			"Javadoc: Missing tag for parameter i\n" + 
			"----------\n" + 
			"2. ERROR in X.java (at line 10)\n" + 
			"	public int publicMethod(long l) { return 0;}\n" + 
			"	       ^^^\n" + 
			"Javadoc: Missing tag for return type\n" + 
			"----------\n" + 
			"3. ERROR in X.java (at line 10)\n" + 
			"	public int publicMethod(long l) { return 0;}\n" + 
			"	                             ^\n" + 
			"Javadoc: Missing tag for parameter l\n" + 
			"----------\n",
		"4. ERROR in X.java (at line 17)\n" + 
			"	protected X(long l) {}\n" + 
			"	                 ^\n" + 
			"Javadoc: Missing tag for parameter l\n" + 
			"----------\n" + 
			"5. ERROR in X.java (at line 19)\n" + 
			"	protected int protectedMethod(long l) { return 0; }\n" + 
			"	          ^^^\n" + 
			"Javadoc: Missing tag for return type\n" + 
			"----------\n" + 
			"6. ERROR in X.java (at line 19)\n" + 
			"	protected int protectedMethod(long l) { return 0; }\n" + 
			"	                                   ^\n" + 
			"Javadoc: Missing tag for parameter l\n" + 
			"----------\n",
		"7. ERROR in X.java (at line 26)\n" + 
			"	X(float f) {}\n" + 
			"	        ^\n" + 
			"Javadoc: Missing tag for parameter f\n" + 
			"----------\n" + 
			"8. ERROR in X.java (at line 28)\n" + 
			"	int packageMethod(long l) { return 0;}\n" + 
			"	^^^\n" + 
			"Javadoc: Missing tag for return type\n" + 
			"----------\n" + 
			"9. ERROR in X.java (at line 28)\n" + 
			"	int packageMethod(long l) { return 0;}\n" + 
			"	                       ^\n" + 
			"Javadoc: Missing tag for parameter l\n" + 
			"----------\n",
		"10. ERROR in X.java (at line 35)\n" + 
			"	private X(double d) {}\n" + 
			"	                 ^\n" + 
			"Javadoc: Missing tag for parameter d\n" + 
			"----------\n" + 
			"11. ERROR in X.java (at line 37)\n" + 
			"	private int privateMethod(long l) { return 0;}\n" + 
			"	        ^^^\n" + 
			"Javadoc: Missing tag for return type\n" + 
			"----------\n" + 
			"12. ERROR in X.java (at line 37)\n" + 
			"	private int privateMethod(long l) { return 0;}\n" + 
			"	                               ^\n" + 
			"Javadoc: Missing tag for parameter l\n" + 
			"----------\n"
	};
	private static String[] Y_ResultForMissingTags = {
		"1. ERROR in Y.java (at line 6)\n" + 
			"	public int publicMethod(long l) { return 0;}\n" + 
			"	       ^^^\n" + 
			"Javadoc: Missing tag for return type\n" + 
			"----------\n" + 
			"2. ERROR in Y.java (at line 6)\n" + 
			"	public int publicMethod(long l) { return 0;}\n" + 
			"	                             ^\n" + 
			"Javadoc: Missing tag for parameter l\n" + 
			"----------\n",
		"3. ERROR in Y.java (at line 8)\n" + 
			"	protected int protectedMethod(long l) { return 0;}\n" + 
			"	          ^^^\n" + 
			"Javadoc: Missing tag for return type\n" + 
			"----------\n" + 
			"4. ERROR in Y.java (at line 8)\n" + 
			"	protected int protectedMethod(long l) { return 0;}\n" + 
			"	                                   ^\n" + 
			"Javadoc: Missing tag for parameter l\n" + 
			"----------\n",
		"5. ERROR in Y.java (at line 10)\n" + 
			"	int packageMethod(long l) { return 0;}\n" + 
			"	^^^\n" + 
			"Javadoc: Missing tag for return type\n" + 
			"----------\n" + 
			"6. ERROR in Y.java (at line 10)\n" + 
			"	int packageMethod(long l) { return 0;}\n" + 
			"	                       ^\n" + 
			"Javadoc: Missing tag for parameter l\n" + 
			"----------\n",
		"7. ERROR in Y.java (at line 12)\n" + 
			"	private int privateMethod(long l) { return 0;}\n" + 
			"	        ^^^\n" + 
			"Javadoc: Missing tag for return type\n" + 
			"----------\n" + 
			"8. ERROR in Y.java (at line 12)\n" + 
			"	private int privateMethod(long l) { return 0;}\n" + 
			"	                               ^\n" + 
			"Javadoc: Missing tag for parameter l\n" + 
			"----------\n",
		"----------\n" + 
			"1. ERROR in Y.java (at line 12)\n" + 
			"	private int privateMethod(long l) { return 0;}\n" + 
			"	        ^^^\n" + 
			"Javadoc: Missing tag for return type\n" + 
			"----------\n" + 
			"2. ERROR in Y.java (at line 12)\n" + 
			"	private int privateMethod(long l) { return 0;}\n" + 
			"	                               ^\n" + 
			"Javadoc: Missing tag for parameter l\n" + 
			"----------\n"
	};

	private String resultForMissingTags(int visibility) {
		StringBuffer result = new StringBuffer("----------\n");
		for (int i=0; i<=visibility; i++) {
			result.append(X_ResultForMissingTags[i]);
		}
		if (CompilerOptions.ENABLED.equals(reportMissingJavadocTagsOverriding)) {
			result.append("----------\n");
			result.append(Y_ResultForMissingTags[PUBLIC_VISIBILITY]);
			if (visibility >= PROTECTED_VISIBILITY) {
				result.append(Y_ResultForMissingTags[PROTECTED_VISIBILITY]);
				if (visibility >= DEFAULT_VISIBILITY) {
					result.append(Y_ResultForMissingTags[DEFAULT_VISIBILITY]);
					if (visibility == PRIVATE_VISIBILITY) {
						result.append(Y_ResultForMissingTags[PRIVATE_VISIBILITY]);
					}
				}
			}
		}
		// Private level is always reported for as it never overrides...
		else if (visibility == PRIVATE_VISIBILITY) {
			result.append(Y_ResultForMissingTags[PRIVATE_VISIBILITY+1]);
		}
		return result.toString();
	}
	
	private static String[] X_ResultForMissingComments = {
		"1. ERROR in X.java (at line 4)\n" + 
			"	public class PublicClass {}\n" + 
			"	             ^^^^^^^^^^^\n" + 
			"Javadoc: Missing comment for public declaration\n" + 
			"----------\n" + 
			"2. ERROR in X.java (at line 5)\n" + 
			"	public int publicField;\n" + 
			"	           ^^^^^^^^^^^\n" + 
			"Javadoc: Missing comment for public declaration\n" + 
			"----------\n" + 
			"3. ERROR in X.java (at line 6)\n" + 
			"	public X(int i) {}\n" + 
			"	       ^^^^^^^^\n" + 
			"Javadoc: Missing comment for public declaration\n" + 
			"----------\n" + 
			"4. ERROR in X.java (at line 7)\n" + 
			"	public int publicMethod(long l) { return 0;}\n" + 
			"	           ^^^^^^^^^^^^^^^^^^^^\n" + 
			"Javadoc: Missing comment for public declaration\n" + 
			"----------\n",
		"5. ERROR in X.java (at line 9)\n" + 
			"	protected class ProtectedClass {}\n" + 
			"	                ^^^^^^^^^^^^^^\n" + 
			"Javadoc: Missing comment for protected declaration\n" + 
			"----------\n" + 
			"6. ERROR in X.java (at line 10)\n" + 
			"	protected int protectedField;\n" + 
			"	              ^^^^^^^^^^^^^^\n" + 
			"Javadoc: Missing comment for protected declaration\n" + 
			"----------\n" + 
			"7. ERROR in X.java (at line 11)\n" + 
			"	protected X(long l) {}\n" + 
			"	          ^^^^^^^^^\n" + 
			"Javadoc: Missing comment for protected declaration\n" + 
			"----------\n" + 
			"8. ERROR in X.java (at line 12)\n" + 
			"	protected int protectedMethod(long l) { return 0; }\n" + 
			"	              ^^^^^^^^^^^^^^^^^^^^^^^\n" + 
			"Javadoc: Missing comment for protected declaration\n" + 
			"----------\n",
		"9. ERROR in X.java (at line 14)\n" + 
			"	class PackageClass {}\n" + 
			"	      ^^^^^^^^^^^^\n" + 
			"Javadoc: Missing comment for default declaration\n" + 
			"----------\n" + 
			"10. ERROR in X.java (at line 15)\n" + 
			"	int packageField;\n" + 
			"	    ^^^^^^^^^^^^\n" + 
			"Javadoc: Missing comment for default declaration\n" + 
			"----------\n" + 
			"11. ERROR in X.java (at line 16)\n" + 
			"	X(float f) {}\n" + 
			"	^^^^^^^^^^\n" + 
			"Javadoc: Missing comment for default declaration\n" + 
			"----------\n" + 
			"12. ERROR in X.java (at line 17)\n" + 
			"	int packageMethod(long l) { return 0;}\n" + 
			"	    ^^^^^^^^^^^^^^^^^^^^^\n" + 
			"Javadoc: Missing comment for default declaration\n" + 
			"----------\n",
		"13. ERROR in X.java (at line 19)\n" + 
			"	private class PrivateClass {}\n" + 
			"	              ^^^^^^^^^^^^\n" + 
			"Javadoc: Missing comment for private declaration\n" + 
			"----------\n" + 
			"14. ERROR in X.java (at line 20)\n" + 
			"	private int privateField;\n" + 
			"	            ^^^^^^^^^^^^\n" + 
			"Javadoc: Missing comment for private declaration\n" + 
			"----------\n" + 
			"15. ERROR in X.java (at line 21)\n" + 
			"	private X(double d) {}\n" + 
			"	        ^^^^^^^^^^^\n" + 
			"Javadoc: Missing comment for private declaration\n" + 
			"----------\n" + 
			"16. ERROR in X.java (at line 22)\n" + 
			"	private int privateMethod(long l) { return 0;}\n" + 
			"	            ^^^^^^^^^^^^^^^^^^^^^\n" + 
			"Javadoc: Missing comment for private declaration\n" + 
			"----------\n"
	};
	private static String[] Y_ResultForMissingComments = {
		"1. ERROR in Y.java (at line 5)\n" + 
			"	public int publicMethod(long l) { return 0;}\n" + 
			"	           ^^^^^^^^^^^^^^^^^^^^\n" + 
			"Javadoc: Missing comment for public declaration\n" + 
			"----------\n",
		"2. ERROR in Y.java (at line 6)\n" + 
			"	protected int protectedMethod(long l) { return 0;}\n" + 
			"	              ^^^^^^^^^^^^^^^^^^^^^^^\n" + 
			"Javadoc: Missing comment for protected declaration\n" + 
			"----------\n",
		"3. ERROR in Y.java (at line 7)\n" + 
			"	int packageMethod(long l) { return 0;}\n" + 
			"	    ^^^^^^^^^^^^^^^^^^^^^\n" + 
			"Javadoc: Missing comment for default declaration\n" + 
			"----------\n",
		"4. ERROR in Y.java (at line 8)\n" + 
			"	private int privateMethod(long l) { return 0;}\n" + 
			"	            ^^^^^^^^^^^^^^^^^^^^^\n" + 
			"Javadoc: Missing comment for private declaration\n" + 
			"----------\n",
		"----------\n" + 
			"1. ERROR in Y.java (at line 8)\n" + 
			"	private int privateMethod(long l) { return 0;}\n" + 
			"	            ^^^^^^^^^^^^^^^^^^^^^\n" + 
			"Javadoc: Missing comment for private declaration\n" + 
			"----------\n"			
	};

	private String resultForMissingComments(int visibility) {
		StringBuffer result = new StringBuffer("----------\n");
		for (int i=0; i<=visibility; i++) {
			result.append(X_ResultForMissingComments[i]);
		}
		if (CompilerOptions.ENABLED.equals(reportMissingJavadocCommentsOverriding)) {
			result.append("----------\n");
			result.append(Y_ResultForMissingComments[PUBLIC_VISIBILITY]);
			if (visibility >= PROTECTED_VISIBILITY) {
				result.append(Y_ResultForMissingComments[PROTECTED_VISIBILITY]);
				if (visibility >= DEFAULT_VISIBILITY) {
					result.append(Y_ResultForMissingComments[DEFAULT_VISIBILITY]);
					if (visibility == PRIVATE_VISIBILITY) {
						result.append(Y_ResultForMissingComments[PRIVATE_VISIBILITY]);
					}
				}
			}
		}
		// Private level is always reported for as it never overrides...
		else if (visibility == PRIVATE_VISIBILITY) {
			result.append(Y_ResultForMissingComments[PRIVATE_VISIBILITY+1]);
		}
		return result.toString();
	}

	public JavadocTestOptions(String name) {
		super(name);
	}

	public static Class testClass() {
		return JavadocTestOptions.class;
	}

	public static Test suite() {
		if (false) {
			TestSuite ts = new TestSuite();
			ts.addTest(new JavadocTestOptions("testMissingTagsErrorPrivate"));
			ts.addTest(new JavadocTestOptions("testMissingCommentsErrorPrivate"));
			return new RegressionTestSetup(ts, COMPLIANCE_1_4);
		}
		return setupSuite(testClass());
	}

	protected Map getCompilerOptions() {
		Map options = super.getCompilerOptions();
		// Set javadoc options if non null
		if (reportInvalidJavadoc != null)
			options.put(CompilerOptions.OPTION_ReportInvalidJavadoc, reportInvalidJavadoc);
		if (reportInvalidJavadocTagsVisibility != null)
			options.put(CompilerOptions.OPTION_ReportInvalidJavadocTagsVisibility, reportInvalidJavadocTagsVisibility);
		if (reportInvalidJavadocTags != null)
			options.put(CompilerOptions.OPTION_ReportInvalidJavadocTags, reportInvalidJavadocTags);
		if (reportMissingJavadocTags != null)
			options.put(CompilerOptions.OPTION_ReportMissingJavadocTags, reportMissingJavadocTags);
		if (reportMissingJavadocTagsVisibility != null)
			options.put(CompilerOptions.OPTION_ReportMissingJavadocTagsVisibility, reportMissingJavadocTagsVisibility);
		if (reportMissingJavadocTagsOverriding != null)
			options.put(CompilerOptions.OPTION_ReportMissingJavadocTagsOverriding, reportMissingJavadocTagsOverriding);
		if (reportMissingJavadocComments != null)
			options.put(CompilerOptions.OPTION_ReportMissingJavadocComments, reportMissingJavadocComments);
		if (reportMissingJavadocCommentsVisibility != null)
			options.put(CompilerOptions.OPTION_ReportMissingJavadocCommentsVisibility, reportMissingJavadocCommentsVisibility);
		if (reportMissingJavadocCommentsOverriding != null)
			options.put(CompilerOptions.OPTION_ReportMissingJavadocCommentsOverriding, reportMissingJavadocCommentsOverriding);
		
		// Ignore other options to avoid polluting warnings
		options.put(CompilerOptions.OPTION_ReportFieldHiding, CompilerOptions.IGNORE);
		options.put(CompilerOptions.OPTION_ReportSyntheticAccessEmulation, CompilerOptions.IGNORE);
		options.put(CompilerOptions.OPTION_ReportUnusedLocal, CompilerOptions.IGNORE);
		options.put(CompilerOptions.OPTION_ReportUnusedPrivateMember, CompilerOptions.IGNORE);
		return options;
	}
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		reportInvalidJavadoc = null;
		reportInvalidJavadocTagsVisibility = null;
		reportInvalidJavadocTags = null;
		reportMissingJavadocTags = null;
		reportMissingJavadocTagsVisibility = null;
		reportMissingJavadocTagsOverriding = null;
		reportMissingJavadocComments = null;
		reportMissingJavadocCommentsVisibility = null;
		reportMissingJavadocCommentsOverriding = null;
	}

	/*
	 * Tests for 'invalid javadoc' options
	 */
	// Test default invalid javadoc (means "ignore" with tags"disabled" and visibility "public")
	public void testInvalidTagsReferencesClassDefaults() {
		runConformTest(InvalidReferencesClassJavadocComments);
	}
	public void testInvalidTagsReferencesFieldDefaults() {
		runConformTest(InvalidReferencesFieldJavadocComments);
	}
	public void testInvalidTagsReferencesMethodDefaults() {
		runConformTest(InvalidReferencesMethodJavadocComments);
	}
	public void testInvalidTagsReferencesConstructorDefaults() {
		runConformTest(InvalidReferencesConstructorJavadocComments);
	}

	// Test invalid javadoc "error" + tags "disabled" and visibility "public"
	public void testInvalidTagsReferencesClassErrorNotags() {
		reportInvalidJavadoc = CompilerOptions.ERROR;
		reportInvalidJavadocTags = CompilerOptions.DISABLED;
		runConformTest(InvalidReferencesClassJavadocComments);
	}
	public void testInvalidTagsReferencesFieldErrorNotags() {
		reportInvalidJavadoc = CompilerOptions.ERROR;
		reportInvalidJavadocTags = CompilerOptions.DISABLED;
		runConformTest(InvalidReferencesFieldJavadocComments);
	}
	public void testInvalidTagsReferencesMethodErrorNotags() {
		reportInvalidJavadoc = CompilerOptions.ERROR;
		reportInvalidJavadocTags = CompilerOptions.DISABLED;
		runConformTest(InvalidReferencesMethodJavadocComments);
	}
	public void testInvalidTagsReferencesConstructorErrorNotags() {
		reportInvalidJavadoc = CompilerOptions.ERROR;
		reportInvalidJavadocTags = CompilerOptions.DISABLED;
		runConformTest(InvalidReferencesConstructorJavadocComments);
	}

	// Test invalid javadoc "error" + tags "enabled" and visibility "public"
	public void testInvalidTagsReferencesClassErrorTagsPublic() {
		reportInvalidJavadoc = CompilerOptions.ERROR;
		reportInvalidJavadocTags = CompilerOptions.ENABLED;
		reportInvalidJavadocTagsVisibility = CompilerOptions.PUBLIC;
		runNegativeTest(InvalidReferencesClassJavadocComments, resultForInvalidTagsReferencesClassOrField(PUBLIC_VISIBILITY));
	}
	public void testInvalidTagsReferencesFieldErrorTagsPublic() {
		reportInvalidJavadoc = CompilerOptions.ERROR;
		reportInvalidJavadocTags = CompilerOptions.ENABLED;
		reportInvalidJavadocTagsVisibility = CompilerOptions.PUBLIC;
		runNegativeTest(InvalidReferencesFieldJavadocComments, resultForInvalidTagsReferencesClassOrField(PUBLIC_VISIBILITY));
	}
	public void testInvalidTagsReferencesMethodErrorTagsPublic() {
		reportInvalidJavadoc = CompilerOptions.ERROR;
		reportInvalidJavadocTags = CompilerOptions.ENABLED;
		reportInvalidJavadocTagsVisibility = CompilerOptions.PUBLIC;
		runNegativeTest(InvalidReferencesMethodJavadocComments, resultForInvalidTagsReferencesMethodOrConstructor(PUBLIC_VISIBILITY));
	}
	public void testInvalidTagsReferencesConstructorErrorTagsPublic() {
		reportInvalidJavadoc = CompilerOptions.ERROR;
		reportInvalidJavadocTags = CompilerOptions.ENABLED;
		reportInvalidJavadocTagsVisibility = CompilerOptions.PUBLIC;
		runNegativeTest(InvalidReferencesConstructorJavadocComments, resultForInvalidTagsReferencesMethodOrConstructor(PUBLIC_VISIBILITY));
	}

	// Test invalid javadoc "error" + tags "enabled" and visibility "private"
	public void testInvalidTagsReferencesClassErrorTagsProtected() {
		reportInvalidJavadoc = CompilerOptions.ERROR;
		reportInvalidJavadocTags = CompilerOptions.ENABLED;
		reportInvalidJavadocTagsVisibility = CompilerOptions.PROTECTED;
		runNegativeTest(InvalidReferencesClassJavadocComments, resultForInvalidTagsReferencesClassOrField(PROTECTED_VISIBILITY));
	}
	public void testInvalidTagsReferencesFieldErrorTagsProtected() {
		reportInvalidJavadoc = CompilerOptions.ERROR;
		reportInvalidJavadocTags = CompilerOptions.ENABLED;
		reportInvalidJavadocTagsVisibility = CompilerOptions.PROTECTED;
		runNegativeTest(InvalidReferencesFieldJavadocComments, resultForInvalidTagsReferencesClassOrField(PROTECTED_VISIBILITY));
	}
	public void testInvalidTagsReferencesMethodErrorTagsProtected() {
		reportInvalidJavadoc = CompilerOptions.ERROR;
		reportInvalidJavadocTags = CompilerOptions.ENABLED;
		reportInvalidJavadocTagsVisibility = CompilerOptions.PROTECTED;
		runNegativeTest(InvalidReferencesMethodJavadocComments, resultForInvalidTagsReferencesMethodOrConstructor(PROTECTED_VISIBILITY));
	}
	public void testInvalidTagsReferencesConstructorErrorTagsProtected() {
		reportInvalidJavadoc = CompilerOptions.ERROR;
		reportInvalidJavadocTags = CompilerOptions.ENABLED;
		reportInvalidJavadocTagsVisibility = CompilerOptions.PROTECTED;
		runNegativeTest(InvalidReferencesConstructorJavadocComments, resultForInvalidTagsReferencesMethodOrConstructor(PROTECTED_VISIBILITY));
	}

	// Test invalid javadoc "error" + tags "enabled" and visibility "private"
	public void testInvalidTagsReferencesClassErrorTagsPackage() {
		reportInvalidJavadoc = CompilerOptions.ERROR;
		reportInvalidJavadocTags = CompilerOptions.ENABLED;
		reportInvalidJavadocTagsVisibility = CompilerOptions.DEFAULT;
		runNegativeTest(InvalidReferencesClassJavadocComments, resultForInvalidTagsReferencesClassOrField(DEFAULT_VISIBILITY));
	}
	public void testInvalidTagsReferencesFieldErrorTagsPackage() {
		reportInvalidJavadoc = CompilerOptions.ERROR;
		reportInvalidJavadocTags = CompilerOptions.ENABLED;
		reportInvalidJavadocTagsVisibility = CompilerOptions.DEFAULT;
		runNegativeTest(InvalidReferencesFieldJavadocComments, resultForInvalidTagsReferencesClassOrField(DEFAULT_VISIBILITY));
	}
	public void testInvalidTagsReferencesMethodErrorTagsPackage() {
		reportInvalidJavadoc = CompilerOptions.ERROR;
		reportInvalidJavadocTags = CompilerOptions.ENABLED;
		reportInvalidJavadocTagsVisibility = CompilerOptions.DEFAULT;
		runNegativeTest(InvalidReferencesMethodJavadocComments, resultForInvalidTagsReferencesMethodOrConstructor(DEFAULT_VISIBILITY));
	}
	public void testInvalidTagsReferencesConstructorErrorTagsPackage() {
		reportInvalidJavadoc = CompilerOptions.ERROR;
		reportInvalidJavadocTags = CompilerOptions.ENABLED;
		reportInvalidJavadocTagsVisibility = CompilerOptions.DEFAULT;
		runNegativeTest(InvalidReferencesConstructorJavadocComments, resultForInvalidTagsReferencesMethodOrConstructor(DEFAULT_VISIBILITY));
	}

	// Test invalid javadoc "error" + tags "enabled" and visibility "private"
	public void testInvalidTagsReferencesClassErrorTagsPrivate() {
		reportInvalidJavadoc = CompilerOptions.ERROR;
		runNegativeTest(InvalidReferencesClassJavadocComments, resultForInvalidTagsReferencesClassOrField(PRIVATE_VISIBILITY));
	}
	public void testInvalidTagsReferencesFieldErrorTagsPrivate() {
		reportInvalidJavadoc = CompilerOptions.ERROR;
		runNegativeTest(InvalidReferencesFieldJavadocComments, resultForInvalidTagsReferencesClassOrField(PRIVATE_VISIBILITY));
	}
	public void testInvalidTagsReferencesMethodErrorTagsPrivate() {
		reportInvalidJavadoc = CompilerOptions.ERROR;
		runNegativeTest(InvalidReferencesMethodJavadocComments, resultForInvalidTagsReferencesMethodOrConstructor(PRIVATE_VISIBILITY));
	}
	public void testInvalidTagsReferencesConstructorErrorTagsPrivate() {
		reportInvalidJavadoc = CompilerOptions.ERROR;
		runNegativeTest(InvalidReferencesConstructorJavadocComments, resultForInvalidTagsReferencesMethodOrConstructor(PRIVATE_VISIBILITY));
	}

	/*
	 * Tests for 'missing javadoc tags' options
	 */
	// Test default missing javadoc tags (means "ignore" with visibility "public" and overriding="enabled")
	public void testMissingTagsDefaults() {
		runConformTest(MissingTags);
	}

	// Test missing javadoc tags "error" + "public" visibility + "enabled" overriding
	public void testMissingTagsErrorPublicOverriding() {
		reportMissingJavadocTags = CompilerOptions.ERROR;
		reportMissingJavadocTagsVisibility = CompilerOptions.PUBLIC;
		reportMissingJavadocTagsOverriding = CompilerOptions.ENABLED;
		runNegativeTest(MissingTags, resultForMissingTags(PUBLIC_VISIBILITY));
	}

	// Test missing javadoc tags "error" + "public" visibility + "disabled" overriding
	public void testMissingTagsErrorPublic() {
		reportMissingJavadocTags = CompilerOptions.ERROR;
		reportMissingJavadocTagsVisibility = CompilerOptions.PUBLIC;
		reportMissingJavadocTagsOverriding = CompilerOptions.DISABLED;
		runNegativeTest(MissingTags, resultForMissingTags(PUBLIC_VISIBILITY));
	}

	// Test missing javadoc tags "error" + "protected" visibility + "enabled" overriding
	public void testMissingTagsErrorProtectedOverriding() {
		reportMissingJavadocTags = CompilerOptions.ERROR;
		reportMissingJavadocTagsVisibility = CompilerOptions.PROTECTED;
		reportMissingJavadocTagsOverriding = CompilerOptions.ENABLED;
		runNegativeTest(MissingTags, resultForMissingTags(PROTECTED_VISIBILITY));
	}

	// Test missing javadoc tags "error" + "protected" visibility + "disabled" overriding
	public void testMissingTagsErrorProtected() {
		reportMissingJavadocTags = CompilerOptions.ERROR;
		reportMissingJavadocTagsVisibility = CompilerOptions.PROTECTED;
		reportMissingJavadocTagsOverriding = CompilerOptions.DISABLED;
		runNegativeTest(MissingTags, resultForMissingTags(PROTECTED_VISIBILITY));
	}

	// Test missing javadoc tags "error" + "default" visibility + "enabled" overriding
	public void testMissingTagsErrorPackageOverriding() {
		reportMissingJavadocTags = CompilerOptions.ERROR;
		reportMissingJavadocTagsVisibility = CompilerOptions.DEFAULT;
		reportMissingJavadocTagsOverriding = CompilerOptions.ENABLED;
		runNegativeTest(MissingTags, resultForMissingTags(DEFAULT_VISIBILITY));
	}

	// Test missing javadoc tags "error" + "default" visibility + "disabled" overriding
	public void testMissingTagsErrorPackage() {
		reportMissingJavadocTags = CompilerOptions.ERROR;
		reportMissingJavadocTagsVisibility = CompilerOptions.DEFAULT;
		reportMissingJavadocTagsOverriding = CompilerOptions.DISABLED;
		runNegativeTest(MissingTags, resultForMissingTags(DEFAULT_VISIBILITY));
	}

	// Test missing javadoc tags "error" + "private" visibility + "enabled" overriding
	public void testMissingTagsErrorPrivateOverriding() {
		reportMissingJavadocTags = CompilerOptions.ERROR;
		reportMissingJavadocTagsVisibility = CompilerOptions.PRIVATE;
		reportMissingJavadocTagsOverriding = CompilerOptions.ENABLED;
		runNegativeTest(MissingTags, resultForMissingTags(PRIVATE_VISIBILITY));
	}

	// Test missing javadoc tags "error" + "private" visibility + "disabled" overriding
	public void testMissingTagsErrorPrivate() {
		reportMissingJavadocTags = CompilerOptions.ERROR;
		reportMissingJavadocTagsVisibility = CompilerOptions.PRIVATE;
		reportMissingJavadocTagsOverriding = CompilerOptions.DISABLED;
		runNegativeTest(MissingTags, resultForMissingTags(PRIVATE_VISIBILITY));
	}

	/*
	 * Tests for 'missing javadoc comments' options
	 */
	// Test default missing javadoc comments (means "ignore" with visibility "public" and overriding="enabled")
	public void testMissingCommentsDefaults() {
		runConformTest(MissingComments);
	}

	// Test missing javadoc comments "error" + "public" visibility + "enabled" overriding
	public void testMissingCommentsErrorPublicOverriding() {
		reportMissingJavadocComments = CompilerOptions.ERROR;
		reportMissingJavadocCommentsVisibility = CompilerOptions.PUBLIC;
		reportMissingJavadocCommentsOverriding = CompilerOptions.ENABLED;
		runNegativeTest(MissingComments, resultForMissingComments(PUBLIC_VISIBILITY));
	}

	// Test missing javadoc comments "error" + "public" visibility + "disabled" overriding
	public void testMissingCommentsErrorPublic() {
		reportMissingJavadocComments = CompilerOptions.ERROR;
		reportMissingJavadocCommentsVisibility = CompilerOptions.PUBLIC;
		reportMissingJavadocCommentsOverriding = CompilerOptions.DISABLED;
		runNegativeTest(MissingComments, resultForMissingComments(PUBLIC_VISIBILITY));
	}

	// Test missing javadoc comments "error" + "protected" visibility + "enabled" overriding
	public void testMissingCommentsErrorProtectedOverriding() {
		reportMissingJavadocComments = CompilerOptions.ERROR;
		reportMissingJavadocCommentsVisibility = CompilerOptions.PROTECTED;
		reportMissingJavadocCommentsOverriding = CompilerOptions.ENABLED;
		runNegativeTest(MissingComments, resultForMissingComments(PROTECTED_VISIBILITY));
	}

	// Test missing javadoc comments "error" + "protected" visibility + "disabled" overriding
	public void testMissingCommentsErrorProtected() {
		reportMissingJavadocComments = CompilerOptions.ERROR;
		reportMissingJavadocCommentsVisibility = CompilerOptions.PROTECTED;
		reportMissingJavadocCommentsOverriding = CompilerOptions.DISABLED;
		runNegativeTest(MissingComments, resultForMissingComments(PROTECTED_VISIBILITY));
	}

	// Test missing javadoc comments "error" + "default" visibility + "enabled" overriding
	public void testMissingCommentsErrorPackageOverriding() {
		reportMissingJavadocComments = CompilerOptions.ERROR;
		reportMissingJavadocCommentsVisibility = CompilerOptions.DEFAULT;
		reportMissingJavadocCommentsOverriding = CompilerOptions.ENABLED;
		runNegativeTest(MissingComments, resultForMissingComments(DEFAULT_VISIBILITY));
	}

	// Test missing javadoc comments "error" + "default" visibility + "disabled" overriding
	public void testMissingCommentsErrorPackage() {
		reportMissingJavadocComments = CompilerOptions.ERROR;
		reportMissingJavadocCommentsVisibility = CompilerOptions.DEFAULT;
		reportMissingJavadocCommentsOverriding = CompilerOptions.DISABLED;
		runNegativeTest(MissingComments, resultForMissingComments(DEFAULT_VISIBILITY));
	}

	// Test missing javadoc comments "error" + "private" visibility + "enabled" overriding
	public void testMissingCommentsErrorPrivateOverriding() {
		reportMissingJavadocComments = CompilerOptions.ERROR;
		reportMissingJavadocCommentsVisibility = CompilerOptions.PRIVATE;
		reportMissingJavadocCommentsOverriding = CompilerOptions.ENABLED;
		runNegativeTest(MissingComments, resultForMissingComments(PRIVATE_VISIBILITY));
	}

	// Test missing javadoc comments "error" + "private" visibility + "disabled" overriding
	public void testMissingCommentsErrorPrivate() {
		reportMissingJavadocComments = CompilerOptions.ERROR;
		reportMissingJavadocCommentsVisibility = CompilerOptions.PRIVATE;
		reportMissingJavadocCommentsOverriding = CompilerOptions.DISABLED;
		runNegativeTest(MissingComments, resultForMissingComments(PRIVATE_VISIBILITY));
	}
	
	/*
	 * Crossed tests
	 */
	public void testInvalidTagsClassWithMissingTagsOption() {
		reportMissingJavadocTags = CompilerOptions.ERROR;
		reportMissingJavadocTagsVisibility = CompilerOptions.PRIVATE;
		runConformTest(InvalidReferencesClassJavadocComments);
	}
	public void testInvalidTagsFieldWithMissingTagsOption() {
		reportMissingJavadocTags = CompilerOptions.ERROR;
		reportMissingJavadocTagsVisibility = CompilerOptions.PRIVATE;
		runConformTest(InvalidReferencesFieldJavadocComments);
	}
	public void testInvalidTagsMethodWithMissingTagsOption() {
		reportMissingJavadocTags = CompilerOptions.ERROR;
		reportMissingJavadocTagsVisibility = CompilerOptions.PRIVATE;
		runConformTest(InvalidReferencesMethodJavadocComments);
	}
	public void testInvalidTagsConstructorWithMissingTagsOption() {
		reportMissingJavadocTags = CompilerOptions.ERROR;
		reportMissingJavadocTagsVisibility = CompilerOptions.PRIVATE;
		runConformTest(InvalidReferencesConstructorJavadocComments);
	}
	public void testMissingTagsWithInvalidTagsOption() {
		reportInvalidJavadoc = CompilerOptions.ERROR;
		reportInvalidJavadocTags = CompilerOptions.ENABLED;
		reportInvalidJavadocTagsVisibility = CompilerOptions.PRIVATE;
		runConformTest(MissingTags);
	}
}
