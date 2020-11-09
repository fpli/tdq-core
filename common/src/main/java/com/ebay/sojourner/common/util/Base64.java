package com.ebay.sojourner.common.util;

public final class Base64
{
  private Base64()
  {
    // no one can create an instance of this class
  }

  /**
   * this method uses standard ending character '=' to encode a string
   *
   * @param data the array of bytes to encode
   * @return base64-coded character array.
   */
  static public String encode(byte[] data)
  {
    return Base64.encode(data, true);
  }

  /**
   * returns a String of base64-encoded characters to represent the
   * passed data array.
   *
   * @param byte[] data - the array of bytes to encode
   * @param boolean isStandard, true means to use standard ending char '=', false to
   *        use eBay ending character '*'. For eBay cookie, use false instead.
   * @return base64-coded String.
   */
  static public String encode(byte[] data, boolean isStandard)
  {
    char[] out = new char[((data.length + 2) / 3) * 4];
    char[]alphabet = isStandard ? alphabet1 : alphabet2;

    //
    // 3 bytes encode to 4 chars.  Output is always an even
    // multiple of 4 characters.
    //
    for(int i = 0, index = 0; i < data.length; i += 3, index += 4)
    {
      boolean quad = false;
      boolean trip = false;
      int val = (0xFF & (int) data[i]);

      val <<= 8;
      if((i + 1) < data.length)
      {
        val |= (0xFF & (int) data[i + 1]);
        trip = true;
      }
      val <<= 8;
      if((i + 2) < data.length)
      {
        val |= (0xFF & (int) data[i + 2]);
        quad = true;
      }

      out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
      val >>= 6;
      out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
      val >>= 6;
      out[index + 1] = alphabet[val & 0x3F];
      val >>= 6;
      out[index + 0] = alphabet[val & 0x3F];
    }

    return new String(out);
  }

  static public byte[] decode(String strData)
  {
    return decode(strData, true);
  }

  /**
   * return decoded array of bytes
   *
   * @param String strData a base64-encoded string
   * @param boolean isStandard - indicating whether the string was encoded
   *        with standard ending character("=").
   * @return decoded byte array
   */
  static public byte[] decode(String strData, boolean isStandard)
  {
    char [] data = strData.toCharArray();
    int len = ((data.length + 3) / 4) * 3;
    final char endingChar = isStandard ? '=' : '*';

    if (data.length > 0 && data[data.length - 1] == endingChar)
      --len;
    if (data.length > 1 && data[data.length - 2] == endingChar)
      --len;

    byte[] out = new byte[len];
    int shift = 0; // # of excess bits stored in accum
    int accum = 0; // excess bits
    int index = 0;
    for(int ix = 0; ix < data.length; ix++)
    {
      int value = codes[data[ix] & 0xFF]; // ignore high byte of char
      if (value >= 0)			// skip over non-code
      {
        accum <<= 6;		// bits shift up by 6 each time thru
        shift += 6;			// loop, with new bits being put in
        accum |= value;		// at the bottom.
        if(shift >= 8)	 	// whenever there are 8 or more shifted in,
        {
          shift -= 8;		// write them out (from the top, leaving any
          out[index++] =	// excess at the bottom for next iteration.
              (byte)((accum >> shift) & 0xff);
        }
      }
    }

    return out;
  }

  //
  // code characters for values 0..63
  //
  final private static char[] alphabet1 = new char[65];
  final private static char[] alphabet2 = new char[65];
  static
  {
    for(int i = 'A'; i <= 'Z'; i++)
      alphabet1[i - 'A'] = (char)i;

    for (int i = 'a'; i <= 'z'; i++)
      alphabet1[(26 + i - 'a')] = (char)i;

    for (int i = '0'; i <= '9'; i++)
      alphabet1[(52 + i - '0')] = (char)i;

    alphabet1[62] = '+';
    alphabet1[63] = '/';

    System.arraycopy(alphabet1, 0, alphabet2, 0, alphabet1.length);

    alphabet1[64] = '=';
    alphabet2[64] = '*';
  }

  //
  // lookup table for converting base64 characters to value in range 0..63
  //
  final static private byte[] codes = new byte[256];
  static
  {
    for(int i = 0; i < 256; i++)
      codes[i] = -1;

    for(int i = 'A'; i <= 'Z'; i++)
      codes[i] = (byte)(i - 'A');

    for(int i = 'a'; i <= 'z'; i++)
      codes[i] = (byte)(26 + i - 'a');

    for(int i = '0'; i <= '9'; i++)
      codes[i] = (byte)(52 + i - '0');

    codes['+'] = 62;
    codes['/'] = 63;
  }

  // DON'T put main(String[] args) here. It is in KernelTests
}