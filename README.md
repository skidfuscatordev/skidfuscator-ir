# skidfuscator-ir

Imagine if you could rename a class and its invokers by doing `klass.setName(<name)`. Well now u can. 

## Usage
Add it in your root build.gradle at the end of repositories:
```java
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
Step 2. Add the dependency
```java
	dependencies {
	        implementation 'com.github.skidfuscatordev:skidfuscator-ir:master-SNAPSHOT'
	}
```

Then, to begin using it, create a hierarchy as follows:
```java
final Map<String, ClassNode> classes = new HashMap();
// Your class loading logic here

final SkidLibraryHierarchy hierarchy = new SkidLibraryHierarchy();
hierarchy.resolveClasses(classes);
```
### Iterating wrapped classes
---
You can iterate all application classes by using
```java
for (final KlassNode node : hierarchy.iterateKlasses()) {
    node.setName("Omg" + node.getName());
}
```
---
### Finding a class
---
You can find a class of the name formatted as "my/searched/Class" by using
```java
final KlassNode node = hierarchy.resolveClass("my/searched/Class");
```

If the class does not exist, if configured, an "UnresolvedKlassNode" will be generated. 
If you seek to not generate a phantom class, please instead use the deprecated `hierarchy#findClass`
until I finish writing the config system.

---

### Modifying the bytecode
---
You can access and modify the bytecode of a class by doing the following:
```java
final KlassNode node = hierarchy.resolveClass("my/searched/Class");

for (final FunctionNode method : node.getMethods()) {
    final InstructionList bytecode = method.getInstructionList();
    bytecode.add(new SimpleInsn(hierarchy, new InsnNode(Opcodes.NOP)));
}
```

The following nodes are wrapped:
- FieldInsnNode --> FieldInsn
- FrameNode --> FrameInsn
- IincInsnNode --> IincInsn
- IntInsnNode --> IntInsn
- InvokeDynamicInsnNode --> InvokeDynamicInsn
- MethodInsnNode --> InvokeInsn
- JumpInsnNode --> JumpInsn
- LabelNode --> LabelInsn
- LdcInsnNode --> LdcInsn
- LineNumberNode --> LineNumberInsn
- LookupSwitchInsnNode --> LookupSwitchInsn
- TableSwitchInsnNode --> TableSwitchNode
- MultiANewArrayInsnNode --> MultiANewArrayInsn
- InsnNode --> SimpleInsn
- TypeInsnNode --> TypeInsn
- VarInsnNode --> VarInsn
---

### Preventing further changes
---
With skidfuscator-ir, you can lock changes on a class. This will effectively prevent
any further changes to the class if you wish to inspect a class and set it as a library
post-resolving it. Practical for analysers wishing to debug what's changing what.

```java
final KlassNode node = hierarchy.resolveClass("my/searched/Class");
node.lock(); // Locks a class
```
---

### Todo
- [x] Finish all insn nodes
- [x] Create test suite
- [x] Add support for fields
- [x] Add support for annotation
- [x] signatures
- [x] mapper for types
- [x] mapper for annotations
- [ ] mapper for annotation method linking
