//import org.apache.hadoop.hbase.HBaseTestingUtility;
//import org.apache.hadoop.hbase.MiniHBaseCluster;
//import org.apache.hadoop.hbase.TableName;
//import org.apache.hadoop.hbase.Waiter;
//import org.apache.hadoop.hbase.client.*;
//import org.apache.hadoop.hbase.util.Bytes;
//import org.apache.hadoop.hbase.util.JVMClusterUtil;
//import org.junit.Assert;
//import org.junit.Test;
//import util.Logger;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.junit.Assert.fail;
//
//public class TestMultiParallel {
//    private static final TableName TEST_TABLE = TableName.valueOf("multi_test_table");
//    private static final byte [][] KEYS = makeKeys();
//    private static final byte[] VALUE = Bytes.toBytes("value");
//    private static final byte[] QUALIFIER = Bytes.toBytes("qual");
//    private static final String FAMILY = "family";
//    private static final byte[] BYTES_FAMILY = Bytes.toBytes(FAMILY);
//    private static final HBaseTestingUtility UTIL = new HBaseTestingUtility();
//
//    @Test(timeout =360000)
//    public void testFlushCommitsWithAbort() throws Exception{
//        HBaseTestingUtility UTIL = new HBaseTestingUtility();
//
//        MiniHBaseCluster hbaseCluster = UTIL.startMiniCluster();
//        UTIL.deleteTable(Bytes.toBytes("mytable"));
//    }
//    //真正进行测试地方
////    private void doTestFlushCommits(boolean doAbort) throws Exception {
////        // Load the data
////        Logger.info("get new table");
////        Table table = UTIL.getConnection().getTable(TEST_TABLE);
////        table.setWriteBufferSize(10 * 1024 * 1024);
////
////        Logger.info("constructPutRequests");
////        List<Put> puts = constructPutRequests();
////        table.put(puts);
////        Logger.info("puts");
////        final int liveRScount = UTIL.getMiniHBaseCluster().getLiveRegionServerThreads()
////                .size();
////        assert liveRScount > 0;
////        JVMClusterUtil.RegionServerThread liveRS = UTIL.getMiniHBaseCluster()
////                .getLiveRegionServerThreads().get(0);
////        if (doAbort) {
////            liveRS.getRegionServer().abort("Aborting for tests",
////                    new Exception("doTestFlushCommits"));
////            // If we wait for no regions being online after we abort the server, we
////            // could ensure the master has re-assigned the regions on killed server
////            // after writing successfully. It means the server we aborted is dead
////            // and detected by matser
////            while (liveRS.getRegionServer().getNumberOfOnlineRegions() != 0) {
////                Thread.sleep(100);
////            }
////            // try putting more keys after the abort. same key/qual... just validating
////            // no exceptions thrown
////            puts = constructPutRequests();
////            table.put(puts);
////        }
////
////        Logger.info("validating loaded data");
////        validateLoadedData(table);
////
////        // Validate server and region count
////        List<JVMClusterUtil.RegionServerThread> liveRSs = UTIL.getMiniHBaseCluster().getLiveRegionServerThreads();
////        int count = 0;
////        for (JVMClusterUtil.RegionServerThread t: liveRSs) {
////            count++;
////            Logger.info("Count=" + count + ", Alive=" + t.getRegionServer());
////        }
////        Logger.info("Count=" + count);
////        Assert.assertEquals("Server count=" + count + ", abort=" + doAbort,
////                (doAbort ? (liveRScount - 1) : liveRScount), count);
////        if (doAbort) {
////            UTIL.getMiniHBaseCluster().waitOnRegionServer(0);
////            UTIL.waitFor(15 * 1000, new Waiter.Predicate<Exception>() {
////                @Override
////                public boolean evaluate() throws Exception {
////                    return UTIL.getMiniHBaseCluster().getMaster()
////                            .getClusterStatus().getServersSize() == (liveRScount - 1);
////                }
////            });
////            UTIL.waitFor(15 * 1000, UTIL.predicateNoRegionsInTransition());
////        }
////
////        table.close();
////        Logger.info("done");
////    }
//
//    private List<Put> constructPutRequests() {
//        List<Put> puts = new ArrayList<>();
//        for (byte[] k : KEYS) {
//            Put put = new Put(k);
//            put.add(BYTES_FAMILY, QUALIFIER, VALUE);
//            puts.add(put);
//        }
//        return puts;
//    }
//
//
//    private void validateLoadedData(Table table) throws IOException {
//        // get the data back and validate that it is correct
//        Logger.info("Validating data on " + table);
//        List<Get> gets = new ArrayList<Get>();
//        for (byte[] k : KEYS) {
//            Get get = new Get(k);
//            get.addColumn(BYTES_FAMILY, QUALIFIER);
//            gets.add(get);
//        }
//        int retryNum = 10;
//        Result[] results = null;
//        do  {
//            results = table.get(gets);
//            boolean finished = true;
//            for (Result result : results) {
//                if (result.isEmpty()) {
//                    finished = false;
//                    break;
//                }
//            }
//            if (finished) {
//                break;
//            }
//            try {
//                Thread.sleep(10);
//            } catch (InterruptedException e) {
//            }
//            retryNum--;
//        } while (retryNum > 0);
//
//        if (retryNum == 0) {
//            fail("Timeout for validate data");
//        } else {
//            if (results != null) {
//                for (Result r : results) {
//                    Assert.assertTrue(r.containsColumn(BYTES_FAMILY, QUALIFIER));
//                    Assert.assertEquals(0, Bytes.compareTo(VALUE, r
//                            .getValue(BYTES_FAMILY, QUALIFIER)));
//                }
//                Logger.info("Validating data on " + table + " successfully!");
//            }
//        }
//    }
//
//    private static byte[][] makeKeys() {
//        byte [][] starterKeys = HBaseTestingUtility.KEYS;
//        // Create a "non-uniform" test set with the following characteristics:
//        // a) Unequal number of keys per region
//
//        // Don't use integer as a multiple, so that we have a number of keys that is
//        // not a multiple of the number of regions
//        int numKeys = (int) ((float) starterKeys.length * 10.33F);
//
//        List<byte[]> keys = new ArrayList<byte[]>();
//        for (int i = 0; i < numKeys; i++) {
//            int kIdx = i % starterKeys.length;
//            byte[] k = starterKeys[kIdx];
//            byte[] cp = new byte[k.length + 1];
//            System.arraycopy(k, 0, cp, 0, k.length);
//            cp[k.length] = new Integer(i % 256).byteValue();
//            keys.add(cp);
//        }
//
//        // b) Same duplicate keys (showing multiple Gets/Puts to the same row, which
//        // should work)
//        // c) keys are not in sorted order (within a region), to ensure that the
//        // sorting code and index mapping doesn't break the functionality
//        for (int i = 0; i < 100; i++) {
//            int kIdx = i % starterKeys.length;
//            byte[] k = starterKeys[kIdx];
//            byte[] cp = new byte[k.length + 1];
//            System.arraycopy(k, 0, cp, 0, k.length);
//            cp[k.length] = new Integer(i % 256).byteValue();
//            keys.add(cp);
//        }
//        return keys.toArray(new byte [][] {new byte [] {}});
//    }
//}
