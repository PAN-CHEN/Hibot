package cn.ybz21.hibotvoice.action;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class CmdText {
	// private static Map<String,String> CmdMap = new HashMap<String,String>();
	// 语音命令对应存储文件路径:./file/speechCmd.txt
	// private static String filePath =
	// "/home/cherylpan/speechfile/twistCmd.txt";
	// 用类加载器读入文件路径
	private static String filePath = CmdText.class.getClassLoader()
			.getResource("../../file/twistCmd.txt").getPath();

	public CmdText() {
		CmdText.setFilePath(filePath);
	}

	public CmdText(String path) {
		// CmdText.setFilePath(filepath);
		CmdText.filePath = CmdText.class.getClassLoader().getResource(path)
				.getPath();
	}

	public static Map<String, String> readTextFile(String path) {
		
		String filePath = CmdText.class.getClassLoader().getResource(path)
				.getPath();
		Map<String, String> CmdMap = new HashMap<String, String>();
		try {
			String encoding = "UTF-8";
			File file = new File(filePath);

			if (file.isFile() && file.exists()) { // 判断文件是否存在

				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				// 将字节流向字符流的转换。
				BufferedReader rd = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = rd.readLine()) != null) {
					if (!" ".equals(lineTxt)) {
						String[] reads = lineTxt.split("#");
						CmdMap.put(reads[0], reads[1]);
					}
				}
				System.out.println("存储了几条语音映射命令：" + CmdMap.size());
				read.close();
				rd.close();

			} else {
				System.out.println("找不到指定文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}

		for (String key : CmdMap.keySet()) {
			System.out.println("key:  " + key + "  value: " + CmdMap.get(key));

		}
		return CmdMap;
	}

	public static String getFilePath() {
		return filePath;
	}

	public static void setFilePath(String filePath) {
		CmdText.filePath = filePath;
	}

}
