package pl.com.bottega.cymes.sanbox;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

public class MapSerializerTest {

    private MapSerializer serializer = new MapSerializer();

    @Test
    public void throwsExceptionForNullInput() {
        assertThatIllegalArgumentException().isThrownBy(() -> serializer.serialize(null));
    }

    @Test
    public void throwsErrorWhenTryingToSerializeSimpleTypes() {
        assertThatIllegalArgumentException().isThrownBy(() -> serializer.serialize(1));
        assertThatIllegalArgumentException().isThrownBy(() -> serializer.serialize(2L));
        assertThatIllegalArgumentException().isThrownBy(() -> serializer.serialize(2.0));
        assertThatIllegalArgumentException().isThrownBy(() -> serializer.serialize(2.0f));
        assertThatIllegalArgumentException().isThrownBy(() -> serializer.serialize(true));
        assertThatIllegalArgumentException().isThrownBy(() -> serializer.serialize(BigDecimal.TEN));
        assertThatIllegalArgumentException().isThrownBy(() -> serializer.serialize(TestEnum.VALUE));
        assertThatIllegalArgumentException().isThrownBy(() -> serializer.serialize(new Integer[]{1, 2, 3}));
        assertThatIllegalArgumentException().isThrownBy(() -> serializer.serialize(List.of(1, 2, 3)));
        assertThatIllegalArgumentException().isThrownBy(() -> serializer.serialize(Set.of(1, 2, 3)));
    }

    @Test
    public void serializesObjectWithOneField() {
        // given
        var object = new ObjectWithOneField();
        object.foo = 10L;

        // when
        var serialized = serializer.serialize(object);

        // then
        assertThat(serialized.keySet()).containsExactly("foo");
        assertThat(serialized).containsEntry("foo", 10L);
    }

    @Test
    public void serializesObjectWithOnePrivateField() {
        // given
        var object = new ObjectWithOnePrivateField("bar");

        // when
        var serialized = serializer.serialize(object);

        // then
        assertThat(serialized.keySet()).containsExactly("foo");
        assertThat(serialized).containsEntry("foo", "bar");
    }

    @Test
    public void serializesObjectWithMultipleFields() {
        // given
        var object = new Object() {
            private final String x = "1";
            String y = "2";
            protected Double z = 20.0;
        };

        // when
        var serialized = serializer.serialize(object);

        // then
        assertThat(serialized.keySet()).containsExactly("x", "y", "z");
        assertThat(serialized).containsEntry("x", "1").containsEntry("y", "2").containsEntry("z", 20.0);
    }

    @Test
    public void serializesObjectWithPrimitiveFields() {
        // given
        var object = new Object() {
            private final long x = 1L;
            boolean b;
        };

        // when
        var serialized = serializer.serialize(object);

        // then
        assertThat(serialized.keySet()).containsExactly("x", "b");
        assertThat(serialized).containsEntry("x", 1L).containsEntry("b", false);
    }

    @Test
    public void serializesObjectWithFieldsAndGetters() {
        // given
        var object = new Object() {
            private final long x = 1L;
            boolean b;

            Long getY() {
                return 2L;
            }

            private String getZ() {
                return "3";
            }

            String someOtherMethod() {
                throw new RuntimeException();
            }
        };

        // when
        var serialized = serializer.serialize(object);

        // then
        assertThat(serialized.keySet()).containsExactly("x", "b", "y", "z");
        assertThat(serialized).containsEntry("x", 1L)
            .containsEntry("b", false)
            .containsEntry("y", 2L)
            .containsEntry("z", "3");
    }
}

enum TestEnum {
    VALUE
}

class ObjectWithOneField {
    Long foo;
}

class ObjectWithOnePrivateField {
    private String foo;

    public ObjectWithOnePrivateField(String foo) {
        this.foo = foo;
    }
}
