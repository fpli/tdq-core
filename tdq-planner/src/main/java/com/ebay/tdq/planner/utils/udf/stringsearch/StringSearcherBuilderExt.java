package com.ebay.tdq.planner.utils.udf.stringsearch;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import org.neosearch.stringsearcher.Algorithm;
import org.neosearch.stringsearcher.StringSearcherBuilder;
import org.neosearch.stringsearcher.StringSearcherConfig;

public class StringSearcherBuilderExt<T> extends StringSearcherBuilder<T> {

  private final StringSearcherConfig config = new StringSearcherConfig();

  private StringSearcherPrepareExt<T> stringMatcher;

  private Queue<Map.Entry<String, T>> stringsearchPayloads = new LinkedList<>();

  private Algorithm algorithm;

  /**
   * Creates a string searcher builder. It defaults to the AHO_COHARICK string matching algorithm. The defaut algorithm
   * can be overriden with
   * <code>algorithm()</code>.
   */
  public StringSearcherBuilderExt() {
    this.algorithm = Algorithm.AHO_COHARICK;
  }

  /**
   * Sets the string searching implementation to be used.
   *
   * @param algorithm Algorithm to use.
   * @return This builder.
   */
  public StringSearcherBuilderExt<T> algorithm(Algorithm algorithm) {
    this.algorithm = algorithm;
    return this;
  }

  /**
   * Configure the StringSearcher to ignore case when searching for keywords in the text. This must be called before
   * calling addSearchString because the algorithm converts keywords to lowercase as they are added, depending on this
   * case sensitivity setting.
   *
   * @return This builder.
   */
  public StringSearcherBuilderExt<T> ignoreCase() {
    this.config.setCaseInsensitive(true);
    return this;
  }

  /**
   * Configure the Trie to ignore overlapping keywords.
   *
   * @return This builder.
   */
  public StringSearcherBuilderExt<T> ignoreOverlaps() {
    this.config.setAllowOverlaps(false);
    return this;
  }

  /**
   * Adds a search string to the StringSearchers list of text search keywords. No payload is supplied.
   *
   * @param searchString The search string to add to the list.
   * @return This builder.
   * @throws NullPointerException if the keyword is null.
   */
  public StringSearcherBuilderExt<T> addSearchString(final String searchString) {
    addSearchStringImpl(searchString, null);
    return this;
  }

  /**
   * Adds an array of search strings without associated payloads to the StringSearcher.
   *
   * @param searchStrings Search String to add to the StringSearcher.
   * @return This builder.
   */
  public StringSearcherBuilderExt<T> addSearchStrings(final String... searchStrings) {
      for (String string : searchStrings) {
          addSearchStringImpl(string, null);
      }

    return this;
  }

  /**
   * Adds a collections of search strings without associated payloads to the StringSearcher.
   *
   * @param searchStrings Search String to add to the StringSearcher.
   * @return This builder.
   */
  public StringSearcherBuilderExt<T> addSearchStrings(final Collection<String> searchStrings) {
      for (String string : searchStrings) {
          addSearchStringImpl(string, null);
      }

    return this;
  }

  /**
   * Adds a keyword and a payload to the StringSeacher' list of text search keywords.
   *
   * @param searchString The keyword to add to the list.
   * @return This builder.
   * @throws NullPointerException if the keyword is null.
   */
  public StringSearcherBuilderExt<T> addSearchString(final String searchString, final T payload) {
    addSearchStringImpl(searchString, payload);
    return this;
  }

  private void addSearchStringImpl(final String searchString, T payload) {
    this.stringsearchPayloads.add(new AbstractMap.SimpleEntry<>(searchString, payload));
  }

  /**
   * Adds a keyword and a payload to the Trie's list of text search keywords.
   *
   * @param entry The keyword and its payload to add to the list.
   * @return This builder.
   * @throws NullPointerException if the keyword is null.
   */
  public StringSearcherBuilderExt<T> addSearchString(final Map.Entry<String, T> entry) {
    this.stringsearchPayloads.add(entry);
    return this;
  }

  /**
   * Configure the StringSearcher to match whole keywords in the text.
   *
   * @return This builder.
   */
  public StringSearcherBuilderExt<T> onlyWholeWords() {
    this.config.setOnlyWholeWords(true);
    return this;
  }

  /**
   * Configure the Trie to match whole keywords that are separated by whitespace in the text. For example, "this keyword
   * thatkeyword" would only match the first occurrence of "keyword".
   *
   * @return This builder.
   */
  public StringSearcherBuilderExt<T> onlyWholeWordsWhiteSpaceSeparated() {
    this.config.setOnlyWholeWordsWhiteSpaceSeparated(true);
    return this;
  }

  /**
   * Configure the Trie to stop after the first keyword is found in the text.
   *
   * @return This builder.
   */
  public StringSearcherBuilderExt<T> stopOnHit() {
    this.config.setStopOnHit(true);
    return this;
  }

  /**
   * Constructs a StringSearcher based on the builder settings.
   *
   * @return The configured StringSearcher.
   */
  public StringSearcherExt<T> build() {
    if (this.algorithm == Algorithm.AHO_COHARICK) {
      this.stringMatcher = new TrieExt<T>(this.config);
      Map.Entry<String, T> simpleEntry = null;
        while ((simpleEntry = stringsearchPayloads.poll()) != null) {
            stringMatcher.addSearchString(simpleEntry.getKey(), simpleEntry.getValue());
        }

      return this.stringMatcher.build();
    }
    return null;
  }
}