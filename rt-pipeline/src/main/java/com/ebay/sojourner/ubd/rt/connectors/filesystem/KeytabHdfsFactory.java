package com.ebay.sojourner.ubd.rt.connectors.filesystem;

import com.ebay.sojourner.ubd.common.model.AgentAttribute;
import com.ebay.sojourner.ubd.rt.util.ExecutionEnvUtil;
import lombok.extern.java.Log;
import org.apache.avro.reflect.ReflectData;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.functions.sink.filesystem.SojHdfsSinkWithKeytab;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static com.ebay.sojourner.ubd.rt.common.constants.PropertiesConstants.PROD_CONFIG;
import static org.apache.hadoop.fs.CommonConfigurationKeysPublic.HADOOP_SECURITY_AUTHENTICATION;

/**
 * Created  2019-08-14 16:21
 *
 * @author : Unikal Liu
 * @version : 1.0.0
 */
@Log
public class KeytabHdfsFactory {

    public static UserGroupInformation getUGI( ParameterTool parameterTool) throws IOException, InterruptedException {

        final boolean isProd = parameterTool.getBoolean(PROD_CONFIG);
        final String keyTabFilename = parameterTool.getRequired("keyTab");
        final String principal = parameterTool.getRequired("principal");
        final String hadoopFs = parameterTool.getRequired("hadoopFs");
        final String krb5Filename = "krb5.conf";
        final String jaasFilename = "jaas.conf";
        UserGroupInformation ugi;

        org.apache.hadoop.conf.Configuration hadoopConfig = getConf(isProd);

        writeTmpFile(keyTabFilename);
        writeTmpFile(krb5Filename);
        writeTmpFile(jaasFilename);
//        storeFile(keyTabFilename);
//        storeFile(krb5Filename);
//        storeFile(jaasFilename);
//        System.setProperty("java.security.krb5.conf","/tmp/" + krb5Filename);
//        System.setProperty("java.security.auth.login.config", "/tmp/" + jaasFilename);

//        System.setProperty("java.security.krb5.conf",KeytabHdfsFactory.class.getResource("/11/" + krb5Filename).getPath() );
        System.setProperty("java.security.auth.login.config", KeytabHdfsFactory.class.getResource("/" + jaasFilename).getPath());
        System.setProperty("sun.security.krb5.debug","true");

        UserGroupInformation.setConfiguration(hadoopConfig);

        if (isProd) {
            //UserGroupInformation.loginUserFromKeytab(principal, "/tmp/" + keytabFilename);
            //UserGroupInformation.getLoginUser().checkTGTAndReloginFromKeytab();
            //UserGroupInformation ugi = UserGroupInformation.getCurrentUser();

            ugi = UserGroupInformation.loginUserFromKeytabAndReturnUGI(principal, "/tmp/" + keyTabFilename);

        } else {
            ugi = UserGroupInformation.getCurrentUser();
        }

        return ugi;
    }


    public static org.apache.hadoop.conf.Configuration getConf(boolean isProd) throws IOException, InterruptedException {

        org.apache.hadoop.conf.Configuration hadoopConfig = new Configuration();
        hadoopConfig.set("parquet.enable.summary-metadata", "false");

        if (isProd) {

            //hadoopConfig.addResource(KeytabHdfsFactory.class.getResource(String.format("%s/core-site.xml", cluster.getConfFolder())));
            hadoopConfig.addResource("core-site.xml");
            hadoopConfig.addResource("hdfs-site.xml");
//            hadoopConfig.addResource(KeytabHdfsFactory.class.getResource("/hdfs/core-site.xml"));
//            hadoopConfig.addResource(KeytabHdfsFactory.class.getResource("/hdfs/hdfs-site.xml"));
            hadoopConfig.set("hadoop.security.authentication", "kerberos");
            hadoopConfig.set("fs.defaultFS", "viewfs://apollo-rno");
            hadoopConfig.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
            hadoopConfig.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());


        } else {

            hadoopConfig.set("fs.defaultFS", FileSystem.DEFAULT_FS);
            hadoopConfig.set(HADOOP_SECURITY_AUTHENTICATION, "simple");
        }

        //log.info("configured filesystem = " + hadoopConfig.get("fs.defaultFS"));
        return hadoopConfig;
    }

    public static UserGroupInformation getUGI() throws IOException, InterruptedException {

        final String keytabFilename = "o_ubi.keytab";
        final String principal = "o_ubi@PROD.EBAY.COM";
        final String krb5Filename = "krb5.conf";
        final String jaasFilename = "jaas.conf";
        final String APOLLO_RNO = "hdfs://apollo-rno";

        writeTmpFile(keytabFilename);
        writeTmpFile(krb5Filename);
        writeTmpFile(jaasFilename);

        storeFile(keytabFilename);
        storeFile(krb5Filename);
        storeFile(jaasFilename);
        System.setProperty("java.security.krb5.conf", "/tmp/" + krb5Filename);
        System.setProperty("java.security.auth.login.config", "/tmp/" + jaasFilename);

        org.apache.hadoop.conf.Configuration hadoopConfig = new Configuration();

        //hadoopConfig.addResource(KeytabHdfsFactory.class.getResource(String.format("%s/core-site.xml", cluster.getConfFolder())));
        hadoopConfig.addResource(Objects.requireNonNull(KeytabHdfsFactory.class.getClassLoader().getResource("core-site.xml")));
        hadoopConfig.addResource(Objects.requireNonNull(KeytabHdfsFactory.class.getClassLoader().getResource("hdfs-site.xml")));
        //hadoopConfig.addResource(Objects.requireNonNull(KeytabHdfsFactory.class.getClassLoader().getResource("mapred-site.xml")));
        //hadoopConfig.addResource(Objects.requireNonNull(KeytabHdfsFactory.class.getClassLoader().getResource("federation-mapping.xml")));


        hadoopConfig.set("hadoop.security.authentication", "kerberos");
        hadoopConfig.set("fs.defaultFS", APOLLO_RNO);

        hadoopConfig.set("fs.hdfs.impl", org.apache.hadoop.hdfs.DistributedFileSystem.class.getName());
        hadoopConfig.set("fs.file.impl", org.apache.hadoop.fs.LocalFileSystem.class.getName());

        UserGroupInformation.setConfiguration(hadoopConfig);


        //UserGroupInformation.loginUserFromKeytab(principal, "/tmp/" + keytabFilename);
        //UserGroupInformation.getLoginUser().checkTGTAndReloginFromKeytab();
        //UserGroupInformation ugi = UserGroupInformation.getCurrentUser();

        UserGroupInformation ugi = UserGroupInformation.loginUserFromKeytabAndReturnUGI(principal, "/tmp/" + keytabFilename);

        return ugi;
    }


    public static void writeTmpFile(String filename) {

        byte[] buffer;

        try (InputStream in = KeytabHdfsFactory.class.getResourceAsStream("/" + filename)) {
            buffer = new byte[in.available()];
            in.read(buffer);
        } catch (Exception e) {
            throw new RuntimeException(filename + " don't exist", e);

        }

        try (OutputStream outputStream = new FileOutputStream("/tmp/" + filename)) {
            outputStream.write(buffer);
        } catch (Exception e) {
            throw new RuntimeException("failed write config file: " + filename, e);
        }
    }

    public static void storeFile(String filePath) throws IllegalStateException, IOException {
        File file = new File("/tmp/"+filePath);
        //设置权限
        Set<PosixFilePermission> perms = new HashSet<PosixFilePermission>();
        perms.add(PosixFilePermission.OWNER_READ);
        perms.add(PosixFilePermission.OWNER_WRITE);
        perms.add(PosixFilePermission.OWNER_EXECUTE);
        perms.add(PosixFilePermission.GROUP_READ);
        perms.add(PosixFilePermission.GROUP_EXECUTE);
        perms.add(PosixFilePermission.OTHERS_READ);
        perms.add(PosixFilePermission.OTHERS_EXECUTE);
        try {
            //设置文件和文件夹的权限
            Path pathParent = Paths.get(file.getParentFile().getAbsolutePath());
            Path pathDest = Paths.get(file.getAbsolutePath());
            Files.setPosixFilePermissions(pathParent, perms);//修改文件夹路径的权限
            Files.setPosixFilePermissions(pathDest, perms);//修改图片文件的权限
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args ) throws Exception {
//        System.out.println(ReflectData.get().getSchema(AgentAttribute.class));
//        System.out.println(SojHdfsSinkWithKeytab.class.getResource("").getPath());
//        System.out.println(SojHdfsSinkWithKeytab.class.getResource("/hdfs/o_ubi.keytab").getPath());
//        System.out.println(SojHdfsSinkWithKeytab.class.getResource("/").getPath());
////        Files.copy()
//        System.out.println(SojHdfsSinkWithKeytab.class.getResource("/"));
//
//        ByteBuffer buffer = ByteBuffer.allocateDirect(10 * 1024 * 1024);

        Configuration conf = new Configuration();
        //conf.addResource("/opt/jediael/hadoop-1.2.1/conf/core-site.xml");
        conf.addResource("/core-site.xml");
        getUGI( ExecutionEnvUtil.createParameterTool(new String[]{"--a=b"}));
        System.out.println(conf.get("hadoop.http.authentication.composite.default-non-browser-handler-type"));
        System.out.println(conf.get("hadoop.tmp.dir"));
        System.out.println(conf.get("io.sort.mb"));


    }

}