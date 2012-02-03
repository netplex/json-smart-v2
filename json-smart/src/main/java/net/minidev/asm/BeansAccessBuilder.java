package net.minidev.asm;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACONST_NULL;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.F_SAME;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.ICONST_2;
import static org.objectweb.asm.Opcodes.ICONST_3;
import static org.objectweb.asm.Opcodes.ICONST_4;
import static org.objectweb.asm.Opcodes.ICONST_5;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.IF_ICMPNE;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class BeansAccessBuilder {
	static private String METHOD_ACCESS_NAME = BeansAccess.class.getName().replace('.', '/');

	final Class<?> type;
	final Accessor[] accs;
	final DynamicClassLoader loader;
	final String className;
	final String accessClassName;
	final String accessClassNameInternal;
	final String classNameInternal;

	public BeansAccessBuilder(Class<?> type, Accessor[] accs, DynamicClassLoader loader) {
		this.type = type;
		this.accs = accs;
		this.loader = loader;

		this.className = type.getName();
		this.accessClassName = className.concat("AccAccess");

		this.accessClassNameInternal = accessClassName.replace('.', '/');
		this.classNameInternal = className.replace('.', '/');
	}


	/**
	 * Build reflect bytecode from accessor list.
	 * 
	 * @param type
	 *            type to be access
	 * @param accs
	 *            used accessor
	 * @param loader
	 *            Loader used to store the generated class
	 * 
	 * @return the new reflect class
	 */
	public Class<?> bulid() {
		ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
		MethodVisitor mv;

		boolean USE_HASH = accs.length > 10;
		int HASH_LIMIT = 14;

		cw.visit(Opcodes.V1_6, ACC_PUBLIC + Opcodes.ACC_SUPER, accessClassNameInternal, null, METHOD_ACCESS_NAME, null);
		// init
		{
			mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
			mv.visitCode();
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, METHOD_ACCESS_NAME, "<init>", "()V");
			mv.visitInsn(RETURN);
			mv.visitMaxs(1, 1);
			mv.visitEnd();
		}

		// if (USE_HASH)
		// set(Object object, int methodIndex, Object value)
		mv = cw.visitMethod(ACC_PUBLIC, "set", "(Ljava/lang/Object;ILjava/lang/Object;)V", null, null);
		mv.visitCode();

		if (accs.length == 0) {
			mv.visitInsn(RETURN);
		} else if (accs.length > HASH_LIMIT) {
			mv.visitVarInsn(ILOAD, 2);
			Label[] labels = ASMUtil.newLabels(accs.length);
			Label defaultLabel = new Label();

			mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);
			int i = 0;
			for (Accessor acc : accs) {
				mv.visitLabel(labels[i++]);
				if (!acc.isWritable()) {
					mv.visitInsn(RETURN);
					continue;
				}
				internalSetFiled(mv, acc);
			}
			mv.visitLabel(defaultLabel);
			mv.visitInsn(RETURN);
		} else {
			Label[] labels = ASMUtil.newLabels(accs.length);

			int i = 0;
			for (Accessor acc : accs) {
				ifNotEqJmp(mv, 2, i, labels[i]);
				internalSetFiled(mv, acc);
				mv.visitLabel(labels[i]);
				mv.visitFrame(F_SAME, 0, null, 0, null);
				i++;
			}
			mv.visitInsn(RETURN);
		}
		mv.visitMaxs(0, 0);
		mv.visitEnd();

		// public Object get(Object object, int fieldId)
		mv = cw.visitMethod(ACC_PUBLIC, "get", "(Ljava/lang/Object;I)Ljava/lang/Object;", null, null);
		mv.visitCode();
		// if (USE_HASH)
		if (accs.length == 0) {
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ARETURN);
		} else if (accs.length > HASH_LIMIT) {
			mv.visitVarInsn(ILOAD, 2);
			Label[] labels = ASMUtil.newLabels(accs.length);
			Label defaultLabel = new Label();
			mv.visitTableSwitchInsn(0, labels.length - 1, defaultLabel, labels);

			int i = 0;
			for (Accessor acc : accs) {
				mv.visitLabel(labels[i++]);
				mv.visitFrame(F_SAME, 0, null, 0, null);
				if (!acc.isReadable()) {
					mv.visitInsn(ACONST_NULL);
					mv.visitInsn(ARETURN);
					continue;
				}
				mv.visitVarInsn(ALOAD, 1);
				mv.visitTypeInsn(CHECKCAST, classNameInternal);
				Type fieldType = Type.getType(acc.getType());

				if (acc.isPublic()) {
					mv.visitFieldInsn(GETFIELD, classNameInternal, acc.getName(), fieldType.getDescriptor());
				} else {
					String sig = Type.getMethodDescriptor(acc.getter);
					mv.visitMethodInsn(INVOKEVIRTUAL, classNameInternal, acc.getter.getName(), sig);
				}
				ASMUtil.autoBoxing(mv, fieldType);
				mv.visitInsn(ARETURN);
			}
			mv.visitLabel(defaultLabel);
			mv.visitFrame(F_SAME, 0, null, 0, null);
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ARETURN);
		} else {
			Label[] labels = ASMUtil.newLabels(accs.length);

			int i = 0;
			for (Accessor acc : accs) {
				ifNotEqJmp(mv, 2, i, labels[i]);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitTypeInsn(CHECKCAST, classNameInternal);

				Type fieldType = Type.getType(acc.getType());

				if (acc.isPublic()) {
					mv.visitFieldInsn(GETFIELD, classNameInternal, acc.getName(), fieldType.getDescriptor());
				} else {
					String sig = Type.getMethodDescriptor(acc.getter);
					mv.visitMethodInsn(INVOKEVIRTUAL, classNameInternal, acc.getter.getName(), sig);
				}
				ASMUtil.autoBoxing(mv, fieldType);
				mv.visitInsn(ARETURN);

				mv.visitLabel(labels[i]);
				mv.visitFrame(F_SAME, 0, null, 0, null);
				i++;
			}
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ARETURN);
		}

		mv.visitMaxs(0, 0);
		mv.visitEnd();

		if (!USE_HASH) {
			// Object get(Object object, String methodName)
			mv = cw.visitMethod(ACC_PUBLIC, "set", "(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/Object;)V", null,
					null);
			mv.visitCode();

			Label[] labels = ASMUtil.newLabels(accs.length);

			int i = 0;
			for (Accessor acc : accs) {
				// if (i > 0) {
				// mv.visitLabel(labels[i - 1]);
				// mv.visitFrame(F_SAME, 0, null, 0, null);
				// }
				mv.visitVarInsn(ALOAD, 2);
				mv.visitLdcInsn(acc.fieldName);
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z");
				mv.visitJumpInsn(IFEQ, labels[i]);
				mv.visitVarInsn(ALOAD, 1);
				mv.visitTypeInsn(CHECKCAST, classNameInternal); // classNameInternal
				mv.visitVarInsn(ALOAD, 3);
				Type fieldType = Type.getType(acc.getType());
				ASMUtil.autoUnBoxing2(mv, fieldType);
				if (acc.isPublic()) {
					mv.visitFieldInsn(PUTFIELD, classNameInternal, acc.getName(), fieldType.getDescriptor());
				} else {
					String sig = Type.getMethodDescriptor(acc.setter);
					mv.visitMethodInsn(INVOKEVIRTUAL, classNameInternal, acc.setter.getName(), sig);
				}
				mv.visitInsn(RETURN);
				mv.visitLabel(labels[i]);
				mv.visitFrame(F_SAME, 0, null, 0, null);
				i++;
			}
			mv.visitInsn(RETURN);
			mv.visitMaxs(0, 0); // 2,4
			mv.visitEnd();
		}

		if (!USE_HASH) {
			// get(Object object, String methodName)
			mv = cw.visitMethod(ACC_PUBLIC, "get", "(Ljava/lang/Object;Ljava/lang/String;)Ljava/lang/Object;", null,
					null);
			mv.visitCode();

			Label[] labels = ASMUtil.newLabels(accs.length);

			int i = 0;
			for (Accessor acc : accs) {
				mv.visitVarInsn(ALOAD, 2); // methodName
				mv.visitLdcInsn(acc.fieldName);
				mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "equals", "(Ljava/lang/Object;)Z");
				mv.visitJumpInsn(IFEQ, labels[i]);
				mv.visitVarInsn(ALOAD, 1); // object
				mv.visitTypeInsn(CHECKCAST, classNameInternal);
				Type fieldType = Type.getType(acc.getType());
				if (acc.isPublic()) {
					mv.visitFieldInsn(GETFIELD, classNameInternal, acc.getName(), fieldType.getDescriptor());
				} else {
					String sig = Type.getMethodDescriptor(acc.getter);
					mv.visitMethodInsn(INVOKEVIRTUAL, classNameInternal, acc.getter.getName(), sig);
				}
				ASMUtil.autoBoxing(mv, fieldType);
				mv.visitInsn(ARETURN);
				mv.visitLabel(labels[i]);
				mv.visitFrame(F_SAME, 0, null, 0, null);
				i++;
			}
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(ARETURN);
			mv.visitMaxs(0, 0); // 1,3 or 2,3
			mv.visitEnd();
		}

		{
			mv = cw.visitMethod(ACC_PUBLIC, "newInstance", "()Ljava/lang/Object;", null, null);
			mv.visitCode();
			mv.visitTypeInsn(NEW, classNameInternal);
			mv.visitInsn(DUP);
			mv.visitMethodInsn(INVOKESPECIAL, classNameInternal, "<init>", "()V");
			mv.visitInsn(ARETURN);
			mv.visitMaxs(2, 1);
			mv.visitEnd();
		}

		cw.visitEnd();

		byte[] data = cw.toByteArray();
		// debug
		// {
		// try {
		// File dest = new
		// File("C:/project/google-code/1.1/json-smart/target/test-classes/"
		// + accessClassName.replace('.', '/') + ".class");
		// dest.getParentFile().mkdirs();
		// FileOutputStream fos = new FileOutputStream(dest);
		// fos.write(data);
		// fos.close();
		// } catch (Exception e) {
		// }
		// }
		return loader.defineClass(accessClassName, data);
	}

	private void internalSetFiled(MethodVisitor mv, Accessor acc) {
		mv.visitVarInsn(ALOAD, 1);
		mv.visitTypeInsn(CHECKCAST, classNameInternal);
		mv.visitVarInsn(ALOAD, 3);
		Type fieldType = Type.getType(acc.getType());
		ASMUtil.autoUnBoxing2(mv, fieldType);
		if (acc.isPublic()) {
			mv.visitFieldInsn(PUTFIELD, classNameInternal, acc.getName(), fieldType.getDescriptor());
		} else {
			String sig = Type.getMethodDescriptor(acc.setter);
			mv.visitMethodInsn(INVOKEVIRTUAL, classNameInternal, acc.setter.getName(), sig);
		}
		mv.visitInsn(RETURN);
	}

	private void internalSetFiledConv(MethodVisitor mv, Accessor acc) {
		// 1 -> object to alter
		// 2 -> id of field
		// 3 -> new value
		mv.visitVarInsn(ALOAD, 1);
		mv.visitTypeInsn(CHECKCAST, classNameInternal);
		mv.visitVarInsn(ALOAD, 3);
		// Label lTest = new Label();
		// if (Number.class.isAssignableFrom(acc.getType())) {
		// mv.visitTypeInsn(Opcodes.INSTANCEOF, "java/lang/Number");
		// mv.visitJumpInsn(Opcodes.IFEQ, lTest);
		// mv.visitVarInsn(Opcodes.ALOAD, 3);
		// } else if (acc.getType().isEnum()) {
		// } else if (acc.getType().equals(String.class)) {
		// }
		// case Good Type
		Type fieldType = Type.getType(acc.getType());
		ASMUtil.autoUnBoxing2(mv, fieldType);
		//
		// if (Number.class.isAssignableFrom(acc.getType())) {
		// mv.visitJumpInsn(Opcodes.GOTO, l2);
		// mv.visitLabel(l1);
		// mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {
		// "net/minidev/asm/BTest" });
		// mv.visitVarInsn(Opcodes.ALOAD, 3);
		// mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object",
		// "toString", "()Ljava/lang/String;");
		// mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/Integer",
		// "valueOf",
		// "(Ljava/lang/String;)Ljava/lang/Integer;");
		// mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL,
		// "java/lang/Integer", "intValue", "()I");
		// mv.visitLabel(l2);
		// mv.visitFrame(Opcodes.F_FULL, 4, new Object[] {
		// "net/minidev/asm/BTestBeansAccessB",
		// "java/lang/Object", "java/lang/String", "java/lang/Object" },
		// 2, new Object[] {
		// "net/minidev/asm/BTest", Opcodes.INTEGER });
		// } else if (acc.getType().isEnum()) {
		// } else if (acc.getType().equals(String.class)) {
		// }
		if (acc.isPublic()) {
			mv.visitFieldInsn(PUTFIELD, classNameInternal, acc.getName(), fieldType.getDescriptor());
		} else {
			String sig = Type.getMethodDescriptor(acc.setter);
			mv.visitMethodInsn(INVOKEVIRTUAL, classNameInternal, acc.setter.getName(), sig);
		}
		mv.visitInsn(RETURN);
	}

	private void ifNotEqJmp(MethodVisitor mv, int param, int value, Label label) {
		mv.visitVarInsn(ILOAD, param);
		if (value == 0) {
			/* notest forvalue 0 */
			mv.visitJumpInsn(IFNE, label);
		} else if (value == 1) {
			mv.visitInsn(ICONST_1);
			mv.visitJumpInsn(IF_ICMPNE, label);
		} else if (value == 2) {
			mv.visitInsn(ICONST_2);
			mv.visitJumpInsn(IF_ICMPNE, label);
		} else if (value == 3) {
			mv.visitInsn(ICONST_3);
			mv.visitJumpInsn(IF_ICMPNE, label);
		} else if (value == 4) {
			mv.visitInsn(ICONST_4);
			mv.visitJumpInsn(IF_ICMPNE, label);
		} else if (value == 5) {
			mv.visitInsn(ICONST_5);
			mv.visitJumpInsn(IF_ICMPNE, label);
		} else if (value >= 6) {
			mv.visitIntInsn(BIPUSH, value);
			mv.visitJumpInsn(IF_ICMPNE, label);
		} else {
			throw new RuntimeException("non supported negative values");
		}
	}

}
