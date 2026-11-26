package at.htl.workloads.playlist;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PlaylistEntryIdTest {

    @Test
    void noArgAndAllFieldsConstructorsExist() throws ReflectiveOperationException {
        var keyType = PlaylistEntryId.class;
        var fields = keyFields();
        var fieldTypes = fields.stream().map(Field::getType).toArray(Class<?>[]::new);

        assertNotNull(keyType.getDeclaredConstructor());
        var allFieldsConstructor = keyType.getDeclaredConstructor(fieldTypes);
        assertNotNull(allFieldsConstructor);

        var values = new Object[fields.size()];
        for (int i = 0; i < fields.size(); i++) {
            values[i] = sampleValue(fields.get(i).getType(), i);
        }
        var constructed = (PlaylistEntryId) allFieldsConstructor.newInstance(values);
        var viaSetters = new PlaylistEntryId();
        for (int i = 0; i < fields.size(); i++) {
            var field = fields.get(i);
            var suffix = Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
            PlaylistEntryId.class.getMethod("set" + suffix, field.getType())
                    .invoke(viaSetters, values[i]);
        }
        assertEquals(viaSetters, constructed);
    }

    @Test
    void everyKeyFieldHasMatchingGetterAndSetter() throws ReflectiveOperationException {
        var key = new PlaylistEntryId();

        for (var field : keyFields()) {
            var suffix = Character.toUpperCase(field.getName().charAt(0)) + field.getName().substring(1);
            var getter = PlaylistEntryId.class.getMethod("get" + suffix);
            var setter = PlaylistEntryId.class.getMethod("set" + suffix, field.getType());
            var value = sampleValue(field.getType(), false);

            assertEquals(field.getType(), getter.getReturnType(), field.getName() + " getter type");
            setter.invoke(key, value);
            assertEquals(value, getter.invoke(key), field.getName() + " accessor round trip");
        }
    }

    @Test
    void emptyKeysWithNullComponentsAreEqualAndHaveEqualHashCodes() {
        var first = new PlaylistEntryId();
        var second = new PlaylistEntryId();

        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    @Test
    void equalityAndHashCodeIncludeEveryKeyField() throws ReflectiveOperationException {
        var fields = keyFields();
        assertFalse(fields.isEmpty(), "The composite key must contain key fields");

        for (var changedField : fields) {
            var first = new PlaylistEntryId();
            var second = new PlaylistEntryId();

            for (var field : fields) {
                var firstValue = sampleValue(field.getType(), false);
                var secondValue = field.equals(changedField)
                        ? sampleValue(field.getType(), true)
                        : firstValue;
                field.set(first, firstValue);
                field.set(second, secondValue);
            }

            assertNotEquals(first, second,
                    () -> "equals must include key field " + changedField.getName());
        }

        var first = new PlaylistEntryId();
        var second = new PlaylistEntryId();
        for (var field : fields) {
            var value = sampleValue(field.getType(), false);
            field.set(first, value);
            field.set(second, value);
        }
        assertEquals(first, second);
        assertEquals(first.hashCode(), second.hashCode());
    }

    private static List<Field> keyFields() {
        return Arrays.stream(PlaylistEntryId.class.getDeclaredFields())
                .filter(field -> !Modifier.isStatic(field.getModifiers()))
                .filter(field -> !field.isSynthetic())
                .peek(field -> field.setAccessible(true))
                .toList();
    }

    private static Object sampleValue(Class<?> type, int index) {
        if (type == Long.class) {
            return 100L + index;
        }
        if (type == Integer.class) {
            return 100 + index;
        }
        if (type == String.class) {
            return "value-" + index;
        }
        throw new AssertionError("Add a test value for composite-key type " + type.getName());
    }

    private static Object sampleValue(Class<?> type, boolean alternate) {
        if (type == Long.class) {
            return alternate ? 2L : 1L;
        }
        if (type == Integer.class) {
            return alternate ? 2 : 1;
        }
        if (type == String.class) {
            return alternate ? "second" : "first";
        }
        throw new AssertionError("Add a test value for composite-key type " + type.getName());
    }
}
