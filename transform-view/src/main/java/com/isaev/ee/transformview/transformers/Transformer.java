package com.isaev.ee.transformview.transformers;

import java.io.IOException;

public interface Transformer<T, R> {

    R transform() throws IOException;
}
