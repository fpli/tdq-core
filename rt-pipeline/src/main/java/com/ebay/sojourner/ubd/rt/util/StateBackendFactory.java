package com.ebay.sojourner.ubd.rt.util;

import org.apache.flink.contrib.streaming.state.RocksDBStateBackend;
import org.apache.flink.runtime.state.StateBackend;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;

public class StateBackendFactory {
    public static String FS = "FS";
    public static String ROCKSDB = "ROCKSDB";

    public static StateBackend getStateBackend(String type) {
        StateBackend sb = null;
        if (FS.equals(type)) {
            sb = (StateBackend) new FsStateBackend("file:///opt/sojourner-ubd/checkpoint");
        } else if (ROCKSDB.equals(type)) {
            try {
                sb = (StateBackend) new RocksDBStateBackend("file:///opt/sojourner-ubd/checkpoint", true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb;
    }
}
