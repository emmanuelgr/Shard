package gr.emmanuel.embox.gles2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

// Copying Memory from Java’s Memory Heap to the Native Memory Heap
public class ByteBuffers {
	public static final int BYTES_PER_FLOAT = 4;
	public static final int BYTES_PER_SHORT = 2;


	public static FloatBuffer FloatBuffer(float[] data) {
		// Create a Bytebuffer
		ByteBuffer bb = ByteBuffer.allocateDirect(data.length * BYTES_PER_FLOAT);
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());
		// create a floating point buffer from the ByteBuffer
		FloatBuffer floatBuffer;
		floatBuffer = bb.asFloatBuffer();
		// Add the data
		floatBuffer.put(data);
		// set the buffer to read the first coordinate
		floatBuffer.position(0);
		return floatBuffer;
	}
//
//	public void setVertexAttribPointer(int dataOffset, int attributeLocation, int componentCount, int stride) {
//		floatBuffer.position(dataOffset);
//		glVertexAttribPointer(attributeLocation, componentCount, GL_FLOAT, false, stride, floatBuffer);
//		glEnableVertexAttribArray(attributeLocation);
//
//		floatBuffer.position(0);
//	}

	public static ShortBuffer ShortBuffer(short[] data) {
		// Create a ShortBuffer
		ByteBuffer bb = ByteBuffer.allocateDirect(data.length * BYTES_PER_SHORT);
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());
		// create a floating point buffer from the ByteBuffer
		ShortBuffer shortBuffer;
		shortBuffer = bb.asShortBuffer();
		// Add the data
		shortBuffer.put(data);
		// set the buffer to read the first coordinate
		shortBuffer.position(0);
		return shortBuffer;
	}
}
