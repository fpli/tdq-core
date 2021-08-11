package com.ebay.tdq.planner.utils.udf.stringsearch;

import org.neosearch.stringsearcher.Emit;
import org.neosearch.stringsearcher.StringSearcher;

public interface StringSearcherExt<T> extends StringSearcher<T> {

  static SimpleStringSearcherBuilderExt builder() {
    return new SimpleStringSearcherBuilderExt();
  }

  Emit<T> firstMatch(CharSequence var1, int startPos);
}