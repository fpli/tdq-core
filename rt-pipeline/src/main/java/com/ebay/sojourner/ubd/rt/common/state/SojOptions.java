package com.ebay.sojourner.ubd.rt.common.state;

import org.apache.flink.contrib.streaming.state.OptionsFactory;
import org.rocksdb.BlockBasedTableConfig;
import org.rocksdb.BloomFilter;
import org.rocksdb.ColumnFamilyOptions;
import org.rocksdb.CompactionStyle;
import org.rocksdb.CompressionType;
import org.rocksdb.DBOptions;

/**
 * @see <a href="https://github.com/facebook/rocksdb/wiki/RocksDB-Tuning-Guide">RocksDB Tuning
 * Guide</a>
 * @see <a href="https://github.com/facebook/rocksdb/wiki/Memory-usage-in-RocksDB">Memory usage in
 * RocksDB</a>
 * @see <a href="https://github.com/facebook/rocksdb/wiki/Block-Cache">Block-Cache</a>
 * @see <a href="https://github.com/facebook/rocksdb/wiki/Write-Buffer-Manager">Write Buffer
 * Manager</a>
 */
public class SojOptions implements OptionsFactory {

  public DBOptions createDBOptions(DBOptions currentOptions) {
    return currentOptions
        .setIncreaseParallelism(4)
        .setMaxBackgroundCompactions(1)
        .setMaxBackgroundFlushes(1)
        .setUseFsync(false)
        .setMaxOpenFiles(-1);
  }

  public ColumnFamilyOptions createColumnOptions(ColumnFamilyOptions currentOptions) {
    return currentOptions
        // By default it is Snappy. RockDB team believe LZ4 is almost always better than Snappy.
        // Snappy is left as default to avoid unexpected compatibility problems to previous users.
        .setCompressionType(CompressionType.LZ4_COMPRESSION)
        .setTableFormatConfig(
            new BlockBasedTableConfig()
                // default: 8 MB
                .setBlockCacheSize(64 * 1024 * 1024)
                .setFilter(new BloomFilter())
                // default: cache_index_and_filter_blocks=false
                .setCacheIndexAndFilterBlocks(true)
                // default: pin_l0_filter_and_index_blocks_in_cache=false
                .setPinL0FilterAndIndexBlocksInCache(true))
        // default: cache_index_and_filter_blocks_with_high_priority=false
        // not configurable in RocksJava
        .setCompactionStyle(CompactionStyle.LEVEL)
        // default: level0_file_num_compaction_trigger=4
        .setLevel0FileNumCompactionTrigger(10)
        // default: write_buffer_size=67108864
        .setWriteBufferSize(64 * 1024 * 1024)
        // default: max_write_buffer_number=2
        .setMaxWriteBufferNumber(4)
        // default: min_write_buffer_number_to_merge=1
        .setMinWriteBufferNumberToMerge(2);
  }
}
