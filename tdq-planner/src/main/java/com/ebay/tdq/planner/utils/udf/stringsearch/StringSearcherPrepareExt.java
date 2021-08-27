package com.ebay.tdq.planner.utils.udf.stringsearch;

import org.neosearch.stringsearcher.StringSearcherPrepare;

/***
 * To implement an StringSearcher-Algorithm, the developer must implement
 * StringSearcherPrepare and StringSearcher.
 * <p>
 * StringSearcherPrepare contains three methods to holds a text ("the fragment")
 * an emits some output. If <code>isMatch</code> returns true, the token matched
 * a search term.
 *
 * @author Daniel Beck
 *
 * @param <T> The type of the emitted payloads.
 */
public interface StringSearcherPrepareExt<T> extends StringSearcherPrepare<T> {

  StringSearcherExt<T> build();
}