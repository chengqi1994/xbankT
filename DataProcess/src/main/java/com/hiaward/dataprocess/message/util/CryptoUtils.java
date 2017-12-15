package com.hiaward.dataprocess.message.util;

/**
 * <p>Title: CryptoUtils</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * @author Yu Gang
 * @version 1.0
 */

public class CryptoUtils
{
/// Ascii to hex value convertion table.
  protected static byte asc_hex_tab[] = {
      0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
      0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
      0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
      0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15,
      0, 10, 11, 12, 13, 14, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0,
      0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
      0, 10, 11, 12, 13, 14, 15,  0,  0,  0,  0,  0,  0,  0,  0,  0,
      0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
      0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
      0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
      0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
      0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
      0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
      0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
      0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,
      0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0,  0 };

  /// Hex value to ascii convertion table.
  public static char hex_asc_tab[] =
      { '0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F' };

  /// Uppercase convertion table.
  protected static char toupper_tab[] = {
      0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15,
      16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
      32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47,
      48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63,
      64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79,
      80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 95,
      96, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79,
      80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90,123,124,125,126,127,
      128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,
      144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,
      160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,
      176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,
      192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,
      208,209,210,211,212,213,214,215,216,217,218,219,220,221,222,223,
      224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,
      240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255 };

  /// ---- Lowercase convertion table. ----
  protected static char tolower_tab[] = {
      0,  1,  2,  3,  4,  5,  6,  7,  8,  9, 10, 11, 12, 13, 14, 15,
      16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31,
      32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47,
      48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63,
      64, 97, 98, 99,100,101,102,103,104,105,106,107,108,109,110,111,
      112,113,114,115,116,117,118,119,120,121,122, 91, 92, 93, 94, 95,
      96, 97, 98, 99,100,101,102,103,104,105,106,107,108,109,110,111,
      112,113,114,115,116,117,118,119,120,121,122,123,124,125,126,127,
      128,129,130,131,132,133,134,135,136,137,138,139,140,141,142,143,
      144,145,146,147,148,149,150,151,152,153,154,155,156,157,158,159,
      160,161,162,163,164,165,166,167,168,169,170,171,172,173,174,175,
      176,177,178,179,180,181,182,183,184,185,186,187,188,189,190,191,
      192,193,194,195,196,197,198,199,200,201,202,203,204,205,206,207,
      208,209,210,211,212,213,214,215,216,217,218,219,220,221,222,223,
      224,225,226,227,228,229,230,231,232,233,234,235,236,237,238,239,
      240,241,242,243,244,245,246,247,248,249,250,251,252,253,254,255 };

   /// Odd parity convertion table.
   protected static byte odd_parity_tab[] = {
       (byte)  1,(byte)  1,(byte)  2,(byte)  2,(byte)  4,(byte)  4,(byte)  7,(byte)  7,
       (byte)  8,(byte)  8,(byte) 11,(byte) 11,(byte) 13,(byte) 13,(byte) 14,(byte) 14,
       (byte) 16,(byte) 16,(byte) 19,(byte) 19,(byte) 21,(byte) 21,(byte) 22,(byte) 22,
       (byte) 25,(byte) 25,(byte) 26,(byte) 26,(byte) 28,(byte) 28,(byte) 31,(byte) 31,
       (byte) 32,(byte) 32,(byte) 35,(byte) 35,(byte) 37,(byte) 37,(byte) 38,(byte) 38,
       (byte) 41,(byte) 41,(byte) 42,(byte) 42,(byte) 44,(byte) 44,(byte) 47,(byte) 47,
       (byte) 49,(byte) 49,(byte) 50,(byte) 50,(byte) 52,(byte) 52,(byte) 55,(byte) 55,
       (byte) 56,(byte) 56,(byte) 59,(byte) 59,(byte) 61,(byte) 61,(byte) 62,(byte) 62,
       (byte) 64,(byte) 64,(byte) 67,(byte) 67,(byte) 69,(byte) 69,(byte) 70,(byte) 70,
       (byte) 73,(byte) 73,(byte) 74,(byte) 74,(byte) 76,(byte) 76,(byte) 79,(byte) 79,
       (byte) 81,(byte) 81,(byte) 82,(byte) 82,(byte) 84,(byte) 84,(byte) 87,(byte) 87,
       (byte) 88,(byte) 88,(byte) 91,(byte) 91,(byte) 93,(byte) 93,(byte) 94,(byte) 94,
       (byte) 97,(byte) 97,(byte) 98,(byte) 98,(byte)100,(byte)100,(byte)103,(byte)103,
       (byte)104,(byte)104,(byte)107,(byte)107,(byte)109,(byte)109,(byte)110,(byte)110,
       (byte)112,(byte)112,(byte)115,(byte)115,(byte)117,(byte)117,(byte)118,(byte)118,
       (byte)121,(byte)121,(byte)122,(byte)122,(byte)124,(byte)124,(byte)127,(byte)127,
       (byte)128,(byte)128,(byte)131,(byte)131,(byte)133,(byte)133,(byte)134,(byte)134,
       (byte)137,(byte)137,(byte)138,(byte)138,(byte)140,(byte)140,(byte)143,(byte)143,
       (byte)145,(byte)145,(byte)146,(byte)146,(byte)148,(byte)148,(byte)151,(byte)151,
       (byte)152,(byte)152,(byte)155,(byte)155,(byte)157,(byte)157,(byte)158,(byte)158,
       (byte)161,(byte)161,(byte)162,(byte)162,(byte)164,(byte)164,(byte)167,(byte)167,
       (byte)168,(byte)168,(byte)171,(byte)171,(byte)173,(byte)173,(byte)174,(byte)174,
       (byte)176,(byte)176,(byte)179,(byte)179,(byte)181,(byte)181,(byte)182,(byte)182,
       (byte)185,(byte)185,(byte)186,(byte)186,(byte)188,(byte)188,(byte)191,(byte)191,
       (byte)193,(byte)193,(byte)194,(byte)194,(byte)196,(byte)196,(byte)199,(byte)199,
       (byte)200,(byte)200,(byte)203,(byte)203,(byte)205,(byte)205,(byte)206,(byte)206,
       (byte)208,(byte)208,(byte)211,(byte)211,(byte)213,(byte)213,(byte)214,(byte)214,
       (byte)217,(byte)217,(byte)218,(byte)218,(byte)220,(byte)220,(byte)223,(byte)223,
       (byte)224,(byte)224,(byte)227,(byte)227,(byte)229,(byte)229,(byte)230,(byte)230,
       (byte)233,(byte)233,(byte)234,(byte)234,(byte)236,(byte)236,(byte)239,(byte)239,
       (byte)241,(byte)241,(byte)242,(byte)242,(byte)244,(byte)244,(byte)247,(byte)247,
       (byte)248,(byte)248,(byte)251,(byte)251,(byte)253,(byte)253,(byte)254,(byte)254};

   // ---- Even parity convertion table. ----
   protected static byte even_parity_tab[] = {
       (byte)  0,(byte)  0,(byte)  3,(byte)  3,(byte)  5,(byte)  5,(byte)  6,(byte)  6,
       (byte)  9,(byte)  9,(byte) 10,(byte) 10,(byte) 12,(byte) 12,(byte) 15,(byte) 15,
       (byte) 17,(byte) 17,(byte) 18,(byte) 18,(byte) 20,(byte) 20,(byte) 23,(byte) 23,
       (byte) 24,(byte) 24,(byte) 27,(byte) 27,(byte) 29,(byte) 29,(byte) 30,(byte) 30,
       (byte) 33,(byte) 33,(byte) 34,(byte) 34,(byte) 36,(byte) 36,(byte) 39,(byte) 39,
       (byte) 40,(byte) 40,(byte) 43,(byte) 43,(byte) 45,(byte) 45,(byte) 46,(byte) 46,
       (byte) 48,(byte) 48,(byte) 51,(byte) 51,(byte) 53,(byte) 53,(byte) 54,(byte) 54,
       (byte) 57,(byte) 57,(byte) 58,(byte) 58,(byte) 60,(byte) 60,(byte) 63,(byte) 63,
       (byte) 65,(byte) 65,(byte) 66,(byte) 66,(byte) 68,(byte) 68,(byte) 71,(byte) 71,
       (byte) 72,(byte) 72,(byte) 75,(byte) 75,(byte) 77,(byte) 77,(byte) 78,(byte) 78,
       (byte) 80,(byte) 80,(byte) 83,(byte) 83,(byte) 85,(byte) 85,(byte) 86,(byte) 86,
       (byte) 89,(byte) 89,(byte) 90,(byte) 90,(byte) 92,(byte) 92,(byte) 95,(byte) 95,
       (byte) 96,(byte) 96,(byte) 99,(byte) 99,(byte)101,(byte)101,(byte)102,(byte)102,
       (byte)105,(byte)105,(byte)106,(byte)106,(byte)108,(byte)108,(byte)111,(byte)111,
       (byte)113,(byte)113,(byte)114,(byte)114,(byte)116,(byte)116,(byte)119,(byte)119,
       (byte)120,(byte)120,(byte)123,(byte)123,(byte)125,(byte)125,(byte)126,(byte)126,
       (byte)129,(byte)129,(byte)130,(byte)130,(byte)132,(byte)132,(byte)135,(byte)135,
       (byte)136,(byte)136,(byte)139,(byte)139,(byte)141,(byte)141,(byte)142,(byte)142,
       (byte)144,(byte)144,(byte)147,(byte)147,(byte)149,(byte)149,(byte)150,(byte)150,
       (byte)153,(byte)153,(byte)154,(byte)154,(byte)156,(byte)156,(byte)159,(byte)159,
       (byte)160,(byte)160,(byte)163,(byte)163,(byte)165,(byte)165,(byte)166,(byte)166,
       (byte)169,(byte)169,(byte)170,(byte)170,(byte)172,(byte)172,(byte)175,(byte)175,
       (byte)177,(byte)177,(byte)178,(byte)178,(byte)180,(byte)180,(byte)183,(byte)183,
       (byte)184,(byte)184,(byte)187,(byte)187,(byte)189,(byte)189,(byte)190,(byte)190,
       (byte)192,(byte)192,(byte)195,(byte)195,(byte)197,(byte)197,(byte)198,(byte)198,
       (byte)201,(byte)201,(byte)202,(byte)202,(byte)204,(byte)204,(byte)207,(byte)207,
       (byte)209,(byte)209,(byte)210,(byte)210,(byte)212,(byte)212,(byte)215,(byte)215,
       (byte)216,(byte)216,(byte)219,(byte)219,(byte)221,(byte)221,(byte)222,(byte)222,
       (byte)225,(byte)225,(byte)226,(byte)226,(byte)228,(byte)228,(byte)231,(byte)231,
       (byte)232,(byte)232,(byte)235,(byte)235,(byte)237,(byte)237,(byte)238,(byte)238,
       (byte)240,(byte)240,(byte)243,(byte)243,(byte)245,(byte)245,(byte)246,(byte)246,
       (byte)249,(byte)249,(byte)250,(byte)250,(byte)252,(byte)252,(byte)255,(byte)255 };

   /**
   * <p>Constructor Utils</p>
   */
  public CryptoUtils() {
  }

  /// Basic algorithms

  /// Utility routine to get the minimum number.
  public static int min( int a, int b ) {
    return a < b ? a : b;
  }

  /// Utility routine to get the maximum number.
  public static int max( int a, int b ) {
    return a > b ? a : b;
  }

  /// Utility routine to fill a block with zeros.
  public static void zeroBlock( byte[] block, int off, int len ) {
    for ( int i = off; i < off + len; ++i )
      block[i] = 0;
  }

  /// Utility routine to fill a block with zeros.
  public static void zeroBlock( byte[] block ) {
    zeroBlock( block, 0, block.length );
  }

  /// Utility routine to XOR two blocks.
  public static void xorBlock( byte[] a, int aOff, byte[] b, int bOff, byte[] dst, int dstOff, int len ) {
    for ( int i = 0; i < len; ++i ) {
      dst[dstOff + i] = (byte) ( a[aOff + i] ^ b[bOff + i] );
    }
  }

  /// Utility routine to XOR two blocks.
  public static void xorBlock( byte[] a, byte[] b, byte[] dst, int len ) {
    xorBlock(a,0,b,0,dst,0,len);
  }

  /// Utility routine to XOR two blocks.
  public static byte[] xorBlock(byte[] a, int aOff, byte[] b, int bOff, int len) {
    byte[] out = new byte[len];
    for( int i=0; i < len; i++ ) {
      out[i] = (byte) ( a[aOff+i] ^ b[bOff+i] );
    }
    return out;
  }

  /// Utility routine to XOR two blocks.
  public static byte[] xorBlock( byte[] a, byte[] b ) {
    int len = a.length ;
    return xorBlock(a,0,b,0,len);
  }

  /// Utility routine to copy one block to another.
  public static void copyBlock( byte[] src, int srcOff, byte[] dst, int dstOff, int len ) {
    for ( int i = 0; i < len; ++i )
      dst[dstOff + i] = src[srcOff + i];
  }

  /// Utility routine to copy one block to another.
  public static void copyBlock( byte[] src, byte[] dst ) {
    copyBlock( src, 0, dst, 0, src.length );
  }

  /// Utility routine to check two blocks for equality.
  public static boolean equalsBlock( byte[] a, int aOff, byte[] b, int bOff, int len ) {
    for ( int i = 0; i < len; ++i )
      if ( a[aOff + i] != b[bOff + i] )
        return false;
    return true;
  }

  /// Utility routine to check two blocks for equality.
  public static boolean equalsBlock( byte[] a, byte[] b ) {
    return equalsBlock( a, 0, b, 0, a.length );
  }

  /// Utility routine fill a block with a given byte.
  public static void fillBlock( byte[] block, int blockOff, byte b, int len ) {
    for ( int i = blockOff; i < blockOff + len; ++i )
      block[i] = b;
  }

  /// Utility routine fill a block with a given byte.
  public static void fillBlock( byte[] block, byte b ) {
    fillBlock( block, 0, b, block.length );
  }

  /// Squash bytes down to ints.
  public static void squashBytesToInts( byte[] inBytes, int inOff, int[] outInts, int outOff, int intLen ) {
    for ( int i = 0; i < intLen; ++i )
      outInts[outOff + i] =
      ( ( inBytes[inOff + i * 4    ] & 0xff ) << 24 ) |
      ( ( inBytes[inOff + i * 4 + 1] & 0xff ) << 16 ) |
      ( ( inBytes[inOff + i * 4 + 2] & 0xff ) <<  8 ) |
      ( ( inBytes[inOff + i * 4 + 3] & 0xff )       );
  }

  /// Spread ints into bytes.
  public static void spreadIntsToBytes( int[] inInts, int inOff, byte[] outBytes, int outOff, int intLen ) {
    for ( int i = 0; i < intLen; ++i ) {
      outBytes[outOff + i * 4    ] = (byte) ( ( inInts[inOff + i] >>> 24 ) & 0xff );
      outBytes[outOff + i * 4 + 1] = (byte) ( ( inInts[inOff + i] >>> 16 ) & 0xff );
      outBytes[outOff + i * 4 + 2] = (byte) ( ( inInts[inOff + i] >>>  8 ) & 0xff );
      outBytes[outOff + i * 4 + 3] = (byte) ( ( inInts[inOff + i]        ) & 0xff );
    }
  }

  /// Squash bytes down to shorts.
  public static void squashBytesToShorts( byte[] inBytes, int inOff, int[] outShorts, int outOff, int shortLen ) {
    for ( int i = 0; i < shortLen; ++i )
      outShorts[outOff + i] =
      ( ( inBytes[inOff + i * 4    ] & 0xff ) <<  8 ) |
      ( ( inBytes[inOff + i * 4 + 1] & 0xff )       );
  }

  /// Spread shorts into bytes.
  public static void spreadShortsToBytes( int[] ininShorts, int inOff, byte[] outBytes, int outOff, int shortLen ) {
    for ( int i = 0; i < shortLen; ++i ) {
      outBytes[outOff + i * 4    ] = (byte) ( ( ininShorts[inOff + i] >>>  8 ) & 0xff );
      outBytes[outOff + i * 4 + 1] = (byte) ( ( ininShorts[inOff + i]        ) & 0xff );
    }
  }

  /// Squash bytes down to ints, little-endian.
  public static void squashBytesToIntsLittle( byte[] inBytes, int inOff, int[] outInts, int outOff, int intLen ) {
    for ( int i = 0; i < intLen; ++i )
      outInts[outOff + i] =
          ( ( inBytes[inOff + i * 4    ] & 0xff )       ) |
          ( ( inBytes[inOff + i * 4 + 1] & 0xff ) <<  8 ) |
          ( ( inBytes[inOff + i * 4 + 2] & 0xff ) << 16 ) |
          ( ( inBytes[inOff + i * 4 + 3] & 0xff ) << 24 );
  }

  /// Spread ints into bytes, little-endian.
  public static void spreadIntsToBytesLittle( int[] inInts, int inOff, byte[] outBytes, int outOff, int intLen ) {
    for ( int i = 0; i < intLen; ++i ) {
      outBytes[outOff + i * 4    ] = (byte) ( ( inInts[inOff + i]        ) & 0xff );
      outBytes[outOff + i * 4 + 1] = (byte) ( ( inInts[inOff + i] >>>  8 ) & 0xff );
      outBytes[outOff + i * 4 + 2] = (byte) ( ( inInts[inOff + i] >>> 16 ) & 0xff );
      outBytes[outOff + i * 4 + 3] = (byte) ( ( inInts[inOff + i] >>> 24 ) & 0xff );
    }
  }

  /// Squash 4 bytes down to a integer number.
  public static int squashBytesToIntLittle( byte[] c ) {
    int outInt;
    outInt = ( c[0]       ) & 0x000000ff;
    outInt|= ( c[1] <<  8 ) & 0x0000ffff;
    outInt|= ( c[2] << 16 ) & 0x00ffffff;
    outInt|= ( c[3] << 24 ) & 0xffffffff;
    return outInt;
  }

  /// Spread a integer number into 4 bytes.
  public static byte[] spreadIntToBytesLittle( int l ) {
    byte[] c = new byte[4];
    c[0] = (byte) ( l        );
    c[1] = (byte) ( l >>>  8 );
    c[2] = (byte) ( l >>> 16 );
    c[3] = (byte) ( l >>> 24 );
    return c;
  }

  /// Utility routine to fill a block with random bytes.
  public static void randomBlock( byte[] block, int off, int len ) {
    for ( int i = off; i < off + len; ++i )
      block[i] = (byte) ( Math.random() * 256.0 );
  }

  /// Utility routine to fill a block with random bytes.
  public static void randomBlock( byte[] block ) {
    randomBlock( block, 0, block.length );
  }

  /// Convert a int number to a appointed string with fixed length
  /// and left fill zero.
  public static String convertIntToString( int a, int len ) {
/*
    StringBuffer q = new StringBuffer(128);
    q.setLength(len);
    while( len-- > 0 ) {
      q.replace(len,len+1,String.valueOf(a%10));
      a /= 10;
    }
    return q.toString();
*/
    String q = "";
    while( len-- > 0 ) {
      q = String.valueOf(a%10) + q;
      a /= 10;
    }
    return q;
  }

  /// Left-rotate a integer number.
  public int leftRotate( int a, int n ) {
    return ( a << n ) | ( a >>> ( 32 - n ) );
  }

  /// Right-rotate a integer number.
  public int rightRotate( int a, int n ) {
    return ( a >>> n ) | ( a << ( 32 - n ) );
  }

  /// Convert onr block into odd parity block.

  /**
  * <p>convert a data to an odd parity number</p>
  * @param a the input byte data
  * @return the result is an odd parity number
  */
  public static byte oddConvertBlock( byte a ) {
    return odd_parity_tab[ a < 0 ? 256+a : a ];
  }

  /**
  * <p>convert a data to an odd parity number</p>
  * @param a the input byte array
  * @param len the length of the input byte array
  * @return the result is an odd parity array
  */
  public static byte[] oddConvertBlock( byte[] a, int len ) {
    byte[] b = new byte[len];
    for( int i=0; i < len; i++ ) {
      b[i] = oddConvertBlock(a[i]);
    }
    return b;
  }

  /**
  * <p>convert a data to an odd parity number</p>
  * @param a the input byte array
  * @return the result is an odd parity array
  */
  public static byte[] oddConvertBlock( byte[] a ) {
    return oddConvertBlock( a, a.length );
  }

  /**
  * <p>convert a data to an odd parity number</p>
  * @param a the input byte array
  * @param b the output byte array
  * @param len the length of the input byte array
  */
  public void oddConvertBlock( byte[] a, byte[] b, int len ) {
    for( int i=0; i < len; i++ ) {
      b[i] = odd_parity_tab[a[i]<0? 256+a[i] : a[i]];
    }
  }

  /**
  * <p>convert a data to an odd parity number</p>
  * @param a the input byte array
  * @param b the output byte array
  */
  public void oddConvertBlock( byte[] a, byte[] b) {
    oddConvertBlock( a, b, a.length );
  }

  /// Convert one block into even parity block.

  /**
  * <p>convert a data to an even parity number</p>
  * @param a the input byte data
  * @return the result is an even parity number
  */
  public static byte evenConvertBlock( byte a ) {
    return even_parity_tab[ a < 0 ? 256+a : a ];
  }

  /**
  * <p>convert a data to an even parity number</p>
  * @param a the input byte array
  * @param bytes the length of the input byte array
  * @return the result is an even parity array
  */
  public static byte[] evenConvertBlock( byte[] a, int len ) {
    byte[] b = new byte[len];
    for( int i=0; i < len; i++ ) {
      b[i] = evenConvertBlock(a[i]);
    }
    return b;
  }

  /**
  * <p>convert a data to an even parity number</p>
  * @param a the input byte array
  * @return the result is an even parity array
  */
  public static byte[] evenConvertBlock( byte[] a ) {
    byte[] b = new byte[a.length];
    for( int i=0; i < a.length; i++ ) {
      b[i] = evenConvertBlock(a[i]);
    }
    return b;
  }

  /**
  * <p>convert a data to an even parity number</p>
  * @param a the input byte array
  * @param b the output byte array
  */
  public void evenConvertBlock( byte[] a, byte[] b, int len ) {
    for( int i=0; i < len; i++ ) {
      b[i] = even_parity_tab[ a[i]<0? 256+a[i] : a[i] ];
    }
  }

  /**
  * <p>convert a data to an even parity number</p>
  * @param a the input byte array
  * @param b the output byte array
  */
  public void evenConvertBlock( byte[] a, byte[] b) {
    for( int i=0; i < a.length; i++ ) {
      b[i] = even_parity_tab[ a[i]<0? 256+a[i] : a[i] ];
    }
  }

/// Check Odd Parity
  /**
  * <p>check the odd parity of a data</p>
  * @param a the input byte data
  * @return the result of the odd parity checking
  */
  public boolean oddCheck( byte a ) {
    if( a != odd_parity_tab[ a < 0 ? 256+a : a ] )
      return false;
    else
      return true;
  }

  /// Check Even Parity
  /**
   * <p>check the even parity of a data</p>
   * @param a the input byte data
   * @return the result of the even parity checking
   */
  public boolean evenCheck( byte a ) {
    if( a != even_parity_tab[ a < 0 ? 256+a : a])
      return false;
    else
      return true;
  }

  /// Convert a HEX string into binary data block.
  /**
   * <p>convert a hex string into binary data block.</p>
   * @param hexStr the input string
   * @param hexStrOff the input string offset
   * @param outBlock the output byte array(binary block)
   * @param outBlockOff the output byte array offset
   * @param len the number to be converted
   */
  public static void toBytesBlock( String hexStr, int hexStrOff, byte[] outBlock, int outBlockOff, int len) {
    int i;
    int outlen = outBlock.length;
    int strlen = hexStr.length();
    if ( hexStrOff > strlen ) {
      for ( i = 0; ( outBlockOff + i ) < outlen; i++ )
        outBlock[ outBlockOff + i] = 0;
    }
    else {
      for( i = 0; ( i < len ) && ( ( i + hexStrOff ) < strlen ) && ( ( outBlockOff + i/2 ) < outlen ); i++ ) {
        if( (i%2) == 0 )
          outBlock[ outBlockOff + i/2 ] = (byte)( asc_hex_tab[ hexStr.charAt( i + hexStrOff) ] << 4 );
        else
          outBlock[ outBlockOff + i/2 ]|= (byte)( asc_hex_tab[ hexStr.charAt( i + hexStrOff) ] & 0x0f );
      }
    }
  }

  /**
   * <p>convert a hex string into binary data block.</p>
   * @param hexStr the input string
   * @param outBlock the output byte array(binary block)
   */
  public static void toBytesBlock( String hexStr, byte[] outBlock ) {
    toBytesBlock( hexStr, 0, outBlock, 0, hexStr.length() );
  }

  /**
   * <p>convert a hex string into binary data block.</p>
   * @param hexStr the input string
   * @param out the output byte array(binary block)
   * @param len the length of the output byte array
   * @return the actual length of the output binary array
   */
  public static byte[] toBytesBlock( String hexStr, int off, int len) {
    byte[] block = new byte[(len+1)/2];
    hexStr = hexStr.length()%2 == 0 ? hexStr : hexStr+'0';
    for( int i=0; i < len; i++ ) {
      if((i%2)==0)
        block[i/2] = (byte) ( asc_hex_tab[ hexStr.charAt(off+i) ] << 4 );
      else
        block[i/2]|= (byte) ( asc_hex_tab[ hexStr.charAt(off+i) ] & 0x0f );
    }
    return block;
  }

  /**
   * <p>convert a hex string into binary data block.</p>
   * @param hexStr the input string
   * @param out the output byte array(binary block)
   * @param len the length of the output byte array
   * @return the actual length of the output binary array
   */
  public static byte[] toBytesBlock( String hexStr) {
    return toBytesBlock(hexStr,0,hexStr.length());
  }

  /// Convert a binary data block into a hex string.
  /**
   * <p>convert a block into string.</p>
   * @param block the input byte array
   * @param off the begin offset of the block
   * @param len the length of the block
   * @return the hex string
   */
  public static String toStringBlock( byte[] block, int off, int len )
  {
    StringBuffer buf = new StringBuffer(len*2);
    for( int i=0; i < len; i++ )
    {
      buf.append(hex_asc_tab[ ( block[off+i] >>> 4) & 0x0f ]);
      buf.append(hex_asc_tab[ ( block[off+i]      ) & 0x0f ]);
    }
    return buf.toString();
  }
  /**
   * <p>convert a block into string.</p>
   * @param block the input byte array
   * @return the hex string
   */
  public static String toStringBlock( byte[] block) {
    return toStringBlock( block, 0, block.length );
  }

  /// Convert a block into a char array.
  /**
   * <p>convert a block into a char array.</p>
   * @param block the input block
   * @param off the offset of the block
   * @param len the length to be converted
   * @return the char array
   */
  public static char[] toCharsArray( byte[] block, int off, int len ) {
    char[] buf = new char[len];
    for( int i=0; i < len; i++ ) {
      buf[off+i] = (char)block[off+i];
    }
    return buf;
  }

  /**
   * <p>convert a block into a char array.</p>
   * @param block the input block
   * @return the char array
   */
  public static char[] toCharsArray( byte[] block) {
    return toCharsArray( block, 0, block.length );
  }

}

