package com.util;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.util.Properties;

/**
 * 读取log4j日志配置
 * 
 * @author wxhntmy
 */
public class LoadLog4jProperties {

	/**
	 * 读取log4j日志配置
	 * 
	 * @param isDefaultConfiguration
	 *            是否使用默认log4j配置，true是，false否
	 * @param logLevel
	 *            日志级别
	 */
	public static void init(boolean isDefaultConfiguration, LogLevel logLevel) {

		if (isDefaultConfiguration) {
			// 自动快速地使用缺省Log4j环境。
			BasicConfigurator.configure();
			switch (logLevel) {
			case info:
				Logger.getRootLogger().setLevel(Level.INFO);
				break;
			case warn:
				Logger.getRootLogger().setLevel(Level.WARN);
				break;
			case debug:
				Logger.getRootLogger().setLevel(Level.DEBUG);
				break;
			case error:
				Logger.getRootLogger().setLevel(Level.ERROR);
				break;
			default:
				break;
			}
		} else {
			// 创建一个系统参数对象
			Properties props = new Properties();
			// 设置输出级别info,可以将info以及更高级别的记录到日志文件中，但其更低的比如debug级别就不会记录到日志文件中
			// stdout是设置的日志记录的目的地（名字可以随便起的当时要对应上）
			props.setProperty("log4j.rootLogger", logLevel + ",stdout");
			// 设置日志记录的目的地（ConsoleAppender是记录到控制台）
			props.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
			// 设置记录的格式或样式(System.err是红色样式、System.out是黑色样式)
			props.setProperty("log4j.appender.stdout.Target", "System.out");
			// PatternLayout是按照我们自定义规则布局 (%d %l %m %n就是指定的规则布局)
			props.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
			props.setProperty("log4j.appender.stdout.layout.ConversionPattern",
					"%d %-5p %-5L --- [%-10t] %-30c : %m %n");

			// 装入log4j配置信息
			PropertyConfigurator.configure(props);
		}
	}
}
