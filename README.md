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
You can iterate all application classes by using
```java
for (final KlassNode node : hierarchy.iterateKlasses()) {
    node.setName("Omg" + node.getName());
}
```

### Todo
- [ ] Finish all insn nodes
- [ ] Create test suite
- [x] Add support for fields
- [x] Add support for annotation
- [ ] signatures
- [ ] mapper for types
- [ ] mapper for annotations
