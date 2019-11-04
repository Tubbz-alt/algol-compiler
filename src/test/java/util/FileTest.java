package util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileTest {

    @Test
    void shouldReadSingleCharFromFile() {
        File file = new File("src/test/resources/util/samples/sample1.alg");

        assertEquals('b', file.readChar());
        assertEquals('e', file.readChar());
        assertEquals('g', file.readChar());
        assertEquals('i', file.readChar());
        assertEquals('n', file.readChar());

        assertEquals(' ', file.readChar());

        assertEquals('t', file.readChar());
        assertEquals('o', file.readChar());

        assertEquals('\n', file.readChar());
        assertEquals('\n', file.readChar());

        assertEquals('e', file.readChar());
        assertEquals('n', file.readChar());
        assertEquals('d', file.readChar());

        assertEquals('\n', file.readChar());

        assertEquals('\0', file.readChar());
    }
}