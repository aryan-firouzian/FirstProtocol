/*
 * Author: Jari Tervonen <jjtervonen@gmail.com> 2013-
 *
 */

package uni.oulu.firstprotocol;

public interface HmdCommunicator{

	public boolean sendData(long data);
	public boolean sendData(int data);
	public boolean sendData(byte data);
	
	public boolean isConnected();

	
	public void findDevice();
	
	public void doStart();
	
	public void doStop();
	public void doResume();
}
