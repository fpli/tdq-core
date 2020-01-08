package com.ebay.sojourner.ubd.rt.connectors.filesystem;

import lombok.extern.java.Log;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.security.UserGroupInformation;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

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

        System.setProperty("java.security.krb5.conf", "/tmp/" + krb5Filename);
        System.setProperty("java.security.auth.login.config", "/tmp/" + jaasFilename);

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

        final String keytabFilename = "b_eip.keytab";
        final String principal = "b_eip@PROD.EBAY.COM";
        final String krb5Filename = "krb5.conf";
        final String jaasFilename = "jaas.conf";
        final String APOLLO_RNO = "hdfs://apollo-rno";

        writeTmpFile(keytabFilename);
        writeTmpFile(krb5Filename);
        writeTmpFile(jaasFilename);

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

        try (InputStream in = KeytabHdfsFactory.class.getResourceAsStream("/hdfs/" + filename)) {
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


}