package net.karmats.sveled.kml.parser;

import java.io.InputStream;

public abstract class BaseParser implements Parser {

    final InputStream source;

    protected BaseParser(InputStream source) {
        this.source = source;
    }

    protected InputStream getInputStream() {
        return source;
    }
}
