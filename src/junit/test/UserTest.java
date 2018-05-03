package junit.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import com.henu.utils.UserUtil;

public class UserTest {

	@Test
	public void testUuid() {
		String s = UserUtil.generateShortUuid();
		System.out.println(s);
	}
	
	@Test
	public void testMd5() {
		String s = UserUtil.md5("666666");
		System.out.println(s.length());
	}
	
	@Test
	public void testTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.CHINESE);
		String src = "2017-12-15 15:40:50";
		Date time = null;
		try {
			time = sdf.parse(src);
		} catch (ParseException e) {
			time = new Date();
		}
		Date now = new Date();
		long diff = now.getTime()-time.getTime();
		System.out.println(diff/1000);
		System.out.println(diff/1000/60);
	}

}
