package dev.skidfuscator.ir.test;

public class SimpleInterfaceHierarchyTest {


    interface A {
        void foo();
    }

    interface B {
        void foo ();
    }

    static class AImpl implements A {
        public void foo () {}
    }
    static class BImpl implements B {
        public void foo () {}
    }

    // Now when C is being resolved, method foo should have the same group with A and B, but it won't. Which group to pick?
    class C implements A, B {
        public void foo() {}
    }
}
