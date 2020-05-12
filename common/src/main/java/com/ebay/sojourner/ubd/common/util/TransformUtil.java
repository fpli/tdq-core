package com.ebay.sojourner.ubd.common.util;

import java.net.InetAddress;
import java.security.MessageDigest;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * @author lubliu
 */
public class TransformUtil {

	private static final Logger LOGGER = Logger.getLogger(TransformUtil.class);

	private static final Map<Character, Integer> dict = new HashMap<Character, Integer>();
	private static final Map<Integer, Character> reDict = new HashMap<Integer, Character>();
	private static final int LEN = 16;
	private static final int HOLE_LEN = 32;
	private static final int BIT_OFF = 4;
	private static final int OFFSET = 10;
	private static final long MARK = 0xf;

	static{
		for(int i=0;i<10;++i){
			dict.put((char)('0'+i), i);
			reDict.put(i, (char)('0'+i));
		}
		for(int i=0;i<6;++i){
			dict.put((char)('a'+i), OFFSET+i);
			reDict.put(OFFSET+i, (char)('a'+i));
		}
	}


	public static Long[] mD522Long(CharSequence cmd){
		if(cmd == null){
			return new Long[]{null, null};
		}
		if(StringUtils.isBlank(cmd)) {
			return new Long[]{0l, 0l};
		}
		String md = cmd.toString();
		int firstLen = LEN;
		int secondLen = 0;
		if(md.length() <= LEN) {
			firstLen = md.length();
		} else {
			secondLen = md.length();
		}
		Long part1 = getOneLong(md.substring(0, firstLen));
		Long part2 = null;
		if(secondLen > 0) {
			part2 = getOneLong(md.substring(LEN, secondLen));
		}
		return new Long[]{part1, part2};
	}

	private static Long getOneLong(String part){
		long res = 0;
		for(int i=0;i<part.length();++i){
			char c = part.charAt(i);
			Integer bitC = dict.get(c);
			if(bitC == null){
				// change behavior, convert illegal char. instead of return the whole as null
				bitC = Math.abs(c % 16);
			}
			long bb = bitC;
			res |= (bb<<(i*BIT_OFF));
		}
		return res;
	}
	private static String recoveryPartMD5(long part){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<LEN;++i){
			sb.append(reDict.get((int)(part&MARK)));
			part >>= BIT_OFF;
		}
		return sb.toString();
	}

	public static CharSequence recoveryMD5(long part1, long part2){
		return recoveryPartMD5(part1)+recoveryPartMD5(part2);
	}

	public static Integer long2Int(Long l){
		if(l == null){
			return null;
		}
		return l.intValue();
	}

	public static Integer ipToInt(CharSequence cipAddr) {
		if (StringUtils.isBlank(cipAddr)) {
			return null;
		}
		String ipAddr = cipAddr.toString();

        try {
            byte[] bytes = InetAddress.getByName(ipAddr).getAddress();
            if(bytes != null && bytes.length == 4) {
                return bytesToInt(bytes);
            } else {
                return ipAddr.hashCode();
            }
        } catch (Exception e) {
            return ipAddr.hashCode();
        }
	}

	public static String int2Ip(int ip){
		StringBuilder sb = new StringBuilder();
		int MASK = 0xFF;
		sb.append((ip>>24)&MASK).append(".");
		sb.append((ip>>16)&MASK).append(".");
		sb.append((ip>>8)&MASK).append(".");
		sb.append(ip&MASK);
		return sb.toString();
	}

	public static String getMD5(CharSequence cmessage) {
		if (cmessage == null) {
			return null;
		}
		String message = cmessage.toString();
		String md5str = "";
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");//NOSONAR
			byte[] input = message.getBytes("UTF-8");
			byte[] buff = md.digest(input);
			md5str = bytesToHex(buff);
		} catch (Exception e) {
			LOGGER.error(e);
		}
		return md5str;
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuffer md5str = new StringBuffer();

		for (int i = 0; i < bytes.length; i++) {
			int digital = bytes[i];

			if (digital < 0) {
				digital += 256;
			}
			if (digital < 16) {
				md5str.append("0");
			}
			md5str.append(Integer.toHexString(digital));
		}
		return md5str.toString().toLowerCase();
	}

	private static int bytesToInt(byte[] bytes) {
		int addr = bytes[3] & 0xFF;
		addr |= bytes[2] << 8 & 0xFF00;
		addr |= bytes[1] << 16 & 0xFF0000;
		addr |= bytes[0] << 24 & 0xFF000000;
		return addr;
	}

//	private static byte[] ipToBytesByInet(String ipAddr) {
//		if(ipAddr == null) {
//			return null;
//		}
//		try {
//			return InetAddress.getByName(ipAddr).getAddress();
//		} catch (UnknownHostException e) {
//			throw new RuntimeException(e);
//		}
//	}

	public static long toLongCheckNull(Long l) {
		if(l == null) {
			return 0;
		} else {
			return l;
		}
	}

	public static int toIntCheckNull(Integer l) {
		if(l == null) {
			return 0;
		} else {
			return l;
		}
	}

	public static String toStringCheckNull(CharSequence l) {
		if(l == null) {
			return null;
		} else {
			return l.toString();
		}
	}

	public static String toStringNotNull(CharSequence l) {
		if(l == null) {
			return "";
		} else {
			return l.toString();
		}
	}

	public static void main(String args[]) throws ParseException {
		/*String guid = "fffecd0d14b0a5e5d8620064ffe7ab47";
		long t = 1426880352790l;
		System.out.println("Old Time:	"+t);
		System.out.println("Int Time:	"+timeLong2Int(t));
		System.out.println("Recovery Time:	"+recoverTimeFromInt(timeLong2Int(t), "20150320/12"));
		Long[] tl = mD522Long(guid);
		System.out.println("Old Guid:\t"+guid);
		System.out.println("New Guid:\t"+tl[0]+"\t"+tl[1]);
		System.out.println("Recovery Guid:\t"+recoveryMD5(tl[0], tl[1]));
		
		System.out.println("New Key:\t"+tl[0]+"\t"+tl[1]+"\t"+timeLong2Int(t));
		*/
		String time = "20150408/17";

	}

}