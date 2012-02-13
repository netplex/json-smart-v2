package net.minidev.asm;

import java.lang.reflect.Constructor;
import java.text.NumberFormat;
import java.util.HashMap;

import junit.framework.TestCase;
import net.minidev.asm.FM.FastMap;
import net.minidev.asm.FM.FastMap1Builder;
import net.minidev.asm.FM.FastMap2Builder;
import net.minidev.asm.FM.FastMapTest_2;
import net.minidev.asm.FM.FastMapTest_3;
import net.minidev.asm.bean.BTest;

public class ASMTest extends TestCase {

	@SuppressWarnings({ "unused", "unchecked" })
	public void testGet() throws Exception {
		long T1;

		BeansAccess acASMAuto = BeansAccess.get(BTest.class);
		// BeansAccess acHand = new BTestBeansAccessB();

		HashMap<String, String> m = new HashMap<String, String>();
		m.put("A", "ab");
		m.put("B", "Bc");
		m.put("C", "cD");
		m.put("D", "de");
		m.put("E", "ef");

		String clsPath = FastMap1Builder.getName(m.size());
		String clsName = clsPath.replace("/", ".");

		byte[] data;

		// data = FastMap1Builder.dump(m.size());
		data = FastMap2Builder.dump(m);
		// data = FastMapTestDump.dump(clsPath);

		DynamicClassLoader loader = new DynamicClassLoader(BTest.class.getClassLoader());
		// byte[] data = BTestBeansAccessDump.dump();
		Class<FastMap> cls = (Class<FastMap>) loader.defineClass(clsName, data);
		Constructor<FastMap> c = (Constructor<FastMap>) cls.getConstructors()[0];
		FastMap f = c.newInstance(m);

		f = new FastMapTest_2<String>(m);
		f = new FastMapTest_3();

		// f = new FastMapTest_2<String>(m);
		// f = new FastMapTest_3();
		// System.out.println(m.get("A"));
		// 4 entré
		// map => 1.279
		// fastMap => 3.323
		// FastMapTest_1 3.323
		// FastMapTest_2 3.323
		// FastMapTest_3 0.015

		// 3 entré
		// map => 0.983
		// fastmap => 1.014
		// 2 entré
		// map => 0,920
		// fastMap => 0,608

		// 7 entree
		// f 2.667
		// m 0,640

		// 6 entree
		// f 2.215
		// m 0,608

		// 4 entree
		// f 0.032
		// m 0,593

		// 5 entree
		// f
		// m 0.609
		// V2 2.402
		// V3 2.247
		for (int i = 0; i < 20000; i++) {
			f.get("A");
			f.get("B");
			f.get("C");
			f.get("D");
			f.get("E");
			f.get("F");
			f.get("G");
			f.get("H");
			f.get("I");
		}
		System.gc();
		long T = System.nanoTime();
		for (int i = 0; i < 20000000; i++) {
			m.get("A");
			m.get("B");
			m.get("C");
			m.get("D");
			m.get("E");
			m.get("F");
			m.get("G");
			m.get("H");
			m.get("I");
		}
		T = System.nanoTime() - T;
		System.out.println(NumberFormat.getInstance().format(T));
		// 10 774 968
		// 596 295 451
		// 2 321 087 341

		BeansAccess ac;
		ac = acASMAuto;
		// ac = acHand;
		// ac = acASMHand;
		subtext(ac);
		// T1 = System.currentTimeMillis();
		// for (int i = 0; i < 2000000; i++)
		// subtext(ac);
		// T1 = System.currentTimeMillis() - T1;
		// System.out.println("// Time: " + T1);
	}

	private void subtext(BeansAccess acc) {
		BTest t = new BTest();
		acc.set(t, "pubBoolValue", true);
		acc.set(t, "pubIntValue", 13);
		acc.set(t, "pubStrValue", "Test");
		acc.set(t, "privIntValue", 16);
		acc.set(t, "privStrValue", "test Priv");
		assertEquals(Integer.valueOf(13), acc.get(t, "pubIntValue"));
		acc.set(t, "pubIntValue", 14);
		assertEquals(Integer.valueOf(14), acc.get(t, "pubIntValue"));
	}

	// public static void test0() {
	// MethodVisitor mv = null;
	// mv.visitLdcInsn("privIntValue");
	// mv.visitVarInsn(Opcodes.ALOAD, 2);
	// mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "equals",
	// "(Ljava/lang/Object;)Z");
	// Label l0 = new Label();
	// mv.visitJumpInsn(Opcodes.IFEQ, l0);
	// mv.visitVarInsn(Opcodes.ALOAD, 1);
	// mv.visitTypeInsn(Opcodes.CHECKCAST, "net/minidev/asm/BTest");
	// mv.visitVarInsn(Opcodes.ALOAD, 3);
	// mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Number");
	//
	//
	// mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Number", "intValue",
	// "()I");
	//
	//
	// mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minidev/asm/BTest",
	// "setPrivIntValue", "(I)V");
	// mv.visitInsn(Opcodes.RETURN);
	// mv.visitLabel(l0);
	// mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
	// }

	// public static void test2() {
	// MethodVisitor mv = null;
	// mv.visitLdcInsn("privIntValue");
	// mv.visitVarInsn(Opcodes.ALOAD, 2);
	// mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/String", "equals",
	// "(Ljava/lang/Object;)Z");
	// Label l0 = new Label();
	// mv.visitJumpInsn(Opcodes.IFEQ, l0);
	// mv.visitVarInsn(Opcodes.ALOAD, 1);
	// mv.visitTypeInsn(Opcodes.CHECKCAST, "net/minidev/asm/BTest");
	// mv.visitVarInsn(Opcodes.ALOAD, 3);
	// mv.visitTypeInsn(Opcodes.INSTANCEOF, "java/lang/Number");
	//
	//
	// Label l1 = new Label();
	// mv.visitJumpInsn(Opcodes.IFEQ, l1);
	// mv.visitVarInsn(Opcodes.ALOAD, 3);
	// mv.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Number");
	//
	// mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Number", "intValue",
	// "()I");
	//
	// Label l2 = new Label();
	// mv.visitJumpInsn(Opcodes.GOTO, l2);
	// mv.visitLabel(l1);
	// mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[]
	// {"net/minidev/asm/BTest"});
	// mv.visitVarInsn(Opcodes.ALOAD, 3);
	// mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "toString",
	// "()Ljava/lang/String;");
	// mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer", "valueOf",
	// "(Ljava/lang/String;)Ljava/lang/Integer;");
	// mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Integer",
	// "intValue", "()I");
	// mv.visitLabel(l2);
	// mv.visitFrame(Opcodes.F_FULL, 4, new Object[]
	// {"net/minidev/asm/BTestBeansAccessB", "java/lang/Object",
	// "java/lang/String", "java/lang/Object"}, 2, new Object[]
	// {"net/minidev/asm/BTest", Opcodes.INTEGER});
	//
	//
	// mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minidev/asm/BTest",
	// "setPrivIntValue", "(I)V");
	// mv.visitInsn(Opcodes.RETURN);
	// mv.visitLabel(l0);
	// mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
	// }

}
