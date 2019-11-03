package util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileTest {

    private File file;

    @BeforeEach
    void setUp() {
        file = new File("src/test/resources/samples/sample1.alg");
    }

    @Test
    void shouldReadSingleCharFromFile() {
        assertEquals('b', file.readChar());
        assertEquals('e', file.readChar());
        assertEquals('g', file.readChar());
        assertEquals('i', file.readChar());
        assertEquals('n', file.readChar());
    }
}