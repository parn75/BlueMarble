package backup1126_beforesendingObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectByteEx {
	public byte[] toByteArray (Object obj)
	{
		byte[] bytes = null;
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			ObjectOutputStream oos = new ObjectOutputStream(bos); 
			oos.writeObject(obj);
			oos.flush(); 
			oos.close(); 
			bos.close();
			bytes = bos.toByteArray ();
		}
		catch (IOException ex) {
			//TODO: Handle the exception
		}
		return bytes;
	}

	public Object toObject (byte[] bytes)
	{
		Object obj = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream (bytes);
			ObjectInputStream ois = new ObjectInputStream (bis);
			obj = ois.readObject();
		}
		catch (IOException ex) {
			//TODO: Handle the exception
		}
		catch (ClassNotFoundException ex) {
			//TODO: Handle the exception
		}
		return obj;
	}
}
