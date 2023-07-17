import dev.skidfuscator.ir.type.TypeWrapper;
import org.objectweb.asm.Type;

public class Main {

    private static final String[] TYPES = {
            "(IJLjava/lang/String;[L/java/lang/Math;[[[Ljava/lang/Integer;)V",
            "(IJLjava/lang/String;[L/java/lang/Math;[[[Ljava/lang/Integer;)[[[Ljava/lang/Integer;",
            "(IJLjava/lang/String;[L/java/lang/Math;[[[Ljava/lang/Integer;)[L/java/lang/Math;",
            "(IJLjava/lang/String;[L/java/lang/Math;[[[Ljava/lang/Integer;)Ljava/lang/String;",
            "[[[Ljava/lang/Integer;",
            "[L/java/lang/Math;",
            "Ljava/lang/String;",
            "I",
            "J"
    };

    public static void main(String[] args) {
        for (String desc : TYPES) {
            Type type = Type.getType(desc);
            TypeWrapper typeWrapper = new TypeWrapper(type, null);
            typeWrapper.resolve();

            System.out.println(type);
            System.out.println(typeWrapper.getDesc());
            System.out.println(typeWrapper.dump());
            System.out.println();
        }
    }
}
