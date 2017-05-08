package cn.ybz21.hibotvoice.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.ybz21.hibotvoice.util.StaticValues;
import edu.wpi.rail.jrosbridge.Ros;
import edu.wpi.rail.jrosbridge.Topic;
import edu.wpi.rail.jrosbridge.messages.geometry.Twist;

//import edu.wpi.rail.jrosbridge.messages.geometry.Twist;
//import edu.wpi.rail.jrosbridge.messages.geometry.Vector3;

public class VoiceNav {
	private String TOPIC_NAME = "/cmd_vel_mux/input/teleop";
	private String Mes_TYPE = "geometry_msgs/Twist";
	// Ros ros = new Ros("192.168.0.104");
	Ros ros = new Ros("172.20.10.5");
	Topic cmdVelPub = new Topic(ros, TOPIC_NAME, Mes_TYPE);
	static Twist twist = Twist
			.fromJsonString("{\"linear\": {\"x\": 0},\"angular\":{\"z\":0}}");
	int count = 0;
	// public String speech = "" ;
	// private HashMap<String, String> cmdMap;
	// static Map<String, String> cmdMap = (HashMap<String, String>)
	// CmdText.readTextFile();
	//读取txt命令映射
	public static Map<String, String> cmdMap = CmdText.readTextFile("../../file/twistCmd.txt");//cmdtext.readTextFile();
	public static Map<String,String> rosIPMap = CmdText.readTextFile("../../file/RosIP.txt");

//	static {
//		for (String key : cmdMap.keySet()) {
//			System.out.println("key:  " + key + "  value: " + cmdMap.get(key));
//		}
//	}

	public VoiceNav() {
		ros.connect();

		// 遍历map中的键

		// cmdMap = new HashMap<String, String>();
		//
		// cmdMap.put("前进，", "forward");
		// cmdMap.put("向前走，", "forward");
		// cmdMap.put("钱，", "forward");
		// cmdMap.put("钱静，", "forward");
		// cmdMap.put("右转，", "turnRight");
		// cmdMap.put("左转，", "turnLeft");
		// cmdMap.put("后退，", "backward");
		// cmdMap.put("停止，", "stop");
		// cmdMap.put("二号，", "ros2");
		// cmdMap.put("一号，", "ros1");
		// for (String key : cmdMap.keySet()) {
		// System.out.println("Key = " + key);
		// }
	}

	// Attempt to match the recognized word or phrases to the cmdMap dictionary
	// and return the appropriate command.

	public void textToCmd(String text,boolean change) {
		String rosip ;
		String cmd = null  ;
		if (change){
			 ros.disconnect();
			 rosip = getCmd(text,rosIPMap);
			 ros = new Ros(rosip);
			 cmdVelPub = new Topic(ros, TOPIC_NAME, Mes_TYPE);
			 ros.connect();
			// change = false;
		}else{
	       cmd = getCmd(text, cmdMap);
	       twist = Twist.fromJsonString(cmd);
		}// 输出String类型的twist消息
		// twist = speechCallback(cmd);
		// switch case
		//twist = Twist.fromJsonString(cmd);//返回Twist消息类型
		if (count == 0) {
			new Thread() {
				public void run() {
					while (true) {
						cmdVelPub.publish(twist);
						System.out.println(twist.getLinear() + ""
								+ twist.getAngular());
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
			count = 1;
		} else {
			twist = Twist.fromJsonString(cmd);
			// twist = speechCallback(text);
		}
		// ros.disconnect();
	}

	// public Twist speechCallback(String cmd) {//String speech
	// // get the motion command from the recognized phrase
	// //String cmd = getCmd(speech);
	// System.out.println("Command:  " + cmd );
	// // Twist twist = null;
	// // String cmdJson;
	// if (cmd.equals("ros1")) {
	// ros.disconnect();
	// ros = new Ros("192.168.0.104");
	// cmdVelPub = new Topic(ros, TOPIC_NAME, Mes_TYPE);
	// ros.connect();
	// System.out.println("AAAAAAAAAAAA");
	// } else if (cmd.equals("ros2")) {
	// ros.disconnect();
	// ros = new Ros("192.168.0.100");
	// cmdVelPub = new Topic(ros, TOPIC_NAME, Mes_TYPE);
	// ros.connect();
	// System.out.println("BBBBBBBBBBB");
	// } else if (cmd.equals( "forward")) {
	// // 初始化一定要linear和angular都有
	// twist =
	// Twist.fromJsonString("{\"linear\": {\"x\": 0.1},\"angular\":{\"z\":0}}");
	// } else if (cmd.equals ("turnLeft")) {
	// twist =
	// Twist.fromJsonString("{\"linear\": {\"x\": 0.1},\"angular\": {\"z\": 0.3}}");
	// } else if (cmd .equals ("turnRight")) {
	// twist =
	// Twist.fromJsonString("{\"linear\": {\"x\": 0.1},\"angular\": {\"z\": -0.3}}");
	// } else if (cmd .equals ("backward")) {
	// twist =
	// Twist.fromJsonString("{\"linear\": {\"x\": -0.1},\"angular\":{\"z\":0}}");
	// } else if (cmd .equals ( "stop") ){
	// twist = new Twist();
	// System.out.println("Turtlebot结束运动。");//////
	// // stop = true;
	// }
	// twist = Twist.fromJsonString(cmd);
	// return twist;
	// }

	public String getCmd(String speech, Map<String,String>Map) {
		Iterator<Entry<String, String>> it = Map.entrySet().iterator();
		System.out.println("输出读入的map大小：" + Map.entrySet().size());// 输出10

		// 返回命令
		String cmd = "";
		boolean noCmd = true;
		while (it.hasNext() && noCmd) {
			Map.Entry entry = (Map.Entry) it.next();
			Object obj = entry.getKey();
			if (obj != null && obj.equals(speech)) {
				cmd = (String) entry.getValue();// 搜索命令
				noCmd = false;
			}
		}
		System.out.println("this is the txt value string :  \t" + cmd);
		return cmd;
	}
}
