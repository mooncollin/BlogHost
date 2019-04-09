package utils;

import java.io.IOException;
import java.io.InputStream;

public class StreamUtils
{
	public static String getStreamString(InputStream stream)
	{
		byte[] bytes = StreamUtils.getStreamBytes(stream);
		if(bytes == null)
		{
			return null;
		}
		
		return new String(bytes);
	}
	
	public static byte[] getStreamBytes(InputStream stream)
	{
		byte[] buf = null;
		try
		{
			buf = new byte[stream.available()];
			stream.read(buf);
		} catch (IOException e)
		{
			return null;
		}
		
		return buf;
	}
}
